package bg.abv.ani1802.rimichka.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bg.abv.ani1802.rimichka.common.FavoriteRhymesRepository
import bg.abv.ani1802.rimichka.common.FavoriteRhymesObserver
import bg.abv.ani1802.rimichka.common.models.RhymePair
import bg.abv.ani1802.rimichka.common.SingleRhymeViewModel
import bg.abv.ani1802.rimichka.common.models.Rhyme

class FavoriteRhymesViewModel : ViewModel(), FavoriteRhymesObserver {

    init {
        FavoriteRhymesRepository.addObserver(this)
    }

    private val _favoriteRhymesViewModels = MutableLiveData<List<SingleRhymeViewModel>>(
        convertRhymePairsToViewModels(FavoriteRhymesRepository.getAllFavoriteRhymes())
    )
    val favoriteRhymesViewModels: LiveData<List<SingleRhymeViewModel>>
        get() = _favoriteRhymesViewModels

    var onClickRhymeListener: ((String) -> Unit)? = null

    override fun onFavoriteRhymesUpdate(newList: List<RhymePair>) {
        _favoriteRhymesViewModels.value = convertRhymePairsToViewModels(newList)
    }

    private fun convertRhymePairsToViewModels(pairs: List<RhymePair>): List<SingleRhymeViewModel> {
        return pairs.map { pair ->
            SingleRhymeViewModel(
                "${pair.parentWord} -> ${pair.rhyme}",
                onToggleListener = { shouldBeAdded ->
                    if (shouldBeAdded) {
                        FavoriteRhymesRepository.addFavoriteRhyme(pair)
                    } else {
                        FavoriteRhymesRepository.removeRhymeFromFavorites(pair)
                    }
                }, onClickRhyme = {
                    onClickRhymeListener?.invoke(pair.parentWord)
                }
            ).apply {
                isMarked.set(true)
            }
        }
    }
}
