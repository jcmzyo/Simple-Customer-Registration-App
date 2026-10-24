package com.example.simplecustomerregistration.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel: ViewModel() {
    private val _username = mutableStateOf("")

    private val _password = mutableStateOf("")

    private val _isCredentialsCorrect = MutableStateFlow(false)
    val isCredentialsCorrect = _isCredentialsCorrect.asStateFlow()


    private val _errorString = MutableStateFlow("")
    val errorString = _errorString.asStateFlow()

    private val _isPasswordHidden = MutableStateFlow(true)
    val isPasswordHidden = _isPasswordHidden.asStateFlow()


    fun getUsername(): String{
        return _username.value
    }

    fun getPassword(): String{
        return _password.value
    }
    fun updateUsername(t: String) {
        _username.value = t
    }

    fun updatePassword(t: String) {
        _password.value = t
    }

    fun getErrorString(): String {
        return errorString.value
    }

    fun updateIsPasswordHidden(b: Boolean) {
        _isPasswordHidden.value = b
    }

    fun validateCredentials(){
        if(getUsername().isEmpty()){
            _errorString.value = "Username must not be empty."
        }
        else if(getPassword().isEmpty()){
            _errorString.value = "Password must not be empty."
        }
        else if(getUsername() != "juandelacruz@gmail.com" || getPassword() != "jcruz@2026"){
            _errorString.value = "Invalid Credentials."
        }
        else{
            _errorString.value = ""
            _isCredentialsCorrect.value = true
        }
    }

}