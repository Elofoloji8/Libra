package com.elo.libra.ui.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.elo.libra.R
import com.elo.libra.data.model.Book
import com.elo.libra.viewmodel.BookViewModel
import kotlinx.coroutines.launch
import android.util.Log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val viewModel: BookViewModel = viewModel()
    val books = viewModel.books
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) { viewModel.loadBooks() }

    val scale = remember { Animatable(1f) }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("profile") },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profil") },
                    label = { Text("Profil") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = {
                        scope.launch {
                            scale.animateTo(1.2f, animationSpec = tween(150))
                            scale.animateTo(1f, animationSpec = tween(150))
                            navController.navigate("addbook")
                        }
                    },
                    icon = {
                        FloatingActionButton(
                            onClick = {
                                scope.launch {
                                    scale.animateTo(1.2f, animationSpec = tween(150))
                                    scale.animateTo(1f, animationSpec = tween(150))
                                    navController.navigate("addbook")
                                }
                            },
                            containerColor = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.scale(scale.value)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Kitap Ekle")
                        }
                    },
                    label = { Text("") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("chatbot") },
                    icon = { Icon(Icons.Default.ChatBubble, contentDescription = "Chatbot") },
                    label = { Text("Asistan") }
                )
            }
        }
    ) { padding ->
        val gradient = Brush.verticalGradient(
            listOf(
                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                MaterialTheme.colorScheme.background
            )
        )

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(gradient)
        ) {
            Text(
                text = "📚 Kitaplarım",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(16.dp)
            )

            if (viewModel.loading.value) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(books) { book ->
                        // ✅ Null ID korumalı çağrı
                        val safeId = book.id ?: ""
                        BookCard(
                            book = book,
                            onEdit = {
                                if (safeId.isNotEmpty()) {
                                    navController.navigate("editbook/$safeId")
                                } else {
                                    Log.e("HomeScreen", "Book ID boş, düzenlenemiyor.")
                                }
                            },
                            onDelete = {
                                if (safeId.isNotEmpty()) {
                                    viewModel.deleteBook(safeId)
                                } else {
                                    Log.e("HomeScreen", "Book ID boş, silinemiyor.")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BookCard(
    book: Book,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 100.dp)
            .padding(horizontal = 8.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_book_placeholder),
                contentDescription = "Book Image",
                modifier = Modifier
                    .size(60.dp)
                    .padding(end = 12.dp)
            )

            Column(Modifier.weight(1f)) {
                Text(book.title, style = MaterialTheme.typography.titleMedium)
                Text(book.author, style = MaterialTheme.typography.bodyMedium)
                Text(
                    book.genre,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // ⋮ Menü
            Box {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Menü")
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Düzenle") },
                        onClick = {
                            menuExpanded = false
                            onEdit()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Sil", color = Color.Red) },
                        onClick = {
                            menuExpanded = false
                            onDelete()
                        }
                    )
                }
            }
        }
    }
}