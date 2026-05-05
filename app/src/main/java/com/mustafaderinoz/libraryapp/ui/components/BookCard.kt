package com.mustafaderinoz.libraryapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mustafaderinoz.libraryapp.data.model.Book

private val TextMain = Color(0xFF1F2937)
private val TextLight = Color(0xFF6B7280)
private val BorderLight = Color(0xFFE5E7EB)

@Composable
fun BookCard(
    book: Book,
    isAlreadyBorrowed: Boolean,
    onBorrowClick: () -> Unit
) {
    val spineColor = categoryColor(book.category)
    val isAvailable = book.avaiableCopies > 0

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, BorderLight)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(spineColor)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                        Text(
                            text = book.title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextMain,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = book.author,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = TextLight,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Surface(
                        color = Color(0xFFF3F4F6),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "${book.avaiableCopies}/${book.totalCopies}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = TextLight,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (book.category.isNotBlank()) {
                        BookBadge(
                            text = book.category,
                            containerColor = Color(0xFFF3E8FF),
                            contentColor = Color(0xFF7E22CE)
                        )
                    }
                    BookBadge(
                        text = "${book.pageCount} sayfa",
                        containerColor = Color(0xFFF3F4F6),
                        contentColor = Color(0xFF4B5563)
                    )
                    BookBadge(
                        text = if (isAvailable) "Mevcut" else "Tükendi",
                        containerColor = if (isAvailable) Color(0xFFDCFCE7) else Color(0xFFFEE2E2),
                        contentColor = if (isAvailable) Color(0xFF16A34A) else Color(0xFFDC2626)
                    )
                }

                if (book.isbn.isNotBlank()) {
                    Text(
                        text = "ISBN: ${book.isbn}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = (-0.5).sp
                        ),
                        color = Color(0xFF9CA3AF),
                        modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
                    )
                } else {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                val isDisabled = !isAvailable || isAlreadyBorrowed
                val buttonText = if (isAlreadyBorrowed) "Ödünç Alındı" else "Ödünç Al"

                Button(
                    onClick = if (isDisabled) { {} } else onBorrowClick,
                    enabled = !isDisabled,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4F46E5),
                        contentColor = Color.White,
                        disabledContainerColor = Color(0xFFF9FAFB),
                        disabledContentColor = Color(0xFFD1D5DB)
                    ),
                    border = if (isDisabled) BorderStroke(1.dp, Color(0xFFF3F4F6)) else null
                ) {
                    Text(
                        text = buttonText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun BookBadge(text: String, containerColor: Color, contentColor: Color) {
    Surface(
        shape = RoundedCornerShape(percent = 50),
        color = containerColor,
        contentColor = contentColor
    ) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

private fun categoryColor(category: String): Color {
    return when (category.lowercase()) {
        "klasik"      -> Color(0xFF3B82F6)
        "roman"       -> Color(0xFFEA580C)
        "bilim kurgu" -> Color(0xFF10B981)
        "tarih"       -> Color(0xFFD97706)
        "biyografi"   -> Color(0xFF8B5CF6)
        "yazılım"     -> Color(0xFFEC4899)
        else          -> Color(0xFF9CA3AF)
    }
}