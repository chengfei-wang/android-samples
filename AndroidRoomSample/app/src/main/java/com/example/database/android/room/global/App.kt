package com.example.database.android.room.global

import android.app.Application
import androidx.room.Room
import com.example.database.android.room.model.AppDatabase
import com.example.database.android.room.model.UserDao

class App : Application() {
    companion object {
        lateinit var database: AppDatabase
        lateinit var androidId: String
        lateinit var userDao: UserDao
    }

    override fun onCreate() {
        super.onCreate()
    }
}