package com.ghivalhrvnsyah.storyappdicoding.view.welcome

import androidx.lifecycle.ViewModel
import com.ghivalhrvnsyah.storyappdicoding.data.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class WelcomeViewModel(private val repository: UserRepository): ViewModel() {

    fun getSession() = runBlocking {
        repository.getSession().first()
    }
}