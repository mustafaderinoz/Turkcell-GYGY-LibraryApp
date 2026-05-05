package com.mustafaderinoz.libraryapp.ui.navigation

import android.window.SplashScreen
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mustafaderinoz.libraryapp.ui.screen.HomeScreen
import com.mustafaderinoz.libraryapp.ui.screen.LoginScreen
import com.mustafaderinoz.libraryapp.ui.screen.RegisterScreen
import com.mustafaderinoz.libraryapp.ui.screen.SplashScreen
import com.mustafaderinoz.libraryapp.ui.viewmodel.AuthViewModel
import com.mustafaderinoz.libraryapp.ui.viewmodel.BookViewModel

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {

    val authViewModel: AuthViewModel = viewModel()
    val bookViewModel: BookViewModel= viewModel()

    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route){
            SplashScreen(authViewModel,
            onAuthenticated = { role->
                navController.navigate(Screen.Homepage.route){
                    popUpTo(Screen.Splash.route){inclusive=true}
                }
            },
                onUnauthenticated = {
                     navController.navigate(Screen.Login.route){
                         popUpTo(Screen.Splash.route){inclusive=true}
                     }
                })

        }
        composable(Screen.Login.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onLoginSuccess ={role->
                    navController.navigate(Screen.Homepage.route){
                        popUpTo(Screen.Login.route){inclusive=true}
                    }
                }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                authViewModel = authViewModel,
                onNavigateToLogin = { navController.popBackStack() },
                onRegisterSuccess ={role->
                    navController.navigate(Screen.Homepage.route){
                        popUpTo(Screen.Login.route){inclusive=true}
                    }
                }
            )
        }
        composable(Screen.Homepage.route) {
            HomeScreen(
                authViewModel=authViewModel,
                bookViewModel=bookViewModel,
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        // Ana sayfayı ve arkasındaki her şeyi temizle ki geri tuşuyla dönülmesin
                        popUpTo(Screen.Homepage.route) { inclusive = true }
                    }
                }
            )
        }
    }
}