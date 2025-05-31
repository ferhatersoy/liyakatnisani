package com.example.liyakatnisani.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.liyakatnisani.data.local.entity.SubjectEntity
import com.example.liyakatnisani.databinding.ItemSubjectBinding

class SubjectsAdapter(
    private val onItemClick: (SubjectEntity) -> Unit
) : ListAdapter<SubjectEntity, SubjectsAdapter.SubjectViewHolder>(SubjectDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        val binding = ItemSubjectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubjectViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
        val subject = getItem(position)
        holder.bind(subject)
        holder.itemView.setOnClickListener {
            onItemClick(subject)
        }
    }

    inner class SubjectViewHolder(private val binding: ItemSubjectBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(subject: SubjectEntity) {
            binding.textViewSubjectName.text = subject.name
            binding.textViewSubjectDescription.text = subject.description
        }
    }

    class SubjectDiffCallback : DiffUtil.ItemCallback<SubjectEntity>() {
        override fun areItemsTheSame(oldItem: SubjectEntity, newItem: SubjectEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SubjectEntity, newItem: SubjectEntity): Boolean {
            return oldItem == newItem
        }
    }
}
