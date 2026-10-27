package com.example.simplecustomerregistration.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import com.example.simplecustomerregistration.constants.CONSTANTS
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel : ViewModel() {
    private val _usernameState = MutableStateFlow(TextFieldState())
    val usernameState = _usernameState.asStateFlow()

    private val _passwordState = MutableStateFlow(TextFieldState())
    val passwordState = _passwordState.asStateFlow()

    private val _isCredentialsCorrect = MutableStateFlow(false)
    val isCredentialsCorrect = _isCredentialsCorrect.asStateFlow()


    private val _errorString = MutableStateFlow("")
    val errorString = _errorString.asStateFlow()

    private val _isPasswordHidden = MutableStateFlow(true)
    val isPasswordHidden = _isPasswordHidden.asStateFlow()


    fun getErrorString(): String {
        return errorString.value
    }

    fun updateIsPasswordHidden(b: Boolean) {
        _isPasswordHidden.value = b
    }

    fun validateCredentials() {
        if (_usernameState.value.text.isEmpty()) {
            _errorString.value = CONSTANTS.ERROR_STRING_USERNAME_EMPTY
        } else if (_passwordState.value.text.isEmpty()) {
            _errorString.value = CONSTANTS.ERROR_STRING_PASSWORD_EMPTY
        } else if (_usernameState.value.text != CONSTANTS.TEST_USERNAME || _passwordState.value.text != CONSTANTS.TEST_PASSWORD) {
            _errorString.value = CONSTANTS.ERROR_STRING_INVALID_CREDENTIALS
        } else {
            _errorString.value = ""
            _isCredentialsCorrect.value = true
        }
    }

}