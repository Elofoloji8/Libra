package com.elo.libra.viewmodel


import android.content.Context

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elo.libra.data.model.Book
import com.elo.libra.data.repository.BookRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// ✅ Context üzerinde DataStore tanımı
private val Context.bookDataStore by preferencesDataStore(name = "book_stats")

class BookViewModel(
    application: Application,
    private val repo: BookRepository = BookRepository()
) : AndroidViewModel(application) {

    // 🔹 DataStore referansı
    private val dataStore = application.bookDataStore
    private val TOTAL_ADDED_KEY = intPreferencesKey("total_added_books")

    private val TOTAL_ADDED_KEY = intPreferencesKey("total_added_books")

    val books = mutableStateListOf<Book>()
    val loading = mutableStateOf(false)

    // ====================================================================================
    //  1) DATASTORE — TOPLAM EKLENEN KİTAP SAYACI
    // ====================================================================================

    /** 📈 Toplam eklenen kitap sayısını 1 artırır */
    private suspend fun incrementTotalAdded(context: Context) {
        context.bookDataStore.edit { prefs ->
            val current = prefs[TOTAL_ADDED_KEY] ?: 0
            prefs[TOTAL_ADDED_KEY] = current + 1
        }
    }

    /** 📊 Toplam eklenen kitap sayısını getirir */
    suspend fun getTotalAdded(context: Context): Int {
        val prefs = context.bookDataStore.data.first()
        return prefs[TOTAL_ADDED_KEY] ?: 0
    }

    // ====================================================================================
    //  2) FIRESTORE — KİTAP İŞLEMLERİ
    // ====================================================================================

    /**  Firestore’dan kitapları yükler */
    fun loadBooks() {
        loading.value = true
        repo.getBooks { list ->
            books.clear()
            books.addAll(list)
            loading.value = false
        }
    }

    /**  Yeni kitap ekler (DataStore sayacı + Firestore güncellemesi) */
    fun addBook(book: Book, context: Context, onSuccess: () -> Unit) {
        loading.value = true
        repo.addBook(book) { success, _ ->
            loading.value = false
            if (success) {
                viewModelScope.launch { incrementTotalAdded(context) } // ✅ Sayaç artır
                loadBooks()
                onSuccess()
            }
        }
    }

    /**  Kitap silme işlemi */
    fun deleteBook(bookId: String) {
        if (bookId.isEmpty()) return
        viewModelScope.launch {
            repo.deleteBook(bookId) { success ->
                if (success) loadBooks()
            }
        }
    }

    /**  Belirli kitabı ID’ye göre bulur */
    fun getBookById(id: String): Book? {
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