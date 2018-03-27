package com.prajwal.room.database

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class User(
        @PrimaryKey
        val primaryKey: Int,
        val firstName: String,
        val lastName: String
)