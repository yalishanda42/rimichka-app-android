package bg.abv.ani1802.rimichka.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bg.abv.ani1802.rimichka.network.Rhyme
import bg.abv.ani1802.rimichka.network.RimichkaApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchRhymesViewModel : ViewModel() {

    private val _rhymes = MutableLiveData<List<Rhyme>>()
    val rhymes: LiveData<List<Rhyme>> get() = _rhymes

    fun fetchRhymesFor(word: String) {
        RimichkaApi.retrofitService.fetchRhymes(word).enqueue(
            object : Callback<List<Rhyme>> {
                override fun onFailure(call: Call<List<Rhyme>>, t: Throwable) {
                    Log.e(this::class.java.toString(), "Could not fetch rhymes (${t.message})")
                    // TODO: Handle error
                }

                override fun onResponse(call: Call<List<Rhyme>>, response: Response<List<Rhyme>>) {
                    _rhymes.value = response.body()?.sortedByDescending { rhyme ->
                        rhyme.precision
                    }
                }

            }
        )
    }
}
