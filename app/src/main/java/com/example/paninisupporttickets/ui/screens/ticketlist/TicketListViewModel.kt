package com.example.paninisupporttickets.ui.screens.ticketlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.paninisupporttickets.core.UserMessages
import com.example.paninisupporttickets.data.AppContainer
import com.example.paninisupporttickets.data.Ticket
import com.example.paninisupporttickets.data.repository.TicketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

data class TicketListUiState(
    val isLoading: Boolean = false,
    val tickets: List<Ticket> = emptyList(),
    val errorMessage: String? = null
)

class TicketListViewModel(
    private val ticketRepository: TicketRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(TicketListUiState())
    val uiState: StateFlow<TicketListUiState> = _uiState.asStateFlow()

    init {
        observeTickets()
    }

    private fun observeTickets() {
        viewModelScope.launch {
            ticketRepository.ticketsFlow
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = e.message ?: UserMessages.TicketList.LOAD_ERROR
                    )
                }
                .collect { tickets ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        tickets = tickets,
                        errorMessage = null
                    )
                }
        }
    }

    fun refresh() {
        _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = null)
    }
}

class TicketListViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TicketListViewModel(AppContainer.ticketRepository) as T
    }
}
