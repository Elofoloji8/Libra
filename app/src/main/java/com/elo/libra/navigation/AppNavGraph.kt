package com.elo.libra.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.elo.libra.ui.auth.LoginScreen
import com.elo.libra.ui.auth.RegisterScreen
import com.elo.libra.ui.home.HomeScreen
import com.elo.libra.ui.profile.ProfileScreen
import com.elo.libra.ui.chatbot.ChatbotScreen
import com.elo.libra.ui.addbook.AddBookScreen

@Composable
fun AppNavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        composable("login") {
            LoginScreen(navController)
        }

        composable("register") {
            RegisterScreen(navController)
        }

        composable("home") {
            HomeScreen(navController)
        }

        composable("profile") {
            ProfileScreen(navController)
        }

        composable("chatbot") {
            ChatbotScreen(navController)
        }
        composable("addbook") {
            AddBookScreen(navController)
        }
        composable("chatbot") {
            ChatbotScreen(navController)
        }
    }
}
