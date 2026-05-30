package com.example.paninisupporttickets.core

object AppConstants {
    object Api {
        const val BASE_URL = "https://api.panini-support.local/"

        object Paths {
            const val AUTH_LOGIN = "auth/login"
            const val TICKETS = "tickets"
            const val TICKET_DETAIL = "tickets/{ticketId}"
            const val TICKET_STATUS = "tickets/{ticketId}/status"
            const val TICKET_PRIORITY = "tickets/{ticketId}/priority"
        }
    }
}
