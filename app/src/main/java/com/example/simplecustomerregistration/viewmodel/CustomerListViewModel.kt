package com.example.simplecustomerregistration.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.simplecustomerregistration.data.Customer
import com.example.simplecustomerregistration.model.CustomerDatabase
import com.example.simplecustomerregistration.model.CustomerRepository
import com.example.simplecustomerregistration.view.SimpleCustomerRegistrationApplication
import kotlinx.coroutines.launch

class CustomerListViewModel(
    val customerRepository: CustomerRepository
) : ViewModel() {

    val customerList: LiveData<List<Customer>> = customerRepository.getAllCustomers()

    fun addCustomer(): Boolean{
        viewModelScope.launch {
            customerRepository.addCustomer(
                fn = "Jihyo",
                ln = "Park"
            )
        }

        return true
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

}
