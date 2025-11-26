package com.elo.libra.ui.profile

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.elo.libra.viewmodel.ProfileViewModel
import com.elo.libra.viewmodel.BookViewModel
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavHostController) {

    val profileViewModel: ProfileViewModel = viewModel()
    val bookViewModel: BookViewModel = viewModel()

    val email = profileViewModel.getUserEmail()
    val books = bookViewModel.books

    val totalBooks = books.size
    val uniqueCategories = books.map { it.genre }.toSet().size
    val recentlyAdded = books.lastOrNull()?.title ?: "Henüz eklenmemiş"

    var totalAdded by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        totalAdded = bookViewModel.getTotalAdded()
        bookViewModel.loadBooks()
    }

    val displayEmail = email ?: "Bilinmiyor"
    val displayInitial = email?.firstOrNull()?.uppercase() ?: "U"

    val progressAnim by animateFloatAsState(
        targetValue = min(totalBooks / 10f, 1f),
        label = "profile-progress"
    )

    val gradient = Brush.verticalGradient(
        listOf(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
            MaterialTheme.colorScheme.background
        )
    )

    Scaffold { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .background(gradient)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ⭐ Profil Başlık + Avatar
            Box(
                Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = displayInitial,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = displayEmail,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Medium
            )

            Spacer(Modifier.height(32.dp))

            // İstatistik Kartı
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(6.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        "Kütüphane Özeti",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(16.dp))

                    Text("📚 Toplam Kitap: $totalBooks", style = MaterialTheme.typography.bodyLarge)
                    Text("🗂️ Kategori Sayısı: $uniqueCategories", style = MaterialTheme.typography.bodyLarge)
                    Text("✨ Son Eklenen: $recentlyAdded", style = MaterialTheme.typography.bodyLarge)
                    Text("📈 Toplam Eklenen Kitap: $totalAdded", style = MaterialTheme.typography.bodyLarge)

                    Spacer(Modifier.height(16.dp))

                    Text("Okuma Seviyesi", style = MaterialTheme.typography.bodyMedium)

                    LinearProgressIndicator(
                        progress = progressAnim,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(MaterialTheme.shapes.medium),
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        text = "${(progressAnim * 100).toInt()}%",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(Modifier.height(40.dp))

            // ⭐ Çıkış Butonu
            Button(
                onClick = {
                    profileViewModel.logout()
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
            ) {
                Text(
                    text = "Çıkış Yap",
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}