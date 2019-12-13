package bg.abv.ani1802.rimichka.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://rimichka.com"
private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface RimichkaApiService {
    @GET("/?json=1")
    fun fetchRhymes(@Query("word") word: String): Call<String>
}

object RimichkaApi {
    val retrofitService : RimichkaApiService by lazy {
        retrofit.create(RimichkaApiService::class.java)
    }
}