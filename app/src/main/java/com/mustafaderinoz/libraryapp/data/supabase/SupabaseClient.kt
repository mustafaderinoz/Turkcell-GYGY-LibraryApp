package com.mustafaderinoz.libraryapp.data.supabase


import com.mustafaderinoz.libraryapp.BuildConfig
//import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.gotrue.Auth

val supabase = createSupabaseClient(
    supabaseKey =BuildConfig.SUPABASE_ANON_KEY,
    supabaseUrl =BuildConfig.SUPABASE_URL,
) {
    install(Postgrest)
    install(Auth)
}