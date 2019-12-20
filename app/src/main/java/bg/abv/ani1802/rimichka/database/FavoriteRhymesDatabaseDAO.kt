package bg.abv.ani1802.rimichka.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import bg.abv.ani1802.rimichka.common.models.RhymePair

@Dao
interface FavoriteRhymesDatabaseDAO {

    @Query("SELECT * FROM favorite_rhymes_table")
    fun getAllRhymePairs(): List<RhymePair>

    @Insert
    fun insertRhymePair(rhymePair: RhymePair)

    @Delete
    fun deleteRhymePair(rhymePair: RhymePair)
}