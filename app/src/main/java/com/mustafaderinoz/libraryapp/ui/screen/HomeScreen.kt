package com.mustafaderinoz.libraryapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mustafaderinoz.libraryapp.R
import com.mustafaderinoz.libraryapp.ui.components.BookCard
import com.mustafaderinoz.libraryapp.ui.viewmodel.AuthViewModel
import com.mustafaderinoz.libraryapp.ui.viewmodel.BookViewModel
import com.mustafaderinoz.libraryapp.ui.viewmodel.BorrowState
import com.mustafaderinoz.libraryapp.ui.viewmodel.BorrowViewModel

private val AppBackground = Color(0xFFF8F9FA)
private val AppPrimary = Color(0xFF5E4B9C)
private val TextMain = Color(0xFF1F2937)
private val TextLight = Color(0xFF6B7280)
private val BorderLight = Color(0xFFE5E7EB)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    authViewModel: AuthViewModel,
    bookViewModel: BookViewModel,
    borrowViewModel: BorrowViewModel,
    onNavigateToMyBorrows: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val profile by authViewModel.profile.collectAsState()
    val books by bookViewModel.books.collectAsState()
    val isLoading by bookViewModel.isLoading.collectAsState()
    val searchQuery by bookViewModel.searchQuery.collectAsState()
    val borrowState by borrowViewModel.borrowState.collectAsState()
    val activeBorrowedBookIds by borrowViewModel.activeBorrowedBookIds.collectAsState()

    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(borrowState) {
        when (borrowState) {
            is BorrowState.Success -> {
                snackbarMessage = "Kitap başarıyla ödünç alındı!"
                borrowViewModel.resetState()
                bookViewModel.loadBooks()
            }
            is BorrowState.Error -> {
                snackbarMessage = (borrowState as BorrowState.Error).message
                borrowViewModel.resetState()
            }
            else -> {}
        }
    }

    LaunchedEffect(profile) {
        profile?.userId?.let { borrowViewModel.loadBorrows(it) }
    }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            snackbarMessage = null
        }
    }

    Scaffold(
        containerColor = AppBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Merhaba, ${profile?.fullName ?: ""}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextMain
                    )
                },
                actions = {
                    IconButton(onClick = onNavigateToMyBorrows) {
                        Icon(
                            painter = painterResource(id = R.drawable.books),
                            contentDescription = "Kiralamalarım",
                            modifier = Modifier.size(40.dp)
                        )
                    }
                    IconButton(onClick = {
                        authViewModel.signOut()
                        onNavigateToLogin()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Çıkış",
                            tint = Color.Red,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppBackground
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { bookViewModel.onSearchQueryChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                placeholder = {
                    Text("Kitap adı veya yazar ara...", color = TextLight, fontSize = 15.sp)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Ara",
                        tint = TextLight
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = AppPrimary,
                    unfocusedBorderColor = BorderLight,
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            when {
                isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(strokeWidth = 2.dp, color = AppPrimary)
                }
                books.isEmpty() -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Kitap bulunamadı.", color = TextLight)
                }
                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 32.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    items(books, key = { it.id }) { book ->
                        BookCard(
                            book = book,
                            isAlreadyBorrowed = activeBorrowedBookIds.contains(book.id),
                            onBorrowClick = {
                                profile?.userId?.let { userId ->
                                    borrowViewModel.borrowBook(userId, book.id)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}