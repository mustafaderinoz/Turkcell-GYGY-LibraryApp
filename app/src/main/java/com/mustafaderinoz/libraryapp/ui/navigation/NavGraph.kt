package com.mustafaderinoz.libraryapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mustafaderinoz.libraryapp.ui.screen.HomeScreen
import com.mustafaderinoz.libraryapp.ui.screen.LoginScreen
import com.mustafaderinoz.libraryapp.ui.screen.MyBorrowsScreen
import com.mustafaderinoz.libraryapp.ui.screen.RegisterScreen
import com.mustafaderinoz.libraryapp.ui.screen.SplashScreen
import com.mustafaderinoz.libraryapp.ui.viewmodel.AuthViewModel
import com.mustafaderinoz.libraryapp.ui.viewmodel.BookViewModel
import com.mustafaderinoz.libraryapp.ui.viewmodel.BorrowViewModel

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {

    val authViewModel: AuthViewModel = viewModel()
    val bookViewModel: BookViewModel = viewModel()
    val borrowViewModel: BorrowViewModel = viewModel()

    NavHost(navController = navController, startDestination = Screen.Splash.route) {

        composable(Screen.Splash.route) {
            SplashScreen(
                authViewModel,
                onAuthenticated = { _ ->
                    navController.navigate(Screen.Homepage.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onUnauthenticated = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onLoginSuccess = { _ ->
                    navController.navigate(Screen.Homepage.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                authViewModel = authViewModel,
                onNavigateToLogin = { navController.popBackStack() },
                onRegisterSuccess = { _ ->
                    navController.navigate(Screen.Homepage.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Homepage.route) {
            HomeScreen(
                authViewModel = authViewModel,
                bookViewModel = bookViewModel,
                borrowViewModel = borrowViewModel,
                onNavigateToMyBorrows = { navController.navigate(Screen.MyBorrows.route) },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true } // back stack'i tamamen temizle
                    }
                }
            )
        }

        composable(Screen.MyBorrows.route) {
            MyBorrowsScreen(
                authViewModel = authViewModel,
                bookViewModel = bookViewModel,
                borrowViewModel = borrowViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}