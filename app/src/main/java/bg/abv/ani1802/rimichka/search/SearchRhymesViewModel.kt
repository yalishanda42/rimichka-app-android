package bg.abv.ani1802.rimichka.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bg.abv.ani1802.rimichka.network.RimichkaApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchRhymesViewModel : ViewModel() {

    private val _response = MutableLiveData<String>()
    val response: LiveData<String> get() = _response

    fun fetchRhymesFor(word: String) {
        RimichkaApi.retrofitService.fetchRhymes(word).enqueue(
            object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    _response.value = "Failure: ${t.message}"
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    _response.value = response.body()
                }

            }
        )
    }
}
