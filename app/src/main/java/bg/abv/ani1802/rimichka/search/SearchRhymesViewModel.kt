package bg.abv.ani1802.rimichka.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bg.abv.ani1802.rimichka.common.SingleRhymeViewModel
import bg.abv.ani1802.rimichka.common.models.Rhyme
import bg.abv.ani1802.rimichka.repository.RimichkaRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchRhymesViewModel : ViewModel() {

    private val logTag = this::class.java.simpleName

    // Bindings

    val searchQuery = MutableLiveData<String>()  // Two-way binding

    private val _searchButtonIsEnabled = MutableLiveData<Boolean>(false)
    val searchButtonIsEnabled: LiveData<Boolean> get() = _searchButtonIsEnabled

    private val _rhymeViewModels = MutableLiveData<List<SingleRhymeViewModel>>()
    val rhymeViewModels: LiveData<List<SingleRhymeViewModel>> get() = _rhymeViewModels

    private val _state = MutableLiveData<SearchRhymesStateEnum>(SearchRhymesStateEnum.INITIAL)
    val state: LiveData<SearchRhymesStateEnum> get() = _state

    // Data

    private var previousSearchQuery: String? = null

    private var rhymes: List<Rhyme> = emptyList()
        set(value) {
            field = value
            _rhymeViewModels.value = value.map { rhyme ->
                val parentWord = searchQuery.value ?: return
                val isToggled = RimichkaRepository.favoriteRhymesContain(rhyme, parentWord)
                SingleRhymeViewModel(
                    rhyme.word,
                    onToggleListener = { shouldBeSaved ->
                        onToggleFavoriteRhyme(rhyme, parentWord, shouldBeSaved)
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
            _state.value = SearchRhymesStateEnum.LOADING
            coroutineScope.launch {
                val (fetchedList, wasSuccessful) = RimichkaRepository.fetchRhymesFor(word, logTag)
                rhymes = listOfFetchedRhymesSorted(fetchedList, removingWord = word)

                if (wasSuccessful) {
                    _state.value = if (rhymes.count() == 0) {
                        SearchRhymesStateEnum.NO_RESULTS
                    } else {
                        SearchRhymesStateEnum.HAS_RESULTS
                    }
                } else {
                    _state.value = SearchRhymesStateEnum.LOADING_FAILED
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

    private fun onToggleFavoriteRhyme(
        rhyme: Rhyme,
        withParentWord: String,
        shouldBeSavedToFavorites: Boolean
    ) {
        if (shouldBeSavedToFavorites) {
            coroutineScope.launch {
                RimichkaRepository.addFavoriteRhyme(rhyme, withParentWord)
            }
        } else {
            coroutineScope.launch {
                RimichkaRepository.removeRhymeFromFavorites(rhyme, withParentWord)
            }
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
