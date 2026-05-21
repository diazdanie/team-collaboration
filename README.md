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
*   Base de Datos: MySQL (conector JDBC)
*   Interfaz Gráfica: Swing

## Requisitos Previos
Antes de ejecutar el proyecto, asegúrate de tener instalado:
1. Java Development Kit (JDK) versión 25 o superior.
2. Servidor de Base de Datos MySQL CordiPOS activo.
3. El archivo del conector JDBC (`.jar`) incluido en la carpeta `lib/`.

## Instrucciones de Instalación en Eclipse

1. Clonar o descargar este repositorio.
2. Importar el proyecto en Eclipse:
Abre Eclipse y ve a `File` > `Import...`
   	Selecciona `General` > `Existing Projects into Workspace`.
   	Busca la carpeta raíz del proyecto y haz clic en `Finish`.
3. Configurar la Base de Datos:
Importa el archivo `CordiPOS.sql` (ubicado en la carpeta `db/`) en tu gestor de base de datos.
Modifica las credenciales de conexión en el archivo `Conexion.java` (usuario y contraseña de tu BD).
4. Configurar las Librerías:
   Si las librerías no se cargan solas, haz clic derecho sobre el proyecto > `Build Path` > `Configure Build Path...`
   En la pestaña `Libraries`, añade el archivo `.jar` del conector de la base de datos que está en la carpeta `lib/`.
5. Ejecutar:
   * Busca la clase `Main.java`, haz clic derecho > `Run As` > `Java Application`.

👥 Autores
*   Diaz Matias Carlos Daniel - Desarrollador Principal - diazdanie
*   Morales de la Cruz Jorge Alberto – Desarrollador – cruzmoralessz
*   Dominguez Morales Kevin – Desarrollador – kevin200820
