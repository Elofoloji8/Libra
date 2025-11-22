package com.elo.libra.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import com.elo.libra.data.repository.AuthRepository

class AuthViewModel(
    private val repo: AuthRepository = AuthRepository()
) : ViewModel() {

    val loading = mutableStateOf(false)
    val error = mutableStateOf<String?>(null)

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        loading.value = true

        repo.login(email, password) { success, message ->
            loading.value = false
            if (success) onSuccess()
            else error.value = message
        }
    }

    fun register(email: String, password: String, onSuccess: () -> Unit) {
        loading.value = true

        repo.register(email, password) { success, message ->
            loading.value = false
            if (success) onSuccess()
            else error.value = message
        }
    }
}