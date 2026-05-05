package com.mustafaderinoz.libraryapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mustafaderinoz.libraryapp.ui.components.BorrowCard
import com.mustafaderinoz.libraryapp.ui.viewmodel.AuthViewModel
import com.mustafaderinoz.libraryapp.ui.viewmodel.BookViewModel
import com.mustafaderinoz.libraryapp.ui.viewmodel.BorrowState
import com.mustafaderinoz.libraryapp.ui.viewmodel.BorrowViewModel

// Özel Renk Tanımlamaları (Ana ekran ile uyumlu)
private val AppBackground = Color(0xFFF8F9FA)
private val TextMain = Color(0xFF1F2937)
private val TextLight = Color(0xFF6B7280)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBorrowsScreen(
    authViewModel: AuthViewModel,
    bookViewModel: BookViewModel,
    borrowViewModel: BorrowViewModel,
    onBack: () -> Unit
) {
    val profile by authViewModel.profile.collectAsState()
    val borrows by borrowViewModel.borrows.collectAsState()
    val books by bookViewModel.books.collectAsState()
    val borrowState by borrowViewModel.borrowState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(profile) {
        profile?.userId?.let { borrowViewModel.loadBorrows(it) }
    }

    LaunchedEffect(borrowState) {
        when (borrowState) {
            is BorrowState.Success -> {
                snackbarHostState.showSnackbar("İşlem başarılı!")
                borrowViewModel.resetState()
                bookViewModel.loadBooks()
            }
            is BorrowState.Error -> {
                snackbarHostState.showSnackbar((borrowState as BorrowState.Error).message)
                borrowViewModel.resetState()
            }
            else -> {}
        }
    }

    // bookId -> Book haritası
    val bookMap = remember(books) { books.associateBy { it.id } }

    Scaffold(
        containerColor = AppBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Kiralamalarım",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextMain
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Geri",
                            tint = TextMain
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppBackground
                )
            )
        }
    ) { padding ->
        when {
            borrows.isEmpty() -> Box(
                Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Henüz kiralama yapılmadı.",
                    color = TextLight,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            else -> LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(borrows, key = { it.id }) { record ->
                    val book = bookMap[record.bookId]
                    BorrowCard(
                        record = record,
                        book = book,
                        onReturnClick = {
                            profile?.userId?.let { userId ->
                                borrowViewModel.returnBook(record.id, userId)
                            }
                        }
                    )
                }
            }
        }
    }
}