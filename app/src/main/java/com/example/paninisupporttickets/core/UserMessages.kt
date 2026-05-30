package com.example.paninisupporttickets.core

object UserMessages {
    object Login {
        const val TITLE = "Panini Support Tickets"
        const val SUBTITLE = "Copa Mundial FIFA 2026"
        const val EMAIL_LABEL = "Correo electronico"
        const val PASSWORD_LABEL = "Contrasena"
        const val LOGIN_BUTTON = "Iniciar sesion"
        const val EMAIL_REQUIRED = "El correo es obligatorio"
        const val EMAIL_INVALID = "El formato del correo no es valido"
        const val PASSWORD_REQUIRED = "La contrasena es obligatoria"
    }

    object TicketList {
        const val TITLE = "Tickets de Soporte"
        const val CREATE_ACTION = "+"
        const val RETRY_ACTION = "Reintentar"
        const val EMPTY_MESSAGE = "No hay tickets registrados"
        const val CREATION_DISABLED = "La creacion de tickets esta deshabilitada para pruebas internas"
        const val LOAD_ERROR = "Error al cargar tickets"
    }

    object TicketPriorityText {
        const val CRITICAL = "Critica"
        const val HIGH = "Alta"
        const val MEDIUM = "Media"
        const val LOW = "Baja"
    }

    object TicketStatusText {
        const val OPEN = "Abierto"
        const val IN_PROGRESS = "En Progreso"
        const val RESOLVED = "Resuelto"
        const val CLOSED = "Cerrado"
    }

    object Placeholder {
        const val CREATE_TICKET = "Crear Ticket - Proximamente"
        const val TICKET_DETAIL_SUFFIX = " - Proximamente"
    }
}
