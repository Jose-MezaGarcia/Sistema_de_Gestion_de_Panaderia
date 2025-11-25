# Dulce Tentación - Sistema de Gestión

Sistema completo de gestión para panadería con diseño personalizado en tonos amarillos/dorados.

## Características Especiales

- **Diseño Personalizado**: Interfaz con tema "Dulce Tentación" en colores amarillos/dorados
- **Login Elegante**: Pantalla de bienvenida con efectos visuales
- **Sistema de Ventas**: Búsqueda por código, cálculo automático
- **Arquitectura MVC + SOLID**: Código limpio y mantenible
- **Persistencia JSON**: Datos guardados con Gson

##  Requisitos

- Java 17 o superior
- Maven 3.6 o superior

##  Instalación y Ejecución

```bash
cd gestion-panaderia
mvn clean compile
mvn javafx:run
```

## Credenciales

- **Admin**: usuario: `admin` / contraseña: `admin123`
- **Vendedor**: usuario: `vendedor` / contraseña: `vendedor123`

##  Productos Disponibles

| Código | Producto | Precio |
|--------|----------|--------|
| 001 | Concha de Vainilla | $10.00 |
| 002 | Croissant | $15.00 |
| 003 | Pastel de Chocolate | $250.00 |
| 004 | Dona Glaseada | $12.00 |
| 005 | Pan Integral | $25.00 |
| 006 | Cupcake de Fresa | $18.00 |

##  Cómo Usar

### Realizar una Venta

1. **Login**: Inicia sesión con tus credenciales
2. **Código**: Ingresa el código del producto (ej: 001) y presiona Enter
3. **Cantidad**: Ingresa la cantidad deseada
4. **Agregar**: Click en "Agregar" para añadir al carrito
5. **Guardar**: Click en "Guardar Venta" para completar

### Búsqueda Rápida

- Escribe el código y presiona **Enter**
- El sistema auto-completa el nombre y precio
- Solo ingresa la cantidad y agrega

##  Características del Diseño

### Login
- Panel izquierdo con gradiente dorado
- Campos redondeados con bordes naranja
- Botones con efectos de sombra
- Mensajes de error/éxito visibles

### Ventas
- Menú lateral amarillo con iconos
- Tabla de productos con bordes redondeados
- Campos auto-completados por código
- Total calculado automáticamente
- Botones con gradientes llamativos

##  Estructura del Proyecto

```
gestion-panaderia/
├── src/main/
│   ├── java/com/example/gestion_panaderia/
│   │   ├── controller/    # LoginController, VentaController
│   │   ├── model/         # Usuario, Producto, Venta, etc.
│   │   ├── service/       # AuthService, ProductoService, VentaService
│   │   └── repository/    # JsonRepository genérico
│   └── resources/fxml/
│       ├── login.fxml     # Diseño personalizado login
│       └── ventas.fxml    # Diseño personalizado ventas
├── usuarios.json          # Datos de usuarios
├── productos.json         # Catálogo de productos
└── ventas.json            # Registro de ventas
```

##  Principios Aplicados

✅ **MVC** - Model View Controller
✅ **SOLID** - Principios de diseño
✅ **DI** - Dependency Injection
✅ **Repository Pattern** - Datos abstraídos
✅ **Generic Types** - Repositorio reutilizable

## Tecnologías

- **Java 17**
- **JavaFX 17**
- **Gson 2.10.1**
- **Maven**

## Flujo de la Aplicación

```
Login (Dulce Tentación) 
    ↓
Autenticación
    ↓
Ventas (Sistema completo)
    ↓
JSON (Persistencia)
```

## Características Técnicas

### Controllers Adaptados
- `LoginController`: Usa IDs de tu FXML (btnAceptar, btnCancelar, boxMensaje)
- `VentaController`: Búsqueda por código, auto-completado, tabla funcional

### Servicios
- `AuthServiceImpl`: Autenticación contra JSON
- `ProductoServiceImpl`: CRUD de productos
- `VentaServiceImpl`: Registro de ventas

### Repositorio Genérico
- `JsonRepository<T>`: Funciona con cualquier tipo
- Métodos: cargar(), guardar(), findById(), eliminar()

## Personalización

Los colores del tema son:
- **Primario**: #FFB347 (Naranja claro)
- **Secundario**: #FFCC33 (Amarillo)
- **Acento**: #FF8C00 (Naranja oscuro)
- **Fondo**: #FFFACD (Amarillo crema)

## Notas Importantes

1.  Los archivos JSON deben estar en la raíz del proyecto
2.  El stock se actualiza automáticamente al vender
3.  Búsqueda por código con auto-completado
4.  Todas las ventas se guardan con fecha y hora
5.  Diseño totalmente personalizado incluido

---

**Desarrollado con el corazon para Dulce Tentación**
