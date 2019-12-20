package bg.abv.ani1802.rimichka.repository

import android.content.Context
import android.util.Log
import bg.abv.ani1802.rimichka.common.models.RhymePair
import bg.abv.ani1802.rimichka.common.models.Rhyme
import bg.abv.ani1802.rimichka.database.FavoriteRhymesDatabase
import bg.abv.ani1802.rimichka.network.RimichkaApi
import kotlinx.coroutines.*

object RimichkaRepository {

    // Network fetch

    /**
     * Fetches rhymes for a given word using the Rimichka.com API.
     *
     * @param word The word to be rhymed.
     * @param logTag (optional) The logger tag to be used on fetch success/fail.
     * @return A pair containing the returned [List] of [Rhyme] objects and a [Boolean] indicating
     * whether the API call was successful or not. If it was not, an empty [List] is returned.
     */
    suspend fun fetchRhymesFor(
        word: String,
        logTag: String = this::class.java.toString()
    ) : Pair<List<Rhyme>, Boolean> {

        val fetchRhymesDeferred = RimichkaApi.retrofitService.fetchRhymesAsync(word)

        return try {
            val listResult = fetchRhymesDeferred.await()

            Log.d(
                logTag,
                "Successfully fetched ${listResult.count()} rhymes for the word '${word}'."
            )

            Pair(listResult, true)

        } catch (e: Exception) {
            Log.e(logTag, "Failed to fetch rhymes (exception: ${e.message})")

            Pair(emptyList<Rhyme>(), false)
        }
    }

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

    suspend fun addFavoriteRhyme(rhyme: Rhyme, parentWord: String) {
        val pair = RhymePair(parentWord, rhyme)
        addFavoriteRhyme(pair)
    }

    suspend fun addFavoriteRhyme(rhymePair: RhymePair) {
        favoriteRhymesSet.add(rhymePair)
        saveRhymePairIntoDatabase(rhymePair)
        notifyObservers()
    }

    suspend fun removeRhymeFromFavorites(rhyme: Rhyme, parentWord: String) {
        val pair = RhymePair(parentWord, rhyme)
        removeRhymeFromFavorites(pair)
    }

    suspend fun removeRhymeFromFavorites(rhymePair: RhymePair) {
        favoriteRhymesSet.remove(rhymePair)
        deleteRhymePairFromDatabase(rhymePair)
        notifyObservers()
    }

    // Observers

    private var observers: MutableList<FavoriteRhymesObserver> = mutableListOf()

    /**
     * Subscribe to changes in the favorite rhymes.
     */
    fun addObserver(observer: FavoriteRhymesObserver) {
        observers.add(observer)
    }

    /**
     * Unsubscribe to changes in the favorite rhymes.
     */
    fun removeObserver(observer: FavoriteRhymesObserver) {
        observers.remove(observer)
    }

    private fun notifyObservers() {
        for (observer in observers) {
            observer.onFavoriteRhymesUpdate(favoriteRhymesSet.toList())
        }
    }

    // Database

    /**
     * Set the application context in order to retrieve the database.
     * This  needs to be set from an Android context-aware class such as MainActivity for example.
     */
    var context: Context? = null
        set(value) {
            field = value
            context?.let {
                getDatabaseInstance()
            }
        }

    private var database: FavoriteRhymesDatabase? = null

    private fun getDatabaseInstance() {
        val context = context ?: return
        database = FavoriteRhymesDatabase.getInstance(context)
    }

    /**
     * Fetch all saved favorite rhymes in the database.
     */
    suspend fun refreshRhymesFromLocalDatabase() {
        val database = database ?: return
        favoriteRhymesSet = withContext(Dispatchers.IO) {
            database.favoriteRhymesDatabaseDAO.getAllRhymePairs().toMutableSet()
        }
    }

    private suspend fun saveRhymePairIntoDatabase(rhymePair: RhymePair) {
        val database = database ?: return
        withContext(Dispatchers.IO) {
            database.favoriteRhymesDatabaseDAO.insertRhymePair(rhymePair)
        }
    }

    private suspend fun deleteRhymePairFromDatabase(rhymePair: RhymePair) {
        val database = database ?: return
        withContext(Dispatchers.IO) {
            database.favoriteRhymesDatabaseDAO.deleteRhymePair(rhymePair)
        }
    }
}