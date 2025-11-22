package com.elo.libra.data.repository

import com.elo.libra.data.firebase.FirestoreService.db
import com.elo.libra.data.model.Book

class BookRepository {

    fun addBook(book: Book, onComplete: (Boolean, String?) -> Unit) {
        val doc = db.collection("books").document()
        val newBook = book.copy(id = doc.id)

        doc.set(newBook)
            .addOnSuccessListener { onComplete(true, null) }
            .addOnFailureListener { e -> onComplete(false, e.message) }
    }

    fun getBooks(onComplete: (List<Book>) -> Unit) {
        db.collection("books")
            .get()
            .addOnSuccessListener { result ->
                val list = result.toObjects(Book::class.java)
                onComplete(list)
            }
    }
}