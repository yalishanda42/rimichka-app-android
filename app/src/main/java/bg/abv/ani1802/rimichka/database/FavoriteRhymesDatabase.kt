package bg.abv.ani1802.rimichka.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import bg.abv.ani1802.rimichka.common.models.RhymePair

@Database(entities = [RhymePair::class], version = 2, exportSchema = false)
abstract class FavoriteRhymesDatabase : RoomDatabase() {

    abstract val favoriteRhymesDatabaseDAO: FavoriteRhymesDatabaseDAO

    companion object {
        @Volatile private var INSTANCE: FavoriteRhymesDatabase? = null
        fun getInstance(context: Context): FavoriteRhymesDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FavoriteRhymesDatabase::class.java,
                        "favorite_rhymes_database"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}