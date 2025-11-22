package com.elo.libra.data.local

import android.content.Context
import com.elo.libra.data.model.Book
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LocalBookDataSource(private val context: Context) {

    fun loadBooks(): List<Book> {
        return try {
            val inputStream = context.assets.open("books.json")
            val json = inputStream.bufferedReader().use { it.readText() }

            val listType = object : TypeToken<List<Book>>() {}.type
            Gson().fromJson(json, listType)

        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}