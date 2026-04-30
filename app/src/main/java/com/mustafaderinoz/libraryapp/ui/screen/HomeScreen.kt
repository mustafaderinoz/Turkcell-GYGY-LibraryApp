package com.mustafaderinoz.libraryapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mustafaderinoz.libraryapp.ui.viewmodel.AuthViewModel


@Composable
fun HomeScreen(
    authViewModel: AuthViewModel
){
    val profileState by authViewModel.profile.collectAsState()

    Column(modifier=Modifier.fillMaxSize(),horizontalAlignment=Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
        Text(profileState?.fullName?:"Profil Bulunamadı")
    }

}