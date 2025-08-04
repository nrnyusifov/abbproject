package com.example.abbproject.ui.screens.register

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import java.io.ByteArrayOutputStream
import android.util.Base64

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

        if (!validateInputs(userData)) return

        _uiState.update { it.copy(isLoading = true, errorMessage = null, isSuccess = false) }

        auth.createUserWithEmailAndPassword(userData.email, userData.password)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid
                if (uid == null) {
                    handleError("User ID is null")
                    return@addOnSuccessListener
                }

                val profileImageBase64 = userData.profileImageUri?.let { uri ->
                    encodeImageToBase64(context, uri)
                } ?: ""

                val user = createUserMap(uid, userData, profileImageBase64)

                saveUserToFirestore(uid, user)
            }
            .addOnFailureListener {
                handleError(it.message ?: "Registration failed")
                auth.currentUser?.delete()
            }
    }

    private fun createUserMap(
        uid: String,
        userData: RegisterUiState,
        base64: String
    ): Map<String, Any> {
        return mapOf(
            "uid" to uid,
            "firstName" to userData.firstName,
            "lastName" to userData.lastName,
            "email" to userData.email,
            "profileImageBase64" to base64
        )
    }

    private fun saveUserToFirestore(uid: String, user: Map<String, Any>) {
        firestore.collection("users").document(uid).set(user)
            .addOnSuccessListener {
                sendVerificationEmail()
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            }
            .addOnFailureListener {
                handleError(it.message ?: "Failed to save user data")
                auth.currentUser?.delete()
            }
    }

    private fun handleError(message: String) {
        _uiState.update { it.copy(isLoading = false, errorMessage = message) }
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


    private fun sendVerificationEmail() {
        auth.currentUser?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    auth.currentUser?.delete()
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Failed to send verification email") }
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
            ""
        }
    }
}
