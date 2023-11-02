package com.ghivalhrvnsyah.storyappdicoding.view.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghivalhrvnsyah.storyappdicoding.data.UserRepository
import com.ghivalhrvnsyah.storyappdicoding.response.ListStoryItem
import com.ghivalhrvnsyah.storyappdicoding.response.StoryResponse
import kotlinx.coroutines.launch

class MapsViewModel(private val repository: UserRepository) : ViewModel() {


    fun getMarker(): LiveData<StoryResponse> {
        val _marker = MutableLiveData<StoryResponse>()
        viewModelScope.launch {
            try {
                val marker = repository.getMarkerStories()
                _marker.postValue(marker)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return _marker
    }

}