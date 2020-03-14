package com.example.database.android.room.model

import androidx.lifecycle.LiveData
import androidx.room.*


@Entity(
    tableName = "user",
    indices = [
        Index(value = ["uid"], unique = true),
        Index(value = ["username"], unique = true)
    ]
)
class User(
    @PrimaryKey val uid: String,
    val username: String,
    val token: String
) {
    override fun toString(): String {
        return "$uid, $username, $token"
    }
}

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(vararg users: User)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateUsers(vararg users: User): Int

    @Delete
    fun deleteUsers(vararg users: User)

    @Query("SELECT * FROM user")
    fun loadAllUsers(): Array<User>

    @Query("SELECT * FROM user WHERE username == :username")
    fun loadAllUsersByUsername(username: String): Array<User>

    @Query("SELECT * FROM user")
    fun autoUpdateLoadUsers(): LiveData<Array<User>>
}
