package com.mustafaderinoz.libraryapp.data.repository

import BorrowRecord
import com.mustafaderinoz.libraryapp.data.supabase.supabase
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.datetime.plus

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
        val now = kotlinx.datetime.Clock.System.now()
        val due = now.plus(5, kotlinx.datetime.DateTimeUnit.DAY, kotlinx.datetime.TimeZone.currentSystemDefault())

        val record = BorrowRecord(
            studentId = studentId,
            bookId = bookId,
            borrowedAt = now.toString(),
            dueDate = due.toString()
        )
        supabase.postgrest["borrow_records"].insert(record)
    }

    // Kitabı iade et
    suspend fun returnBook(borrowId: String): Result<Unit> = runCatching {
        val now = kotlinx.datetime.Clock.System.now().toString()
        supabase.postgrest["borrow_records"].update(
            mapOf("returned_at" to now)
        ) {
            filter { eq("id", borrowId) }
        }
    }


}