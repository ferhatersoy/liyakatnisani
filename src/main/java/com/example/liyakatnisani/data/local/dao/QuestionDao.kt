package com.example.liyakatnisani.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.liyakatnisani.data.local.entity.QuestionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(question: QuestionEntity): Long

    @Query("SELECT * FROM QuestionEntity WHERE lectureNoteId = :lectureNoteId")
    fun getQuestionsForLectureNote(lectureNoteId: Long): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM QuestionEntity WHERE id = :questionId")
    fun getQuestionById(questionId: Long): Flow<QuestionEntity?>

    @Update
    suspend fun update(question: QuestionEntity)

    @Delete
    suspend fun delete(question: QuestionEntity)
}
