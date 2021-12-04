package com.ssindher.movieazy.di

import com.ssindher.movieazy.data.api.ApiInterface
import com.ssindher.movieazy.data.repository.MovieRepository
import com.ssindher.movieazy.ui.main.viewmodel.HomepageViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppModule {

    private val BASE_URL = "https://api.themoviedb.org/3/"

    val appModule = module {
        single { MovieRepository(get()) }
        viewModel { HomepageViewModel(get()) }
    }

    val netModule = module {
        single {
            val interceptor = HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
            OkHttpClient.Builder().addInterceptor(interceptor).build()
        }
        factory {
            Retrofit.Builder()
                .client(get())
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        single { get<Retrofit>().create(ApiInterface::class.java) }
    }
}