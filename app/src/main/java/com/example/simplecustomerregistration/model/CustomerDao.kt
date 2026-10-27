package com.example.simplecustomerregistration.model

import androidx.lifecycle.LiveData
import androidx.room3.Dao
import androidx.room3.DaoReturnTypeConverters
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import androidx.room3.livedata.LiveDataDaoReturnTypeConverter
import com.example.simplecustomerregistration.data.Customer

@Dao
@DaoReturnTypeConverters(LiveDataDaoReturnTypeConverter::class)
interface CustomerDao {
    @Insert
    suspend fun addCustomer(customer: Customer): Long

    @Query("SELECT * FROM Customer ORDER BY id DESC")
    fun getAllCustomers(): LiveData<List<Customer>>

    @Update
    suspend fun updateCustomer(customer: Customer)

    @Delete
    suspend fun deleteCustomer(customer: Customer)
}