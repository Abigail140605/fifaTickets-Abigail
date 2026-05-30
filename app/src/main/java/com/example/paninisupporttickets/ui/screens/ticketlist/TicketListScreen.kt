package com.example.paninisupporttickets.ui.screens.ticketlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.paninisupporttickets.core.UserMessages
import com.example.paninisupporttickets.ui.components.EmptyState
import com.example.paninisupporttickets.ui.components.TicketCard
import com.example.paninisupporttickets.util.FeatureFlags

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketListScreen(
    onTicketClick: (String) -> Unit,
    onCreateTicketClick: () -> Unit,
    viewModel: TicketListViewModel = viewModel(factory = TicketListViewModelFactory()),
    modifier: Modifier = Modifier
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val canCreateTicket by FeatureFlags.enableTicketCreation.collectAsStateWithLifecycle()
    val canUpdatePriority by FeatureFlags.enablePriorityUpdate.collectAsStateWithLifecycle()

    var showSettingsDialog by remember { mutableStateOf(false) }

    if (showSettingsDialog) {
        FeatureFlagsDialog(
            canCreateTicket = canCreateTicket,
            canUpdatePriority = canUpdatePriority,
            onTicketCreationChanged = FeatureFlags::setTicketCreationEnabled,
            onPriorityUpdateChanged = FeatureFlags::setPriorityUpdateEnabled,
            onDismiss = { showSettingsDialog = false }
        )
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(UserMessages.TicketList.TITLE) },
                actions = {
                    IconButton(onClick = { showSettingsDialog = true }) {
                        Text("⚙️")
                    }
                }
            )
        },
        floatingActionButton = {
            if (canCreateTicket) {
                FloatingActionButton(
                    onClick = onCreateTicketClick
                ) {
                    Text(UserMessages.TicketList.CREATE_ACTION)
                }
            }
        }
    ) { padding ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.errorMessage != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = uiState.errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Button(
                        onClick = viewModel::refresh,
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text(UserMessages.TicketList.RETRY_ACTION)
                    }
                }
            }

            uiState.tickets.isEmpty() -> {
                EmptyState(
                    message = UserMessages.TicketList.EMPTY_MESSAGE,
                    modifier = Modifier.padding(padding)
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.tickets) { ticket ->
                        TicketCard(
                            ticket = ticket,
                            onClick = { onTicketClick(ticket.id) }
                        )
                    }
                }
            }
        }

        if (!canCreateTicket) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding)
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text(
                    text = UserMessages.TicketList.CREATION_DISABLED,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun FeatureFlagsDialog(
    canCreateTicket: Boolean,
    canUpdatePriority: Boolean,
    onTicketCreationChanged: (Boolean) -> Unit,
    onPriorityUpdateChanged: (Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Feature Flags") },
        text = {
            Column {
                FeatureFlagRow(
                    label = "Creación de tickets",
                    description = "Permite crear nuevos tickets desde el listado",
                    checked = canCreateTicket,
                    onCheckedChange = onTicketCreationChanged
                )

                Spacer(modifier = Modifier.height(16.dp))

                FeatureFlagRow(
                    label = "Actualización de prioridad",
                    description = "Permite cambiar la prioridad desde el detalle",
                    checked = canUpdatePriority,
                    onCheckedChange = onPriorityUpdateChanged
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}

@Composable
private fun FeatureFlagRow(
    label: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
