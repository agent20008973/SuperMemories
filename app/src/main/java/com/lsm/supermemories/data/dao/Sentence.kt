package com.lsm.supermemories.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Sentence(
    @PrimaryKey(autoGenerate = true)
    val uid: Long = 0,
    var contents: String,
    var source: String,
    var randomDate: String = ""
){

}
