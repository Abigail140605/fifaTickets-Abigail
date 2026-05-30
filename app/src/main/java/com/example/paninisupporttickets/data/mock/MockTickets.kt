package com.example.paninisupporttickets.data.mock

import com.example.paninisupporttickets.data.Ticket
import com.example.paninisupporttickets.data.TicketCategory
import com.example.paninisupporttickets.data.TicketPriority
import com.example.paninisupporttickets.data.TicketStatus

object MockTickets {
    val tickets = listOf(
        Ticket(
            id = "ticket-001",
            title = "Faltante de paquetes en lote regional",
            description = "El punto de venta reporta 120 paquetes menos respecto al despacho confirmado para la ruta GAM-03. Se requiere verificación urgente del inventario en tránsito.",
            providerName = "Distribuidora La Sabana",
            category = TicketCategory.INVENTORY,
            priority = TicketPriority.HIGH,
            status = TicketStatus.OPEN,
            createdAt = "2026-05-28T10:30:00Z",
            reportedBy = "Carlos Ramírez",
            location = "Punto de Venta GAM-03"
        ),
        Ticket(
            id = "ticket-002",
            title = "Retraso en entrega hacia puntos de venta de Sarapiquí",
            description = "La ruta programada para la mañana no llegó a tres comercios registrados, afectando la disponibilidad de sobres del álbum Panini FIFA 2026.",
            providerName = "Logística Caribe Norte",
            category = TicketCategory.DELIVERY_DELAY,
            priority = TicketPriority.CRITICAL,
            status = TicketStatus.IN_PROGRESS,
            createdAt = "2026-05-27T14:15:00Z",
            reportedBy = "María González",
            location = "Sarapiquí"
        ),
        Ticket(
            id = "ticket-003",
            title = "Cajas con paquetes dañados por humedad",
            description = "Se detectaron cajas con daño físico en el empaque durante la recepción de inventario para reposición semanal. Los paquetes internos presentan deterioro visible.",
            providerName = "Central de Distribución Heredia",
            category = TicketCategory.DAMAGED_PRODUCT,
            priority = TicketPriority.MEDIUM,
            status = TicketStatus.OPEN,
            createdAt = "2026-05-26T09:45:00Z",
            reportedBy = "Roberto Chen",
            location = "Heredia Centro"
        ),
        Ticket(
            id = "ticket-004",
            title = "Proveedor no confirma reposición solicitada",
            description = "El comercio reporta falta de respuesta ante una solicitud de reposición generada hace más de 48 horas. Se requiere contacto directo con el proveedor.",
            providerName = "Punto Venta Alajuela Centro",
            category = TicketCategory.SUPPLIER,
            priority = TicketPriority.HIGH,
            status = TicketStatus.OPEN,
            createdAt = "2026-05-25T16:20:00Z",
            reportedBy = "Ana Jiménez",
            location = "Alajuela Centro"
        ),
        Ticket(
            id = "ticket-005",
            title = "Error en conteo de álbumes recibidos",
            description = "El sistema registra 500 álbumes pero físicamente solo se recibieron 487 unidades. Diferencia de 13 unidades sin justificación en documentación.",
            providerName = "Distribuidora del Pacífico",
            category = TicketCategory.INVENTORY,
            priority = TicketPriority.MEDIUM,
            status = TicketStatus.RESOLVED,
            createdAt = "2026-05-24T11:30:00Z",
            reportedBy = "Luis Vargas",
            location = "Puntarenas"
        ),
        Ticket(
            id = "ticket-006",
            title = "Camión de distribución con falla mecánica",
            description = "El vehículo asignado a la ruta Cartago-Sur presenta problemas de transmisión. Se requiere reprogramación de entregas y vehículo de reemplazo.",
            providerName = "Transportes Centroamérica",
            category = TicketCategory.DISTRIBUTION,
            priority = TicketPriority.CRITICAL,
            status = TicketStatus.IN_PROGRESS,
            createdAt = "2026-05-23T08:00:00Z",
            reportedBy = "Patricia Mora",
            location = "Cartago"
        ),
        Ticket(
            id = "ticket-007",
            title = "Sobres con cromos duplicados en lote",
            description = "Cliente mayorista reporta que aproximadamente el 15% de los sobres abiertos contienen cromos duplicados, afectando la experiencia del coleccionista.",
            providerName = "Impresora Nacional",
            category = TicketCategory.DAMAGED_PRODUCT,
            priority = TicketPriority.LOW,
            status = TicketStatus.CLOSED,
            createdAt = "2026-05-22T13:45:00Z",
            reportedBy = "Diego Salazar",
            location = "San José"
        ),
        Ticket(
            id = "ticket-008",
            title = "Demora en procesamiento de devoluciones",
            description = "Tres puntos de venta tienen solicitudes de devolución pendientes desde hace más de una semana sin respuesta del departamento de logística.",
            providerName = "Logística Integrada CR",
            category = TicketCategory.OTHER,
            priority = TicketPriority.MEDIUM,
            status = TicketStatus.OPEN,
            createdAt = "2026-05-21T10:15:00Z",
            reportedBy = "Sofía Rojas",
            location = "Múltiples ubicaciones"
        )
    )
}
