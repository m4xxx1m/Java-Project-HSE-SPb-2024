package network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    const val BASE_URL: String = "http://192.168.1.5:8080"

    private var token: String? = null

    fun setToken(token: String) {
        if (this.token == null) {
            this.token = token
            retrofit_ = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(getHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            retrofitCall_ = retrofit.create(ApiInterface::class.java)
        }
    }

    private fun getHttpClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        val interceptor = Interceptor { chain ->
            val requestBuilder = chain.request().newBuilder()
            requestBuilder.header("Authorization", "Bearer $token")
            chain.proceed(requestBuilder.build())
        }
        httpClient.addInterceptor(interceptor)
        return httpClient.build()
    }

    private var retrofit_: Retrofit? = null

    private val retrofit: Retrofit
        get() = retrofit_!!

    private var retrofitCall_: ApiInterface? = null

    val retrofitCall: ApiInterface
        get() = retrofitCall_!!
    
}
