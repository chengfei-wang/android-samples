package com.example.database.android.room

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.Observer
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

        liveDataQuery()

    }

    private fun normalQuery() {
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

    private fun liveDataQuery() {
        val users = userDao.autoUpdateLoadUsers()

        users.observe(this, Observer {
            Log.d(javaClass.name, "--------------------------------")
            it.forEach { it1->
                Log.d(javaClass.name, "$it1")
            }
        })

        Thread {
            val hhr = User("54654579", "hhr", "00000000000000000000000000000001")
            val wcf = User("31653115", "wcf", "00000000000000000000000000000002")
            val cht = User("24238423", "cht", "00000000000000000000000000000003")

            Thread.sleep(3000)

            userDao.insertUsers(hhr, wcf)

            Thread.sleep(3000)

            userDao.deleteUsers(wcf)

            Thread.sleep(3000)

            userDao.insertUsers(wcf, cht)
        }.start()
    }
}