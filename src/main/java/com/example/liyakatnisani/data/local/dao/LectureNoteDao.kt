package com.example.liyakatnisani.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.liyakatnisani.data.local.entity.LectureNoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LectureNoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(lectureNote: LectureNoteEntity): Long

    @Query("SELECT * FROM LectureNoteEntity WHERE subjectId = :subjectId ORDER BY title ASC")
    fun getLectureNotesForSubject(subjectId: Long): Flow<List<LectureNoteEntity>>

    @Query("SELECT * FROM LectureNoteEntity WHERE id = :lectureNoteId")
    fun getLectureNoteById(lectureNoteId: Long): Flow<LectureNoteEntity?>

    @Update
    suspend fun update(lectureNote: LectureNoteEntity)

    @Delete
    suspend fun delete(lectureNote: LectureNoteEntity)
}
