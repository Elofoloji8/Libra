package com.elo.libra.ui.profile

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.elo.libra.viewmodel.BookViewModel
import com.elo.libra.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavHostController) {
    val profileViewModel: ProfileViewModel = viewModel()
    val bookViewModel: BookViewModel = viewModel()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val email = profileViewModel.getUserEmail()
    val books = bookViewModel.books

    val totalBooks = books.size
    val uniqueCategories = books.map { it.genre }.toSet().size
    val recentlyAdded = books.lastOrNull()?.title ?: "Henüz eklenmemiş"

    var totalAdded by remember { mutableStateOf(0) }

    // 🔹 Veriler yükleniyor
    LaunchedEffect(Unit) {
        bookViewModel.loadBooks()
        totalAdded = bookViewModel.getTotalAdded(context)
    }

    val progressAnim by animateFloatAsState(
        targetValue = min(totalBooks / 10f, 1f),
        label = "progress"
    )

    val gradient = Brush.verticalGradient(
        listOf(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
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

            // 👤 Avatar (arka planla birlikte)
            Box(
                Modifier
                    .size(130.dp)
                    .background(
                        brush = Brush.radialGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.25f),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = email?.firstOrNull()?.uppercase() ?: "U",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = email ?: "Bilinmiyor",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(Modifier.height(32.dp))

            // 📊 İstatistik Kartı
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text(
                        "Kütüphane Özeti",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(Modifier.height(16.dp))

                    StatRow(Icons.Default.Book, "Toplam Kitap", totalBooks.toString())
                    StatRow(Icons.Default.Category, "Kategori Sayısı", uniqueCategories.toString())
                    StatRow(Icons.Default.Star, "Son Eklenen", recentlyAdded)
                    StatRow(Icons.Default.AutoGraph, "Toplam Eklenen", totalAdded.toString())

                    Spacer(Modifier.height(20.dp))
                    Text("Okuma Seviyesi", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium))

                    Spacer(Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(28.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                    ) {
                        // Doluluk barı
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(progressAnim)
                                .background(MaterialTheme.colorScheme.primary)
                        )

                        // Yüzde metni barın içinde ortalanmış şekilde
                        Text(
                            text = "${(progressAnim * 100).toInt()}%",
                            modifier = Modifier.align(Alignment.Center),
                            color = if (progressAnim > 0.5f)
                                MaterialTheme.colorScheme.onPrimary
                            else
                                MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }

                    Spacer(Modifier.height(8.dp))
                    Text(
                        "🎯 Hedef: 10 kitap — devam et!",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            Spacer(Modifier.height(40.dp))

            // 🚪 Çıkış Butonu
            Button(
                onClick = {
                    profileViewModel.logout()
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
            ) {
                Text("Çıkış Yap", fontWeight = FontWeight.Bold)
            }
        }
    }

    // 🔁 Kitap eklendikçe güncel toplam sayıyı yenile
    LaunchedEffect(books.size) {
        totalAdded = bookViewModel.getTotalAdded(context)
    }
}

@Composable
fun StatRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(
        Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(label, style = MaterialTheme.typography.bodyLarge)
        }
        Text(value, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium))
    }
}