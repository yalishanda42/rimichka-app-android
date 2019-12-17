package bg.abv.ani1802.rimichka.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_rhymes_table")
data class RhymePairEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "parent_word")
    val parentWord: String = "",

    @ColumnInfo(name = "rhyme")
    val rhyme: String = "",

    @ColumnInfo(name = "precision")
    val precision: Int = 0
)