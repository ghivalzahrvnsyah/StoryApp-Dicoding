package com.ghivalhrvnsyah.storyappdicoding.data

import com.ghivalhrvnsyah.storyappdicoding.api.ApiConfig
import com.ghivalhrvnsyah.storyappdicoding.data.pref.UserPreferences
import com.ghivalhrvnsyah.storyappdicoding.response.StoryResponse

class StoryRepository private constructor(private val userPref: UserPreferences){

    private val apiService = ApiConfig.getApiService()

    suspend fun getStories(page: Int?, size: Int?, location: Int?): StoryResponse {
        return apiService.getStories(page, size, location)
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        fun getInstance(userPref: UserPreferences): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(userPref)

            }.also { instance = it }
    }
}