package com.example.abbproject.ui.screens.emailverification

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EmailVerifyViewModel @Inject constructor() : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    fun reloadUser(onResult: (Boolean) -> Unit) {
        auth.currentUser?.reload()?.addOnCompleteListener {
            onResult(auth.currentUser?.isEmailVerified == true)
        }
    }

    fun resendVerification(onResult: (Boolean, String?) -> Unit) {
        val user = auth.currentUser
        if (user == null) {
            Log.e("EmailVerify", "No user signed in")
            onResult(false, "User not signed in")
            return
        }

        user.reload().addOnSuccessListener {
            if (user.isEmailVerified) {
                onResult(false, "Email already verified")
                return@addOnSuccessListener
            }

            user.sendEmailVerification()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d("EmailVerify", "Verification email sent")
                        onResult(true, null)
                    } else {
                        Log.e("EmailVerify", "Send failed: ${it.exception?.message}")
                        onResult(false, it.exception?.message)
                    }
                }
        }.addOnFailureListener {
            Log.e("EmailVerify", "User reload failed: ${it.message}")
            onResult(false, it.message)
        }
    }


}