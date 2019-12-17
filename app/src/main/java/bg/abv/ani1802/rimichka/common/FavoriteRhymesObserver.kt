package bg.abv.ani1802.rimichka.common

import bg.abv.ani1802.rimichka.models.RhymePair

interface FavoriteRhymesObserver {
    fun onFavoriteRhymesUpdate(newList: List<RhymePair>)
}
