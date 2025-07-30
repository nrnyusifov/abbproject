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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject
import androidx.core.net.toUri

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    fun updateFirstName(name: String) = _uiState.update { it.copy(firstName = name) }
    fun updateLastName(name: String) = _uiState.update { it.copy(lastName = name) }
    fun updateEmail(email: String) = _uiState.update { it.copy(email = email) }
    fun updatePassword(password: String) = _uiState.update { it.copy(password = password) }
    fun updateProfileImage(uri: Uri?) = _uiState.update { it.copy(profileImageUri = uri) }

    fun registerUser(context: Context) {
        val userData = _uiState.value

        Log.d("Register", "Starting registration process...")

        if (!validateInputs(userData)) return

        _uiState.update { it.copy(isLoading = true, errorMessage = null, isSuccess = false) }

        auth.createUserWithEmailAndPassword(userData.email, userData.password)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid
                if (uid == null) {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "User ID is null") }
                    return@addOnSuccessListener
                }
                finalizeUserRegistration(context, uid, userData)
            }
            .addOnFailureListener {
                _uiState.update { it.copy(isLoading = false, errorMessage = it.errorMessage ?: "Registration failed") }
                auth.currentUser?.delete()
            }
    }

    private fun validateInputs(userData: RegisterUiState): Boolean {
        return when {
            userData.firstName.isBlank() || userData.lastName.isBlank() -> {
                _uiState.update { it.copy(errorMessage = "Name and surname cannot be empty.") }
                false
            }
            userData.email.isBlank() -> {
                _uiState.update { it.copy(errorMessage = "Email is required.") }
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(userData.email).matches() -> {
                _uiState.update { it.copy(errorMessage = "Please enter a valid email address.") }
                false
            }
            userData.password.length < 6 -> {
                _uiState.update { it.copy(errorMessage = "Password must be at least 6 characters.") }
                false
            }
            else -> true
        }
    }

    private fun finalizeUserRegistration(
        context: Context,
        uid: String,
        userData: RegisterUiState
    ) {
        userData.profileImageUri?.let { uri ->
            uploadProfileImage(
                context,
                uid,
                uri,
                onSuccess = { imageUrl ->
                    sendVerificationEmail()
                    saveUserData(uid, userData, imageUrl)
                },
                onError = { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.message ?: "Image upload failed") }
                    auth.currentUser?.delete()
                }
            )
        } ?: run {
            sendVerificationEmail()
            saveUserData(uid, userData, "")
        }
    }

    private fun sendVerificationEmail() {
        auth.currentUser?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    auth.currentUser?.delete()
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Failed to send verification email") }
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
            Log.d("Upload", "Creating temp file from URI...")
            val tempFile = createTempFileFromUri(context, uid, uri)
            Log.d("Upload", "Temp file created: ${tempFile.absolutePath}, size: ${tempFile.length()} bytes")

            val storageRef = Firebase.storage.reference.child("users/$uid/profile.jpg")
            Log.d("Upload", "Starting upload to Firebase at: users/$uid/profile.jpg")

            storageRef.putFile(tempFile.toUri())
                .addOnSuccessListener {
                    Log.d("Upload", "Upload successful. Fetching download URL...")

                    storageRef.downloadUrl
                        .addOnSuccessListener { downloadUri ->
                            Log.d("Upload", "Download URL fetched successfully: $downloadUri")
                            onSuccess(downloadUri.toString())
                            val deleted = tempFile.delete()
                            Log.d("Upload", "Temp file deleted after success: $deleted")
                        }
                        .addOnFailureListener { downloadError ->
                            Log.e("Upload", "Failed to get download URL: ${downloadError.message}", downloadError)
                            onError(downloadError)
                            val deleted = tempFile.delete()
                            Log.d("Upload", "Temp file deleted after download URL failure: $deleted")
                        }

                }
                .addOnFailureListener { uploadError ->
                    Log.e("Upload", "Upload failed: ${uploadError.message}", uploadError)
                    onError(uploadError)
                    val deleted = tempFile.delete()
                    Log.d("Upload", "Temp file deleted after upload failure: $deleted")
                }

        } catch (e: Exception) {
            Log.e("Upload", "Unexpected exception: ${e.message}", e)
            onError(e)
        }
    }

    private fun createTempFileFromUri(context: Context, uid: String, uri: Uri): File {
        val tempFile = File.createTempFile("profile_${uid}", ".jpg", context.cacheDir)
        Log.d("Upload", "Temp file path: ${tempFile.absolutePath}")

        context.contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(tempFile).use { output ->
                input.copyTo(output)
                Log.d("Upload", "Copied URI content to temp file.")
            }
        } ?: throw IOException("Cannot open stream from URI: $uri")

        return tempFile
    }


    private fun saveUserData(
        uid: String,
        userData: RegisterUiState,
        imageUrl: String
    ) {
        val userMap = mapOf(
            "uid" to uid,
            "firstName" to userData.firstName,
            "lastName" to userData.lastName,
            "email" to userData.email,
            "imageUrl" to imageUrl
        )

        firestore.collection("users").document(uid).set(userMap)
            .addOnSuccessListener {
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            }
            .addOnFailureListener {
                _uiState.update { it.copy(isLoading = false, errorMessage = it.errorMessage ?: "Failed to save user data") }
                auth.currentUser?.delete()
            }
    }
}
