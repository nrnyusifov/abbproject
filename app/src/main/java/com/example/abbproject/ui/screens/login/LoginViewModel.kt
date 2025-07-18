package com.example.abbproject.ui.screens.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.abbproject.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            Log.d("LoginViewModel", "Login attempt started for email: $email")
            _loginState.value = LoginState.Loading

            val result = authRepository.login(email, password)

            if (result.isSuccess) {
                Log.d("LoginViewModel", "Login successful for email: $email")

                val user = authRepository.getCurrentUser()
                if (user != null && !authRepository.isEmailVerified()) {
                    Log.d("LoginViewModel", "Email not verified for user: $email")
                    _loginState.value = LoginState.Error(
                        "Your email address hasn’t been verified. Please check your inbox or try signing up again."
                    )
                    return@launch
                }

                _loginState.value = LoginState.Success
                Log.d("LoginViewModel", "User logged in successfully: ${user?.email}")
            } else {
                val exceptionMessage = result.exceptionOrNull()?.message ?: ""
                Log.e("LoginViewModel", "Login failed with exception: $exceptionMessage")

                val userMessage = when {
                    "There is no user record" in exceptionMessage -> "No account found with this email."
                    "The password is invalid" in exceptionMessage -> "Incorrect password."
                    "The email address is badly formatted" in exceptionMessage -> "Invalid email format."
                    "A network error" in exceptionMessage -> "Network error. Please check your internet connection."
                    else -> "Login failed. Please check your credentials and try again."
                }

                _loginState.value = LoginState.Error(userMessage)
                Log.d("LoginViewModel", "Error message: $userMessage")
            }
        }
    }

    fun resetState() {
        _loginState.value = LoginState.Idle
        Log.d("LoginViewModel", "Login state reset to Idle")
    }

    fun isUserLoggedIn(): Boolean {
        val isLoggedIn = authRepository.isUserLoggedIn()
        Log.d("LoginViewModel", "User logged in: $isLoggedIn")
        return isLoggedIn
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
}
