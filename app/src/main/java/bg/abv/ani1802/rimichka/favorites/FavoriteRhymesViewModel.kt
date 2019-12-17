package bg.abv.ani1802.rimichka.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bg.abv.ani1802.rimichka.common.FavoriteRhymesManager
import bg.abv.ani1802.rimichka.common.FavoriteRhymesObserver
import bg.abv.ani1802.rimichka.common.RhymePair
import bg.abv.ani1802.rimichka.common.SingleRhymeViewModel
import bg.abv.ani1802.rimichka.network.Rhyme

class FavoriteRhymesViewModel : ViewModel(), FavoriteRhymesObserver {

    init {
        FavoriteRhymesManager.addObserver(this)
    }

    private val _favoriteRhymesViewModels = MutableLiveData<List<SingleRhymeViewModel>>(
        convertRhymePairsToViewModels(FavoriteRhymesManager.getAllFavoriteRhymes())
    )
    val favoriteRhymesViewModels: LiveData<List<SingleRhymeViewModel>>
        get() = _favoriteRhymesViewModels

    override fun onFavoriteRhymesUpdate(newList: List<RhymePair>) {
        _favoriteRhymesViewModels.value = convertRhymePairsToViewModels(newList)
    }

    private fun convertRhymePairsToViewModels(pairs: List<RhymePair>): List<SingleRhymeViewModel> {
        return pairs.map { pair ->
            SingleRhymeViewModel(
                "${pair.parentWord} -> ${pair.rhyme}"
            ) { shouldBeAdded ->
                val rhyme = Rhyme(pair.rhyme, pair.precision)
                if (shouldBeAdded) {
                    FavoriteRhymesManager.addFavoriteRhyme(pair)
                } else {
                    FavoriteRhymesManager.removeRhymeFromFavorites(pair)
                }
            }.apply {
                isMarked.set(true)
            }
        }
    }
}
