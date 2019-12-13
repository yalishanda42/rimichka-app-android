package bg.abv.ani1802.rimichka.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bg.abv.ani1802.rimichka.network.Rhyme
import bg.abv.ani1802.rimichka.network.RimichkaApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchRhymesViewModel : ViewModel() {

    val logTag = this::class.java.simpleName

    private val _rhymes = MutableLiveData<List<Rhyme>>()
    val rhymes: LiveData<List<Rhyme>> get() = _rhymes

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    fun fetchRhymesFor(word: String) {
       coroutineScope.launch {
           val fetchRhymesDeferred = RimichkaApi.retrofitService.fetchRhymesAsync(word)
           try {
               val listResult = fetchRhymesDeferred.await()
               Log.d(logTag, "Successfully fetched ${listResult.count()} rhymes for the word '${word}'.")
               _rhymes.value = listResult.sortedByDescending { rhyme -> rhyme.precision }
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
}
