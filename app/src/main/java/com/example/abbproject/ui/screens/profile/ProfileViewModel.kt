package com.example.abbproject.ui.screens.profile

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.abbproject.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    init {
        observeUser()
    }

    private fun observeUser() {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("users").document(uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("ProfileViewModel", "Snapshot error: ${error.message}")
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val userData = snapshot.toObject<User>()
                    _user.value = userData
                    Log.d("ProfileViewModel", "User updated from snapshot.")
                }
            }
    }

    fun logout() {
        auth.signOut()
    }

    fun updateProfileImageFromUri(context: Context, uri: Uri) {
        val uid = auth.currentUser?.uid ?: return

        viewModelScope.launch {
            try {
                val base64Image = encodeImageToBase64(context, uri)

                if (base64Image.isNotBlank()) {
                    firestore.collection("users").document(uid)
                        .update("profileImageBase64", base64Image)
                        .addOnSuccessListener {
                            Log.d("ProfileViewModel", "Profile image updated successfully.")
                        }
                        .addOnFailureListener {
                            Log.e("ProfileViewModel", "Failed to update profile image: ${it.message}")
                        }
                } else {
                    Log.e("ProfileViewModel", "Encoded image is blank.")
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Exception in updateProfileImage: ${e.message}")
            }
        }
    }

    private fun encodeImageToBase64(context: Context, uri: Uri): String {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
            Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
        } catch (e: Exception) {
            Log.e("ProfileViewModel", "Image encoding failed: ${e.message}")
            ""
        }
    }
}
