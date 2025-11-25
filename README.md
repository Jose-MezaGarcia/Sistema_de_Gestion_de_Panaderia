# Dulce Tentación - Sistema de Gestión de Panadería

Sistema completo de gestión para panadería “Dulce Tentación”, desarrollado en Java con JavaFX, arquitectura MVC y persistencia JSON, que cumple con requerimientos funcionales y no funcionales para la operación integral del negocio. El sistema incluye módulos de productos, pedidos, inventario, ventas, clientes, facturación, reportes y una interfaz personalizada en tonos amarillos/dorados.



# Requerimientos
# Requerimientos Funcionales - Sistema de Gestión de Panadería


## RF-01: Gestión de Productos

**RF-01.1 - Mantenimiento de Catálogo de Productos**
- El sistema permitirá crear, modificar, consultar y eliminar productos del catálogo de panadería, incluyendo panes artesanales, pasteles, galletas y productos horneados especializados.
- Cada producto almacenará la siguiente información: nombre comercial, descripción detallada del producto, precio unitario de venta, estado de disponibilidad y clasificación por categoría.

**RF-01.2 - Clasificación de Productos**
- El sistema permitirá categorizar productos según su tipo: pan, pastel, galleta y otros productos horneados.



## RF-02: Toma de Pedidos

**RF-02.1 - Registro de Pedidos**
- El sistema permitirá registrar pedidos de clientes mediante la selección de productos del catálogo y la especificación de las cantidades requeridas.
- Cada pedido registrará: fecha y hora de creación, identificación del cliente, productos solicitados con sus respectivas cantidades y estado actual del pedido.

**RF-02.2 - Cálculo Automático de Total**
- El sistema calculará automáticamente el precio total del pedido mediante la sumatoria de los subtotales de cada producto (precio unitario × cantidad).



## RF-03: Inventario y Control de Stock

**RF-03.1 - Gestión de Inventario**
- El sistema permitirá registrar y actualizar las existencias de productos terminados disponibles en el establecimiento.
- El sistema mantendrá un registro histórico completo de todos los movimientos de inventario, incluyendo entradas y salidas de mercancía.

**RF-03.2 - Control de Disponibilidad**
- El sistema validará la disponibilidad de productos en existencia antes de confirmar cualquier pedido.
- El sistema actualizará automáticamente los niveles de inventario al momento de registrar cada venta.

**RF-03.3 - Alertas de Inventario Bajo**
- El sistema generará alertas automáticas cuando los niveles de inventario de un producto alcancen o desciendan del punto de reorden previamente configurado.



## RF-04: Registro de Ventas

**RF-04.1 - Historial de Ventas**
- El sistema mantendrá un registro completo y detallado de todas las ventas realizadas, incluyendo: fecha y hora de la transacción, productos vendidos, cantidades comercializadas, precios aplicados y cliente asociado a la venta.

**RF-04.2 - Consulta de Ventas Anteriores**
- El sistema permitirá buscar y consultar ventas anteriores mediante filtros configurables por fecha, cliente o producto específico.



## RF-05: Gestión de Clientes

**RF-05.1 - Registro de Clientes**
- El sistema permitirá crear, modificar, consultar y eliminar registros de clientes habituales del establecimiento.
- Cada cliente almacenará: nombre completo, información de contacto (teléfono, correo electrónico, dirección postal) y preferencias personales.

**RF-05.2 - Programas de Lealtad**
- El sistema permitirá administrar programas de lealtad y esquemas de descuentos especiales para clientes frecuentes.
- El sistema registrará los puntos acumulados por cliente y los descuentos aplicables según el programa de fidelización.



## RF-06: Generación de Facturas o Recibos

**RF-06.1 - Emisión de Recibos**
- El sistema generará recibos de venta con formato profesional y presentación estandarizada para cada transacción realizada.

**RF-06.2 - Contenido del Recibo**
- Cada recibo incluirá de manera estructurada: número de folio consecutivo, fecha y hora de emisión, datos fiscales del negocio, detalle de productos vendidos con sus cantidades y precios unitarios, subtotal de la operación, descuentos aplicados (cuando corresponda), importe total y datos del cliente (si aplica).



## RF-07: Descuentos y Promociones

**RF-07.1 - Aplicación de Descuentos**
- El sistema permitirá aplicar descuentos tanto a productos específicos como al total general de la compra.
- Los descuentos podrán configurarse como porcentaje de reducción o como monto fijo a descontar.

**RF-07.2 - Cálculo Automático de Descuentos**
- El sistema calculará automáticamente los descuentos aplicables según las reglas de negocio configuradas y los aplicará al total de la venta de forma inmediata.



## RF-08: Reportes de Ventas y Finanzas

**RF-08.1 - Reportes Periódicos de Ventas**
- El sistema generará informes de ventas con periodicidad configurable: diaria, semanal, mensual y anual.
- Los reportes incluirán: importe total de ventas del período, cantidad de transacciones realizadas y listado de productos más vendidos.

**RF-08.2 - Análisis de Ventas**
- El sistema proporcionará informes de análisis detallado de ventas por producto, categoría y período de tiempo seleccionado.
- Los reportes incluirán representaciones gráficas y estadísticas comparativas para facilitar la toma de decisiones.



## RF-09: Búsqueda y Consulta de Productos

**RF-09.1 - Búsqueda Rápida**
- El sistema permitirá buscar productos mediante filtros configurables: nombre del producto, número de artículo y categoría.
- La búsqueda mostrará resultados de manera inmediata en tiempo real.

**RF-09.2 - Información Detallada de Productos**
- El sistema mostrará información completa y detallada de cada producto: precio vigente, existencias actuales en inventario, descripción del producto y especificaciones técnicas relevantes.



## Requerimientos No Funcionales

**RNF-01: Usabilidad**
- El sistema contará con una interfaz gráfica de usuario intuitiva, amigable y de fácil navegación, desarrollada con tecnología **JavaFX**.
- La navegación será clara y coherente, con pantallas y formularios organizados de manera lógica para facilitar las operaciones de gestión de productos, procesamiento de ventas, generación de reportes y demás funcionalidades del sistema.
- La interfaz aprovechará los componentes modernos de JavaFX para asegurar una experiencia de usuario contemporánea, fluida y responsiva.

**RNF-02: Tecnología**
- El sistema será desarrollado utilizando **Java** como lenguaje de programación principal, aprovechando sus capacidades de programación orientada a objetos y portabilidad multiplataforma.
- La interfaz gráfica de usuario se implementará con el framework **JavaFX** para proporcionar una experiencia visual moderna, consistente y de alto rendimiento.ramework **JavaFX** para proporcionar una experiencia visual moderna, consistente y de alto rendimiento.


# Dulce Tentación - Sistema de Gestión

Sistema completo de gestión para panadería con diseño personalizado en tonos amarillos/dorados.

## Características Especiales

- **Diseño Personalizado**: Interfaz con tema "Dulce Tentación" en colores amarillos/dorados
- **Login Elegante**: Pantalla de bienvenida con efectos visuales
- **Sistema de Ventas**: Búsqueda por código, cálculo automático
- **Arquitectura MVC + SOLID**: Código limpio y mantenible
- **Persistencia JSON**: Datos guardados con Gson

## Requisitos

- Java 17 o superior
- Maven 3.6 o superior

## Instalación y Ejecución

