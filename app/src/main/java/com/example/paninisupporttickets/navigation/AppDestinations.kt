package com.example.paninisupporttickets.navigation

object AppDestinations {
    const val LOGIN = "login"
    const val TICKETS = "tickets"
    const val TICKET_DETAIL = "ticketDetail/{ticketId}"
    const val CREATE_TICKET = "createTicket"

    fun ticketDetailRoute(ticketId: String): String {
        return "ticketDetail/$ticketId"
    }
}
