package com.example.simplecustomerregistration.model

import androidx.lifecycle.LiveData
import com.example.simplecustomerregistration.data.Customer

class CustomerRepository(
    private val customerDao: CustomerDao
) {
    suspend fun addCustomer(
        fn: String = "",
        ln: String = "",
        mn: String = "",
        a: String = "",
        p: String = "") {
        customerDao.addCustomer(Customer(
            firstName = fn,
            lastName = ln,
            mobileNumber = mn,
            address = a,
            photo = p
        ))
    }

    fun getAllCustomers(): LiveData<List<Customer>> {
        return customerDao.getAllCustomers()
    }
}