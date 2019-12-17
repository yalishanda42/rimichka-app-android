package bg.abv.ani1802.rimichka.common

interface FavoriteRhymesObserver {
    fun onFavoriteRhymesUpdate(newList: List<RhymePair>)
}
