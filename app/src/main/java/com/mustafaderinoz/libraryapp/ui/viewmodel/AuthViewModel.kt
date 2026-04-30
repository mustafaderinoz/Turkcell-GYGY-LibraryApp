package com.mustafaderinoz.libraryapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mustafaderinoz.libraryapp.data.model.Profile
import com.mustafaderinoz.libraryapp.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


// Sistem bu 4ünden birinde olabilir.
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val role: String) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel :ViewModel(){
    private val repository = AuthRepository()
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState;
    private val _profile=MutableStateFlow<Profile?>(null)
    val profile:StateFlow<Profile?> =_profile

    fun signIn(email: String, password: String)
    {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            repository
                .signIn(email, password)
                .onSuccess {
                    _authState.value = AuthState.Success("student")
                    val userId=repository.getCurrentUserId()
                    if(userId!=null){
                        val profile=repository.getProfile(userId)
                        _profile.value=profile
                    }else{
                        _authState.value=AuthState.Error("Profil Bulunamadı")
                    }
                }
                .onFailure { ex -> _authState.value = AuthState.Error(ex.message ?: "Giriş başarısız") }
        }
    }
    // ✅ Yeni eklendi
    fun signUp(
        email: String,
        password: String,
        fullName:String,
        studentNo:String?
        ) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            repository
                .signUp(email, password,fullName,studentNo)
                .onSuccess {
                    _authState.value = AuthState.Success("student")
                    val userId=repository.getCurrentUserId()
                    if(userId!=null){
                        val profile=repository.getProfile(userId)
                        _profile.value=profile
                    }else{
                        _authState.value=AuthState.Error("Profil Bulunamadı")
                    }
                }
                .onFailure { ex -> _authState.value = AuthState.Error(ex.message ?: "Kayıt başarısız") }
        }
    }

    // Ekranlar arası geçişte state'i sıfırlamak için
    fun resetState() {
        _authState.value = AuthState.Idle
    }
}