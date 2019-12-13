package bg.abv.ani1802.rimichka.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RimichkaApiService {
    @GET("/?json=1")
    fun fetchRhymes(@Query("word") word: String): Call<List<Rhyme>>
}