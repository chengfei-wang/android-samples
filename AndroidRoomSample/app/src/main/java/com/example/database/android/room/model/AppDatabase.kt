package com.example.database.android.room.model

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.database.android.room.model.User
import com.example.database.android.room.model.UserDao

@Database(entities = [User::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
}