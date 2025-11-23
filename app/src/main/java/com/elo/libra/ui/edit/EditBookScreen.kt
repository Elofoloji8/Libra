package com.elo.libra.ui.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.elo.libra.data.model.Book
import com.elo.libra.viewmodel.BookViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBookScreen(bookId: String, navController: NavHostController) {
    val viewModel: BookViewModel = viewModel()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var selectedGenre by remember { mutableStateOf("") }

    val genreOptions = listOf(
        "Roman", "Bilimkurgu", "Fantastik", "Polisiye",
        "Dram", "Klasik", "Kişisel Gelişim", "Tarih"
    )

    // 🔹 Kitap bilgilerini yükle
    LaunchedEffect(bookId) {
        val book = viewModel.getBookById(bookId)
        if (book != null) {
            title = book.title
            author = book.author
            selectedGenre = book.genre
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Kitabı Düzenle") })
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .padding(16.dp)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Kitap Adı") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = author,
                onValueChange = { author = it },
                label = { Text("Yazar") },
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Tür Seçin",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Start)
            )

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                genreOptions.forEach { genre ->
                    val isSelected = genre == selectedGenre
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                if (isSelected)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.surfaceVariant
                            )
                            .clickable { selectedGenre = genre }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = genre,
                            color = if (isSelected) Color.White else Color.Black
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        if (title.isNotBlank() && author.isNotBlank() && selectedGenre.isNotBlank()) {
                            val updatedBook = Book(
                                id = bookId,
                                title = title,
                                author = author,
                                genre = selectedGenre
                            )

                            scope.launch {
                                viewModel.updateBook(updatedBook) { success ->
                                    // callback suspend değil, bu yüzden ayrı bir coroutine içinde çağırıyoruz
                                    scope.launch {
                                        if (success) {
                                            snackbarHostState.showSnackbar(
                                                message = "✅ Kitap başarıyla güncellendi",
                                                duration = SnackbarDuration.Short
                                            )
                                            delay(1500)
                                            navController.navigate("home") {
                                                popUpTo("home") { inclusive = true }
                                            }
                                        } else {
                                            snackbarHostState.showSnackbar(
                                                message = "❌ Güncelleme başarısız!",
                                                duration = SnackbarDuration.Short
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Kaydet")
                }

                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("İptal")
                }
            }
        }
    }
}