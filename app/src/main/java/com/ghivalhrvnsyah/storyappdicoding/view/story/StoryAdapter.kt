package com.ghivalhrvnsyah.storyappdicoding.view.story

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ghivalhrvnsyah.storyappdicoding.databinding.ItemStoryBinding
import com.ghivalhrvnsyah.storyappdicoding.response.ListStoryItem

class StoryAdapter: RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    private lateinit var binding: ItemStoryBinding
    private var listStory = ArrayList<ListStoryItem>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }
    fun setStories(newListStory: ArrayList<ListStoryItem>) {
        listStory.clear()
        listStory.addAll(newListStory)
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StoryViewHolder {
        binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bind(listStory[position])
    }

    override fun getItemCount(): Int = listStory.size


    inner class StoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(story: ListStoryItem) {
            binding.root.setOnClickListener {
                onItemClickCallback?.onItemClicked(story)
            }
            with(binding) {
                Glide.with(itemView.context)
                    .load(story.photoUrl)
                    .centerCrop()
                    .into(imgView)
                tvName.text = story.name
                tvDescription.text = story.description
            }
        }
    }
    interface OnItemClickCallback {
        fun onItemClicked(data: ListStoryItem)
    }
}