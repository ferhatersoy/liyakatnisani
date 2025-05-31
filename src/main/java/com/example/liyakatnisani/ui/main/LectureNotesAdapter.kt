package com.example.liyakatnisani.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.liyakatnisani.data.local.entity.LectureNoteEntity
import com.example.liyakatnisani.databinding.ItemLectureNoteBinding

class LectureNotesAdapter(
    private val onItemClick: (LectureNoteEntity) -> Unit
) : ListAdapter<LectureNoteEntity, LectureNotesAdapter.LectureNoteViewHolder>(LectureNoteDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LectureNoteViewHolder {
        val binding = ItemLectureNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LectureNoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LectureNoteViewHolder, position: Int) {
        val lectureNote = getItem(position)
        holder.bind(lectureNote)
        holder.itemView.setOnClickListener {
            onItemClick(lectureNote)
        }
    }

    inner class LectureNoteViewHolder(private val binding: ItemLectureNoteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(lectureNote: LectureNoteEntity) {
            binding.textViewLectureNoteTitle.text = lectureNote.title
        }
    }

    class LectureNoteDiffCallback : DiffUtil.ItemCallback<LectureNoteEntity>() {
        override fun areItemsTheSame(oldItem: LectureNoteEntity, newItem: LectureNoteEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: LectureNoteEntity, newItem: LectureNoteEntity): Boolean {
            return oldItem == newItem
        }
    }
}
