package com.example.pokedex.data.repository

import com.example.pokedex.data.model.PokemonDetail
import com.example.pokedex.data.model.PokemonListItem
import com.example.pokedex.data.network.ProfessorOakNetworkClient
import javax.inject.Inject

class PokemonRepository @Inject constructor(
    private val networkClient: ProfessorOakNetworkClient
) {
    suspend fun getGen1Pokemon(): List<PokemonListItem> {
        val allPokemon = mutableListOf<PokemonListItem>()
        var offset = 0
        val limit = 42
        val maxCount = 151

        while (allPokemon.size < maxCount) {
            val remaining = maxCount - allPokemon.size
            val fetchLimit = minOf(limit, remaining)

            val response = networkClient.apiService.getPokemonList(
                limit = fetchLimit,
                offset = offset
            )
            allPokemon.addAll(response.results)
            offset += fetchLimit
        }
        return allPokemon
    }
    suspend fun getPokemonDetail(id: Int): PokemonDetail {
        return networkClient.apiService.getPokemonDetail(id)
    }
}