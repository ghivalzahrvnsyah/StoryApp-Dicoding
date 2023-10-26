package com.ghivalhrvnsyah.storyappdicoding.data

import com.ghivalhrvnsyah.storyappdicoding.api.ApiConfig
import com.ghivalhrvnsyah.storyappdicoding.api.ApiService
import com.ghivalhrvnsyah.storyappdicoding.data.model.UserModel
import com.ghivalhrvnsyah.storyappdicoding.data.pref.UserPreferences
import com.ghivalhrvnsyah.storyappdicoding.response.ListStoryItem
import com.ghivalhrvnsyah.storyappdicoding.response.LoginResponse
import com.ghivalhrvnsyah.storyappdicoding.response.RegisterResponse
import com.ghivalhrvnsyah.storyappdicoding.response.StoryResponse
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(private val apiService: ApiService, private val userPref: UserPreferences) {


    suspend fun saveSession(user: UserModel) {
        userPref.saveSession(user)

    }

    fun getSession(): Flow<UserModel> {
        return userPref.getSession()
    }
    suspend fun logout() {
        userPref.logout()
    }
    suspend fun register(name: String, email: String, password: String) : RegisterResponse {
        return apiService.register(name, email, password)
    }
    suspend fun login(email:String, password: String): LoginResponse {
        return apiService.login(email, password)
    }

    suspend fun getStories(): List<ListStoryItem?>? {
        return apiService.getStories().listStory
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(apiService: ApiService,userPref: UserPreferences): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userPref)

            }.also { instance = it }
    }
}