# Panini Support Tickets - FIFA 2026

Aplicación móvil de prueba de concepto para la gestión de tickets internos de soporte relacionados con proveedores, inventario, distribución y logística del álbum Panini de la Copa Mundial FIFA 2026.

## Descripción general

Panini necesita centralizar el seguimiento de incidencias internas relacionadas con la operación del álbum FIFA 2026. Actualmente, este tipo de reportes puede manejarse mediante correos, hojas de cálculo o mensajes informales, lo que dificulta el seguimiento, genera duplicidad de solicitudes y retrasa la atención de problemas operativos.

Esta aplicación propone una PoC móvil para registrar, visualizar y actualizar tickets de soporte interno.

El proyecto no implementa un backend real. La aplicación funciona con datos simulados, pero deja preparada la estructura necesaria para una futura integración mediante Retrofit, DTOs y contratos de API en YAML.

## Estado del proyecto

**Tipo de entrega:** Prueba de Concepto móvil
**Backend:** No implementado
**Datos:** Mock data en memoria
**Autenticación:** Simulada
**Arquitectura:** MVVM simple con Repository Pattern

## Funcionalidades principales

La aplicación permite:

* iniciar sesión de forma simulada;
* visualizar un listado de tickets;
* consultar el detalle de un ticket;
* crear nuevos tickets;
* actualizar el estado de un ticket;
* actualizar la prioridad de un ticket;
* reordenar automáticamente el listado según prioridad;
* habilitar o deshabilitar funcionalidades mediante Feature Flags.

## Tecnologías utilizadas

* Kotlin
* Jetpack Compose
* Material 3
* Navigation Compose
* ViewModel
* Kotlin Coroutines
* Flow / StateFlow
* Retrofit
* Gson
* Gradle Kotlin DSL

## Estructura del repositorio

```text
fifaTickets-Abigail/
├── app/
├── contracts/
├── docs/
├── video/
└── README.md
```

## Estructura principal de la aplicación

```text
app/src/main/java/com/example/paninisupporttickets/
├── MainActivity.kt
│
├── core/
│   ├── AppConstants.kt
│   └── UserMessages.kt
│
├── data/
│   ├── Ticket.kt
│   ├── AppContainer.kt
│   ├── mock/
│   │   └── MockTickets.kt
│   ├── remote/
│   │   ├── ApiService.kt
│   │   ├── RetrofitClient.kt
│   │   └── model/
│   │       ├── AuthModels.kt
│   │       └── TicketModels.kt
│   └── repository/
│       ├── ApiResult.kt
│       └── TicketRepository.kt
│
├── navigation/
│   ├── AppDestinations.kt
│   └── AppNavHost.kt
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

## Arquitectura

El proyecto utiliza **MVVM simple con Repository Pattern**.

```text
Jetpack Compose Screen
        ↓
ViewModel
        ↓
TicketRepository
        ↓
Mock Data / Futura API con Retrofit
```

La UI observa estados expuestos por los ViewModels mediante `StateFlow`. Los ViewModels coordinan las acciones de cada pantalla y delegan el acceso a datos al repositorio.

`TicketRepository` funciona como la fuente principal de datos para los tickets. En esta PoC trabaja con mocks, pero la estructura permite reemplazar esa fuente por llamadas reales a una API en una fase posterior.

Más detalle en:

* [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md)
* [docs/EVENT_FLOW.md](docs/EVENT_FLOW.md)
* [docs/FEATURE_FLAGS.md](docs/FEATURE_FLAGS.md)

## Flujo reactivo

La aplicación utiliza `StateFlow` para mantener actualizado el listado de tickets.

Cuando se crea un ticket, el repositorio actualiza la lista interna y emite un nuevo estado. El listado observa ese flujo, por lo que el nuevo ticket aparece automáticamente sin recargar manualmente la pantalla.

Cuando se modifica la prioridad de un ticket, el repositorio actualiza el ticket, reordena la lista y emite el nuevo estado. Esto permite que los tickets de mayor prioridad se muestren primero.

Orden de prioridad utilizado:

```text
Critical
High
Medium
Low
```

## Feature Flags

El proyecto incluye Feature Flags simples para controlar funcionalidades durante pruebas internas.

```kotlin
object FeatureFlags {
    const val ENABLE_TICKET_CREATION = true
    const val ENABLE_PRIORITY_UPDATE = true
}
```

Estos flags permiten habilitar o deshabilitar:

* creación de tickets;
* actualización de prioridad.

La validación se realiza tanto en la UI como en el ViewModel, evitando que la lógica dependa únicamente de elementos visuales.

## API Contracts

La carpeta `/contracts` contiene los contratos de API en formato YAML.

Estos contratos definen la estructura esperada para una futura integración con backend, incluyendo operaciones como:

* login simulado;
* listado de tickets;
* detalle de ticket;
* creación de ticket;
* actualización de estado;
* actualización de prioridad.

El objetivo de estos contratos no es implementar el backend, sino dejar claro cómo debería comunicarse la aplicación móvil con una API real en una fase posterior.

## Datos simulados

Los datos mock representan escenarios realistas del contexto Panini, por ejemplo:

* faltantes de inventario en puntos de venta;
* retrasos de proveedores;
* errores en distribución;
* paquetes incompletos;
* problemas logísticos entre bodega y tiendas;
* solicitudes urgentes por disponibilidad de sobres o álbumes.

Esto permite que la PoC sea coherente con el problema planteado y no dependa de datos genéricos.

## Instrucciones de ejecución

### Requisitos previos

* Android Studio
* JDK compatible con el proyecto
* Android SDK instalado
* Emulador o dispositivo físico Android

### Pasos para ejecutar

1. Clonar el repositorio:

```bash
git clone https://github.com/tu-usuario/fifaTickets-Abigail.git
cd fifaTickets-Abigail
```

2. Abrir el proyecto en Android Studio.

3. Esperar la sincronización de Gradle.

4. Ejecutar la aplicación en un emulador o dispositivo físico.

### Compilar desde terminal

```bash
./gradlew assembleDebug
```

Para instalar en un dispositivo conectado:

```bash
./gradlew installDebug
```

## Uso de la aplicación

Para probar la aplicación:

1. Ingresar un correo con formato válido.
2. Ingresar cualquier contraseña no vacía.
3. Acceder al listado de tickets.
4. Seleccionar un ticket para ver su detalle.
5. Crear un nuevo ticket desde la acción correspondiente.
6. Cambiar estado o prioridad desde el detalle.

## Preparación para backend futuro

Aunque actualmente la app utiliza mocks, ya existe una estructura preparada para integración real:

* `ApiService.kt` define los endpoints esperados.
* `RetrofitClient.kt` centraliza la configuración HTTP.
* Los DTOs viven en `data/remote/model`.
* Los contratos YAML documentan las requests y responses esperadas.
* El repositorio puede reemplazar la fuente mock por llamadas a Retrofit.

Para conectar un backend real, los principales cambios estarían concentrados en:

* `AppConstants.kt`, para actualizar la URL base;
* `TicketRepository.kt`, para reemplazar mocks por llamadas a `ApiService`;
* manejo de errores de red mediante `ApiResult`.

## Documentación adicional

* [Arquitectura](docs/ARCHITECTURE.md)
* [Flujo reactivo con StateFlow](docs/EVENT_FLOW.md)
* [Feature Flags](docs/FEATURE_FLAGS.md)
* [Video demo](video/demo-link.md)

## Autor

Abigail Ramírez
Diseño y Programación de Plataformas Móviles
Universidad Nacional
I Ciclo 2026
