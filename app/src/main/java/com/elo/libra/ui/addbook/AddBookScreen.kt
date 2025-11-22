package com.elo.libra.ui.addbook

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.elo.libra.data.model.Book
import com.elo.libra.viewmodel.BookViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AddBookScreen(navController: NavHostController) {

    val viewModel: BookViewModel = viewModel()

    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Scaffold { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            Text("Kitap Ekle", style = MaterialTheme.typography.headlineMedium)

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Kitap Adı") })
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(value = author, onValueChange = { author = it }, label = { Text("Yazar") })
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(value = genre, onValueChange = { genre = it }, label = { Text("Tür / Kategori") })
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Açıklama") }
            )

            Spacer(Modifier.height(24.dp))

            Button(onClick = {
                val book = Book(
                    title = title,
                    author = author,
                    genre = genre,
                    description = description
                )

                viewModel.addBook(book) {
                    navController.popBackStack()
                }
            }) {
                Text("Kaydet")
            }
        }
    }
}