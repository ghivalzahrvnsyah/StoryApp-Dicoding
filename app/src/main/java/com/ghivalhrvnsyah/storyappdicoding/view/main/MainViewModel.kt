package com.ghivalhrvnsyah.storyappdicoding.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ghivalhrvnsyah.storyappdicoding.data.UserRepository
import com.ghivalhrvnsyah.storyappdicoding.data.pref.UserPreferences
import com.ghivalhrvnsyah.storyappdicoding.response.ListStoryItem
import kotlinx.coroutines.launch
import java.io.File

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    private val _stories = MutableLiveData<List<ListStoryItem?>?>()
    val stories: LiveData<List<ListStoryItem?>?> = _stories

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    init {
        getStories()
    }
    fun uploadImage(file: File, description: String) = repository.uploadImage(file, description)

   fun getStories(): List<ListStoryItem?>? {
        viewModelScope.launch {
            try {
                _isLoading.postValue(true)
                val stories = repository.getStories()
                _stories.postValue(stories)
                _isLoading.postValue(false)
            } catch (e: Exception) {
                _isLoading.postValue(false)
                Log.d("MainViewModel", e.message.toString())
            }
        }
        return null
   }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }


    fun getThemeSettings() = repository.getThemeSettings().asLiveData()
    fun saveThemeSetting(checked: Boolean) {
        viewModelScope.launch {
            repository.saveThemeSetting(checked)
        }
    }
}