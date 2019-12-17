package bg.abv.ani1802.rimichka.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteRhymesDatabaseDAO {

    @Query("SELECT * FROM favorite_rhymes_table ORDER BY id DESC")
    fun getAllRhymePairs(): LiveData<List<RhymePairEntity>>

    @Insert
    fun insertRhymePair(rhymePair: RhymePairEntity)

    @Delete
    fun deleteRhymePair(rhymePair: RhymePairEntity)
}