package bg.abv.ani1802.rimichka.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RhymePairEntity::class], version = 1, exportSchema = false)
abstract class FavoriteRhymeDatabase : RoomDatabase() {

    abstract val favoriteRhymesDatabaseDAO: FavoriteRhymesDatabaseDAO

    companion object {
        @Volatile private var INSTANCE: FavoriteRhymeDatabase? = null
        fun getInstance(context: Context): FavoriteRhymeDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FavoriteRhymeDatabase::class.java,
                        "favorite_rhymes_database"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}