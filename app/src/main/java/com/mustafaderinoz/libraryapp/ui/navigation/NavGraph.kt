package com.mustafaderinoz.libraryapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mustafaderinoz.libraryapp.ui.screen.LoginScreen
import com.mustafaderinoz.libraryapp.ui.screen.RegisterScreen
import com.mustafaderinoz.libraryapp.ui.viewmodel.AuthViewModel

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {

    // ViewModel burada tek bir yerde oluşturuluyor → her iki ekran paylaşıyor
    val authViewModel: AuthViewModel = viewModel()

    NavHost(navController = navController, startDestination = Screen.Login.route) {

        composable(Screen.Login.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                authViewModel = authViewModel,
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }
    }
}