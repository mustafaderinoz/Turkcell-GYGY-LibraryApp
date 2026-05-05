package com.mustafaderinoz.libraryapp.ui.viewmodel

import BorrowRecord
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.mustafaderinoz.libraryapp.data.repository.BorrowRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class BorrowState {
    object Idle : BorrowState()
    object Loading : BorrowState()
    object Success : BorrowState()
    data class Error(val message: String) : BorrowState()
}

class BorrowViewModel : ViewModel() {
    private val repository = BorrowRepository()

    private val _borrows = MutableStateFlow<List<BorrowRecord>>(emptyList())
    val borrows: StateFlow<List<BorrowRecord>> = _borrows

    private val _borrowState = MutableStateFlow<BorrowState>(BorrowState.Idle)
    val borrowState: StateFlow<BorrowState> = _borrowState

    // Aktif ödünç alınan kitap ID'lerini tutar — UI'da buton kontrolü için
    private val _activeBorrowedBookIds = MutableStateFlow<Set<String>>(emptySet())
    val activeBorrowedBookIds: StateFlow<Set<String>> = _activeBorrowedBookIds

    fun loadBorrows(studentId: String) {
        viewModelScope.launch {
            repository.getBorrowsByStudent(studentId)
                .onSuccess { records ->
                    // Aktif olanları en üste almak için listeyi sıralıyoruz
                    val sortedRecords = records.sortedWith(
                        compareBy<BorrowRecord> { it.returnedAt != null }
                            .thenByDescending { it.borrowedAt }
                    )

                    _borrows.value = sortedRecords
                    _activeBorrowedBookIds.value = sortedRecords
                        .filter { it.returnedAt == null }
                        .map { it.bookId }
                        .toSet()
                }
                .onFailure { _borrowState.value = BorrowState.Error(it.message ?: "Hata") }
        }
    }

    fun borrowBook(studentId: String, bookId: String) {
        viewModelScope.launch {
            _borrowState.value = BorrowState.Loading
            repository.borrowBook(studentId, bookId)
                .onSuccess {
                    _borrowState.value = BorrowState.Success
                    loadBorrows(studentId) // listeyi yenile
                }
                .onFailure {
                    _borrowState.value = BorrowState.Error(it.message ?: "Ödünç alma başarısız")
                }
        }
    }

    fun returnBook(borrowId: String, studentId: String) {
        viewModelScope.launch {
            _borrowState.value = BorrowState.Loading
            repository.returnBook(borrowId)
                .onSuccess {
                    _borrowState.value = BorrowState.Success
                    loadBorrows(studentId) // listeyi yenile
                }
                .onFailure {
                    _borrowState.value = BorrowState.Error(it.message ?: "İade başarısız")
                }
        }
    }


    fun resetState() {
        _borrowState.value = BorrowState.Idle
    }
}