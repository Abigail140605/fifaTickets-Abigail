package com.example.paninisupporttickets.core

object UserMessages {
    object Login {
        const val TITLE = "Panini Support Tickets"
        const val SUBTITLE = "Copa Mundial FIFA 2026"
        const val EMAIL_LABEL = "Correo electrónico"
        const val PASSWORD_LABEL = "Contraseña"
        const val LOGIN_BUTTON = "Iniciar sesión"
        const val EMAIL_REQUIRED = "El correo es obligatorio"
        const val EMAIL_INVALID = "El formato del correo no es válido"
        const val PASSWORD_REQUIRED = "La contraseña es obligatoria"
    }

    object TicketList {
        const val TITLE = "Tickets de Soporte"
        const val CREATE_ACTION = "+"
        const val RETRY_ACTION = "Reintentar"
        const val EMPTY_MESSAGE = "No hay tickets registrados"
        const val CREATION_DISABLED = "La creación de tickets está deshabilitada para pruebas internas"
        const val LOAD_ERROR = "Error al cargar tickets"
    }

    object TicketDetail {
        const val TITLE = "Detalle del Ticket"
        const val BACK = "Volver"
        const val DESCRIPTION = "Descripción"
        const val PROVIDER = "Proveedor"
        const val CATEGORY = "Categoría"
        const val CREATED_AT = "Fecha de creación"
        const val REPORTED_BY = "Reportado por"
        const val LOCATION = "Ubicación"
        const val STATUS = "Estado"
        const val PRIORITY = "Prioridad"
        const val UPDATE_STATUS = "Actualizar estado"
        const val UPDATE_PRIORITY = "Actualizar prioridad"
        const val PRIORITY_DISABLED = "La actualización de prioridad está deshabilitada para pruebas internas"
        const val NOT_FOUND = "Ticket no encontrado"
        const val LOAD_ERROR = "Error al cargar detalle del ticket"
    }

    object CreateTicket {
        const val TITLE = "Crear Ticket"
        const val BACK = "Volver"
        const val TITLE_LABEL = "Título"
        const val DESCRIPTION_LABEL = "Descripción"
        const val PROVIDER_LABEL = "Proveedor"
        const val REPORTED_BY_LABEL = "Reportado por"
        const val LOCATION_LABEL = "Ubicación"
        const val CATEGORY_LABEL = "Categoría"
        const val PRIORITY_LABEL = "Prioridad"
        const val SUBMIT = "Crear ticket"
        const val CREATION_DISABLED = "La creación de tickets está deshabilitada para pruebas internas"
        const val REQUIRED_FIELDS = "Completa todos los campos obligatorios"
        const val CREATE_ERROR = "No se pudo crear el ticket"
    }

    object TicketPriorityText {
        const val CRITICAL = "Crítica"
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

    object TicketCategoryText {
        const val INVENTORY = "Inventario"
        const val DISTRIBUTION = "Distribución"
        const val SUPPLIER = "Proveedor"
        const val DAMAGED_PRODUCT = "Producto dañado"
        const val DELIVERY_DELAY = "Retraso entrega"
        const val OTHER = "Otro"
    }
}
