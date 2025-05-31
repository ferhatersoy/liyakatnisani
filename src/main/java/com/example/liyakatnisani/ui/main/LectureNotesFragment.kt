package com.example.liyakatnisani.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.liyakatnisani.databinding.FragmentLectureNotesBinding

class LectureNotesFragment : Fragment() {

    private var _binding: FragmentLectureNotesBinding? = null
    private val binding get() = _binding!!

    private val args: LectureNotesFragmentArgs by navArgs()
    private val lectureNotesViewModel: LectureNotesViewModel by viewModels {
        LectureNotesViewModelFactory(requireActivity().application, args.subjectId)
    }
    private lateinit var lectureNotesAdapter: LectureNotesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLectureNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        lectureNotesViewModel.lectureNotes.observe(viewLifecycleOwner) { notes ->
            lectureNotesAdapter.submitList(notes)
        }
    }

    private fun setupRecyclerView() {
        lectureNotesAdapter = LectureNotesAdapter { lectureNote ->
            // Ders notu tıklandığında LectureNoteDetailFragment'a lectureNoteId ile navigate et
            val action = LectureNotesFragmentDirections.actionLectureNotesFragmentToLectureNoteDetailFragment(lectureNote.id)
            findNavController().navigate(action)
        }
        binding.recyclerViewLectureNotes.apply {
            adapter = lectureNotesAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
