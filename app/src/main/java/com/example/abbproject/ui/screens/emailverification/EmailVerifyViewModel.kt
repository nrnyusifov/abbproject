package com.example.abbproject.ui.screens.emailverification

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
        auth.currentUser?.sendEmailVerification()
            ?.addOnCompleteListener {
                if (it.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(false, it.exception?.message)
                }
            }
    }
}