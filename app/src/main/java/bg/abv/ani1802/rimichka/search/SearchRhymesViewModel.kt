package bg.abv.ani1802.rimichka.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bg.abv.ani1802.rimichka.common.SingleRhymeViewModel
import bg.abv.ani1802.rimichka.network.Rhyme
import bg.abv.ani1802.rimichka.network.RimichkaApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchRhymesViewModel : ViewModel() {

    private var rhymes: List<Rhyme> = emptyList()
        set(value) {
            field = value
            _rhymeViewModels.value = value.map { rhyme ->
                SingleRhymeViewModel(rhyme.word) // TODO: save favorite rhymes
            }
        }

    private val _rhymeViewModels = MutableLiveData<List<SingleRhymeViewModel>>()
    val rhymeViewModels: LiveData<List<SingleRhymeViewModel>> get() = _rhymeViewModels

    fun fetchRhymesFor(word: String) {
        RimichkaApi.retrofitService.fetchRhymes(word).enqueue(
            object : Callback<List<Rhyme>> {
                override fun onFailure(call: Call<List<Rhyme>>, t: Throwable) {
                    Log.e(this::class.java.toString(), "Could not fetch rhymes (${t.message})")
                    // TODO: Handle error
                }

                override fun onResponse(call: Call<List<Rhyme>>, response: Response<List<Rhyme>>) {
                    response.body()?.let { fetchedRhymes ->
                        rhymes = listOfFetchedRhymesSorted(fetchedRhymes, removingWord = word)
                    } ?: run {
                        rhymes = emptyList()
                    }
                }
            }
        )
    }

    private fun listOfFetchedRhymesSorted(rhymes: List<Rhyme>, removingWord: String?): List<Rhyme> {
        var result = rhymes.sortedByDescending { rhyme -> rhyme.precision }
        removingWord?.let { word ->
            result = result.filter { rhyme -> rhyme.word != word }
        }
        return result
    }
}
