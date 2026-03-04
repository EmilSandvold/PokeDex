package com.example.pokedex.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.pokedex.data.model.PokemonDetail
import com.example.pokedex.data.model.Sprites
import com.example.pokedex.data.model.TypeInfo
import com.example.pokedex.data.model.TypeSlot
import com.example.pokedex.ui.theme.PokeDexTheme


@Composable
fun PokemonDetailScreen(
    pokemonId: Int,
    onBackClick: () -> Unit,
    viewModel: PokemonDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(pokemonId) {
        viewModel.loadPokemon(pokemonId)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PokemonDetailScreenContent(
        uiState = uiState,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PokemonDetailScreenContent(
    uiState: PokemonDetailViewModel.UiState,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "PokeDex") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            when (uiState) {
                is PokemonDetailViewModel.UiState.Loading -> {
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is PokemonDetailViewModel.UiState.Error -> {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "Error: ${uiState.message}",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                is PokemonDetailViewModel.UiState.Success -> {
                    PokemonDetailContent(pokemon = uiState.pokemon)
                }
            }
        }
    }
}

@Composable
private fun PokemonDetailContent(pokemon: PokemonDetail) {
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = pokemon.name.capitalize(Locale.current),
            style = MaterialTheme.typography.headlineLarge
        )

        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(pokemon.sprites.frontDefault)
                .memoryCachePolicy(CachePolicy.DISABLED)
                .diskCachePolicy(CachePolicy.DISABLED)
                .build(),

            contentDescription = pokemon.name,
            modifier = Modifier.size(200.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Height",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = "${pokemon.height / 10.0} m",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Weight",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = "${pokemon.weight / 10.0} kg",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        Text(
            text = "Types",
            style = MaterialTheme.typography.labelMedium
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            pokemon.types.forEach { typeSlot ->
                AssistChip(
                    onClick = {},
                    label = {
                        Text(typeSlot.type.name.capitalize(Locale.current))
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PokemonDetailScreenPreview() {
    PokeDexTheme {
        PokemonDetailScreenContent(
            uiState = PokemonDetailViewModel.UiState.Success(
                pokemon = PokemonDetail(
                    id = 1,
                    name = "bulbasaur",
                    height = 7,
                    weight = 69,
                    sprites = Sprites("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png"),
                    types = listOf(
                        TypeSlot(TypeInfo("grass")),
                        TypeSlot(TypeInfo("poison"))
                    )
                )
            ),
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PokemonDetailScreenLoadingPreview() {
    PokeDexTheme {
        PokemonDetailScreenContent(
            uiState = PokemonDetailViewModel.UiState.Loading,
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PokemonDetailScreenErrorPreview() {
    PokeDexTheme {
        PokemonDetailScreenContent(
            uiState = PokemonDetailViewModel.UiState.Error("Failed to load Pokemon"),
            onBackClick = {}
        )
    }
}
