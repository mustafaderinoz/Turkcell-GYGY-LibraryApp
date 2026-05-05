package com.mustafaderinoz.libraryapp.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mustafaderinoz.libraryapp.ui.viewmodel.AuthState
import com.mustafaderinoz.libraryapp.ui.viewmodel.AuthViewModel

// Özel Renk Tanımlamaları
private val AppBackground = Color(0xFFF8F9FA)
private val AppPrimary = Color(0xFF4F46E5) // Indigo 600 (Sadece butonlarda kullanılacak)
private val TextMain = Color(0xFF1F2937)
private val TextLight = Color(0xFF6B7280)
private val BorderLight = Color(0xFFE5E7EB)
private val ErrorColor = Color(0xFFDC2626)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onLoginSuccess: (role: String) -> Unit,
    onNavigateToRegister: () -> Unit
) {

    val authState by authViewModel.authState.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            onLoginSuccess((authState as AuthState.Success).role)
            authViewModel.resetState()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = AppBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Kütüphane Sistemi",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = TextMain
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Hesabınıza giriş yapın",
                style = MaterialTheme.typography.bodyLarge,
                color = TextLight
            )

            Spacer(modifier = Modifier.height(40.dp))

            // E-posta Alanı
            OutlinedTextField(
                enabled = authState !is AuthState.Loading,
                modifier = Modifier.fillMaxWidth(),
                value = email,
                label = { Text("E-posta") },
                onValueChange = { value -> email = value },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape = RoundedCornerShape(16.dp),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Email, contentDescription = "E-posta")
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color(0xFFF3F4F6),
                    focusedBorderColor = TextMain, // Odaklanınca kenarlık koyu gri
                    unfocusedBorderColor = BorderLight,
                    cursorColor = TextMain, // İmleç rengi düzeltildi
                    focusedLabelColor = TextMain, // Yukarı kayan yazının mor olması engellendi
                    unfocusedLabelColor = TextLight,
                    focusedLeadingIconColor = TextMain, // İkon rengi uyumlu hale getirildi
                    unfocusedLeadingIconColor = TextLight,
                    focusedTextColor = TextMain,
                    unfocusedTextColor = TextMain
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Şifre Alanı
            OutlinedTextField(
                enabled = authState !is AuthState.Loading,
                modifier = Modifier.fillMaxWidth(),
                value = password,
                label = { Text("Şifre") },
                onValueChange = { value -> password = value },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(16.dp),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = "Şifre")
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color(0xFFF3F4F6),
                    focusedBorderColor = TextMain,
                    unfocusedBorderColor = BorderLight,
                    cursorColor = TextMain,
                    focusedLabelColor = TextMain,
                    unfocusedLabelColor = TextLight,
                    focusedLeadingIconColor = TextMain,
                    unfocusedLeadingIconColor = TextLight,
                    focusedTextColor = TextMain,
                    unfocusedTextColor = TextMain
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Giriş Yap Butonu
            if (authState is AuthState.Loading) {
                Button(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppPrimary)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.5.dp,
                        color = Color.White
                    )
                }
            } else {
                Button(
                    onClick = {
                        authViewModel.signIn(email, password)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppPrimary)
                ) {
                    Text(
                        text = "Giriş Yap",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Kayıt Ol Butonu
            OutlinedButton(
                onClick = {
                    authViewModel.resetState()
                    onNavigateToRegister()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, BorderLight),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = TextMain,
                    containerColor = Color.White
                )
            ) {
                Text(
                    text = "Hesabın yok mu? Kayıt Ol",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (authState is AuthState.Success) {
                Text(
                    text = "Giriş Yapıldı, yönlendiriliyorsunuz...",
                    color = Color(0xFF16A34A),
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            } else if (authState is AuthState.Error) {
                Surface(
                    color = Color(0xFFFEF2F2),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = (authState as AuthState.Error).message,
                        color = ErrorColor,
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}