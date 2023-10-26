package com.ghivalhrvnsyah.storyappdicoding.di

import android.content.Context
import com.ghivalhrvnsyah.storyappdicoding.api.ApiConfig
import com.ghivalhrvnsyah.storyappdicoding.data.UserRepository
import com.ghivalhrvnsyah.storyappdicoding.data.pref.UserPreferences
import com.ghivalhrvnsyah.storyappdicoding.data.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreferences.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return UserRepository.getInstance(apiService, pref)
    }
}