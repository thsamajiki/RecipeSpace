package com.hero.recipespace.data.user.service

import android.net.Uri
import android.text.TextUtils
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.hero.recipespace.data.chat.ChatData
import com.hero.recipespace.data.chat.service.ChatServiceImpl
import com.hero.recipespace.data.message.service.MessageServiceImpl
import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.data.recipe.service.RecipeServiceImpl
import com.hero.recipespace.data.user.UserData
import com.hero.recipespace.di.FirebaseModule
import com.hero.recipespace.domain.user.entity.Email
import com.hero.recipespace.domain.user.entity.Password
import com.hero.recipespace.domain.user.request.LoginUserRequest
import com.hero.recipespace.domain.user.request.SignUpUserRequest
import com.hero.recipespace.util.WLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class UserServiceImpl
@Inject
constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage,
) : UserService {
    override suspend fun login(request: LoginUserRequest): UserData {
        val userKey =
            loginToFirebase(
                request.email,
                request.pwd,
            )

        return getUserData(userKey)
    }

    override suspend fun getUserData(userKey: String): UserData {
        return suspendCoroutine { continuation ->
            db.collection("User")
                .document(userKey)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot == null) {
                        return@addOnSuccessListener
                    }

                    val userData = documentSnapshot.toObject(UserData::class.java)

                    continuation.resume(userData as UserData)
                }
                .addOnFailureListener(
                    OnFailureListener {
                        continuation.resumeWithException(it)
                    },
                )
        }
    }

    private suspend fun loginToFirebase(
        email: String,
        pwd: String,
    ): String {
        return suspendCoroutine { continuation ->
            auth.signInWithEmailAndPassword(
                email,
                pwd,
            )
                .addOnSuccessListener { authResult ->
                    val userKey = authResult.user?.uid
                    if (userKey != null) {
                        continuation.resume(userKey)
                    } else {
                        continuation.resumeWithException(Exception("user uid is null"))
                    }
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }

    override fun getFirebaseAuthProfile(): UserData {
        val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

        val profileImageUrl: String = firebaseUser?.photoUrl.toString()

        return UserData(
            firebaseUser?.uid.orEmpty(),
            firebaseUser?.displayName,
            firebaseUser?.email,
            profileImageUrl,
        )
    }

    override fun getDataList(): List<UserData> {
        TODO("Not yet implemented")
    }

    override suspend fun add(request: SignUpUserRequest): UserData {
        if (createAccount(
                request.email,
                request.pwd,
            )
        ) {
            val userData =
                UserData(
                    key = auth.uid.orEmpty(),
                    name = request.name,
                    email = request.email.value,
                    profileImageUrl = null,
                )

            return suspendCoroutine<UserData> { continuation ->
                db.collection("User")
                    .document(userData.key)
                    .set(userData)
                    .addOnSuccessListener {
                        updateUserProfile(
                            request.name,
                            onSuccess = {
                                continuation.resume(userData)
                            },
                            onFailure = {
                                WLog.e("$it")
                                continuation.resume(userData)
                            },
                        )
                    }
                    .addOnFailureListener { continuation.resumeWithException(it) }
            }
        } else {
            error("failed create account")
        }
    }

    private suspend fun createAccount(
        email: Email,
        pwd: Password,
    ): Boolean {
//        val user = firebaseAuth.currentUser
//
//        val request = userProfileChangeRequest {
//            displayName = name
//            photoUri = Uri.parse("https://i.pinimg.com/280x280_RS/02/64/d5/0264d5d1d7f4a60d3eb210809dcce729.jpg")
//        }

        return suspendCoroutine { continuation ->
            auth.createUserWithEmailAndPassword(
                email.value,
                pwd.value,
            )
                .addOnSuccessListener {
                    continuation.resume(true)
                }
                .addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }

    private fun updateUserProfile(
        name: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit,
    ) {
        val user = auth.currentUser

        val userProfileChangeRequest = UserProfileChangeRequest.Builder()
        userProfileChangeRequest.displayName = name
        val request = userProfileChangeRequest.build()

        user!!.updateProfile(request)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure(it)
            }
    }

    override suspend fun update(userData: UserData): UserData {
        val editData = HashMap<String, Any>()

        val profileImageUrl = userData.profileImageUrl
        if (!profileImageUrl.isNullOrEmpty()) {
            editData["profileImageUrl"] = profileImageUrl
        }
        editData["name"] = userData.name.orEmpty()

        val recipeDataList = getMyRecipeDataList(userData.key) // 레시피 리스트 조회해서 넣기,
        val chatDataList = getChatDataList(userData.key) // 채팅 리스트 조회해서 넣기

        return suspendCoroutine<UserData> { continuation ->
            db.collection("User")
                .document(userData.key)
                .update(editData)
                .addOnSuccessListener {
                    updateUserProfile(
                        name = userData.name!!,
                        onSuccess = {
                            updateUserWithRecipeAndChat(
                                userData,
                                recipeDataList = recipeDataList,
                                chatDataList = chatDataList,
                            )

                            continuation.resume(userData)
                        },
                        onFailure = {
                            continuation.resumeWithException(it)
                        },
                    )
                }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
    }

    override suspend fun uploadImage(
        profileImageUrl: String,
        progress: (Float) -> Unit,
    ): String {
        val imageFolderRef = firebaseStorage.reference.child(PROFILE_IMAGE_PATH)

        return withContext(Dispatchers.IO) {
            val photoPath = profileImageUrl
            val imageRef = imageFolderRef.child("${Uri.parse(photoPath).lastPathSegment}")

            async {
                kotlin.runCatching {
                    val imageFile = Uri.parse(photoPath)
                    val uploadTask = imageRef.putFile(imageFile)

                    uploadTask
                        .await()
                        .storage
                        .downloadUrl
                        .await()
                        .toString()
                }
                    .onFailure {
                        WLog.e(it)
                    }
            }
                .await()
                .onFailure {
                    WLog.e("$it")
                }
                .getOrNull()
                .orEmpty()
        }
    }

    // FirebaseAuth 객체 수정
    private fun updateLocalUser(userData: UserData) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser!!
        val builder =
            UserProfileChangeRequest.Builder()
                .setDisplayName(userData.name)
        if (!TextUtils.isEmpty(userData.profileImageUrl)) {
            builder.photoUri = Uri.parse(userData.profileImageUrl)
        }
        val request = builder.build()
        firebaseUser.updateProfile(request)
            .addOnSuccessListener { }
            .addOnFailureListener { }
    }

    private suspend fun getMyRecipeDataList(userKey: String): List<RecipeData> {
        val recipeService =
            RecipeServiceImpl(
                FirebaseModule.provideFirestore(),
                FirebaseModule.provideFirebaseStorage(),
            )
        return recipeService.getMyRecipeList(userKey)
    }

    private suspend fun getChatDataList(userKey: String): List<ChatData> {
        val auth = FirebaseModule.provideFirebaseAuth()
        val db = FirebaseModule.provideFirestore()
        val chatService =
            ChatServiceImpl(
                FirebaseModule.provideFirebaseAuth(),
                FirebaseModule.provideFirestore(),
                this,
                MessageServiceImpl(auth, db),
            )
        return chatService.getDataList(userKey.orEmpty())
    }

    private fun updateUserWithRecipeAndChat(
        userData: UserData,
        recipeDataList: List<RecipeData>,
        chatDataList: List<ChatData>,
    ) {
        db.runTransaction { transaction ->
            val editUserData = HashMap<String, Any>()

            // 1. UserData 의 profileImageUrl, name
            if (!TextUtils.isEmpty(userData.profileImageUrl)) {
                editUserData["profileImageUrl"] = userData.profileImageUrl as String
            }
            editUserData["name"] = userData.name as String
            val userRef: DocumentReference =
                db.collection("User")
                    .document(userData.key)

            transaction.update(userRef, editUserData)

            // 2. RecipeData 의 profileImageUrl, name
            // for 문 돌리기
            val editRecipeData = HashMap<String, Any>()
            if (!TextUtils.isEmpty(userData.profileImageUrl)) {
                editRecipeData["profileImageUrl"] = userData.profileImageUrl as String
            }
            editRecipeData["userName"] = userData.name as String

            for (i: Int in recipeDataList.indices) {
                val recipeRef: DocumentReference =
                    db.collection("Recipe")
                        .document(recipeDataList[i].key)

                transaction.update(recipeRef, editRecipeData)
            }

            // 3. ChatData 의 profileImageUrl, name
            // for 문 돌리기
            val editChatData = HashMap<String, Any>()
            if (!TextUtils.isEmpty(userData.profileImageUrl)) {
                editChatData["userProfileImages.${userData.key}"] =
                    userData.profileImageUrl as String
            }
            editChatData["userNames.${userData.key}"] = userData.name as String

            for (i: Int in chatDataList.indices) {
                val chatRef: DocumentReference =
                    db.collection("Chat")
                        .document(chatDataList[i].key)

                transaction.update(chatRef, editChatData)
            }

            null
        }
            .addOnSuccessListener {
                updateLocalUser(userData)
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
    }

    override suspend fun remove(userData: UserData): UserData {
        return suspendCoroutine<UserData> { continuation ->
            auth.currentUser?.delete()
                ?.addOnSuccessListener {
                    removeUser(userData)
                    continuation.resume(userData)
                }
                ?.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }

    private fun removeUser(userData: UserData) {
        db.collection("User")
            .document(userData.key)
            .delete()
            .addOnSuccessListener { }
            .addOnFailureListener { }
    }

    override suspend fun signOut() {
        auth.signOut()
    }

    companion object {
        const val PROFILE_IMAGE_PATH = "profile/"
    }
}
