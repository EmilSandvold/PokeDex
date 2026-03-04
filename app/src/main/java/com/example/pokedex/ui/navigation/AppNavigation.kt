package com.example.pokedex.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import com.example.pokedex.ui.detail.PokemonDetailScreen
import com.example.pokedex.ui.list.PokemonListScreen
import androidx.navigation.toRoute

@Serializable
object PokemonListRoute

@Serializable
data class PokemonDetailRoute(val pokemonId: Int)

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = PokemonListRoute
    ) {
        composable<PokemonListRoute> {
            PokemonListScreen(
                onPokemonClick = { pokemonId ->
                    navController.navigate(PokemonDetailRoute(pokemonId))
                }
            )
        }

        composable<PokemonDetailRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<PokemonDetailRoute>()
            PokemonDetailScreen(
                pokemonId = route.pokemonId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}