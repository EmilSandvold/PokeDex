package com.example.pokedex.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class PokemonDetail (
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "height") val height: Int,
    @Json(name = "weight") val weight: Int,
    @Json(name = "sprites") val sprites: Sprites,
    @Json(name = "types") val types: List<TypeSlot>
)

@JsonClass(generateAdapter = true)
data class Sprites (
    @Json(name = "front_default") val frontDefault: String?
)

@JsonClass(generateAdapter = true)
data class TypeSlot (
    @Json(name = "type") val type: TypeInfo
)

@JsonClass(generateAdapter = true)
data class TypeInfo (
    @Json(name = "name") val name: String
)