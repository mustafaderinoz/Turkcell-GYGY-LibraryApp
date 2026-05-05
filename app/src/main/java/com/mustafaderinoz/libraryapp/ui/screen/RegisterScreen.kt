package com.mustafaderinoz.libraryapp.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
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
private val AppPrimary = Color(0xFF4F46E5)
private val TextMain = Color(0xFF1F2937)
private val TextLight = Color(0xFF6B7280)
private val BorderLight = Color(0xFFE5E7EB)
private val ErrorColor = Color(0xFFDC2626)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: (role: String) -> Unit
) {
    val authState by authViewModel.authState.collectAsState()

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var studentNumber by remember { mutableStateOf("") }

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            onRegisterSuccess((authState as AuthState.Success).role)
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
                text = "Yeni bir hesap oluşturun",
                style = MaterialTheme.typography.bodyLarge,
                color = TextLight
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Ortak TextField Renk Teması
            val customTextFieldColors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color(0xFFF3F4F6),
                focusedBorderColor = TextMain, // Koyu Gri Kenarlık
                unfocusedBorderColor = BorderLight,
                cursorColor = TextMain, // Koyu Gri İmleç
                focusedLabelColor = TextMain, // Koyu Gri Kayan Yazı
                unfocusedLabelColor = TextLight,
                focusedLeadingIconColor = TextMain,
                unfocusedLeadingIconColor = TextLight,
                focusedTextColor = TextMain,
                unfocusedTextColor = TextMain
            )

            // Ad Soyad
            OutlinedTextField(
                enabled = authState !is AuthState.Loading,
                modifier = Modifier.fillMaxWidth(),
                value = fullName,
                label = { Text("Ad Soyad") },
                onValueChange = { fullName = it },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Person, contentDescription = "Ad Soyad")
                },
                colors = customTextFieldColors
            )
            Spacer(modifier = Modifier.height(12.dp))

            // E-posta
            OutlinedTextField(
                enabled = authState !is AuthState.Loading,
                modifier = Modifier.fillMaxWidth(),
                value = email,
                label = { Text("E-posta") },
                onValueChange = { email = it },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape = RoundedCornerShape(16.dp),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Email, contentDescription = "E-posta")
                },
                colors = customTextFieldColors
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Şifre
            OutlinedTextField(
                enabled = authState !is AuthState.Loading,
                modifier = Modifier.fillMaxWidth(),
                value = password,
                label = { Text("Şifre") },
                onValueChange = { password = it },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(16.dp),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = "Şifre")
                },
                colors = customTextFieldColors
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Öğrenci No
            OutlinedTextField(
                enabled = authState !is AuthState.Loading,
                modifier = Modifier.fillMaxWidth(),
                value = studentNumber,
                label = { Text("Öğrenci No (Opsiyonel)") },
                onValueChange = { studentNumber = it },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(16.dp),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.AccountBox, contentDescription = "Öğrenci No")
                },
                colors = customTextFieldColors
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Kayıt Ol Butonu
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
                        authViewModel.signUp(
                            email = email.trim(),
                            password = password,
                            fullName = fullName.trim(),
                            studentNo = studentNumber.trim().ifEmpty { null }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppPrimary)
                ) {
                    Text(
                        text = "Kayıt Ol",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Giriş Yap'a Yönlendirme Butonu
            OutlinedButton(
                onClick = {
                    authViewModel.resetState()
                    onNavigateToLogin()
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
                    text = "Zaten hesabın var mı? Giriş Yap",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            when (authState) {
                is AuthState.Success -> {
                    Text(
                        text = "Kayıt başarılı, yönlendiriliyorsunuz...",
                        color = Color(0xFF16A34A),
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                }
                is AuthState.Error -> {
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
                else -> {}
            }
        }
    }
}