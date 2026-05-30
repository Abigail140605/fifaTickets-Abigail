package com.example.paninisupporttickets.ui.screens.ticketdetail

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.paninisupporttickets.core.UserMessages
import com.example.paninisupporttickets.data.Ticket
import com.example.paninisupporttickets.data.TicketPriority
import com.example.paninisupporttickets.data.TicketStatus
import com.example.paninisupporttickets.ui.components.PriorityBadge
import com.example.paninisupporttickets.ui.components.StatusBadge

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketDetailScreen(
    ticketId: String,
    onBackClick: () -> Unit,
    viewModel: TicketDetailViewModel = viewModel(factory = TicketDetailViewModelFactory(ticketId)),
    modifier: Modifier = Modifier
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(UserMessages.TicketDetail.TITLE) },
                navigationIcon = {
                    TextButton(onClick = onBackClick) {
                        Text(UserMessages.TicketDetail.BACK)
                    }
                }
            )
        }
    ) { padding ->
        when {
            uiState.isLoading -> LoadingDetail(Modifier.padding(padding))
            uiState.ticket == null -> ErrorDetail(uiState.errorMessage, Modifier.padding(padding))
            else -> TicketDetailContent(
                ticket = uiState.ticket,
                isUpdating = uiState.isUpdating,
                canUpdatePriority = uiState.canUpdatePriority,
                errorMessage = uiState.errorMessage,
                onStatusChange = viewModel::updateStatus,
                onPriorityChange = viewModel::updatePriority,
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@Composable
private fun LoadingDetail(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorDetail(message: String?, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message ?: UserMessages.TicketDetail.LOAD_ERROR,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun TicketDetailContent(
    ticket: Ticket,
    isUpdating: Boolean,
    canUpdatePriority: Boolean,
    errorMessage: String?,
    onStatusChange: (TicketStatus) -> Unit,
    onPriorityChange: (TicketPriority) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = ticket.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            PriorityBadge(ticket.priority)
            StatusBadge(ticket.status)
        }

        InfoRow(UserMessages.TicketDetail.DESCRIPTION, ticket.description)
        InfoRow(UserMessages.TicketDetail.PROVIDER, ticket.providerName)
        InfoRow(UserMessages.TicketDetail.CATEGORY, ticket.category.name.replace("_", " "))
        InfoRow(UserMessages.TicketDetail.CREATED_AT, ticket.createdAt.take(10))
        InfoRow(UserMessages.TicketDetail.REPORTED_BY, ticket.reportedBy)
        InfoRow(UserMessages.TicketDetail.LOCATION, ticket.location)

        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        TicketStatusActions(
            selectedStatus = ticket.status,
            isUpdating = isUpdating,
            onStatusChange = onStatusChange
        )

        if (canUpdatePriority) {
            TicketPriorityActions(
                selectedPriority = ticket.priority,
                isUpdating = isUpdating,
                onPriorityChange = onPriorityChange
            )
        } else {
            Text(
                text = UserMessages.TicketDetail.PRIORITY_DISABLED,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun TicketStatusActions(
    selectedStatus: TicketStatus,
    isUpdating: Boolean,
    onStatusChange: (TicketStatus) -> Unit
) {
    ActionSectionTitle(UserMessages.TicketDetail.UPDATE_STATUS)
    HorizontalActionRow {
        TicketStatus.entries.forEach { status ->
            ActionButton(
                text = status.displayText(),
                selected = status == selectedStatus,
                enabled = !isUpdating,
                onClick = { onStatusChange(status) }
            )
        }
    }
}

@Composable
private fun TicketPriorityActions(
    selectedPriority: TicketPriority,
    isUpdating: Boolean,
    onPriorityChange: (TicketPriority) -> Unit
) {
    ActionSectionTitle(UserMessages.TicketDetail.UPDATE_PRIORITY)
    HorizontalActionRow {
        TicketPriority.entries.forEach { priority ->
            ActionButton(
                text = priority.displayText(),
                selected = priority == selectedPriority,
                enabled = !isUpdating,
                onClick = { onPriorityChange(priority) }
            )
        }
    }
}

@Composable
private fun ActionSectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun HorizontalActionRow(content: @Composable () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        content()
    }
    Spacer(modifier = Modifier.height(4.dp))
}

@Composable
private fun ActionButton(
    text: String,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    if (selected) {
        Button(onClick = onClick, enabled = enabled) {
            Text(text)
        }
    } else {
        OutlinedButton(onClick = onClick, enabled = enabled) {
            Text(text)
        }
    }
}

private fun TicketStatus.displayText(): String {
    return when (this) {
        TicketStatus.OPEN -> UserMessages.TicketStatusText.OPEN
        TicketStatus.IN_PROGRESS -> UserMessages.TicketStatusText.IN_PROGRESS
        TicketStatus.RESOLVED -> UserMessages.TicketStatusText.RESOLVED
        TicketStatus.CLOSED -> UserMessages.TicketStatusText.CLOSED
    }
}

private fun TicketPriority.displayText(): String {
    return when (this) {
        TicketPriority.CRITICAL -> UserMessages.TicketPriorityText.CRITICAL
        TicketPriority.HIGH -> UserMessages.TicketPriorityText.HIGH
        TicketPriority.MEDIUM -> UserMessages.TicketPriorityText.MEDIUM
        TicketPriority.LOW -> UserMessages.TicketPriorityText.LOW
    }
}
