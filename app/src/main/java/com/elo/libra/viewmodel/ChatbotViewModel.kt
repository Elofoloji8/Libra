package com.elo.libra.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.elo.libra.data.model.Book
import com.google.gson.Gson
import com.google.firebase.firestore.FirebaseFirestore

class ChatbotViewModel : ViewModel() {

    var localBooks: List<Book> = emptyList()

    var firestoreBooks: List<Book> = emptyList()

    val messages = mutableStateListOf<String>()

    var step by mutableStateOf(0)
        private set

    private var preferredGenres: String = ""
    private var mood: String = ""
    private var lastBook: String = ""

    // Kullanıcı seçenekleri
    val genreOptions = listOf(
        "Klasik", "Roman", "Bilimkurgu", "Fantastik",
        "Polisiye", "Dram", "Kişisel Gelişim", "Macera"
    )

    val moodOptions = listOf(
        "Harika", "Mutlu", "İyi", "İdare eder",
        "Yorgun", "Üzgün", "Bitkin", "Gergin"
    )

    val lastReadOptions = listOf(
        "Gece Yarısı Kütüphanesi", "1984", "Suç ve Ceza", "Simyacı",
        "Hayvan Çiftliği", "Uçurtma Avcısı", "Olasılıksız", "Dönüşüm",
        "Burada yok"
    )

    init {
        messages.add("👋 Merhaba, kitap öneri asistanına hoş geldin!")
        messages.add("Hangi türleri seviyorsun?")
    }

    // Kullanıcının seçtiği seçenekleri işleyen fonksiyon
    fun sendMessage(userMessage: String) {
        messages.add("👤: $userMessage")
        process(userMessage)
    }

    // Local JSON dataset yükleme
    fun initLocalDataset(context: Context) {
        val inputStream = context.assets.open("books.json")
        val json = inputStream.bufferedReader().use { it.readText() }
        localBooks = Gson().fromJson(json, Array<Book>::class.java).toList()

        loadFirestoreBooks()
    }

    // Firestore kitaplarını yükleme
    private fun loadFirestoreBooks() {
        FirebaseFirestore.getInstance()
            .collection("books")
            .get()
            .addOnSuccessListener { result ->
                firestoreBooks = result.toObjects(Book::class.java)
            }
    }

    // Soru akışı
    private fun process(input: String) {
        when (step) {

            // 1. Soru → Tür seçimi
            0 -> {
                preferredGenres = input.lowercase()
                messages.add("📚 Harika! Peki bugün kendini nasıl hissediyorsun?")
                step++
            }

            // 2. Soru → Mood seçimi
            1 -> {
                mood = input.lowercase()
                messages.add("📝 Son olarak yakın zamanda okuduğun bir kitap var mı?")
                step++
            }

            // 3. Soru → Son okunan kitap seçimi
            2 -> {
                if (input == "Burada yok") {
                    lastBook = ""
                    messages.add("📖 Sorun değil! Son okuduğun kitabı bilmesem de öneri yapabilirim.")
                } else {
                    lastBook = input.lowercase()
                    messages.add("🔍 Harika! Şimdi sana uygun kitapları buluyorum...")
                }

                step++
                recommendBooks()
            }
        }
    }

    private fun recommendBooks() {

        messages.add("📖 Öneriler hazırlanıyor...")

        // Tüm kitap listesini birleştir
        val allBooks = (localBooks + firestoreBooks).distinctBy { it.title }

        // Kitaplara skor ver
        val scored = allBooks.map { book: Book ->
            var score = 0

            val genre = book.genre.lowercase()
            val title = book.title.lowercase()

            // Tür eşleşmesi
            if (genre.contains(preferredGenres)) score += 3

            // Mood ilişkili eşleşmeler
            if (mood.contains("iyi") && genre.contains("klasik")) score += 1
            if (mood.contains("mutlu") && genre.contains("roman")) score += 2
            if (mood.contains("üzgün") && genre.contains("kişisel")) score += 2
            if (mood.contains("heyecan") && genre.contains("macera")) score += 2

            // Son okunan kitap benzerliği
            if (lastBook.isNotBlank() && title.contains(lastBook)) score += 5

            book to score
        }

        // En yüksek skorluları seç
        val top = scored.sortedByDescending { it.second }
            .map { it.first }
            .take(5)

        if (top.isEmpty()) {
            messages.add("✨ Mükemmel eşleşme bulamadım ama popüler kitapları öneriyorum:")
            allBooks.shuffled().take(5).forEach { b ->
                messages.add("• ${b.title} — ${b.author} (${b.genre})")
            }
        } else {
            messages.add("✨ İşte sana en uygun kitaplar:")
            top.forEach { b ->
                messages.add("• ${b.title} — ${b.author} (${b.genre})")
            }
        }
    }
}