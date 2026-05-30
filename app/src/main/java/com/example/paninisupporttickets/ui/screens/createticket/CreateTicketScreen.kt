package com.example.paninisupporttickets.ui.screens.createticket

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.paninisupporttickets.core.UserMessages
import com.example.paninisupporttickets.data.TicketCategory
import com.example.paninisupporttickets.data.TicketPriority

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTicketScreen(
    onBackClick: () -> Unit,
    onTicketCreated: () -> Unit,
    viewModel: CreateTicketViewModel = viewModel(factory = CreateTicketViewModelFactory()),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isCreated) {
        if (uiState.isCreated) onTicketCreated()
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(UserMessages.CreateTicket.TITLE) },
                navigationIcon = {
                    TextButton(onClick = onBackClick) {
                        Text(UserMessages.CreateTicket.BACK)
                    }
                }
            )
        }
    ) { padding ->
        if (!uiState.canCreateTicket) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = UserMessages.CreateTicket.CREATION_DISABLED,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            TicketTextField(
                value = uiState.title,
                onValueChange = viewModel::updateTitle,
                label = UserMessages.CreateTicket.TITLE_LABEL,
                enabled = !uiState.isLoading
            )

            TicketTextField(
                value = uiState.description,
                onValueChange = viewModel::updateDescription,
                label = UserMessages.CreateTicket.DESCRIPTION_LABEL,
                enabled = !uiState.isLoading,
                minLines = 3
            )

            TicketTextField(
                value = uiState.providerName,
                onValueChange = viewModel::updateProviderName,
                label = UserMessages.CreateTicket.PROVIDER_LABEL,
                enabled = !uiState.isLoading
            )

            TicketTextField(
                value = uiState.reportedBy,
                onValueChange = viewModel::updateReportedBy,
                label = UserMessages.CreateTicket.REPORTED_BY_LABEL,
                enabled = !uiState.isLoading
            )

            TicketTextField(
                value = uiState.location,
                onValueChange = viewModel::updateLocation,
                label = UserMessages.CreateTicket.LOCATION_LABEL,
                enabled = !uiState.isLoading
            )

            CategoryPicker(
                selected = uiState.category,
                enabled = !uiState.isLoading,
                onSelected = viewModel::updateCategory
            )

            PriorityPicker(
                selected = uiState.priority,
                enabled = !uiState.isLoading,
                onSelected = viewModel::updatePriority
            )

            uiState.errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Button(
                onClick = viewModel::createTicket,
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text(UserMessages.CreateTicket.SUBMIT)
                }
            }
        }
    }
}

@Composable
private fun TicketTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    minLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        enabled = enabled,
        minLines = minLines,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
private fun CategoryPicker(
    selected: TicketCategory,
    enabled: Boolean,
    onSelected: (TicketCategory) -> Unit
) {
    OptionSectionTitle(UserMessages.CreateTicket.CATEGORY_LABEL)
    HorizontalOptions {
        TicketCategory.entries.forEach { category ->
            OptionButton(
                text = category.displayText(),
                selected = category == selected,
                enabled = enabled,
                onClick = { onSelected(category) }
            )
        }
    }
}

@Composable
private fun PriorityPicker(
    selected: TicketPriority,
    enabled: Boolean,
    onSelected: (TicketPriority) -> Unit
) {
    OptionSectionTitle(UserMessages.CreateTicket.PRIORITY_LABEL)
    HorizontalOptions {
        TicketPriority.entries.forEach { priority ->
            OptionButton(
                text = priority.displayText(),
                selected = priority == selected,
                enabled = enabled,
                onClick = { onSelected(priority) }
            )
        }
    }
}

@Composable
private fun OptionSectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun HorizontalOptions(content: @Composable () -> Unit) {
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        content()
    }
}

@Composable
private fun OptionButton(
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

private fun TicketCategory.displayText(): String {
    return when (this) {
        TicketCategory.INVENTORY -> UserMessages.TicketCategoryText.INVENTORY
        TicketCategory.DISTRIBUTION -> UserMessages.TicketCategoryText.DISTRIBUTION
        TicketCategory.SUPPLIER -> UserMessages.TicketCategoryText.SUPPLIER
        TicketCategory.DAMAGED_PRODUCT -> UserMessages.TicketCategoryText.DAMAGED_PRODUCT
        TicketCategory.DELIVERY_DELAY -> UserMessages.TicketCategoryText.DELIVERY_DELAY
        TicketCategory.OTHER -> UserMessages.TicketCategoryText.OTHER
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
