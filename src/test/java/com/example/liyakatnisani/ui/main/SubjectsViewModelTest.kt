package com.example.liyakatnisani.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.liyakatnisani.data.local.dao.SubjectDao
import com.example.liyakatnisani.data.local.entity.SubjectEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import androidx.lifecycle.Observer

@ExperimentalCoroutinesApi
class SubjectsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule() // LiveData için anında execution

    private lateinit var viewModel: SubjectsViewModel
    private lateinit var mockSubjectDao: SubjectDao
    private val testDispatcher = UnconfinedTestDispatcher()

    // LiveData'yı gözlemlemek için bir Observer
    private lateinit var observer: Observer<List<SubjectEntity>>

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockSubjectDao = mock()
        viewModel = SubjectsViewModel(mockSubjectDao)
    }

    @Test
    fun `subjects LiveData should be updated from dao`() = runTest {
        val testSubjects = listOf(
            SubjectEntity(id = 1, name = "Tarih", description = "Geçmiş olaylar"),
            SubjectEntity(id = 2, name = "Coğrafya", description = "Yeryüzü şekilleri")
        )
        // Mock DAO'nun getAllSubjects çağrıldığında testSubjects listesini bir Flow olarak döndürmesini sağla
        whenever(mockSubjectDao.getAllSubjects()).thenReturn(flowOf(testSubjects))

        // LiveData'yı observe et
        val observedValues = mutableListOf<SubjectEntity>()
        observer = Observer<List<SubjectEntity>> { subjects ->
            observedValues.clear()
            observedValues.addAll(subjects)
        }
        viewModel.subjects.observeForever(observer)

        // ViewModel yeniden oluşturulduğunda veya DAO'dan yeni veri geldiğinde LiveData güncellenir.
        // Bu test senaryosunda, ViewModel başlatıldığında getAllSubjects çağrılır ve LiveData güncellenir.
        // UnconfinedTestDispatcher kullandığımız için, Flow'dan gelen veri hemen LiveData'ya yansır.

        assertEquals(testSubjects.size, observedValues.size)
        if (observedValues.isNotEmpty()) { // Ekstra kontrol, eğer observedValues boşsa index hatası almamak için
            assertEquals(testSubjects[0].name, observedValues[0].name)
            assertEquals(testSubjects[1].description, observedValues[1].description)
        } else {
            // Eğer observedValues boşsa, bu testin mantığında bir sorun var demektir.
            // Örneğin, Flow'dan veri gelmemiş olabilir veya Observer tetiklenmemiş olabilir.
            // Bu durumu da bir assertion ile belirtebiliriz.
            assertEquals("Observed values should not be empty", true, observedValues.isNotEmpty())
        }

        // Test bittikten sonra observer'ı kaldır
        viewModel.subjects.removeObserver(observer)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        // Eğer observer hala ekliyse (test başarısız olursa diye), burada kaldır
        if (::observer.isInitialized && viewModel.subjects.hasObservers()) {
             viewModel.subjects.removeObserver(observer)
        }
    }
}
