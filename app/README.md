#  COMPRAMASS - Tienda Virtual Android

Aplicación móvil de comercio electrónico desarrollada con Java y Android Studio, que permite a los usuarios visualizar productos, gestionarlos en un carrito de compras y realizar pedidos con múltiples métodos de pago.

## Tabla de Contenidos
- [Características](#características)
- [Tecnologías](#tecnologías)
- [Requisitos Previos](#requisitos-previos)
- [Instalación y Ejecución](#instalación-y-ejecución)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Patrones de Diseño Implementados](#patrones-de-diseño-implementados)
- [Pruebas](#pruebas)
- [Equipo de Desarrollo](#equipo-de-desarrollo)

## Características

- 📱 Catálogo de productos con búsqueda en tiempo real
- 🛒 Carrito de compras con funcionalidad Undo (deshacer)
-  Múltiples métodos de pago (Efectivo, Tarjeta, Yape/Plin)
- 📦 Seguimiento de pedidos con estados
- 💾 Persistencia de datos con Room Database
- 🎨 Interfaz moderna con Material Design

##  Tecnologías

- **Lenguaje:** Java 8+
- **IDE:** Android Studio Arctic Fox o superior
- **Base de Datos:** Room (SQLite)
- **UI:** XML + Material Design Components
- **Pruebas:** JUnit 4
- **Arquitectura:** Clean Architecture + MVVM

## Requisitos Previos

- Android Studio (versión más reciente recomendada)
- JDK 8 o superior
- SDK de Android (API 21 como mínimo)
- Emulador Android o dispositivo físico con Android 5.0+
- Conexión a internet (para descargar dependencias Gradle)

##  Instalación y Ejecución

### Paso 1: Clonar o Descargar el Proyecto
git clone <https://github.com/dianacopacondori/ApplicationTienda.git>
cd COMPRAMASS

O simplemente abre el proyecto directamente desde Android Studio:
- File → Open → Selecciona la carpeta del proyecto

### Paso 2: Sincronizar Dependencias

Al abrir el proyecto, Android Studio sincronizará automáticamente las dependencias de Gradle. Si no lo hace:
- Click derecho en `build.gradle` → "Sync Gradle"
- O haz click en el icono  "Sync Project with Gradle Files"

### Paso 3: Configurar el Emulador

1. Abre **Device Manager** en Android Studio
2. Crea un nuevo dispositivo virtual (AVD):
    - Selecciona un dispositivo (ej: Pixel 4)
    - Elige una imagen del sistema (recomendado: API 30 o superior)
    - Finaliza la configuración
3. Inicia el emulador

### Paso 4: Ejecutar la Aplicación

- Click en el botón verde ▶️ "Run 'app'"
- O presiona `Shift + F10`

La aplicación se instalará automáticamente en el emulador/dispositivo y se abrirá en la pantalla de productos.

## 📁 Estructura del Proyecto
```
app/src/main/
├── java/com/example/applicationtienda/
│   ├── ui/                              # Capa de Presentación
│   │   ├── CarritoActivity.java
│   │   ├── CheckoutActivity.java
│   │   ├── ProductosActivity.java
│   │   └── [Otros Activities y Adapters]
│   │
│   ├── domain/                          # Capa de Dominio
│   │   └── model/                       # Entidades (Product, Cart, Order)
│   │
│   ├── infrastructure/                  # Capa de Infraestructura
│   │   ├── di/                          # Inyección (ServiceContainer)
│   │   ├── network/                     # Servicios de red
│   │   └── persistence/                 # Room (AppDatabase, DAOs, Repositories)
│   │
│   ── patterns/                        # Patrones de Diseño (GoF)
│       ├── behavioral/                  # Command, Observer, State
│       ├── creational/                  # Singleton, Factory, Builder
│       └── structural/                  # Adapter, Decorator, Facade
│
└── res/                                 # Recursos
├── drawable/                        # Imágenes y fondos
└── layout/                          # Diseños XML de pantallas
```
## 🎯 Patrones de Diseño Implementados (GoF)

El proyecto cumple y supera el requisito mínimo de la rúbrica (≥2 por categoría), aplicando los patrones de manera funcional y justificada:

### Patrones Creacionales (3 implementados)
- ✅ **Singleton:** `CartManager`, `AppDatabase` y `ServiceContainer`. Garantizan una única instancia global para el carrito y la base de datos, evitando duplicidad de estado.
- ✅ **Factory Method:** `ProductFactory`, `ElectronicsFactory` y `ClothingFactory`. Centralizan la creación de productos, permitiendo añadir nuevas categorías sin modificar la lógica de negocio.
- ✅ **Builder:** `OrderBuilder`. Construye el objeto `Order` de manera progresiva y segura antes de persistirlo en la base de datos.

### Patrones Estructurales (2 implementados)
- ✅ **Adapter:** `ProductoRecyclerAdapter`, `CarritoRecyclerAdapter`, `CheckoutAdapter`. Adaptan los modelos de dominio (`Product`, `CartItem`) a las vistas (`ViewHolder`) de los RecyclerView.
- ✅ **Facade:** `ShopFacade`. Simplifica la interacción de la capa UI con los subsistemas complejos de creación de productos y gestión de pedidos.

### Patrones de Comportamiento (3 implementados)
- ✅ **Command:** `AddToCartCommand`, `RemoveFromCartCommand`, `DisminuirCantidadCommand` y `CommandHistory`. Encapsulan las acciones del carrito como objetos, permitiendo la funcionalidad de **Undo (Deshacer)** de manera robusta.
- ✅ **State:** `OrderState` y sus implementaciones (`PendingState`, `ProcessingState`, `ShippedState`, `DeliveredState`, `CancelledState`). Gestionan el ciclo de vida del pedido, cambiando su comportamiento dinámicamente sin usar múltiples `if/else`.
- ✅ **Observer:** `CartObserver`, `InventoryObserver` y `NotificationObserver`. Notifican automáticamente a los componentes interesados cuando el estado del carrito o el inventario cambia.

---

## 🏛️ Principios SOLID y GRASP

### Evidencia SOLID
- **SRP (Responsabilidad Única):** Las Activities (`CarritoActivity`, `CheckoutActivity`) solo manejan la UI; los Repositories (`RoomOrderRepository`) solo manejan la persistencia.
- **OCP (Abierto/Cerrado):** El sistema está abierto a nuevas extensiones. Por ejemplo, para agregar un nuevo estado de pedido, solo se crea una nueva clase que extienda `OrderState` (ej: `ReturnedState`), sin modificar la clase `Order` ni el `CommandHistory`.
- **LSP (Sustitución de Liskov):** Cualquier implementación de `OrderState` o `Command` puede sustituir a su interfaz base sin romper el contrato ni el flujo de la aplicación.
- **ISP (Segregación de Interfaces):** Interfaces pequeñas y específicas como `CartObserver` (solo notifica cambios del carrito) o `Command` (solo `execute` y `undo`).
- **DIP (Inversión de Dependencias):** La capa de UI y Dominio dependen de abstracciones (Interfaces de Repositorios) y no de la implementación concreta de Room.

### Evidencia GRASP
- **Information Expert:** La clase `Cart` calcula su propio total y gestiona sus `CartItem`, ya que posee la información necesaria.
- **Creator:** `OrderBuilder` es el responsable de crear instancias complejas de `Order`.
- **Polymorphism:** Las clases de `OrderState` reemplazan las estructuras condicionales, permitiendo que el pedido se comporte diferente según su estado.
- **Low Coupling:** La UI no conoce a Room Database; se comunica a través de abstracciones.

---

## 🧪 Pruebas (Testing)

El proyecto incluye pruebas unitarias y de integración para garantizar la estabilidad del flujo principal y el correcto funcionamiento de los patrones.

### Cómo correr las pruebas

**Opción 1: Desde Android Studio (Recomendado)**
1. En el panel lateral izquierdo, navega a la carpeta `app/src/test/java` (para unitarias) o `app/src/androidTest/java` (para integración).
2. Haz clic derecho sobre la carpeta `com.example.applicationtienda`.
3. Selecciona **"Run 'All Tests'"**.
4. Para ver la cobertura, selecciona **"Run 'All Tests' with Coverage"**.

**Opción 2: Desde la Terminal (Gradle)**
```bash
# Ejecutar pruebas unitarias locales
./gradlew test

# Ejecutar pruebas de instrumentación (requiere emulador o dispositivo)
./gradlew connectedAndroidTest
