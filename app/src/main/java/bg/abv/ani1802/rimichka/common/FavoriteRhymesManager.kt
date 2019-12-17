package bg.abv.ani1802.rimichka.common

import bg.abv.ani1802.rimichka.network.Rhyme

object FavoriteRhymesManager {

    private var favoriteRhymes: MutableSet<RhymePair> = fetchFromLocalDatabase()

    fun favoriteRhymesContain(rhyme: Rhyme, parentWord: String): Boolean {
        val pair = RhymePair(parentWord, rhyme)
        return favoriteRhymes.contains(pair)
    }

    fun addFavoriteRhyme(rhyme: Rhyme, parentWord: String) {
        val pair = RhymePair(parentWord, rhyme)
        favoriteRhymes.add(pair)
        saveRhymePairIntoDatabase(pair)
    }

    fun removeRhymeFromFavorites(rhyme: Rhyme, parentWord: String) {
        val pair = RhymePair(parentWord, rhyme)
        favoriteRhymes.remove(pair)
        deleteRhymePairFromDatabase(pair)
    }

    private fun fetchFromLocalDatabase(): MutableSet<RhymePair> {
        return mutableSetOf() // TODO: Implement persistence
    }

    private fun saveRhymePairIntoDatabase(rhymePair: RhymePair) {
        // TODO: Implement persistence
    }

    private fun deleteRhymePairFromDatabase(rhymePair: RhymePair) {
        // TODO: Implement persistence
    }
}