package com.elo.libra.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.elo.libra.data.repository.BookRepository
import com.elo.libra.data.model.Book
import kotlinx.coroutines.launch

class BookViewModel(
    private val repo: BookRepository = BookRepository()
) : ViewModel() {

    val books = mutableStateListOf<Book>()
    val loading = mutableStateOf(false)

    /** 🔹 Firestore'dan kitapları yükler */
    fun loadBooks() {
        loading.value = true
        repo.getBooks { list ->
            books.clear()
            books.addAll(list)
            loading.value = false
        }
    }

    /** 🔹 Yeni kitap ekler */
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

    /** 🔹 Kitabı siler */
    fun deleteBook(bookId: String) {
        if (bookId.isEmpty()) return
        viewModelScope.launch {
            repo.deleteBook(bookId) { success ->
                if (success) loadBooks()
            }
        }
    }

    /** 🔹 ID’ye göre kitabı getirir */
    suspend fun getBookById(id: String): Book? {
        return books.firstOrNull { it.id == id }
    }

    /** 🔹 Kitap bilgilerini günceller */
    fun updateBook(book: Book, onComplete: (Boolean) -> Unit) {
        repo.updateBook(book) { success ->
            if (success) {
                loadBooks()
                onComplete(true)
            } else {
                onComplete(false)
            }
        }
    }
}
