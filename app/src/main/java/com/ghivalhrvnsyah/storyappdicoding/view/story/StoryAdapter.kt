package com.ghivalhrvnsyah.storyappdicoding.view.story

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ghivalhrvnsyah.storyappdicoding.databinding.ItemStoryBinding
import com.ghivalhrvnsyah.storyappdicoding.response.ListStoryItem

class StoryAdapter : PagingDataAdapter<ListStoryItem, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    private var onItemClickCallback: OnItemClickCallback? = null

    // Mengatur callback untuk item yang diklik
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StoryViewHolder {
        // Membuat tampilan ViewHolder
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            // Mengikat data ke ViewHolder
            holder.bind(story)
        }
    }

    inner class StoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemStoryBinding.bind(itemView)

        // Mengikat data dari model ke tampilan
        fun bind(story: ListStoryItem) {
            binding.root.setOnClickListener {
                onItemClickCallback?.onItemClicked(story)
            }

            Glide.with(itemView.context)
                .load(story.photoUrl)
                .centerCrop()
                .into(binding.imgView)
            binding.tvName.text = story.name
            binding.tvDescription.text = story.description
        }
    }

    // Interface untuk meng-handle klik item
    interface OnItemClickCallback {
        fun onItemClicked(data: ListStoryItem)
    }

    companion object {
        // Callback untuk menentukan kesamaan item
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
