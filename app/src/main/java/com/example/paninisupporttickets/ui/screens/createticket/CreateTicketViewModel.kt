package com.example.paninisupporttickets.ui.screens.createticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.paninisupporttickets.core.UserMessages
import com.example.paninisupporttickets.data.AppContainer
import com.example.paninisupporttickets.data.TicketCategory
import com.example.paninisupporttickets.data.TicketPriority
import com.example.paninisupporttickets.data.repository.ApiResult
import com.example.paninisupporttickets.data.repository.TicketRepository
import com.example.paninisupporttickets.util.FeatureFlags
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CreateTicketUiState(
    val title: String = "",
    val description: String = "",
    val providerName: String = "",
    val reportedBy: String = "",
    val location: String = "",
    val category: TicketCategory = TicketCategory.INVENTORY,
    val priority: TicketPriority = TicketPriority.MEDIUM,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isCreated: Boolean = false,
    val canCreateTicket: Boolean = FeatureFlags.ENABLE_TICKET_CREATION
)

class CreateTicketViewModel(
    private val ticketRepository: TicketRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateTicketUiState())
    val uiState: StateFlow<CreateTicketUiState> = _uiState.asStateFlow()

    fun updateTitle(value: String) = updateState { copy(title = value, errorMessage = null) }
    fun updateDescription(value: String) = updateState { copy(description = value, errorMessage = null) }
    fun updateProviderName(value: String) = updateState { copy(providerName = value, errorMessage = null) }
    fun updateReportedBy(value: String) = updateState { copy(reportedBy = value, errorMessage = null) }
    fun updateLocation(value: String) = updateState { copy(location = value, errorMessage = null) }
    fun updateCategory(value: TicketCategory) = updateState { copy(category = value, errorMessage = null) }
    fun updatePriority(value: TicketPriority) = updateState { copy(priority = value, errorMessage = null) }

    fun createTicket() {
        val state = _uiState.value

        if (!state.canCreateTicket) {
            _uiState.value = state.copy(errorMessage = UserMessages.CreateTicket.CREATION_DISABLED)
            return
        }

        if (state.title.isBlank() || state.description.isBlank() || state.providerName.isBlank() ||
            state.reportedBy.isBlank() || state.location.isBlank()
        ) {
            _uiState.value = state.copy(errorMessage = UserMessages.CreateTicket.REQUIRED_FIELDS)
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = ticketRepository.createTicket(
                title = state.title.trim(),
                description = state.description.trim(),
                providerName = state.providerName.trim(),
                category = state.category,
                priority = state.priority,
                reportedBy = state.reportedBy.trim(),
                location = state.location.trim()
            )

            _uiState.value = when (result) {
                is ApiResult.Success -> _uiState.value.copy(isLoading = false, isCreated = true)
                is ApiResult.Error -> _uiState.value.copy(
                    isLoading = false,
                    errorMessage = result.message.ifBlank { UserMessages.CreateTicket.CREATE_ERROR }
                )
            }
        }
    }

    private fun updateState(block: CreateTicketUiState.() -> CreateTicketUiState) {
        _uiState.value = _uiState.value.block()
    }
}

class CreateTicketViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CreateTicketViewModel(AppContainer.ticketRepository) as T
    }
}
