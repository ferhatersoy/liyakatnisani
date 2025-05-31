package com.example.liyakatnisani.ui.test

import android.app.Application
import androidx.lifecycle.*
import com.example.liyakatnisani.data.local.AppDatabase
import com.example.liyakatnisani.data.local.dao.QuestionDao
import com.example.liyakatnisani.data.local.entity.QuestionEntity
import kotlinx.coroutines.launch

// Event wrapper
open class Event<out T>(private val content: T) {
    var hasBeenHandled = false
        private set

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }
    @Suppress("unused")
    fun peekContent(): T = content
}

data class TestResultArgs(
    val correctCount: Int,
    val wrongCount: Int,
    val totalQuestions: Int,
    val lectureNoteId: Long // Testi yeniden başlatmak veya kaynağı bilmek için
)

class TestViewModel(
    application: Application,
    private val lectureNoteId: Long,
    private val questionDao: QuestionDao
) : AndroidViewModel(application) {

    private val _questions = MutableLiveData<List<QuestionEntity>>()
    // val questions: LiveData<List<QuestionEntity>> get() = _questions // Gerekirse dışa açılabilir

    private var currentQuestionIndex = 0
    private var correctAnswers = 0
    private var wrongAnswers = 0

    private val _currentQuestion = MutableLiveData<QuestionEntity?>()
    val currentQuestion: LiveData<QuestionEntity?> get() = _currentQuestion

    private val _questionNumberText = MutableLiveData<String>()
    val questionNumberText: LiveData<String> get() = _questionNumberText

    private val _showExplanationButtonVisibility = MutableLiveData(false)
    val showExplanationButtonVisibility: LiveData<Boolean> get() = _showExplanationButtonVisibility

    private val _explanationText = MutableLiveData<String?>()
    val explanationText: LiveData<String?> get() = _explanationText

    private val _explanationVisibility = MutableLiveData(false)
    val explanationVisibility: LiveData<Boolean> get() = _explanationVisibility

    private val _navigateToTestResult = MutableLiveData<Event<TestResultArgs>>()
    val navigateToTestResult: LiveData<Event<TestResultArgs>> get() = _navigateToTestResult

    private val _isAnswerSubmitted = MutableLiveData(false)
    val isAnswerSubmitted: LiveData<Boolean> get() = _isAnswerSubmitted

    private val _radioGroupEnabled = MutableLiveData(true)
    val radioGroupEnabled: LiveData<Boolean> get() = _radioGroupEnabled


    init {
        loadQuestions()
    }

    private fun loadQuestions() {
        viewModelScope.launch {
            val questionList = questionDao.getQuestionsForLectureNote(lectureNoteId).asLiveData().value ?: emptyList()
            // val questionList = questionDao.getQuestionsForLectureNoteSynchronous(lectureNoteId) // Eğer Flow kullanmıyorsanız
            _questions.value = questionList.shuffled() // Soruları karıştır
            if (_questions.value.isNullOrEmpty()) {
                // Hiç soru yoksa testi bitir veya hata göster
                _navigateToTestResult.value = Event(TestResultArgs(0,0,0, lectureNoteId))
            } else {
                displayQuestion()
            }
        }
    }


    private fun displayQuestion() {
        _radioGroupEnabled.value = true
        _isAnswerSubmitted.value = false
        _explanationVisibility.value = false
        _showExplanationButtonVisibility.value = false

        val questionsList = _questions.value
        if (questionsList != null && currentQuestionIndex < questionsList.size) {
            _currentQuestion.value = questionsList[currentQuestionIndex]
            _questionNumberText.value = "Soru: ${currentQuestionIndex + 1}/${questionsList.size}"
        } else {
            // Test bitti
            finishTest()
        }
    }

    fun checkAnswer(selectedOptionId: Int) { // RadioButton ID'si
        if (_isAnswerSubmitted.value == true) return // Cevap zaten gönderilmişse bir şey yapma

        val question = _currentQuestion.value ?: return
        val selectedOptionIndex = when(selectedOptionId) {
            com.example.liyakatnisani.R.id.radioButtonOptionA -> 1
            com.example.liyakatnisani.R.id.radioButtonOptionB -> 2
            com.example.liyakatnisani.R.id.radioButtonOptionC -> 3
            com.example.liyakatnisani.R.id.radioButtonOptionD -> 4
            else -> -1
        }

        if (selectedOptionIndex == question.correctOption) {
            correctAnswers++
        } else {
            wrongAnswers++
        }
        _isAnswerSubmitted.value = true
        _radioGroupEnabled.value = false // Cevap verildikten sonra seçenekler pasifleşsin
        if (!question.explanation.isNullOrBlank()) {
            _showExplanationButtonVisibility.value = true
            _explanationText.value = question.explanation
        }
    }

    fun nextQuestion() {
        if (_isAnswerSubmitted.value == false && _questions.value?.getOrNull(currentQuestionIndex)?.explanation.isNullOrBlank()) {
            // Eğer cevap verilmemişse ve sorunun açıklaması yoksa bir şey yapma (veya uyarı ver)
            // Bu senaryo, kullanıcının cevap vermeden sonraki soruya geçmesini engellemek için
            // Ama eğer açıklama varsa, cevap vermeden açıklamaya bakıp sonraki soruya geçebilir.
            // Şimdilik basit tutalım: cevap vermeden sonraki soruya geçilemesin.
            // Bu durum checkAnswer içinde _isAnswerSubmitted ile yönetiliyor.
            // Eğer kullanıcı cevap vermeden "Sonraki Soru" butonuna basarsa, checkAnswer çağrılmalı (veya -1 gibi bir değerle)
            // Şimdilik UI'da cevap seçilmeden buton aktif olmayacak şekilde düzenlenebilir.
            // Veya burada boş cevap -1 ile checkAnswer çağrılabilir.
            // checkAnswer(-1) // Örneğin, seçilmemişse -1 ile çağır.
        }


        currentQuestionIndex++
        if (currentQuestionIndex < (_questions.value?.size ?: 0)) {
            displayQuestion()
        } else {
            finishTest()
        }
    }

    fun showExplanation() {
        _explanationVisibility.value = true
    }

    private fun finishTest() {
        _navigateToTestResult.value = Event(
            TestResultArgs(
                correctAnswers,
                wrongAnswers,
                _questions.value?.size ?: 0,
                lectureNoteId
            )
        )
    }
}

class TestViewModelFactory(
    private val application: Application,
    private val lectureNoteId: Long
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TestViewModel::class.java)) {
            val questionDao = AppDatabase.getInstance(application).questionDao()
            @Suppress("UNCHECKED_CAST")
            return TestViewModel(application, lectureNoteId, questionDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
