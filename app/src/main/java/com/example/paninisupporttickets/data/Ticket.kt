package com.example.paninisupporttickets.data

data class Ticket(
    val id: String,
    val title: String,
    val description: String,
    val providerName: String,
    val category: TicketCategory,
    val priority: TicketPriority,
    val status: TicketStatus,
    val createdAt: String,
    val reportedBy: String,
    val location: String
)

enum class TicketPriority {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL
}

enum class TicketStatus {
    OPEN,
    IN_PROGRESS,
    RESOLVED,
    CLOSED
}

enum class TicketCategory {
    INVENTORY,
    DISTRIBUTION,
    SUPPLIER,
    DAMAGED_PRODUCT,
    DELIVERY_DELAY,
    OTHER
}
