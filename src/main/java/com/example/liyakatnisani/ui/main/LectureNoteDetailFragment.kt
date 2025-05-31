package com.example.liyakatnisani.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.liyakatnisani.databinding.FragmentLectureNoteDetailBinding

class LectureNoteDetailFragment : Fragment() {

    private var _binding: FragmentLectureNoteDetailBinding? = null
    private val binding get() = _binding!!

    private val args: LectureNoteDetailFragmentArgs by navArgs()
    private val lectureNoteDetailViewModel: LectureNoteDetailViewModel by viewModels {
        LectureNoteDetailViewModelFactory(requireActivity().application, args.lectureNoteId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLectureNoteDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lectureNoteDetailViewModel.lectureNote.observe(viewLifecycleOwner) { lectureNote ->
            lectureNote?.let {
                binding.textViewLectureNoteDetailTitle.text = it.title
                binding.textViewLectureNoteDetailContent.text = it.content
            }
        }

        binding.buttonStartTest.setOnClickListener {
            // TestFragment'a lectureNoteId ile navigate et
            val action = LectureNoteDetailFragmentDirections.actionLectureNoteDetailFragmentToTestFragment(args.lectureNoteId)
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
