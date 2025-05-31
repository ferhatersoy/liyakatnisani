package com.example.liyakatnisani.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = LectureNoteEntity::class,
            parentColumns = ["id"],
            childColumns = ["lectureNoteId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class QuestionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(index = true)
    val lectureNoteId: Long,
    val questionText: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctOption: Int, // 1 (A), 2 (B), 3 (C), 4 (D)
    val difficulty: String, // "Kolay", "Orta", "Zor"
    val explanation: String?
)
