package ru.debian17.findme.app.di.module

import android.content.Context
import android.net.ConnectivityManager
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.debian17.findme.data.network.WebAPI
import ru.debian17.findme.data.network.WebAPIService
import javax.inject.Singleton

@Module(includes = [ContextModule::class])
class WebAPIModule {

    companion object {
        private const val BASE_URL = "http://192.168.137.1:8080/api/"
    }

    @Singleton
    @Provides
    fun provideWebAPI(context: Context): WebAPIService {
        val gson = GsonBuilder().create()
        val gsonConverterFactory = GsonConverterFactory.create(gson)

        val callAdapterFactory = RxJava2CallAdapterFactory.create()

        val okHttpClient = OkHttpClient.Builder()
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(callAdapterFactory)
            .build()

        val webAPI = retrofit.create(WebAPI::class.java)

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return WebAPIService(webAPI, connectivityManager)

    }

}