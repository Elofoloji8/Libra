package com.elo.libra.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.elo.libra.data.repository.BookRepository
import com.elo.libra.data.model.Book

class BookViewModel(
    private val repo: BookRepository = BookRepository()
) : ViewModel() {

    val books = mutableStateListOf<Book>()
    val loading = mutableStateOf(false)

    fun loadBooks() {
        loading.value = true
        repo.getBooks { list ->
            books.clear()
            books.addAll(list)
            loading.value = false
        }
    }

    fun addBook(book: Book, onSuccess: () -> Unit) {
        loading.value = true
        repo.addBook(book) { success, _ ->
            loading.value = false
            if (success) {
                loadBooks()
                onSuccess()
            }
        }
    }
}