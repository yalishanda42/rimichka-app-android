package bg.abv.ani1802.rimichka.common

import androidx.lifecycle.MutableLiveData
import bg.abv.ani1802.rimichka.network.Rhyme

object FavoriteRhymesManager {

    // In-memory list

    private var favoriteRhymesSet: MutableSet<RhymePair> = fetchFromLocalDatabase()
        set(value) {
            field = value
            notifyObservers()
        }

    fun getAllFavoriteRhymes(): List<RhymePair> {
        return favoriteRhymesSet.toList()
    }

    fun favoriteRhymesContain(rhyme: Rhyme, parentWord: String): Boolean {
        val pair = RhymePair(parentWord, rhyme)
        return favoriteRhymesSet.contains(pair)
    }

    fun addFavoriteRhyme(rhyme: Rhyme, parentWord: String) {
        val pair = RhymePair(parentWord, rhyme)
        addFavoriteRhyme(pair)
    }

    fun addFavoriteRhyme(rhymePair: RhymePair) {
        favoriteRhymesSet.add(rhymePair)
        saveRhymePairIntoDatabase(rhymePair)
        notifyObservers()
    }

    fun removeRhymeFromFavorites(rhyme: Rhyme, parentWord: String) {
        val pair = RhymePair(parentWord, rhyme)
        removeRhymeFromFavorites(pair)
    }

    fun removeRhymeFromFavorites(rhymePair: RhymePair) {
        favoriteRhymesSet.remove(rhymePair)
        deleteRhymePairFromDatabase(rhymePair)
        notifyObservers()
    }

    // Observers

    private var observers: MutableList<FavoriteRhymesObserver> = mutableListOf()

    fun addObserver(observer: FavoriteRhymesObserver) {
        observers.add(observer)
    }

    fun removeObserver(observer: FavoriteRhymesObserver) {
        observers.remove(observer)
    }

    private fun notifyObservers() {
        for (observer in observers) {
            observer.onFavoriteRhymesUpdate(favoriteRhymesSet.toList())
        }
    }

    // Database helpers

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