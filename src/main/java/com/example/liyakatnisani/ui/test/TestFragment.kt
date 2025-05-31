package com.example.liyakatnisani.ui.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.liyakatnisani.R
import com.example.liyakatnisani.databinding.FragmentTestBinding

class TestFragment : Fragment() {

    private var _binding: FragmentTestBinding? = null
    private val binding get() = _binding!!

    private val args: TestFragmentArgs by navArgs()
    private val viewModel: TestViewModel by viewModels {
        TestViewModelFactory(requireActivity().application, args.lectureNoteId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        setupListeners()
    }

    private fun observeViewModel() {
        viewModel.currentQuestion.observe(viewLifecycleOwner) { question ->
            question?.let {
                binding.textViewQuestionText.text = it.questionText
                binding.radioButtonOptionA.text = it.optionA
                binding.radioButtonOptionB.text = it.optionB
                binding.radioButtonOptionC.text = it.optionC
                binding.radioButtonOptionD.text = it.optionD
                binding.radioGroupOptions.clearCheck() // Her yeni soruda seçimi temizle
            }
        }

        viewModel.questionNumberText.observe(viewLifecycleOwner) { text ->
            binding.textViewQuestionNumber.text = text
        }

        viewModel.navigateToTestResult.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { resultArgs ->
                val action = TestFragmentDirections.actionTestFragmentToTestResultFragment(
                    correctCount = resultArgs.correctCount,
                    wrongCount = resultArgs.wrongCount,
                    totalQuestions = resultArgs.totalQuestions,
                    lectureNoteId = resultArgs.lectureNoteId
                )
                findNavController().navigate(action)
            }
        }

        viewModel.isAnswerSubmitted.observe(viewLifecycleOwner) { isSubmitted ->
            binding.buttonNextQuestion.text = if (isSubmitted) "Sonraki Soru" else "Cevapla"
        }

        viewModel.radioGroupEnabled.observe(viewLifecycleOwner) { isEnabled ->
            binding.radioButtonOptionA.isEnabled = isEnabled
            binding.radioButtonOptionB.isEnabled = isEnabled
            binding.radioButtonOptionC.isEnabled = isEnabled
            binding.radioButtonOptionD.isEnabled = isEnabled
        }

        viewModel.showExplanationButtonVisibility.observe(viewLifecycleOwner) {isVisible ->
            binding.buttonShowExplanation.isVisible = isVisible
        }

        viewModel.explanationText.observe(viewLifecycleOwner) { explanation ->
            binding.textViewExplanation.text = explanation
        }

        viewModel.explanationVisibility.observe(viewLifecycleOwner) { isVisible ->
            binding.textViewExplanation.isVisible = isVisible
        }
    }

    private fun setupListeners() {
        binding.buttonNextQuestion.setOnClickListener {
            val selectedOptionId = binding.radioGroupOptions.checkedRadioButtonId
            if (selectedOptionId == -1 && viewModel.isAnswerSubmitted.value == false) {
                Toast.makeText(context, "Lütfen bir cevap seçin.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (viewModel.isAnswerSubmitted.value == false) { // Henüz cevaplanmadıysa
                viewModel.checkAnswer(selectedOptionId)
            } else { // Cevaplandıysa, sonraki soruya geç
                viewModel.nextQuestion()
            }
        }

        binding.buttonShowExplanation.setOnClickListener {
            viewModel.showExplanation()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
