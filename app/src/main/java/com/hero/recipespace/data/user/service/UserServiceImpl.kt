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
import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.data.user.UserData
import com.hero.recipespace.domain.user.request.LoginUserRequest
import com.hero.recipespace.domain.user.request.SignUpUserRequest
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class UserServiceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage
) : UserService {
    override suspend fun login(request: LoginUserRequest): UserData {
        val userKey = loginToFirebase(request.email, request.pwd)

        return getUserData(userKey)
    }

    private suspend fun getUserData(userKey: String) : UserData {
        return suspendCoroutine { continuation ->
            firebaseFirestore.collection("User")
                .document(userKey)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot == null) {
                        return@addOnSuccessListener
                    }
    //                    val userData = documentSnapshot.toObject(UserData::class.java)
                    val userData = documentSnapshot.data?.getValue(userKey)

                    continuation.resume(userData as UserData)
                }
                .addOnFailureListener(OnFailureListener {
                    continuation.resumeWithException(it)
                })
        }
    }

    private suspend fun loginToFirebase(email: String, pwd: String) : String {
        return suspendCoroutine { continuation ->
            firebaseAuth.signInWithEmailAndPassword(email, pwd)
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
            profileImageUrl
        )
    }

    override fun getDataList(): List<UserData> {
        TODO("Not yet implemented")
    }

    override suspend fun add(request: SignUpUserRequest): UserData {
        if (createAccount(request.email, request.pwd)) {
            val userData = UserData(
                key = firebaseAuth.uid.orEmpty(),
                name = request.name,
                email = request.email,
                profileImageUrl = firebaseAuth.currentUser?.photoUrl?.toString().orEmpty()
            )

            return suspendCoroutine<UserData> { continuation ->
                firebaseFirestore.collection("User")
                    .document(userData.key.orEmpty())
                    .set(userData)
                    .addOnSuccessListener {
                        continuation.resume(userData)
                    }
                    .addOnFailureListener { continuation.resumeWithException(it) }
            }
        } else {
            error("failed create account")
        }
    }

    private suspend fun createAccount(email: String, pwd: String): Boolean {
        return suspendCoroutine { continuation ->
            firebaseAuth.createUserWithEmailAndPassword(email, pwd)
                .addOnSuccessListener {
                    firebaseAuth.currentUser?.updateProfile(
                        UserProfileChangeRequest
                            .Builder()
                            .build()
                    )
                    continuation.resume(true)
                }
                .addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }

    // TODO: 2022-12-15 하단에 있는 update 메소드들과 합치기
    override suspend fun update(userData: UserData) : UserData {
        val editData = HashMap<String, Any>()

        if (!TextUtils.isEmpty(userData.profileImageUrl)) {
            editData["profileImageUrl"] = userData.profileImageUrl as String
        }
        editData["name"] = userData.name as String

        return suspendCoroutine<UserData> { continuation ->
            firebaseFirestore.collection("User")
                .document(userData.key.orEmpty())
                .update(editData)
                .addOnSuccessListener { continuation.resume(userData) }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
    }

    override suspend fun uploadImage(
        profileImageUrl: String,
        progress: (Float) -> Unit): String {

        return suspendCoroutine { continuation ->
            val storageRef =
                firebaseStorage.reference.child(DEFAULT_IMAGE_PATH)

            val photoPath: String = profileImageUrl
            val imageFile = Uri.fromFile(File(photoPath))

//            val photoRef = storageRef.child(DEFAULT_IMAGE_PATH + Uri.parse(photoPath).lastPathSegment)
            val photoRef = storageRef.child(DEFAULT_IMAGE_PATH + "${imageFile.lastPathSegment}")

            val uploadTask = photoRef.putFile(imageFile)

            uploadTask
                .addOnProgressListener { taskSnapshot ->
                    val btf = taskSnapshot.bytesTransferred
                    val tbc = taskSnapshot.totalByteCount
                    val percent = btf.toFloat() / tbc.toFloat() * 100
                    progress(percent)
                }.continueWithTask { task ->
                    if (!task.isSuccessful) {
//                        throw task.exception!!
                        task.exception?.let {
                            throw it
                        }
                    }
                    storageRef.downloadUrl
                }
                .addOnSuccessListener { uri ->
//                    val photoMap: HashMap<String, Any> = HashMap()
//                    photoMap["photoUrl"] = uri

//                    photoRef.downloadUrl.addOnSuccessListener {
//
//                    }
                    continuation.resume(uri.toString())

                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }

    // FirebaseAuth 객체 수정
    private fun updateLocalUser(userData: UserData) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser!!
        val builder = UserProfileChangeRequest.Builder()
            .setDisplayName(userData.name)
        if (!TextUtils.isEmpty(userData.profileImageUrl)) {
            builder.photoUri = Uri.parse(userData.profileImageUrl)
        }
        val request = builder.build()
        firebaseUser.updateProfile(request)
            .addOnSuccessListener { }
            .addOnFailureListener { }
    }


    // TODO EditProfileActivity를 통해 나의 사용자 정보(나의 프로필 사진, 나의 사용자 이름)가 변경되었을 때
    //  update() 메소드에서 트랜잭션 처리할 때 필요한 데이터
    //  1. UserData 의 profileImageUrl, name
    //  2. RecipeData 의 profileImageUrl, userName
    //  3. ChatActivity의 profileImageUrl, name
    private fun updateUserWithRecipeAndChat(userData: UserData, recipeData: RecipeData, chatData: ChatData) {
        firebaseFirestore.runTransaction { transaction ->
            val editUserData = HashMap<String, Any>()

            // 1. UserData 의 profileImageUrl, name
            if (!TextUtils.isEmpty(userData.profileImageUrl)) {
                editUserData["profileImageUrl"] = userData.profileImageUrl as String
            }
            editUserData["name"] = userData.name as String
            val userRef: DocumentReference = firebaseFirestore.collection("User")
                .document(userData.key.orEmpty())
            transaction.update(userRef, editUserData)


            // 2. RecipeData 의 profileImageUrl, name
            val editRecipeData = HashMap<String, Any>()
            if (!TextUtils.isEmpty(recipeData.profileImageUrl)) {
                editRecipeData["profileImageUrl"] = recipeData.profileImageUrl as String
            }
            editRecipeData["name"] = recipeData.userName as String
            val recipeRef: DocumentReference = firebaseFirestore.collection("Recipe")
                .document(recipeData.key.orEmpty())
            transaction.update(recipeRef, editRecipeData)

            // 3. ChatData 의 profileImageUrl, name
//            val editChatData = HashMap<String, Any>()
//            if (!TextUtils.isEmpty(chatData.profileImageUrl)) {
//                editChatData["profileImageUrl"] = chatData.profileImageUrl as String
//            }
//            editChatData["name"] = chatData.userName as String
//            val chatRef: DocumentReference = firebaseFirestore.collection("Chat")
//                .document(chatData.key.orEmpty())
//            transaction.update(chatRef, editChatData)

            null
        }
            .addOnSuccessListener {
                updateLocalUser(userData)
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
    }

    override suspend fun remove(userData: UserData) : UserData {
        return suspendCoroutine<UserData> { continuation ->
            firebaseFirestore.collection("User")
                .document(userData.key.orEmpty())
                .delete()
                .addOnSuccessListener { continuation.resume(userData) }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
    }

    override suspend fun signOut() {
        firebaseAuth.signOut()
    }

    companion object {
        const val DEFAULT_IMAGE_PATH = "images/"
        const val PROFILE_IMAGE_PATH = "profile/"
    }
}