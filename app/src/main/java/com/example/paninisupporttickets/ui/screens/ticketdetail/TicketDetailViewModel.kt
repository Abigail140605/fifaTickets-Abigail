package com.example.paninisupporttickets.ui.screens.ticketdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.paninisupporttickets.core.UserMessages
import com.example.paninisupporttickets.data.AppContainer
import com.example.paninisupporttickets.data.Ticket
import com.example.paninisupporttickets.data.TicketPriority
import com.example.paninisupporttickets.data.TicketStatus
import com.example.paninisupporttickets.data.repository.ApiResult
import com.example.paninisupporttickets.data.repository.TicketRepository
import com.example.paninisupporttickets.util.FeatureFlags
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TicketDetailUiState(
    val isLoading: Boolean = true,
    val isUpdating: Boolean = false,
    val ticket: Ticket? = null,
    val errorMessage: String? = null,
    val canUpdatePriority: Boolean = FeatureFlags.ENABLE_PRIORITY_UPDATE
)

class TicketDetailViewModel(
    private val ticketId: String,
    private val ticketRepository: TicketRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(TicketDetailUiState())
    val uiState: StateFlow<TicketDetailUiState> = _uiState.asStateFlow()

    init {
        observeTicket()
    }

    private fun observeTicket() {
        viewModelScope.launch {
            ticketRepository.getTicketFlow(ticketId).collect { ticket ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    ticket = ticket,
                    errorMessage = if (ticket == null) UserMessages.TicketDetail.NOT_FOUND else null
                )
            }
        }
    }

    fun updateStatus(status: TicketStatus) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isUpdating = true, errorMessage = null)
            when (val result = ticketRepository.updateTicketStatus(ticketId, status)) {
                is ApiResult.Success -> _uiState.value = _uiState.value.copy(isUpdating = false)
                is ApiResult.Error -> _uiState.value = _uiState.value.copy(
                    isUpdating = false,
                    errorMessage = result.message
                )
            }
        }
    }

    fun updatePriority(priority: TicketPriority) {
        if (!FeatureFlags.ENABLE_PRIORITY_UPDATE) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isUpdating = true, errorMessage = null)
            when (val result = ticketRepository.updateTicketPriority(ticketId, priority)) {
                is ApiResult.Success -> _uiState.value = _uiState.value.copy(isUpdating = false)
                is ApiResult.Error -> _uiState.value = _uiState.value.copy(
                    isUpdating = false,
                    errorMessage = result.message
                )
            }
        }
    }
}

class TicketDetailViewModelFactory(
    private val ticketId: String
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TicketDetailViewModel(
            ticketId = ticketId,
            ticketRepository = AppContainer.ticketRepository
        ) as T
    }
}
