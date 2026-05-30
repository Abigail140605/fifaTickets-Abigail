package com.example.paninisupporttickets.data.repository

import com.example.paninisupporttickets.data.Ticket
import com.example.paninisupporttickets.data.TicketPriority
import com.example.paninisupporttickets.data.TicketStatus
import com.example.paninisupporttickets.data.mock.MockTickets
import com.example.paninisupporttickets.data.remote.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class TicketRepository(private val apiService: ApiService) {
    private val _ticketsFlow = MutableStateFlow(sortByPriority(MockTickets.tickets))
    val ticketsFlow: StateFlow<List<Ticket>> = _ticketsFlow.asStateFlow()

    fun getTicketFlow(ticketId: String): Flow<Ticket?> {
        return _ticketsFlow.map { tickets ->
            tickets.find { it.id == ticketId }
        }
    }

    suspend fun createTicket(
        title: String,
        description: String,
        providerName: String,
        category: String,
        priority: String,
        reportedBy: String,
        location: String
    ): ApiResult<Ticket> {
        return try {
            val newTicket = Ticket(
                id = "ticket-${System.currentTimeMillis()}",
                title = title,
                description = description,
                providerName = providerName,
                category = com.example.paninisupporttickets.data.TicketCategory.valueOf(category),
                priority = TicketPriority.valueOf(priority),
                status = TicketStatus.OPEN,
                createdAt = java.time.Instant.now().toString(),
                reportedBy = reportedBy,
                location = location
            )

            val currentTickets = _ticketsFlow.value.toMutableList()
            currentTickets.add(newTicket)
            _ticketsFlow.value = sortByPriority(currentTickets)

            ApiResult.Success(newTicket)
        } catch (e: Exception) {
            ApiResult.Error(message = "Error al crear ticket: ${e.message}")
        }
    }

    suspend fun updateTicketStatus(ticketId: String, status: TicketStatus): ApiResult<Ticket> {
        return try {
            val currentTickets = _ticketsFlow.value.toMutableList()
            val index = currentTickets.indexOfFirst { it.id == ticketId }

            if (index == -1) {
                return ApiResult.Error(message = "Ticket no encontrado", statusCode = 404)
            }

            val updatedTicket = currentTickets[index].copy(status = status)
            currentTickets[index] = updatedTicket
            _ticketsFlow.value = sortByPriority(currentTickets)

            ApiResult.Success(updatedTicket)
        } catch (e: Exception) {
            ApiResult.Error(message = "Error al actualizar estado: ${e.message}")
        }
    }

    suspend fun updateTicketPriority(ticketId: String, priority: TicketPriority): ApiResult<Ticket> {
        return try {
            val currentTickets = _ticketsFlow.value.toMutableList()
            val index = currentTickets.indexOfFirst { it.id == ticketId }

            if (index == -1) {
                return ApiResult.Error(message = "Ticket no encontrado", statusCode = 404)
            }

            val updatedTicket = currentTickets[index].copy(priority = priority)
            currentTickets[index] = updatedTicket
            _ticketsFlow.value = sortByPriority(currentTickets)

            ApiResult.Success(updatedTicket)
        } catch (e: Exception) {
            ApiResult.Error(message = "Error al actualizar prioridad: ${e.message}")
        }
    }

    private fun sortByPriority(tickets: List<Ticket>): List<Ticket> {
        val priorityOrder = mapOf(
            TicketPriority.CRITICAL to 0,
            TicketPriority.HIGH to 1,
            TicketPriority.MEDIUM to 2,
            TicketPriority.LOW to 3
        )
        return tickets.sortedBy { priorityOrder[it.priority] ?: 99 }
    }
}
