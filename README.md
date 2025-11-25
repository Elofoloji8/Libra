📚 Libra – Akıllı Kitap Yönetim Uygulaması

Libra, kullanıcıların kendi kişisel kütüphanelerini yönetmesini sağlayan modern ve sade bir Android kitap yönetim uygulamasıdır. Jetpack Compose ile geliştirilmiş dinamik arayüzü, kategori bazlı filtreleme sistemi, kitap ekleme/güncelleme işlemleri ve entegre sohbet asistanıyla kullanıcıya etkileşimli bir deneyim sunar.

Uygulama; kitapları düzenleme, tür seçimi, veri saklama, profil benzeri kullanım akışları ve akıllı yönlendirme gibi birçok işlevi minimalist bir yapıda bir araya getirir.

🌟 Özellikler
📘 Kitap Yönetimi

Kitap ekleme, silme ve güncelleme

Dinamik input alanları

Kitap adını ve yazarını hızlı giriş

Tür seçimi için modern seçenek baloncukları (Chips)

🏷 Kategori Sistemi

JSON tabanlı kategori listesi

Kategoriye göre filtreleme

Ana sayfada kategori kartları

Kategoriye özel akıcı geçiş animasyonları

💬 Akıllı Sohbet Asistanı

Kitap önerisi

Kategori yönlendirmesi

Basit yardımcı komutlarla uygulama içinde rehberlik

Kullanıcı deneyimini artıran hafif chatbot modülü

🎨 Modern Arayüz (Jetpack Compose)

Material 3 bileşenleri

Yumuşak animasyonlar

Gradient arka planlar

Responsive layout

Snackbar ile başarılı işlem bildirimleri

🔄 Gerçek Zamanlı Düzenleme Akışı

Kitap düzenleme ekranından snackbar bildirimi

Snackbar sonrası otomatik HomeScreen dönüş

ViewModel tabanlı güncel veri yönetimi

🧱 Mimari

MVVM (Model–View–ViewModel)

Jetpack Compose Navigation

State Hoisting & Recomposition Mantığı

Modüler UI bileşenleri

ViewModel ile kitap listesi yönetimi

📂 Proje Yapısı
Libra/
├── data/
│   └── model/
│       └── Book.kt
│
├── ui/
│   ├── home/
│   │   ├── HomeScreen.kt
│   │   ├── BookCategory.kt
│   │   └── categories.json
│   │
│   ├── edit/
│   │   └── EditBookScreen.kt
│   │
│   ├── add/
│   │   └── AddBookScreen.kt
│   │
│   ├── chatbot/
│   │   └── ChatBotScreen.kt
│   │
│   └── theme/
│       └── Color.kt, Typography.kt, Theme.kt
│
├── viewmodel/
│   └── BookViewModel.kt
│
└── MainActivity.kt

🚀 Kurulum

Repoyu klonlayın:

git clone https://github.com/<kullaniciadi>/Libra.git


Android Studio ile açın.

Gradle senkronize olduktan sonra uygulamayı çalıştırın.
