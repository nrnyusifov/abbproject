package com.example.abbproject.data.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(private val auth: FirebaseAuth){

    suspend fun login(email: String, password: String) : Result<Unit>{
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(email:String, password: String) : Result<Unit>{
        return try{
            auth.createUserWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            FirebaseAuth.getInstance().sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun isUserLoggedIn() : Boolean = auth.currentUser != null

    fun getCurrentUser() = auth.currentUser

    fun isEmailVerified(): Boolean {
        return auth.currentUser?.isEmailVerified == true
    }

}