# Feature Flags - Panini Support Tickets PoC

## 1. Propósito

La aplicación utiliza Feature Flags para habilitar o deshabilitar funcionalidades específicas sin tener que modificar varias partes del código.

En esta prueba de concepto, los flags permiten simular decisiones internas del producto durante una fase de validación. Por ejemplo, la empresa podría querer mostrar el listado y el detalle de tickets, pero desactivar temporalmente la creación o el cambio de prioridad mientras se revisan esos flujos.

La implementación se mantiene simple porque el proyecto no requiere configuración remota ni un sistema productivo de administración de flags.

---

## 2. Decisión técnica

Los Feature Flags se definen en un único archivo:

```text
util/FeatureFlags.kt
```

La implementación actual utiliza constantes en tiempo de compilación:

```kotlin
object FeatureFlags {
    const val ENABLE_TICKET_CREATION = true
    const val ENABLE_PRIORITY_UPDATE = true
}
```

Esta decisión es suficiente para el alcance de la PoC porque:

* no requiere backend;
* no requiere Firebase Remote Config;
* no agrega dependencias externas;
* es fácil de entender;
* permite activar o desactivar funcionalidades rápidamente durante pruebas internas.

El objetivo no es construir un sistema completo de configuración remota, sino demostrar que la aplicación está preparada para controlar funcionalidades de forma centralizada.

---

## 3. Flags implementados

| Flag                     | Funcionalidad que controla                  | Valor inicial |
| ------------------------ | ------------------------------------------- | ------------- |
| `ENABLE_TICKET_CREATION` | Permite crear nuevos tickets                | `true`        |
| `ENABLE_PRIORITY_UPDATE` | Permite modificar la prioridad de un ticket | `true`        |

Estos dos flags fueron seleccionados porque controlan acciones importantes dentro del flujo de soporte y se relacionan directamente con los requerimientos de la PoC.

---

## 4. Flag: ENABLE_TICKET_CREATION

Este flag controla si el usuario puede crear nuevos tickets.

Cuando está habilitado:

* se muestra la acción para crear un ticket;
* el usuario puede navegar a la pantalla de creación;
* el ViewModel permite registrar el ticket.

Cuando está deshabilitado:

* la acción de creación no debe estar disponible desde la UI;
* si por algún flujo alterno se intenta crear un ticket, el ViewModel bloquea la acción;
* se puede mostrar un mensaje indicando que la funcionalidad está deshabilitada para pruebas internas.

La validación no se deja únicamente en la interfaz. Aunque la UI oculte el botón de creación, el ViewModel también debe validar el flag antes de ejecutar la acción.

Esto evita que la lógica dependa solo de elementos visuales.

---

## 5. Flag: ENABLE_PRIORITY_UPDATE

Este flag controla si el usuario puede modificar la prioridad de un ticket.

Cuando está habilitado:

* la pantalla de detalle permite cambiar la prioridad;
* el ViewModel procesa la acción;
* el repositorio actualiza el ticket;
* el listado se reordena automáticamente según la nueva prioridad.

Cuando está deshabilitado:

* la prioridad puede seguir mostrándose como información de solo lectura;
* las acciones para modificarla no deben estar disponibles;
* si se intenta ejecutar el cambio desde el ViewModel, la acción se bloquea.

Este flag es importante porque el cambio de prioridad puede representar una acción más sensible dentro de un flujo empresarial. En una versión futura, esta acción podría depender de roles internos, permisos o aprobación de un supervisor.

---

## 6. Dónde se aplican los flags

Los flags se aplican en dos niveles:

```text
UI
↓
ViewModel
```

### En la UI

La UI utiliza los flags para mostrar u ocultar acciones.

Ejemplo:

```kotlin
if (FeatureFlags.ENABLE_TICKET_CREATION) {
    // Mostrar acción para crear ticket
}
```

Esto permite adaptar la experiencia visual del usuario según las funcionalidades habilitadas.

### En el ViewModel

El ViewModel también valida los flags antes de ejecutar una acción.

Ejemplo:

```kotlin
fun updatePriority(priority: TicketPriority) {
    if (!FeatureFlags.ENABLE_PRIORITY_UPDATE) return

    // Ejecutar actualización de prioridad
}
```

Esta segunda validación es importante porque la UI no debe ser la única barrera de control.

---

## 7. Por qué no usar configuración remota

No se utilizó Firebase Remote Config ni un sistema remoto de Feature Flags porque sería innecesario para esta prueba de concepto.

La aplicación no tiene backend real, no requiere segmentación de usuarios y no necesita cambiar flags desde un servidor.

Agregar configuración remota implicaría:

* más dependencias;
* más configuración;
* más puntos de falla;
* mayor complejidad para explicar y mantener.

Para el alcance actual, constantes centralizadas en `FeatureFlags.kt` resuelven el problema de forma clara y suficiente.

---

## 8. Posible evolución futura

Si el proyecto evolucionara hacia una versión productiva, los flags podrían migrarse a una fuente remota.

Por ejemplo:

* backend propio;
* archivo de configuración remoto;
* sistema de permisos por rol;
* Firebase Remote Config, si el proyecto lo justificara.

Ese cambio debería quedar encapsulado para que las pantallas y ViewModels sigan consultando una única fuente de configuración.

La idea sería reemplazar la implementación interna de los flags, no dispersar condiciones por todo el proyecto.

---

## 9. Criterio de ingeniería

Los Feature Flags se implementaron de forma simple porque el alcance del proyecto también es simple.

La decisión busca balancear control y mantenibilidad:

* las funcionalidades están centralizadas;
* los nombres son claros;
* no se agregan dependencias innecesarias;
* se valida tanto en UI como en ViewModel;
* el proyecto queda preparado para una evolución futura si se requiere.

En esta PoC, los Feature Flags permiten demostrar cómo la aplicación podría habilitar o deshabilitar funcionalidades internas sin complicar la arquitectura.
