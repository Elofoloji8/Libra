package com.elo.libra.data.repository

import com.elo.libra.data.firebase.FirestoreService.db
import com.elo.libra.data.model.Book
import com.google.firebase.firestore.SetOptions

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
            .addOnFailureListener {
                onComplete(emptyList())
            }
    }

    fun deleteBook(id: String, onComplete: (Boolean) -> Unit) {
        db.collection("books")
            .document(id)
            .delete()
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun updateBook(book: Book, onComplete: (Boolean) -> Unit) {
        db.collection("books")
            .document(book.id)
            .set(book, SetOptions.merge())
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }
}