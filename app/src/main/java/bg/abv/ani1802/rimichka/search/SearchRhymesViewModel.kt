package bg.abv.ani1802.rimichka.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bg.abv.ani1802.rimichka.common.SingleRhymeViewModel
import bg.abv.ani1802.rimichka.network.Rhyme
import bg.abv.ani1802.rimichka.network.RimichkaApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchRhymesViewModel : ViewModel() {

    val logTag = this::class.java.simpleName

    private var rhymes: List<Rhyme> = emptyList()
        set(value) {
            field = value
            _rhymeViewModels.value = value.map { rhyme ->
                SingleRhymeViewModel(rhyme.word) // TODO: save favorite rhymes
            }
        }

    private val _rhymeViewModels = MutableLiveData<List<SingleRhymeViewModel>>()
    val rhymeViewModels: LiveData<List<SingleRhymeViewModel>> get() = _rhymeViewModels

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    fun fetchRhymesFor(word: String) {
       coroutineScope.launch {
           val fetchRhymesDeferred = RimichkaApi.retrofitService.fetchRhymesAsync(word)
           try {
               val listResult = fetchRhymesDeferred.await()
               Log.d(logTag, "Successfully fetched ${listResult.count()} rhymes for the word '${word}'.")
               rhymes = listOfFetchedRhymesSorted(listResult, removingWord = word)
           } catch (e: Exception) {
               Log.e(logTag, "Failed to fetch rhymes (${e.message})")
               // TODO: Handle error in UI
           }
       }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private fun listOfFetchedRhymesSorted(rhymes: List<Rhyme>, removingWord: String?): List<Rhyme> {
        var result = rhymes.sortedByDescending { rhyme -> rhyme.precision }
        removingWord?.let { word ->
            result = result.filter { rhyme -> rhyme.word != word }
        }
        return result
    }
}
