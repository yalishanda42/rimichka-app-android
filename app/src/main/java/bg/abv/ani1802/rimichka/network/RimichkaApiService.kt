package bg.abv.ani1802.rimichka.network

import bg.abv.ani1802.rimichka.models.Rhyme
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface RimichkaApiService {
    @GET("/?json=1")
    fun fetchRhymesAsync(@Query("word") word: String): Deferred<List<Rhyme>>
}