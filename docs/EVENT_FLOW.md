# Flujo de eventos reactivo - Panini Support Tickets PoC

## 1. Propósito

La aplicación utiliza comunicación reactiva para que los cambios importantes en los tickets se reflejen automáticamente en la interfaz.

El objetivo no es implementar un sistema complejo de eventos, sino resolver de forma clara dos necesidades principales de la PoC:

* actualizar el listado cuando se crea un nuevo ticket;
* reordenar el listado cuando cambia la prioridad de un ticket.

Para esto se utiliza `StateFlow`, manteniendo el estado principal de tickets en el repositorio.

---

## 2. Decisión técnica

El flujo reactivo se centraliza en `TicketRepository`.

```text
TicketRepository
        ↓ StateFlow<List<Ticket>>
ViewModels
        ↓ StateFlow<UiState>
Jetpack Compose Screens
```

El repositorio mantiene internamente un `MutableStateFlow<List<Ticket>>` y expone una versión de solo lectura para que los ViewModels puedan observar los cambios.

```kotlin
private val _ticketsFlow = MutableStateFlow(sortByPriority(MockTickets.tickets))
val ticketsFlow: StateFlow<List<Ticket>> = _ticketsFlow.asStateFlow()
```

Esta decisión permite que la lista de tickets tenga una única fuente de verdad y evita que cada pantalla maneje su propia copia de datos.

---

## 3. Fuente única de verdad

`TicketRepository` funciona como la fuente única de verdad para los tickets.

Esto significa que las pantallas no crean, actualizan ni reordenan tickets directamente. Todas esas acciones pasan por el repositorio.

Esta decisión reduce duplicación de lógica y permite que cualquier cambio en los tickets se propague automáticamente a las pantallas que estén observando el flujo.

---

## 4. Flujo: creación de tickets

Cuando el usuario crea un ticket desde `CreateTicketScreen`, la acción llega al `CreateTicketViewModel`.

El ViewModel valida la acción y llama al repositorio.

```text
CreateTicketScreen
        ↓
CreateTicketViewModel
        ↓
TicketRepository.createTicket()
        ↓
_ticketsFlow emite nueva lista
        ↓
TicketListViewModel recibe el cambio
        ↓
TicketListScreen se actualiza
```

El repositorio agrega el nuevo ticket a la lista actual y emite una nueva lista mediante `StateFlow`.

```kotlin
val currentTickets = _ticketsFlow.value.toMutableList()
currentTickets.add(newTicket)
_ticketsFlow.value = sortByPriority(currentTickets)
```

Como `TicketListViewModel` observa `ticketsFlow`, no es necesario recargar manualmente el listado. Cuando el usuario vuelve al listado, el nuevo ticket ya aparece reflejado.

Esta implementación cumple el requerimiento de que la creación de tickets actualice la interfaz de forma reactiva.

---

## 5. Flujo: actualización de prioridad

Cuando el usuario cambia la prioridad de un ticket desde la pantalla de detalle, la acción llega al `TicketDetailViewModel`.

```text
TicketDetailScreen
        ↓
TicketDetailViewModel
        ↓
TicketRepository.updateTicketPriority()
        ↓
Repository actualiza y reordena
        ↓
_ticketsFlow emite lista actualizada
        ↓
TicketListScreen muestra el nuevo orden
```

La prioridad no se ordena en la UI. El ordenamiento se realiza en el repositorio porque forma parte del comportamiento de los datos.

El orden definido para la PoC es:

```text
Critical
High
Medium
Low
```

Cuando una prioridad cambia, el repositorio actualiza el ticket, ordena nuevamente la lista y emite el nuevo estado.

```kotlin
val updatedTicket = currentTickets[index].copy(priority = priority)
currentTickets[index] = updatedTicket
_ticketsFlow.value = sortByPriority(currentTickets)
```

Con esto, si un ticket pasa de prioridad media a crítica, sube automáticamente en el listado. Si baja de prioridad, también cambia su posición sin lógica adicional en la pantalla.

---

## 6. Flujo: detalle de ticket

La pantalla de detalle puede observar un ticket específico a partir del flujo principal de tickets.

```kotlin
fun getTicketFlow(ticketId: String): Flow<Ticket?> {
    return _ticketsFlow.map { tickets ->
        tickets.find { it.id == ticketId }
    }
}
```

Esto permite que `TicketDetailScreen` también se mantenga actualizada si el ticket cambia.

Por ejemplo, si se modifica el estado o la prioridad del ticket, la pantalla de detalle puede reflejar el cambio sin consultar una fuente de datos diferente.

---

## 7. Rol de los ViewModels

Los ViewModels no son la fuente de verdad de los tickets.

Su trabajo es observar el repositorio, transformar la información en un `UiState` y exponer ese estado a la UI.

Ejemplo general:

```kotlin
ticketRepository.ticketsFlow.collect { tickets ->
    _uiState.value = _uiState.value.copy(
        isLoading = false,
        tickets = tickets,
        errorMessage = null
    )
}
```

Esto mantiene la lógica de datos en el repositorio y deja al ViewModel como coordinador del estado visual.

---

## 8. Rol de la UI

Las pantallas de Jetpack Compose observan el estado expuesto por el ViewModel.

```kotlin
val uiState by viewModel.uiState.collectAsStateWithLifecycle()
```

La UI no necesita saber cómo se actualizan los tickets internamente. Solo reacciona al estado recibido.

Esto mantiene los Composables simples y evita mezclar lógica de negocio dentro de la interfaz.

---

## 9. Por qué StateFlow

Se eligió `StateFlow` porque se integra de forma natural con Kotlin Coroutines y Jetpack Compose.

Para esta PoC, `StateFlow` permite:

* mantener un estado observable;
* emitir cambios de forma inmediata;
* evitar recargas manuales;
* mantener una comunicación desacoplada entre pantallas;
* observar cambios desde distintos ViewModels;
* trabajar sin librerías externas.

Además, encaja con el alcance del proyecto porque resuelve los escenarios requeridos sin introducir mecanismos más complejos.

---

## 10. Por qué no EventBus

No se utilizó un `EventBus` porque agregaría complejidad innecesaria para esta prueba de concepto.

Un EventBus puede volver más difícil rastrear de dónde vienen los cambios, especialmente en una app pequeña donde el flujo de datos puede resolverse de forma más clara con `StateFlow`.

Con `StateFlow`, los cambios salen del repositorio y llegan a los ViewModels de forma explícita y tipada.

---

## 11. Por qué no callbacks entre pantallas

Tampoco se usaron callbacks manuales entre pantallas para actualizar el listado.

Un enfoque basado en callbacks obligaría a pasar eventos entre pantallas o a recordar manualmente cuándo refrescar la información.

Eso sería más frágil y menos mantenible.

Con el flujo actual, las pantallas no dependen entre sí. Todas observan la misma fuente de datos.

---

## 12. Manejo de errores

Las operaciones del repositorio retornan `ApiResult`.

```kotlin
sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(
        val message: String,
        val statusCode: Int? = null
    ) : ApiResult<Nothing>()
}
```

Esto permite que los ViewModels manejen los resultados de forma clara, sin depender de excepciones en la UI.

Cuando ocurre un error, el ViewModel actualiza su `UiState` y la pantalla muestra el mensaje correspondiente.

---

## 13. Resultado del flujo

Con esta estrategia:

* el repositorio mantiene la lista real de tickets;
* los ViewModels observan los cambios;
* las pantallas se actualizan automáticamente;
* la creación de tickets se refleja en el listado;
* el cambio de prioridad reordena los tickets;
* no hay recargas manuales;
* no hay EventBus;
* no hay callbacks innecesarios entre pantallas.

La solución es simple, mantenible y suficiente para el alcance de la prueba de concepto.
