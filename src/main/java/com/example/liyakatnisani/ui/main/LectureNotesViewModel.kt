package com.example.liyakatnisani.ui.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.liyakatnisani.data.local.AppDatabase
import com.example.liyakatnisani.data.local.entity.LectureNoteEntity

class LectureNotesViewModel(application: Application, private val subjectId: Long) : ViewModel() {

    private val lectureNoteDao = AppDatabase.getInstance(application).lectureNoteDao()

    val lectureNotes: LiveData<List<LectureNoteEntity>> = lectureNoteDao.getLectureNotesForSubject(subjectId).asLiveData()

}

class LectureNotesViewModelFactory(
    private val application: Application,
    private val subjectId: Long
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LectureNotesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LectureNotesViewModel(application, subjectId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
