package com.hero.recipespace.database.user.datastore

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.hero.recipespace.data.user.UserData
import com.hero.recipespace.database.CloudStore
import com.hero.recipespace.listener.OnCompleteListener

class UserCloudStore(
    private val context: Context
) : CloudStore<UserData>(context, FirebaseFirestore.getInstance()) {
    companion object {
        private lateinit var instance : UserCloudStore

        fun getInstance(context: Context) : UserCloudStore {
            return instance ?: synchronized(this) {
                instance ?: UserCloudStore(context).also {
                    instance = it
                }
            }
        }
    }

    override fun getData(vararg params: Any?, onCompleteListener: OnCompleteListener<UserData>) {
        TODO("Not yet implemented")
    }

    override fun getDataList(
        vararg params: Any?,
        onCompleteListener: OnCompleteListener<List<UserData>>,
    ) {
        TODO("Not yet implemented")
    }

    override fun add(data: UserData, onCompleteListener: OnCompleteListener<UserData>) {
        TODO("Not yet implemented")
    }

    override fun update(data: UserData, onCompleteListener: OnCompleteListener<UserData>) {
        TODO("Not yet implemented")
    }

    override fun remove(data: UserData, onCompleteListener: OnCompleteListener<UserData>) {
        TODO("Not yet implemented")
    }
}