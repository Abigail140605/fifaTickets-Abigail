package com.example.paninisupporttickets.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.paninisupporttickets.core.UserMessages
import com.example.paninisupporttickets.data.TicketPriority
import com.example.paninisupporttickets.ui.theme.BadgeOnDarkContainer
import com.example.paninisupporttickets.ui.theme.BadgeOnLightContainer
import com.example.paninisupporttickets.ui.theme.PriorityCriticalContainer
import com.example.paninisupporttickets.ui.theme.PriorityHighContainer
import com.example.paninisupporttickets.ui.theme.PriorityLowContainer
import com.example.paninisupporttickets.ui.theme.PriorityMediumContainer

@Composable
fun PriorityBadge(
    priority: TicketPriority,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, textColor, displayText) = when (priority) {
        TicketPriority.CRITICAL -> Triple(PriorityCriticalContainer, BadgeOnDarkContainer, UserMessages.TicketPriorityText.CRITICAL)
        TicketPriority.HIGH -> Triple(PriorityHighContainer, BadgeOnDarkContainer, UserMessages.TicketPriorityText.HIGH)
        TicketPriority.MEDIUM -> Triple(PriorityMediumContainer, BadgeOnLightContainer, UserMessages.TicketPriorityText.MEDIUM)
        TicketPriority.LOW -> Triple(PriorityLowContainer, BadgeOnDarkContainer, UserMessages.TicketPriorityText.LOW)
    }

    Box(
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = displayText,
            style = MaterialTheme.typography.labelSmall,
            color = textColor
        )
    }
}
