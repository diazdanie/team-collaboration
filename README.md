# team-collaboration
producto integrador Punto de Venta para Abarrotes - Proyecto Final
## Descripción
Sistema de Punto de Venta (POS) diseñado para la gestión y automatización de una tienda de abarrotes. Permite controlar el inventario en tiempo real, realizar ventas de productos (por pieza o kilogramo), emitir tickets de compra y generar reportes de ventas diarias para los administradores.

Desarrollado como proyecto final para la materia Programación avanzada en la Universidad Autónoma de Tamaulipas

## Características Principales
*   Gestión de Inventario: Altas, bajas, modificaciones y alertas de stock mínimo.
*   Módulo de Ventas: Carrito de compras, cálculo de cambio, soporte para lector de código de barras.
*   Roles de Usuario: Login seguro para Administradores (acceso total) y Cajeros (solo ventas).
*   Reportes: Estadísticas de productos más vendidos y corte de caja diario.

## Tecnologías Utilizadas
*   Lenguaje: Java (JDK 25)
*   IDE: Eclipse
*   Servidor Local: XAMPP (Apache + MySQL) 
*   Gestor de BD: phpMyAdmin 
*   Conector: `mysql-connector-j-8.x.jar`
*   Interfaz Gráfica: Swing

## Requisitos Previos
Antes de ejecutar el proyecto, asegúrate de tener instalado:
1. XAMPP instalado y corriendo. 
2. Java Runtime Environment (JRE) compatible con tu versión de compilación. 
3. El puerto 3306 libre para la conexión a MySQL.
### Configuración de la Base de Datos (XAMPP) 
1.	Inicia el panel de control de XAMPP y activa los módulos 
2.	Apache y MySQL. 
3.	Ve a [http://localhost/phpmyadmin](http://localhost/phpmyadmin) en tu navegador. 
4.	Crea una base de datos nueva llamada `CordiPOS`. 
5.	Importa el archivo `CordiPOS.sql` (ubicado en la carpeta `database/` de este proyecto) para cargar automáticamente las 7 tablas y los 150 productos precargados.
## Instrucciones de Instalación en Eclipse.
1. Clonar o descargar este repositorio.
2. Importación en Eclipse 
1. Abre Eclipse y selecciona `File > Import > General > Existing Projects into Workspace`. 
2. Selecciona la carpeta raíz del proyecto. 
3. Verifica que el archivo `mysql-connector-j-8.x.jar` esté presente en el **Build Path** (`Project > Properties > Java Build Path > Libraries`). 
### Credenciales de Acceso Para probar el sistema, utiliza los siguientes usuarios registrados en la DB: 
* Administrador: Usuario: `administrador` | Contraseña: `123` 
* Cajero: Usuario: `cajero` | Contraseña: `123` 
## 📂 Estructura del Proyecto * `src/com/cordi/modelo`: Clases de datos y conexión (`ConexionBD.java`). * `src/com/cordi/vista`: Interfaces gráficas (Ventanas). 
* `src/com/cordi/controlador`: Lógica de eventos y validaciones. 
* `lib/`: Librerías externas (Driver JDBC). 
* `database/`: Scripts SQL para la creación de la DB. 
👥 Autores
*   Diaz Matias Carlos Daniel - Desarrollador - diazdanie
*   Morales de la Cruz Jorge Alberto – Desarrollador – cruzmoralessz
*   Dominguez Morales Kevin – Desarrollador – kevin200820
