package com.example.abbproject.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.abbproject.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel(){

    // Internal variable used to update the state (private).
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)

    // Public read-only version so UI can observe it.
    val loginState: StateFlow<LoginState> = _loginState

    fun login(email: String, password: String){
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            val result = authRepository.login(email, password)
            if (result.isSuccess) {
                val user = authRepository.getCurrentUser()
                if (user != null && !authRepository.isEmailVerified()) {
                    _loginState.value = LoginState.Error("Please verify your email before logging in.")
                    return@launch
                }
            }


            _loginState.value = if (result.isSuccess) {
                LoginState.Success
            } else {
                LoginState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }

    // Resets the state back to normal (Idle)
    fun resetState() { _loginState.value = LoginState.Idle }

    fun isUserLoggedIn(): Boolean = authRepository.isUserLoggedIn()

}


sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
}