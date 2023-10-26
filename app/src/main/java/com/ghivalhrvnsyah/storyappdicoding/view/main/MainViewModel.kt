package com.ghivalhrvnsyah.storyappdicoding.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghivalhrvnsyah.storyappdicoding.data.UserRepository
import com.ghivalhrvnsyah.storyappdicoding.response.ListStoryItem
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    private val _stories = MutableLiveData<List<ListStoryItem?>?>()
    val stories: LiveData<List<ListStoryItem?>?> get() = _stories

    init {
        getStories()
    }

   fun getStories(): List<ListStoryItem?>? {
        viewModelScope.launch {
            try {
                val stories = repository.getStories()
                _stories.postValue(stories)
            } catch (e: Exception) {
                Log.d("MainViewModel", e.message.toString())
            }
        }
        return null
   }
}