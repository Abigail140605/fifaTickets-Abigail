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
import com.example.paninisupporttickets.data.TicketStatus
import com.example.paninisupporttickets.ui.theme.BadgeOnDarkContainer
import com.example.paninisupporttickets.ui.theme.StatusClosedContainer
import com.example.paninisupporttickets.ui.theme.StatusInProgressContainer
import com.example.paninisupporttickets.ui.theme.StatusOpenContainer
import com.example.paninisupporttickets.ui.theme.StatusResolvedContainer

@Composable
fun StatusBadge(
    status: TicketStatus,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, textColor, displayText) = when (status) {
        TicketStatus.OPEN -> Triple(StatusOpenContainer, BadgeOnDarkContainer, UserMessages.TicketStatusText.OPEN)
        TicketStatus.IN_PROGRESS -> Triple(StatusInProgressContainer, BadgeOnDarkContainer, UserMessages.TicketStatusText.IN_PROGRESS)
        TicketStatus.RESOLVED -> Triple(StatusResolvedContainer, BadgeOnDarkContainer, UserMessages.TicketStatusText.RESOLVED)
        TicketStatus.CLOSED -> Triple(StatusClosedContainer, BadgeOnDarkContainer, UserMessages.TicketStatusText.CLOSED)
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
