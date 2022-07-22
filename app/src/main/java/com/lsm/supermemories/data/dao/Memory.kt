package com.lsm.supermemories.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Memory(

        @PrimaryKey(autoGenerate = true)
    val uid: Long = 0,
        var title: String,
        var memoryText: String,
        val Date: String,
        var memoryImage: String,
        var latitude: Double,
        var longitude: Double
        ){

}