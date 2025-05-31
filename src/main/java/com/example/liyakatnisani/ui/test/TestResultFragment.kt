package com.example.liyakatnisani.ui.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.liyakatnisani.databinding.FragmentTestResultBinding

class TestResultFragment : Fragment() {

    private var _binding: FragmentTestResultBinding? = null
    private val binding get() = _binding!!

    private val args: TestResultFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTestResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textViewCorrectAnswers.text = "Doğru Sayısı: ${args.correctCount}"
        binding.textViewWrongAnswers.text = "Yanlış Sayısı: ${args.wrongCount}"
        binding.textViewTotalQuestions.text = "Toplam Soru: ${args.totalQuestions}"

        binding.buttonRestartTest.setOnClickListener {
            // TestFragment'a lectureNoteId ile geri dön ve testi yeniden başlat
            val action = TestResultFragmentDirections.actionTestResultFragmentToTestFragment(args.lectureNoteId)
            findNavController().navigate(action)
        }

        binding.buttonBackToLectureNote.setOnClickListener {
            // LectureNoteDetailFragment'a lectureNoteId ile geri dön
            val action = TestResultFragmentDirections.actionTestResultFragmentToLectureNoteDetailFragment(args.lectureNoteId)
            findNavController().navigate(action)
        }

        binding.buttonBackToSubjects.setOnClickListener {
            // SubjectsFragment'a geri dön, aradaki tüm fragmentları temizle
            val action = TestResultFragmentDirections.actionTestResultFragmentToSubjectsFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
