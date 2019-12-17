package bg.abv.ani1802.rimichka.common

import android.content.Context
import android.util.Log
import bg.abv.ani1802.rimichka.common.models.RhymePair
import bg.abv.ani1802.rimichka.common.models.Rhyme
import bg.abv.ani1802.rimichka.database.FavoriteRhymesDatabase
import kotlinx.coroutines.*

object FavoriteRhymesRepository {

    // Coroutines

    private val job = Job()
    private val scope = CoroutineScope(job)

    // In-memory list

    private var favoriteRhymesSet: MutableSet<RhymePair> = mutableSetOf()
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
        scope.launch {
            saveRhymePairIntoDatabase(rhymePair)
        }
        notifyObservers()
    }

    fun removeRhymeFromFavorites(rhyme: Rhyme, parentWord: String) {
        val pair = RhymePair(parentWord, rhyme)
        removeRhymeFromFavorites(pair)
    }

    fun removeRhymeFromFavorites(rhymePair: RhymePair) {
        favoriteRhymesSet.remove(rhymePair)
        scope.launch {
            deleteRhymePairFromDatabase(rhymePair)
        }
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
                scope.launch {
                    favoriteRhymesSet = fetchFromLocalDatabase()
                }
            }
        }

    private fun getDatabaseInstance() {
        val context = context ?: return
        database = FavoriteRhymesDatabase.getInstance(context)
    }

    private suspend fun fetchFromLocalDatabase(): MutableSet<RhymePair> {
        val database = database ?: return mutableSetOf()
        return withContext(Dispatchers.IO) {
            val fetchedEntities = database.favoriteRhymesDatabaseDAO.getAllRhymePairs()
            fetchedEntities.value?.toMutableSet() ?: mutableSetOf<RhymePair>()
        }
    }

    private suspend fun saveRhymePairIntoDatabase(rhymePair: RhymePair) {
        val database = database ?: return
        withContext(Dispatchers.IO) {
            database.favoriteRhymesDatabaseDAO.insertRhymePair(rhymePair)
            val test = database.favoriteRhymesDatabaseDAO
                .getAllRhymePairs()
            Log.e("test", test.value.toString())
        }
    }

    private suspend fun deleteRhymePairFromDatabase(rhymePair: RhymePair) {
        val database = database ?: return
        withContext(Dispatchers.IO) {
            database.favoriteRhymesDatabaseDAO.deleteRhymePair(rhymePair)
        }
    }
}