package com.example.simplecustomerregistration.viewmodel

import android.net.Uri
import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.simplecustomerregistration.data.Customer
import com.example.simplecustomerregistration.model.CustomerRepository
import com.example.simplecustomerregistration.view.SimpleCustomerRegistrationApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CustomerListViewModel(
    val customerRepository: CustomerRepository
) : ViewModel() {

    val customerList = customerRepository.getAllCustomers()
    private val _isEditMode = MutableStateFlow(false)
    val isEditMode = _isEditMode.asStateFlow()
    private val _isAddMode = MutableStateFlow(false)
    val isAddMode = _isAddMode.asStateFlow()
    private val _isPhotoPickerMode = MutableStateFlow(false)
    val isPhotoPickerMode = _isPhotoPickerMode.asStateFlow()

    private val emptyCurrentCustomer = CustomerDataState()
    private val _currentFocusedCustomerState = MutableStateFlow(CustomerDataState())
    val currentFocusedCustomerDataState = _currentFocusedCustomerState.asStateFlow()
    private fun resetState() {
        _isAddMode.value = false
        _isEditMode.value = false
        _currentFocusedCustomerState.value = emptyCurrentCustomer
    }

    fun addCustomerToDb() {
        viewModelScope.launch {
            customerRepository.addCustomer(currentFocusedCustomerDataState.value.convertToCustomer())
        }
        resetState()
    }

    fun updateCustomerToDb() {
        viewModelScope.launch {
            customerRepository.updateCustomer(currentFocusedCustomerDataState.value.convertToCustomer())
        }
        resetState()
    }

    fun deleteCustomerToDb() {
        viewModelScope.launch {
            customerRepository.deleteCustomer(currentFocusedCustomerDataState.value.convertToCustomer())
        }
        resetState()
    }

    fun toggleEdit() {
        _isEditMode.value = !_isEditMode.value
    }

    fun toggleAdd() {
        _isAddMode.value = !_isAddMode.value
        _isEditMode.value = true
        _currentFocusedCustomerState.value = CustomerDataState()
    }

    fun editCurrentCustomerImage(currentCustomerDataState: CustomerDataState) {
        _isEditMode.value =
            (_currentFocusedCustomerState.value.idState == currentCustomerDataState.idState)
        _currentFocusedCustomerState.value = currentCustomerDataState
    }

    fun editCurrentCustomerImage(imageUri: Uri?) {
        _currentFocusedCustomerState.value = CustomerDataState(
            _currentFocusedCustomerState.value.convertToCustomer(),
            photoUri = (imageUri ?: Uri.EMPTY).toString()
        )
        _isPhotoPickerMode.value = false
    }

    fun removeCurrentCustomerImage() {
        _currentFocusedCustomerState.value = CustomerDataState(
            _currentFocusedCustomerState.value.convertToCustomer(), photoUri = Uri.EMPTY.toString()
        )
        _isPhotoPickerMode.value = false
    }


    fun togglePhotoPicker(mode: Boolean) {
        _isPhotoPickerMode.value = mode
    }

    companion object {

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val myRepository =
                    (this[APPLICATION_KEY] as SimpleCustomerRegistrationApplication).customerRepository
                CustomerListViewModel(
                    myRepository
                )
            }
        }
    }

    class CustomerDataState(
        customer: Customer? = null,
        id: Long? = null,
        firstName: TextFieldState? = null,
        lastName: TextFieldState? = null,
        mobileNumber: TextFieldState? = null,
        address: TextFieldState? = null,
        photoUri: String? = null,
    ) {
        var idState = id ?: customer?.id
        var firstNameState = firstName ?: TextFieldState(customer?.firstName ?: "")
        var lastNameState = lastName ?: TextFieldState(customer?.lastName ?: "")
        var mobileNumberState = mobileNumber ?: TextFieldState(customer?.mobileNumber ?: "")
        var addressState = address ?: TextFieldState(customer?.address ?: "")
        var photoUriState = photoUri ?: customer?.photo ?: Uri.EMPTY.toString()
        fun convertToCustomer(): Customer {
            return Customer(
                idState,
                firstNameState.text.toString(),
                lastNameState.text.toString(),
                mobileNumberState.text.toString(),
                addressState.text.toString(),
                photoUriState
            )
        }

        fun checkValidity(): Boolean {
            return FIRSTNAME_REGEX.matches(firstNameState.text) && LASTNAME_REGEX.matches(
                lastNameState.text
            ) && MOBILE_NUMBER_REGEX.matches(mobileNumberState.text) && ADDRESS_REGEX.matches(
                addressState.text
            )
        }

        companion object {
            val FIRSTNAME_REGEX = "^(?=.*[A-Za-z])[A-Za-z ]+\$".toRegex()
            val LASTNAME_REGEX = "^(?=.*[A-Za-z])[A-Za-z ]+\$".toRegex()
            val MOBILE_NUMBER_REGEX = "^[0-9]+\$".toRegex()
            val ADDRESS_REGEX = "^[A-Za-z0-9 .,/#'-]+\$".toRegex()
        }
    }

}
