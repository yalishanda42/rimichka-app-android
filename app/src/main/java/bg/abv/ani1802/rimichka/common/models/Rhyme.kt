package bg.abv.ani1802.rimichka.common.models

import com.squareup.moshi.Json

data class Rhyme(
    @Json(name = "wrd") val word: String,
    @Json(name = "pri") val precision: Int
)