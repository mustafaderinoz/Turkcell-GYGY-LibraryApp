package com.mustafaderinoz.libraryapp.data.repository

import BorrowRecord
import com.mustafaderinoz.libraryapp.data.supabase.supabase
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

class BorrowRepository {

    // Kullanıcının tüm kiralamalarını getir (kitap bilgisiyle birlikte)
    suspend fun getBorrowsByStudent(studentId: String): Result<List<BorrowRecord>> = runCatching {
        supabase.postgrest["borrow_records"]
            .select {
                filter { eq("student_id", studentId) }
            }
            .decodeList<BorrowRecord>()
    }

    // Kitabı ödünç al (max 5 gün)
    suspend fun borrowBook(studentId: String, bookId: String): Result<Unit> = runCatching {
        val tz = TimeZone.of("Europe/Istanbul")
        val nowInstant = Clock.System.now()

        // Sadece tarih kısmını alıyoruz (Örn: 2026-05-06)
        val todayDate = nowInstant.toLocalDateTime(tz).date
        val dueDate = todayDate.plus(5, DateTimeUnit.DAY)

        val record = BorrowRecord(
            studentId = studentId,
            bookId = bookId,
            borrowedAt = todayDate.toString(),
            dueDate = dueDate.toString()
        )
        supabase.postgrest["borrow_records"].insert(record)
    }

    // Kitabı iade et
    suspend fun returnBook(borrowId: String): Result<Unit> = runCatching {
        val tz = TimeZone.of("Europe/Istanbul")
        val todayDate = Clock.System.now().toLocalDateTime(tz).date
        supabase.postgrest["borrow_records"].update(
            mapOf("returned_at" to todayDate.toString())
        ) {
            filter { eq("id", borrowId) }
        }
    }


}