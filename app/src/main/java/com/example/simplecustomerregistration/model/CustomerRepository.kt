package com.example.simplecustomerregistration.model

import androidx.lifecycle.LiveData
import com.example.simplecustomerregistration.data.Customer


class CustomerRepository(
    private val customerDao: CustomerDao,
) {
    fun getAllCustomers(): LiveData<List<Customer>> {
        val ret = customerDao.getAllCustomers()
        println("getAllCustomers(): ${ret.value?.size}")
        return ret
    }

    suspend fun addCustomer(
        customer: Customer
    ) {
        customerDao.addCustomer(customer)
    }

    suspend fun updateCustomer(
        customer: Customer
    ) {
        val id = customerDao.updateCustomer(customer)
    }

    suspend fun deleteCustomer(
        customer: Customer
    ) {
        customerDao.deleteCustomer(customer)
    }
}