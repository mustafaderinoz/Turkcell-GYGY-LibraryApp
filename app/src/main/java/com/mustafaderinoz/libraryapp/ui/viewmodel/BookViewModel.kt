package com.mustafaderinoz.libraryapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mustafaderinoz.libraryapp.data.model.Book
import com.mustafaderinoz.libraryapp.data.repository.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BookViewModel:ViewModel() {
    private val repository = BookRepository()

    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> = _books

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    init {
        loadBooks()
    }

    fun loadBooks(showLoadingIndicator: Boolean = true) {
        viewModelScope.launch {
            // Sadece ilk açılışta veya bilerek istendiğinde yükleme animasyonu göster
            if (showLoadingIndicator) {
                _isLoading.value = true
            }

            repository
                .getAllBooks()
                .onSuccess { books->_books.value = books.sortedBy { it.avaiableCopies } }
                .onFailure { _error.value = it.message }
            _isLoading.value = false


        }
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
        if (newQuery.isNotEmpty()) {
            searchBooks(newQuery)
        } else if (newQuery.isEmpty()) {
            loadBooks() // Arama temizlenince tüm listeyi getir
        }
    }

    fun searchBooks(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.searchBooks(query)
                .onSuccess {
                    _books.value = it
                    _error.value = null
                }
                .onFailure {
                    _error.value = it.message ?: "Arama başarısız"
                }
            _isLoading.value = false
        }
    }

}