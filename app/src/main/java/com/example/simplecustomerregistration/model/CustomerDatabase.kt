package com.example.simplecustomerregistration.model

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import com.example.simplecustomerregistration.data.Customer

@Database(
    entities = [Customer::class],
    version = 1,
    exportSchema = true
)
abstract class CustomerDatabase : RoomDatabase() {
    abstract fun getDAO(): CustomerDao

    companion object
    {
        @Volatile
        private var INSTANCE: CustomerDatabase? = null

        fun getDatabase(
            context: Context
        ): CustomerDatabase {
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CustomerDatabase::class.java,
                    "Customer"
                )
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}