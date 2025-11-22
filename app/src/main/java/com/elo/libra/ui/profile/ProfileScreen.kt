package com.elo.libra.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.elo.libra.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(navController: NavHostController) {

    val viewModel: ProfileViewModel = viewModel()
    val email = viewModel.getUserEmail()

    Scaffold { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Profil", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(24.dp))

            Text(text = "Email: $email", style = MaterialTheme.typography.titleMedium)

            Spacer(Modifier.height(32.dp))

            Button(onClick = {
                viewModel.logout()

                navController.navigate("login") {
                    popUpTo("home") { inclusive = true }
                }
            }) {
                Text("Çıkış Yap")
            }
        }
    }
}