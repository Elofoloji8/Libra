package com.elo.libra.ui.home

import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

data class BookCategory(
    val name: String,         // UI'da gösterilecek isim
    val key: String,          // Filtreleme için kullanılacak gerçek anahtar
    val color: Color,         // Chip seçili rengi
    val icon: ImageVector     // Chip ikonu
)

val categories = listOf(
    BookCategory("Roman", "roman", Color(0xFF64B5F6), Icons.Default.MenuBook),
    BookCategory("Bilimkurgu", "bilimkurgu", Color(0xFFBA68C8), Icons.Default.Science),
    BookCategory("Fantastik", "fantastik", Color(0xFF81C784), Icons.Default.AutoAwesome),
    BookCategory("Polisiye", "polisiye", Color(0xFFFF8A65), Icons.Default.Security),
    BookCategory("Dram", "dram", Color(0xFFA1887F), Icons.Default.TheaterComedy),
    BookCategory("Klasik", "klasik", Color(0xFF4DD0E1), Icons.Default.Book),
    BookCategory("Kişisel Gelişim", "kisisel_gelisim", Color(0xFFFFD54F), Icons.Default.Lightbulb),
    BookCategory("Tarih", "tarih", Color(0xFF90A4AE), Icons.Default.AccountBalance)
)