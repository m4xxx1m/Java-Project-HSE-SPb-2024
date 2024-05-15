package network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientPreAuth {
    private const val BASE_URL: String = "http://192.168.1.5:8080"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitCall: ApiInterfacePreAuth = retrofit.create(ApiInterfacePreAuth::class.java)
}
