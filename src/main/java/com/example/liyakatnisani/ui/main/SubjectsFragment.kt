package com.example.liyakatnisani.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.liyakatnisani.data.local.AppDatabase
import com.example.liyakatnisani.databinding.FragmentSubjectsBinding

class SubjectsFragment : Fragment() {

    private var _binding: FragmentSubjectsBinding? = null
    private val binding get() = _binding!!

    private val subjectsViewModel: SubjectsViewModel by viewModels {
        val subjectDao = AppDatabase.getInstance(requireContext().applicationContext).subjectDao()
        SubjectsViewModelFactory(subjectDao)
    }
    private lateinit var subjectsAdapter: SubjectsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubjectsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        subjectsViewModel.subjects.observe(viewLifecycleOwner) { subjects ->
            subjectsAdapter.submitList(subjects)
        }
    }

    private fun setupRecyclerView() {
        subjectsAdapter = SubjectsAdapter { subject ->
            // Konu tıklandığında LectureNotesFragment'a subjectId ile navigate et
            val action = SubjectsFragmentDirections.actionSubjectsFragmentToLectureNotesFragment(subject.id)
            findNavController().navigate(action)
        }
        binding.recyclerViewSubjects.apply {
            adapter = subjectsAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // ViewBinding'i temizle
    }
}
