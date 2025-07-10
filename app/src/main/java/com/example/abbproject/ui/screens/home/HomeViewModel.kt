package com.example.abbproject.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.abbproject.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    init {
        loadUser()
    }

    private fun loadUser() {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("users").document(uid)
            .get()
            .addOnSuccessListener { document ->
                val userData = document.toObject(User::class.java)
                _user.value = userData
            }
            .addOnFailureListener {
                Log.e("HomeViewModel", "Failed to load user: ${it.message}")
            }
    }
}
