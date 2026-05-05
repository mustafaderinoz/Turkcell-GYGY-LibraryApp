package com.mustafaderinoz.libraryapp.data.repository

import com.mustafaderinoz.libraryapp.data.model.Book
import com.mustafaderinoz.libraryapp.data.supabase.supabase
import io.github.jan.supabase.postgrest.postgrest

class BookRepository {
    suspend fun getAllBooks(): Result<List<Book>> = runCatching {
        supabase.postgrest["books"]
            .select()
            .decodeList<Book>()
    }

    suspend fun searchBooks(query: String): Result<List<Book>> = runCatching {
        supabase.postgrest["books"]
            .select {
                filter {
                    or {
                        ilike("title", "%$query%")
                        ilike("author", "%$query%")
                    }
                }
            }
            .decodeList<Book>()
    }


    //Veri tabanında veri olduğunu kabul edip ona göre uygulmayı geliştirdim buraları kullanmadım
    /*
   suspend fun getBookById(id: String): Result<Book> = runCatching {
       supabase.postgrest["books"]
           .select { filter { eq("id", id) } }
           .decodeSingle<Book>()
   }

   suspend fun addBook(book: Book): Result<Unit> = runCatching {
       supabase.postgrest["books"].insert(book)
   }

   //Belirtilen ID'ye sahip kitabı günceller.
   suspend fun updateBook(id: String, updatedBook: Book): Result<Unit> = runCatching {
       supabase.postgrest["books"].update(updatedBook) {
           filter {
               eq("id", id)
           }
       }
   }


   //Belirtilen ID'ye sahip kitabı veritabanından siler.
   suspend fun deleteBook(id: String): Result<Unit> = runCatching {
       supabase.postgrest["books"].delete {
           filter {
               eq("id", id)
           }
       }
   }

    */


}