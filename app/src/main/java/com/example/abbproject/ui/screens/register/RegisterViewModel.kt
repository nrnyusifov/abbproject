package com.example.abbproject.ui.screens.register

import android.content.Context
import android.net.Uri
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.core.net.toUri

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

    fun registerUser(
        context: Context,
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        profileImageUri: Uri? = null
    ) {
        Log.d("Register", "Starting registration process...")
        when {
            firstName.isBlank() || lastName.isBlank() -> {
                _registerState.value = RegisterState.Error("Name and surname cannot be empty.")
                return
            }
            email.isBlank() -> {
                _registerState.value = RegisterState.Error("Email is required.")
                return
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                _registerState.value = RegisterState.Error("Please enter a valid email address.")
                return
            }
            password.length < 6 -> {
                _registerState.value = RegisterState.Error("Password must be at least 6 characters.")
                return
            }
        }
        _registerState.value = RegisterState.Loading

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid
                if (uid == null) {
                    Log.e("Register", "User ID is null after registration.")
                    _registerState.value = RegisterState.Error("User ID is null")
                    return@addOnSuccessListener
                }

                if (profileImageUri != null) {
                    Log.d("Register", "Profile image URI is provided: $profileImageUri")

                    uploadProfileImage(
                        context,
                        uid,
                        profileImageUri,
                        onSuccess = { imageUrl ->
                            Log.d("Register", "Image uploaded successfully: $imageUrl")
                            sendVerificationEmail()
                            Log.d("Register", "Verification email sent.")
                            saveUserData(uid, firstName, lastName, email, imageUrl)
                        },
                        onError = { error ->
                            _registerState.value = RegisterState.Error(error.message ?: "Image upload failed")
                            auth.currentUser?.delete()
                            Log.e("Register", "Image upload failed: ${error.message}")
                        }
                    )
                } else {
                    Log.d("Register", "No profile image provided, proceeding to save user data.")
                    sendVerificationEmail()
                    Log.d("Register", "Verification email sent.")
                    saveUserData(uid, firstName, lastName, email, "")
                    val user = FirebaseAuth.getInstance().currentUser
                    if (user != null) {
                        user.sendEmailVerification()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d("Register", "Verification email sent.")
                                    saveUserData(uid, firstName, lastName, email, "")
                                } else {
                                    Log.e("Register", "Failed to send email: ${task.exception?.message}")
                                }
                            }
                    } else {
                        Log.e("Register", "No current user found, possibly deleted or not created.")
                    }

                }
            }
            .addOnFailureListener {
                Log.e("Register", "User creation failed: ${it.message}")
                _registerState.value = RegisterState.Error(it.message ?: "Registration failed")
                auth.currentUser?.delete()
            }
    }


    private fun sendVerificationEmail() {
        auth.currentUser?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Register", "Verification email sent.")
                } else {
                    Log.e("Register", "Failed to send email: ${task.exception?.message}")
                    auth.currentUser?.delete()
                    _registerState.value = RegisterState.Error("Failed to send verification email")
                }
            }
    }


    private fun uploadProfileImage(
        context: Context,
        uid: String,
        uri: Uri,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        try {
            val tempFile = File.createTempFile("profile_${uid}", ".jpg", context.cacheDir)
            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(tempFile).use { output ->
                    input.copyTo(output)
                }
            } ?: throw IOException("Cannot open stream from URI: $uri")

            val storageRef = Firebase.storage.reference.child("users/$uid/profile.jpg")
            Log.d("Upload", "Uploading from temp file: ${tempFile.absolutePath}")

            storageRef.putFile(tempFile.toUri())
                .addOnSuccessListener {
                    Log.d("Upload", "Upload success. Getting download URL...")
                    storageRef.downloadUrl
                        .addOnSuccessListener { downloadUri ->
                            Log.d("Upload", "Download URL: $downloadUri")
                            onSuccess(downloadUri.toString())
                            tempFile.delete()
                        }
                        .addOnFailureListener { error ->
                            Log.e("Upload", "Failed to get download URL: ${error.message}")
                            onError(error)
                            tempFile.delete()
                        }
                }
                .addOnFailureListener { error ->
                    Log.e("Upload", "Temp file upload failed: ${error.message}")
                    onError(error)
                    tempFile.delete()
                }

        } catch (e: Exception) {
            Log.e("Upload", "Exception: ${e.message}")
            onError(e)
        }
    }

    private fun saveUserData(
        uid: String,
        firstName: String,
        lastName: String,
        email: String,
        imageUrl: String
    ) {
        val userMap = createUserMap(uid, firstName, lastName, email, imageUrl)

        Log.d("Register", "Saving user data to Firestore...")

        firestore.collection("users").document(uid).set(userMap)
            .addOnSuccessListener {
                Log.d("Register", "User data saved successfully.")
                _registerState.value = RegisterState.Success
            }
            .addOnFailureListener {
                Log.e("Register", "Failed to save user data: ${it.message}")
                _registerState.value = RegisterState.Error(it.message ?: "Failed to save user data")
                auth.currentUser?.delete()
            }
    }

    private fun createUserMap(
        uid: String,
        firstName: String,
        lastName: String,
        email: String,
        imageUrl: String
    ): Map<String, Any> {
        return mapOf(
            "uid" to uid,
            "firstName" to firstName,
            "lastName" to lastName,
            "email" to email,
            "imageUrl" to imageUrl
        )
    }
}

sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    object Success : RegisterState()
    data class Error(val message: String) : RegisterState()
}
