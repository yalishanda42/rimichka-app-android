package bg.abv.ani1802.rimichka.common

import bg.abv.ani1802.rimichka.network.Rhyme

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