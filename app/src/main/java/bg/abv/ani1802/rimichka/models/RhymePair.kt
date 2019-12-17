package bg.abv.ani1802.rimichka.models

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