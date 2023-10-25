package com.ghivalhrvnsyah.storyappdicoding.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghivalhrvnsyah.storyappdicoding.data.StoryRepository
import com.ghivalhrvnsyah.storyappdicoding.response.ListStoryItem
import com.ghivalhrvnsyah.storyappdicoding.response.StoryResponse
import kotlinx.coroutines.launch

class MainViewModel(private val repository: StoryRepository): ViewModel() {

    private val _stories = MutableLiveData<List<ListStoryItem>?>()
    val stories: LiveData<List<ListStoryItem>?> get() = _stories

    fun getStories(page: Int?, size: Int?, location: Int?) {

            viewModelScope.launch {
                val response = repository.getStories(page, size, location)
                if (response.listStory != null) {
                    _stories.postValue(response.listStory)
                }
            }

    }
}