package bg.abv.ani1802.rimichka.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bg.abv.ani1802.rimichka.repository.RimichkaRepository
import bg.abv.ani1802.rimichka.repository.FavoriteRhymesObserver
import bg.abv.ani1802.rimichka.common.models.RhymePair
import bg.abv.ani1802.rimichka.common.SingleRhymeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FavoriteRhymesViewModel : ViewModel(),
    FavoriteRhymesObserver {

    // Coroutines

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    // Bindings

    private val _favoriteRhymesViewModels = MutableLiveData<List<SingleRhymeViewModel>>(
        convertRhymePairsToViewModels(RimichkaRepository.getAllFavoriteRhymes())
    )
    val favoriteRhymesViewModels: LiveData<List<SingleRhymeViewModel>>
        get() = _favoriteRhymesViewModels

    // Init

    init {
        RimichkaRepository.addObserver(this)
        coroutineScope.launch {
            RimichkaRepository.refreshRhymesFromLocalDatabase()
        }
    }

    // Listeners

    var onClickRhymeListener: ((String) -> Unit)? = null

    override fun onFavoriteRhymesUpdate(newList: List<RhymePair>) {
        _favoriteRhymesViewModels.value = convertRhymePairsToViewModels(newList)
    }

    private fun onFavoriteRhymeToggleListener(
        rhymePair: RhymePair,
        shouldBeAddedToFavorites: Boolean
    ) {
        coroutineScope.launch {
            if (shouldBeAddedToFavorites) {
                RimichkaRepository.addFavoriteRhyme(rhymePair)
            } else {
                RimichkaRepository.removeRhymeFromFavorites(rhymePair)
            }
        }
    }

    // Lifecycle

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    // Helpers

    private fun convertRhymePairsToViewModels(pairs: List<RhymePair>): List<SingleRhymeViewModel> {
        return pairs.map { pair ->
            SingleRhymeViewModel(
                "${pair.parentWord} -> ${pair.rhyme}",
                onToggleListener = { shouldBeAdded ->
                    // Clicking the favorite icon toggles whether it is favorite or not
                    onFavoriteRhymeToggleListener(pair, shouldBeAdded)
                }, onClickRhyme = {
                    // Clicking the text searches for rhymes with that parent word
                    onClickRhymeListener?.invoke(pair.parentWord)
                }
            ).apply {
                isMarked.set(true)
            }
        }
    }
}
