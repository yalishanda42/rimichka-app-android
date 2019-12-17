package bg.abv.ani1802.rimichka.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bg.abv.ani1802.rimichka.common.FavoriteRhymesRepository
import bg.abv.ani1802.rimichka.common.SingleRhymeViewModel
import bg.abv.ani1802.rimichka.network.Rhyme
import bg.abv.ani1802.rimichka.network.RimichkaApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchRhymesViewModel : ViewModel() {

    private val logTag = this::class.java.simpleName

    // Bindings

    val searchQuery = MutableLiveData<String>()

    private val _searchButtonIsEnabled = MutableLiveData<Boolean>(false)
    val searchButtonIsEnabled: LiveData<Boolean> get() = _searchButtonIsEnabled

    private val _rhymeViewModels = MutableLiveData<List<SingleRhymeViewModel>>()
    val rhymeViewModels: LiveData<List<SingleRhymeViewModel>> get() = _rhymeViewModels

    // Data

    private var previousSearchQuery: String? = null

    private var rhymes: List<Rhyme> = emptyList()
        set(value) {
            field = value
            _rhymeViewModels.value = value.map { rhyme ->
                val parentWord = searchQuery.value ?: return
                val isToggled = FavoriteRhymesRepository.favoriteRhymesContain(rhyme, parentWord)
                SingleRhymeViewModel(
                    rhyme.word,
                    onToggleListener = { shouldBeSaved ->
                        if (shouldBeSaved) {
                            FavoriteRhymesRepository.addFavoriteRhyme(rhyme, parentWord)
                        } else {
                            FavoriteRhymesRepository.removeRhymeFromFavorites(rhyme, parentWord)
                        }
                    }, onClickRhyme = {
                        searchQuery.value = rhyme.word
                        fetchRhymes()
                }).apply {
                    isMarked.set(isToggled)
                }
            }
        }

    // Coroutines

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    // Actions and listeners

    fun fetchRhymes() {
        searchQuery.value?.let { word ->
            _searchButtonIsEnabled.value = false
            previousSearchQuery = word
            coroutineScope.launch {
                val fetchRhymesDeferred = RimichkaApi.retrofitService.fetchRhymesAsync(word)
                try {
                    val listResult = fetchRhymesDeferred.await()
                    Log.d(
                        logTag,
                        "Successfully fetched ${listResult.count()} rhymes for the word '${word}'."
                    )
                    rhymes = listOfFetchedRhymesSorted(listResult, removingWord = word)
                } catch (e: Exception) {
                    Log.e(logTag, "Failed to fetch rhymes (${e.message})")
                    // TODO: Handle error in UI
                }
            }
        }
    }

    fun onSearchQueryChanged() {
        searchQuery.value?.let { current ->
            if (current == "") {
                _searchButtonIsEnabled.value = false
                return
            }

            previousSearchQuery?.let { last ->
                if (current == last) {
                    _searchButtonIsEnabled.value = false
                    return
                }
            }

            _searchButtonIsEnabled.value = true

        } ?: run {
            _searchButtonIsEnabled.value = false
        }
    }

    // Lifecycle

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    // Helpers

    private fun listOfFetchedRhymesSorted(rhymes: List<Rhyme>, removingWord: String?): List<Rhyme> {
        var result = rhymes.sortedByDescending { rhyme -> rhyme.precision }
        removingWord?.let { word ->
            result = result.filter { rhyme -> rhyme.word != word }
        }
        return result
    }
}
