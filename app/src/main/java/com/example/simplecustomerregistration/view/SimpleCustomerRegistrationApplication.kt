package com.example.simplecustomerregistration.view

import android.app.Application
import com.example.simplecustomerregistration.model.CustomerDatabase
import com.example.simplecustomerregistration.model.CustomerRepository

class SimpleCustomerRegistrationApplication : Application() {
    private val customerDb by lazy { CustomerDatabase.getDatabase(this) }
    val customerRepository by lazy { CustomerRepository(customerDb.getDAO()) }
}