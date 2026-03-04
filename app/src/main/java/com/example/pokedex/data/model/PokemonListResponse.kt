package com.example.pokedex.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass



@JsonClass(generateAdapter = true)
data class PokemonListResponse (
    @Json(name = "results") val results: List<PokemonListItem>
)


data class PokemonListItem (
    @Json(name = "name") val name: String,
    @Json(name = "url") val url: String
) {
    val id: Int
        get() = url.trimEnd('/').substringAfterLast('/').toInt()

    val imageUrl: String
        get() = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
}