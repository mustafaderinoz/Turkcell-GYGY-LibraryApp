package com.mustafaderinoz.libraryapp.data.repository

import com.mustafaderinoz.libraryapp.data.model.Profile
import com.mustafaderinoz.libraryapp.data.supabase.supabase
//import io.github.jan.supabase.auth.OtpType
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.delay
import kotlin.random.Random

class AuthRepository
{
    suspend fun signIn(email: String, password:String) : Result<Unit> = runCatching {
       supabase.auth.signInWith(Email){
           this.email=email
           this.password=password
       }
    }

    //Sign up fonksiyonu
    suspend fun signUp(
        email: String,
        password: String,
        fullName: String,
        studentNo: String?
    ) : Result<Unit> = runCatching {
        supabase.auth.signUpWith(Email){
            this.email = email
            this.password = password
        }

        val userId = supabase.auth.currentUserOrNull()?.id ?: error("Kullanıcı bulunamadı")
        supabase.postgrest["profiles"].insert(
            Profile(userId,"student",fullName,studentNo)
        )

    }

}