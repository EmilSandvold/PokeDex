package com.example.pokedex.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.data.model.PokemonDetail
import com.example.pokedex.data.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    sealed class UiState {
        object Loading : UiState()
        data class Success(val pokemon: PokemonDetail) : UiState()
        data class Error(val message: String) : UiState()
    }

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    fun loadPokemon(id: Int) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val pokemon = repository.getPokemonDetail(id)
                _uiState.value = UiState.Success(pokemon)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Ukjent feil")
            }
        }
    }
}