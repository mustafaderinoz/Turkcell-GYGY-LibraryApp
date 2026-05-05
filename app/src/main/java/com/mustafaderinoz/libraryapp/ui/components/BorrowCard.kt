package com.mustafaderinoz.libraryapp.ui.components

import BorrowRecord
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mustafaderinoz.libraryapp.data.model.Book

private val TextMain = Color(0xFF1F2937)
private val TextLight = Color(0xFF6B7280)
private val BorderLight = Color(0xFFE5E7EB)

@Composable
fun BorrowCard(
    record: BorrowRecord,
    book: Book?,
    onReturnClick: () -> Unit
) {
    val isActive = record.returnedAt == null

    // Tarihleri düzenli göster (ISO string'i kırp)
    fun formatDate(raw: String): String {
        return raw.take(10) // "2025-05-01" formatına indir
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, BorderLight)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            // Üst satır: Kitap adı + Durum etiketi
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                    Text(
                        text = book?.title ?: "Bilinmeyen Kitap",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextMain,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = book?.author ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = TextLight,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // Aktif / İade Edildi etiketi (Hap Şeklinde - Pill Shape)
                Surface(
                    shape = RoundedCornerShape(percent = 50),
                    color = if (isActive) Color(0xFFDCFCE7) else Color(0xFFF3F4F6),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        text = if (isActive) "AKTİF" else "İADE EDİLDİ",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        fontWeight = FontWeight.Bold,
                        color = if (isActive) Color(0xFF16A34A) else Color(0xFF4B5563),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = BorderLight)
            Spacer(modifier = Modifier.height(16.dp))

            // Tarih bilgileri
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DateInfoColumn(label = "Alış Tarihi", value = formatDate(record.borrowedAt))
                DateInfoColumn(label = "Teslim Tarihi", value = formatDate(record.dueDate))
                if (!isActive && record.returnedAt != null) {
                    DateInfoColumn(label = "İade Tarihi", value = formatDate(record.returnedAt))
                }
            }

            // İade et butonu (sadece aktif kiralamalar için)
            if (isActive) {
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedButton(
                    onClick = onReturnClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color(0xFFFCA5A5)), // Açık kırmızı kenarlık
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFDC2626), // Kırmızı metin
                        containerColor = Color(0xFFFEF2F2) // Çok açık kırmızı arka plan
                    )
                ) {
                    Text(
                        text = "İade Et",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun DateInfoColumn(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.sp,
                letterSpacing = 0.5.sp
            ),
            color = TextLight,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = TextMain
        )
    }
}