package com.prajwal.room.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAllUser(): List<User>

    @Query("SELECT * FROM user WHERE firstName LIKE :firstName AND lastName LIKE :lastName")
    fun getUserWithName(firstName: String, lastName: String): List<User>

    @Insert
    fun insertUser(user: User)

    @Delete
    fun deleteUser(user: User)
}