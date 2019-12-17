package bg.abv.ani1802.rimichka.common.models

import androidx.room.Entity

@Entity(primaryKeys = ["parentWord", "rhyme"], tableName = "favorite_rhymes_table")
data class RhymePair(

    val parentWord: String,

    val rhyme: String,

    val precision: Int
) {
    constructor(parentWord: String, rhyme: Rhyme) : this(
        parentWord,
        rhyme.word,
        rhyme.precision
    )
}