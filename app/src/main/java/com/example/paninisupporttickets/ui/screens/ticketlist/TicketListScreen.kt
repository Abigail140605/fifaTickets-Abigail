package com.example.paninisupporttickets.ui.screens.ticketlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(UserMessages.TicketList.TITLE) }
            )
        },
        floatingActionButton = {
            if (FeatureFlags.ENABLE_TICKET_CREATION) {
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

        if (!FeatureFlags.ENABLE_TICKET_CREATION) {
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
