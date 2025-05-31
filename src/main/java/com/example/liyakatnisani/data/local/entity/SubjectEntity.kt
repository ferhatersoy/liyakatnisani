package com.example.liyakatnisani.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SubjectEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String?
)
