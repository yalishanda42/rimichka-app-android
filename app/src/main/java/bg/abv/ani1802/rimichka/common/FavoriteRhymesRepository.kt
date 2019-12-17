package bg.abv.ani1802.rimichka.common

import android.content.Context
import bg.abv.ani1802.rimichka.common.models.RhymePair
import bg.abv.ani1802.rimichka.common.models.Rhyme
import bg.abv.ani1802.rimichka.database.FavoriteRhymesDatabase
import bg.abv.ani1802.rimichka.database.RhymePairEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

object FavoriteRhymesRepository {

    // Coroutines

    private val job = Job()
    private val scope = CoroutineScope(job)

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

    // Database

    //
    var context: Context? = null
        set(value) {
            field = value
            context?.let {
                getDatabaseInstance()
            }
        }

    private var database: FavoriteRhymesDatabase? = null
        set(value) {
            field = value
            value?.let {
                fetchFromLocalDatabase()
            }
        }

    private fun getDatabaseInstance() {
        val context = context ?: return
        database = FavoriteRhymesDatabase.getInstance(context)
    }

    private fun fetchFromLocalDatabase(): MutableSet<RhymePair> {
        val database = database ?: return mutableSetOf()
        val fetchedEntities = database.favoriteRhymesDatabaseDAO.getAllRhymePairs()
        val toMutableSet = fetchedEntities.value?.map {
            RhymePair(it.parentWord, it.rhyme, it.precision)
        }?.toMutableSet()
        return if (toMutableSet != null) toMutableSet else mutableSetOf()
    }

    private fun saveRhymePairIntoDatabase(rhymePair: RhymePair) {
        val database = database ?: return
        val newEntity = RhymePairEntity(
            parentWord = rhymePair.parentWord,
            rhyme = rhymePair.rhyme,
            precision = rhymePair.precision
        )
        database.favoriteRhymesDatabaseDAO.insertRhymePair(newEntity)
    }

    private fun deleteRhymePairFromDatabase(rhymePair: RhymePairEntity) {
        val database = database ?: return

    }
}