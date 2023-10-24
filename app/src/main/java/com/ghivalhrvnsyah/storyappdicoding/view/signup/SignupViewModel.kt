package com.ghivalhrvnsyah.storyappdicoding.view.signup

import androidx.lifecycle.ViewModel
import com.ghivalhrvnsyah.storyappdicoding.data.UserRepository
import com.ghivalhrvnsyah.storyappdicoding.response.RegisterResponse

class SignupViewModel(private val repository: UserRepository): ViewModel() {

    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return repository.register(name, email, password)
    }
}