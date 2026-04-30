package com.mustafaderinoz.libraryapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.mustafaderinoz.libraryapp.ui.viewmodel.AuthState
import com.mustafaderinoz.libraryapp.ui.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess:(role:String) -> Unit
) {
    val authState by authViewModel.authState.collectAsState()

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var studentNumber by remember { mutableStateOf("") }

    // Yalnızca authState değişirse çalış, tüm recompositionlarda değil..
    LaunchedEffect(authState) {
        if(authState is AuthState.Success)
        {
            onRegisterSuccess((authState as AuthState.Success).role)
            authViewModel.resetState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Kütüphane Sistemi", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Kayıt Ol", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(24.dp))

        // Ad Soyad
        OutlinedTextField(
            enabled = authState !is AuthState.Loading,
            modifier = Modifier.fillMaxWidth(),
            value = fullName,
            label = { Text("Ad Soyad") },
            onValueChange = { fullName = it },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(10.dp))

        // E-posta
        OutlinedTextField(
            enabled = authState !is AuthState.Loading,
            modifier = Modifier.fillMaxWidth(),
            value = email,
            label = { Text("E-posta") },
            onValueChange = { email = it },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        Spacer(modifier = Modifier.height(10.dp))

        // Şifre
        OutlinedTextField(
            enabled = authState !is AuthState.Loading,
            modifier = Modifier.fillMaxWidth(),
            value = password,
            label = { Text("Şifre") },
            onValueChange = { password = it },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(10.dp))

        // Öğrenci No (opsiyonel)
        OutlinedTextField(
            enabled = authState !is AuthState.Loading,
            modifier = Modifier.fillMaxWidth(),
            value = studentNumber,
            label = { Text("Öğrenci No (opsiyonel)") },
            onValueChange = { studentNumber = it },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(10.dp))

        if (authState is AuthState.Loading) {
            Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        } else {
            Button(
                onClick = {
                    authViewModel.signUp(
                        email=email.trim(),
                        password=password,
                        fullName=fullName.trim(),
                        studentNo = studentNumber.trim().ifEmpty { null })
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Kayıt Ol")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = {
                authViewModel.resetState()
                onNavigateToLogin()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Zaten hesabın var mı? Giriş Yap")
        }

        Spacer(modifier = Modifier.height(8.dp))

        when (authState) {
            is AuthState.Success -> Text("Kayıt başarılı!", color = MaterialTheme.colorScheme.primary)
            is AuthState.Error -> Text(
                (authState as AuthState.Error).message,
                color = MaterialTheme.colorScheme.error
            )
            else -> {}
        }
    }
}