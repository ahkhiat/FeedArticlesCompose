package com.devid_academy.feedarticlescompose.ui.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.devid_academy.feedarticlescompose.data.dto.ArticleDTO
import com.devid_academy.feedarticlescompose.ui.screen.auth.AuthViewModel
import com.devid_academy.feedarticlescompose.ui.screen.auth.LoginScreen
import com.devid_academy.feedarticlescompose.ui.screen.auth.LoginViewModel
import com.devid_academy.feedarticlescompose.ui.screen.auth.RegisterScreen
import com.devid_academy.feedarticlescompose.ui.screen.splash.SplashScreen
import com.devid_academy.feedarticlescompose.ui.screen.splash.SplashViewModel
import com.example.feedarticlescompose.ui.screen.edit.EditScreen
import com.example.feedarticlescompose.ui.screen.main.MainScreen
import com.example.feedarticlescompose.ui.screen.main.MainViewModel

@Composable
fun MyAppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable(Screen.Splash.route) {
            val splashViewModel: SplashViewModel = hiltViewModel()
            SplashScreen(navController, splashViewModel)
        }
        composable(Screen.Login.route) {
            val loginViewModel: LoginViewModel = hiltViewModel()
            LoginScreen(navController, loginViewModel)
        }
        composable(Screen.Register.route) {
            RegisterScreen(navController)
        }
        composable(Screen.Main.route) {
            val mainViewModel: MainViewModel = hiltViewModel()
            val authViewModel: AuthViewModel = hiltViewModel()
            MainScreen(navController,mainViewModel, authViewModel)
        }
        composable(Screen.Edit.route) {
            EditScreen()
        }
//        composable(
//            Screen.Profile.route,
//            enterTransition = {
//                slideInVertically { -it } },
//            exitTransition = { slideOutVertically { -it } }
//            ) {
//                val authViewModel: AuthViewModel = hiltViewModel()
//                val profileViewModel: ProfileViewModel = hiltViewModel()
//                ProfileScreen(navController, authViewModel, profileViewModel)
//        }
//        composable(Screen.CreateEvent.route) {
//            val createEventViewModel: CreateEventViewModel = hiltViewModel()
//            CreateEventScreen(navController, createEventViewModel)
//        }
    }
}



sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Main : Screen("main")
    object Login : Screen("login")
    object Register : Screen("register")
    object Edit : Screen("edit")

}



