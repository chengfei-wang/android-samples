package com.example.database.android.room

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.room.Room
import com.example.database.android.room.global.App
import com.example.database.android.room.global.App.Companion.userDao
import com.example.database.android.room.model.AppDatabase
import com.example.database.android.room.model.User

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        App.database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database")
                .allowMainThreadQueries().build()
        userDao = App.database.userDao()

        App.androidId = Settings.System.getString(applicationContext.contentResolver, Settings.Secure.ANDROID_ID)

        Log.d(javaClass.name, "--------------------------------")
        val hhr = User("54654511", "hhr", "00000000000000000000000000000001")
        val wcf = User("316513113", "wcf", "00000000000000000000000000000002")
        userDao.insertUsers(hhr, wcf)
        var users = userDao.loadAllUsers()
        users.forEach {
            Log.d(javaClass.name, "$it")
        }
        Log.d(javaClass.name, "--------------------------------")
        userDao.deleteUsers(wcf)
        users = userDao.loadAllUsers()
        users.forEach {
            Log.d(javaClass.name, "$it")
        }
        Log.d(javaClass.name, "--------------------------------")
        userDao.insertUsers(wcf)
        users = userDao.loadAllUsersByUsername("wcf")
        users.forEach {
            Log.d(javaClass.name, "$it")
        }
        Log.d(javaClass.name, "--------------------------------")
    }
}