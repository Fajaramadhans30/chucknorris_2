package com.chucknorris.myapplication.network

import com.chucknorris.myapplication.model.RandomCategory
import com.chucknorris.myapplication.model.SearchFreeResponse
import com.chucknorris.myapplication.util.API_URL
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface Service {
    @GET("/jokes/categories")
    fun getCategories(): Observable<ArrayList<String>>

    @GET("/jokes/random")
    fun getRandomCategory(@Query("category") id: String): Observable<RandomCategory>

    @GET("/jokes/random")
    fun getRandom(): Observable<RandomCategory>

    @GET("/jokes/search")
    fun getSearch(@Query("query") query: String): Observable<SearchFreeResponse>

    companion object Factory {
        fun create(): Service {

            val logging = HttpLoggingInterceptor()
            // set your desired log level
            logging.apply { logging.level = HttpLoggingInterceptor.Level.BODY }

            val httpClient = OkHttpClient.Builder()
            // add logging as last interceptor
            httpClient.addInterceptor(logging)

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(API_URL)
                .client(httpClient.build())
                .build()
            return retrofit.create(Service::class.java)
        }
    }
}
