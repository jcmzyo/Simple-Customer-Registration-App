package com.example.simplecustomerregistration.data

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity
data class Customer(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var firstName: String = "",
    var lastName: String = "",
    var mobileNumber: String = "",
    var address: String = "",
    var photo: String = ""
) {


}