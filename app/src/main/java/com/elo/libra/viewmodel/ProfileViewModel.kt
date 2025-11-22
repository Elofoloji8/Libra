package com.elo.libra.viewmodel

import androidx.lifecycle.ViewModel
import com.elo.libra.data.repository.AuthRepository

class ProfileViewModel(
    private val repo: AuthRepository = AuthRepository()
) : ViewModel() {

    fun logout() {
        repo.logout()
    }

    fun getUserEmail(): String? {
        return repo.getCurrentUserEmail()
    }
}