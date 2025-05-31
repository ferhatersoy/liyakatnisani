package com.example.liyakatnisani.ui.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.liyakatnisani.data.local.AppDatabase
import com.example.liyakatnisani.data.local.entity.LectureNoteEntity

class LectureNoteDetailViewModel(application: Application, private val lectureNoteId: Long) : ViewModel() {

    private val lectureNoteDao = AppDatabase.getInstance(application).lectureNoteDao()

    val lectureNote: LiveData<LectureNoteEntity?> = lectureNoteDao.getLectureNoteById(lectureNoteId).asLiveData()

}

class LectureNoteDetailViewModelFactory(
    private val application: Application,
    private val lectureNoteId: Long
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LectureNoteDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LectureNoteDetailViewModel(application, lectureNoteId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
