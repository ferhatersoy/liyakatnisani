package com.example.liyakatnisani.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.liyakatnisani.data.local.dao.SubjectDao
import com.example.liyakatnisani.data.local.entity.SubjectEntity

class SubjectsViewModel(private val subjectDao: SubjectDao) : ViewModel() {

    // Dao artık constructor üzerinden geliyor.
    // private val subjectDao = AppDatabase.getInstance(application).subjectDao()

    val subjects: LiveData<List<SubjectEntity>> = subjectDao.getAllSubjects().asLiveData()

}

// ViewModel'a SubjectDao'yu sağlamak için bir Factory
class SubjectsViewModelFactory(
    private val subjectDao: SubjectDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SubjectsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SubjectsViewModel(subjectDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
