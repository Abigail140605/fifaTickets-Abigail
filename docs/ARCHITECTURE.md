# Arquitectura - Panini Support Tickets PoC

## 1. Contexto

Este proyecto es una prueba de concepto móvil para la gestión de tickets internos de soporte relacionados con la operación de Panini para el álbum oficial de la Copa Mundial FIFA 2026.

La aplicación se enfoca en incidencias asociadas a proveedores, disponibilidad de inventario, distribución de paquetes, productos faltantes, retrasos logísticos y solicitudes de soporte desde puntos de venta.

Esta prueba de concepto no incluye un backend real. La aplicación funciona con datos simulados, pero la estructura del proyecto se prepara intencionalmente para permitir una futura integración con backend sin tener que rediseñar toda la app.

El objetivo principal de la arquitectura es mantener el proyecto claro, mantenible y fácil de continuar por otro ingeniero.

---

## 2. Decisión arquitectónica

El proyecto utiliza una arquitectura **MVVM simple con Repository Pattern**.

```text
Jetpack Compose Screen
        ↓
ViewModel
        ↓
TicketRepository
        ↓
Mock Data / Futura API con Retrofit
```

Esta estructura fue seleccionada porque permite separar responsabilidades sin agregar complejidad innecesaria para el alcance de la prueba.

La aplicación necesita demostrar un flujo claro de gestión de tickets: autenticación simulada, listado de tickets, detalle del ticket, creación de tickets, actualización de estado y actualización de prioridad. Por esa razón, un enfoque MVVM simple y bien organizado resulta más adecuado que una arquitectura con demasiadas capas adicionales.

---

## 3. Por qué MVVM

MVVM fue seleccionado porque la aplicación tiene pantallas que dependen de estados cambiantes.

Cada pantalla cuenta con un ViewModel encargado de exponer su estado mediante `StateFlow`. El Composable observa ese estado y renderiza la interfaz correspondiente.

Esto permite que la UI se concentre únicamente en la presentación.

Ejemplo:

```text
TicketListScreen observa TicketListUiState
TicketListViewModel carga y reacciona a cambios en los tickets
TicketRepository mantiene los datos de tickets
```

Con esta separación se evita colocar lógica de tickets directamente dentro de los Composables, lo cual mejora la mantenibilidad del proyecto.

---

## 4. Por qué Repository Pattern

`TicketRepository` se utiliza como la fuente principal de datos para la información de tickets.

En esta PoC, el repositorio trabaja con datos simulados. Sin embargo, el resto de la aplicación no necesita saber si los datos vienen de mocks o de una API real.

Esta decisión permite que la app funcione actualmente sin backend, pero mantiene abierto el camino para una futura integración.

En una versión posterior, la integración con backend afectaría principalmente al repositorio y a la capa remota, no a las pantallas de UI.

---

## 5. Por qué no se usa Clean Architecture completa

No se utilizó una Clean Architecture completa de forma intencional.

Agregar carpetas como `domain`, `usecase`, múltiples interfaces y mappers adicionales aumentaría la cantidad de código sin aportar mucho valor real para esta prueba de concepto.

El alcance actual tiene una entidad principal: `Ticket`.

La aplicación no contiene reglas de negocio suficientemente complejas como para justificar casos de uso independientes. La lógica principal se concentra en:

* crear un ticket;
* actualizar el estado de un ticket;
* actualizar la prioridad de un ticket;
* mantener el listado ordenado por prioridad;
* actualizar la interfaz de forma reactiva cuando los datos cambian.

Por este motivo, la arquitectura seleccionada mantiene el código organizado, pero evita caer en sobreingeniería.

---

## 6. Estructura del proyecto

El proyecto se organiza por responsabilidad.

```text
com.panini.support/
├── PaniniSupportApplication.kt
├── MainActivity.kt
├── AppNavigation.kt
│
├── core/
│   └── AppConstants.kt
│
├── data/
│   ├── Ticket.kt
│   ├── AppContainer.kt
│   │
│   ├── remote/
│   │   ├── ApiService.kt
│   │   ├── RetrofitClient.kt
│   │   └── model/
│   │       └── TicketModels.kt
│   │
│   ├── mock/
│   │   └── MockTickets.kt
│   │
│   └── repository/
│       ├── ApiResult.kt
│       └── TicketRepository.kt
│
├── ui/
│   ├── components/
│   ├── screens/
│   │   ├── login/
│   │   ├── ticketlist/
│   │   ├── ticketdetail/
│   │   └── createticket/
│   └── theme/
│
└── util/
    └── FeatureFlags.kt
```

Esta estructura mantiene el proyecto pequeño, pero separa las responsabilidades principales de la aplicación.

---

## 7. Responsabilidades por capa

### 7.1 Capa de UI

La capa de UI está construida con Jetpack Compose.

Su responsabilidad es renderizar la interfaz y enviar acciones del usuario hacia el ViewModel.

La UI no accede directamente al repositorio y tampoco modifica datos de tickets por cuenta propia.

Ejemplos de responsabilidades de la UI:

* mostrar el listado de tickets;
* mostrar indicadores de prioridad y estado;
* navegar al detalle de un ticket;
* mostrar estados de carga, error o contenido vacío;
* enviar acciones como crear ticket o actualizar estado.

---

### 7.2 Capa de ViewModel

El ViewModel actúa como conexión entre la UI y la capa de datos.

Sus responsabilidades son:

* exponer `UiState` mediante `StateFlow`;
* recibir acciones desde la UI;
* llamar al repositorio;
* manejar estados de carga y error;
* mantener las pantallas Compose libres de lógica de negocio.

Cada pantalla tiene su propia representación de estado. Esto facilita entender el comportamiento de cada vista y evita compartir estado innecesario entre pantallas que no lo necesitan.

---

### 7.3 Capa de datos

La capa de datos contiene el modelo de dominio, el repositorio, los datos simulados y la estructura preparada para red.

El modelo principal es `Ticket`, que representa la información que utiliza la aplicación.

El repositorio administra la colección de tickets y la expone de forma reactiva. Esto permite que la UI se actualice automáticamente cuando se crea un ticket o cuando cambia su prioridad.

Los datos simulados se mantienen dentro de `data/mock` porque representan la fuente temporal de información para la prueba de concepto.

---

### 7.4 Capa remota

La capa remota contiene las clases necesarias para una futura integración con backend.

Incluye:

* `ApiService.kt`;
* `RetrofitClient.kt`;
* DTOs dentro de `remote/model`.

Aunque durante esta PoC la aplicación no consume un backend real, esta estructura muestra cómo se conectaría posteriormente a una API REST.

Esto evita mezclar modelos de API con modelos usados por la UI y deja preparada la integración sin obligar a implementar un backend.

---

## 8. Comunicación reactiva con StateFlow

La aplicación utiliza `StateFlow` para mantener reactivo el listado de tickets.

El repositorio mantiene internamente un `MutableStateFlow<List<Ticket>>` y expone una versión de solo lectura.

Cuando se crea un nuevo ticket, el repositorio actualiza la lista y emite el nuevo valor. La pantalla del listado observa ese estado, por lo que el ticket aparece automáticamente sin tener que recargar manualmente la vista.

Cuando cambia la prioridad de un ticket, el repositorio actualiza el ticket y vuelve a ordenar la lista. Como el listado se emite mediante `StateFlow`, la UI recibe automáticamente el nuevo orden.

Esto cubre los dos escenarios reactivos requeridos:

1. el listado se actualiza al crear un nuevo ticket;
2. los tickets se reordenan cuando cambia su prioridad.

---

## 9. Ordenamiento por prioridad

El ordenamiento por prioridad se maneja en el repositorio, no en la UI.

Esta decisión se tomó porque el orden de atención de los tickets forma parte del comportamiento de los datos, no de la presentación visual.

La UI solo debe mostrar la lista que recibe.

El orden esperado es:

```text
Critical
High
Medium
Low
```

Mantener esta lógica en el repositorio evita duplicar el ordenamiento en diferentes pantallas y facilita el mantenimiento.

---

## 10. Estrategia de datos simulados

Los datos simulados no son aleatorios. Representan casos empresariales realistas relacionados con la operación de Panini.

Ejemplos de incidencias simuladas:

* faltantes de inventario en un punto de venta;
* retrasos en entregas de proveedores;
* distribución incompleta de paquetes;
* problemas logísticos entre bodega y tiendas;
* cantidades incorrectas de producto;
* solicitudes urgentes relacionadas con disponibilidad de sobres o álbumes.

Esto hace que la prueba de concepto sea más coherente con el contexto del problema y permite que la demostración se acerque más a una herramienta interna real.

---

## 11. API Contracts

El repositorio incluye una carpeta `/contracts` en la raíz del proyecto.

Los contratos de API están escritos en YAML y describen los endpoints que un backend futuro debería exponer.

Los contratos están alineados con el comportamiento real de la app:

* autenticación simulada;
* listar tickets;
* obtener detalle de ticket;
* crear ticket;
* actualizar estado;
* actualizar prioridad.

El objetivo no es implementar el backend, sino definir un acuerdo claro de integración entre la aplicación móvil y una futura API.

---

## 12. Manejo de dependencias

El proyecto utiliza inyección de dependencias manual mediante `AppContainer`.

Esta decisión se tomó porque la PoC tiene pocas dependencias y no necesita un framework completo de inyección.

`AppContainer` centraliza la creación de objetos como el repositorio y el servicio de API futuro, manteniendo la implementación fácil de entender.

Este enfoque es suficiente para el alcance actual y podría ser reemplazado por Hilt si el proyecto crece.

---

## 13. Feature Flags

Los Feature Flags se utilizan para habilitar o deshabilitar funcionalidades específicas sin modificar múltiples partes del proyecto.

Para esta PoC, los Feature Flags controlan acciones como:

* creación de tickets;
* actualización de prioridad.

Esto permite simular escenarios de pruebas internas donde ciertas funcionalidades pueden estar activas o desactivadas según la fase de validación.

La implementación se mantiene simple porque el proyecto no requiere configuración remota ni un sistema productivo de Feature Flags.

---

## 14. Tecnologías no utilizadas

### Room

Room no se incluyó porque la PoC no requiere persistencia local.

La aplicación trabaja con datos simulados en memoria, y agregar una base de datos aumentaría la complejidad sin ser necesario para el alcance solicitado.

### Firebase

Firebase no se incluyó porque el examen no requiere autenticación real, notificaciones push, analytics ni configuración remota.

### Hilt

Hilt no se incluyó porque la inyección manual mediante `AppContainer` es suficiente para el tamaño actual del proyecto.

Agregar Hilt implicaría configuración adicional que no aporta valor directo a esta prueba de concepto.

---

## 15. Evolución futura

La arquitectura actual permite mejoras futuras sin una reestructuración grande.

Posibles siguientes pasos:

* reemplazar los datos simulados por llamadas reales con Retrofit;
* conectar la app a un backend que respete los contratos YAML;
* agregar persistencia local solo si se requiere soporte offline;
* reemplazar `AppContainer` por Hilt si aumenta la cantidad de dependencias;
* agregar más categorías, filtros o vistas administrativas.

Lo importante es que la estructura actual no bloquea estas extensiones.

---

## 16. Criterio final de ingeniería

La arquitectura fue diseñada con las siguientes prioridades:

* mantener el proyecto simple;
* evitar capas innecesarias;
* separar UI, estado y datos;
* soportar actualizaciones reactivas con `StateFlow`;
* utilizar datos simulados realistas;
* preparar la estructura de red para backend futuro;
* facilitar que otro ingeniero pueda entender, mantener y continuar el proyecto.

Esta solución no pretende ser un sistema productivo completo. Es una prueba de concepto móvil clara, mantenible y alineada con el alcance solicitado.
