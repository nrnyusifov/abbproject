package com.example.abbproject.ui.screens.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.abbproject.data.local.UserPreferences
import com.example.abbproject.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    val rememberMe = userPreferences.rememberMeFlow
    val savedEmail = userPreferences.savedEmailFlow

    fun updateEmail(value: String) {
        _uiState.update { it.copy(email = value) }
    }

    fun updatePassword(value: String) {
        _uiState.update { it.copy(password = value) }
    }

    fun updateRememberMe(value: Boolean) {
        _uiState.update { it.copy(rememberMe = value) }
    }

    fun login() {
        val state = _uiState.value

        viewModelScope.launch {
            Log.d("LoginViewModel", "Login attempt started for email: ${state.email}")
            _uiState.update { it.copy(isLoading = true, errorMessage = null, isSuccess = false) }

            val result = authRepository.login(state.email, state.password)

            if (result.isSuccess) {
                val user = authRepository.getCurrentUser()
                if (user != null && !authRepository.isEmailVerified()) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Your email address hasn’t been verified. Please check your inbox.",
                            isSuccess = false
                        )
                    }
                    return@launch
                }

                userPreferences.saveCredentials(state.email, state.rememberMe)
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            } else {
                val exceptionMessage = result.exceptionOrNull()?.message ?: ""
                val userMessage = when {
                    "There is no user record" in exceptionMessage -> "No account found with this email."
                    "The password is invalid" in exceptionMessage -> "Incorrect password."
                    "The email address is badly formatted" in exceptionMessage -> "Invalid email format."
                    "A network error" in exceptionMessage -> "Network error. Please check your internet connection."
                    else -> "Login failed. Please check your credentials and try again."
                }

                _uiState.update { it.copy(isLoading = false, errorMessage = userMessage, isSuccess = false) }
            }
        }
    }

    fun resetState() {
        _uiState.update { it.copy(isLoading = false, errorMessage = null, isSuccess = false) }
    }

    fun isUserLoggedIn(): Boolean = authRepository.isUserLoggedIn()

    fun clearErrorMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }


    fun sendPasswordReset(email: String) {
        viewModelScope.launch {
            if (email.isBlank()) {
                _uiState.update { it.copy(errorMessage = "Please enter your email first.") }
                return@launch
            }

            val result = authRepository.sendPasswordResetEmail(email)
            _uiState.update {
                it.copy(
                    errorMessage = if (result.isSuccess) "Password reset email sent!" else
                        result.exceptionOrNull()?.message ?: "Failed to send reset email."
                )
            }
        }
    }
}
