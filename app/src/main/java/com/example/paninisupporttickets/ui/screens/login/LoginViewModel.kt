package com.example.paninisupporttickets.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.paninisupporttickets.core.UserMessages
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class LoginUiState(
    val isLoading: Boolean = false,
    val email: String = "",
    val password: String = "",
    val errorMessage: String? = null,
    val isLoginSuccessful: Boolean = false
)

class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun updateEmail(email: String) {
        _uiState.value = _uiState.value.copy(email = email, errorMessage = null)
    }

    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(password = password, errorMessage = null)
    }

    fun login() {
        val currentState = _uiState.value
        val email = currentState.email.trim()
        val password = currentState.password

        if (email.isEmpty()) {
            _uiState.value = currentState.copy(errorMessage = UserMessages.Login.EMAIL_REQUIRED)
            return
        }

        if (!email.contains("@")) {
            _uiState.value = currentState.copy(errorMessage = UserMessages.Login.EMAIL_INVALID)
            return
        }

        if (password.isEmpty()) {
            _uiState.value = currentState.copy(errorMessage = UserMessages.Login.PASSWORD_REQUIRED)
            return
        }

        _uiState.value = currentState.copy(
            isLoading = true,
            errorMessage = null
        )

        _uiState.value = _uiState.value.copy(
            isLoading = false,
            isLoginSuccessful = true
        )
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

class LoginViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel() as T
    }
}
