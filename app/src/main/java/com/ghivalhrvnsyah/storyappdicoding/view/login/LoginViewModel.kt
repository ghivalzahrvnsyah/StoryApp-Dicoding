package com.ghivalhrvnsyah.storyappdicoding.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import com.ghivalhrvnsyah.storyappdicoding.data.UserRepository
import com.ghivalhrvnsyah.storyappdicoding.data.model.UserModel
import com.ghivalhrvnsyah.storyappdicoding.response.LoginResponse

class LoginViewModel(private val repository: UserRepository): ViewModel() {

    suspend fun login(email: String, password: String): LoginResponse {
        return repository.login(email, password)
    }

}