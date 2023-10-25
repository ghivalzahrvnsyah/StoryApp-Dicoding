package com.ghivalhrvnsyah.storyappdicoding.di

import android.content.Context
import com.ghivalhrvnsyah.storyappdicoding.api.ApiConfig
import com.ghivalhrvnsyah.storyappdicoding.data.StoryRepository
import com.ghivalhrvnsyah.storyappdicoding.data.UserRepository
import com.ghivalhrvnsyah.storyappdicoding.data.pref.UserPreferences
import com.ghivalhrvnsyah.storyappdicoding.data.pref.dataStore
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreferences.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
   fun provideStoryRepository(context: Context): StoryRepository {
        val pref = UserPreferences.getInstance(context.dataStore)
        return StoryRepository.getInstance(pref)
    }
}