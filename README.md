# Sistema de Facturación - Mapeo Objeto-Relacional (ORM) con JPA y Hibernate

Este proyecto corresponde a la práctica de la asignatura Desarrollo de Software. El objetivo principal es implementar un modelo de dominio completo, utilizando JPA (Java Persistence API) y Hibernate como motor de persistencia, demostrando el mapeo de entidades, relaciones entre clases, integridad referencial y la propagación de operaciones mediante persistencia en cascada (`CascadeType.ALL`).

---

## Decisiones de Arquitectura e Infraestructura

Con el objetivo de trabajar con un motor de base de datos relacional estándar y poder realizar la inspección directa del esquema y los datos generados por Hibernate, se decidió utilizar una instancia de **PostgreSQL ejecutándose localmente**.

Para la capa de persistencia se utiliza **JPA**, implementado mediante **Hibernate ORM**, mientras que la comunicación con PostgreSQL se realiza mediante el driver JDBC oficial.

### Justificación de la elección:

- **Entorno local:** Permite desarrollar y probar la aplicación sin depender de servicios externos o proveedores de nube.

- **Inspección de esquema y datos:** PostgreSQL, mediante herramientas como **pgAdmin**, permite verificar directamente las tablas creadas por Hibernate, los registros insertados, las claves primarias y las claves foráneas generadas a partir de las relaciones entre las entidades.

- **Compatibilidad estándar:** Al utilizar PostgreSQL como motor de base de datos relacional, no se requiere código propietario. La aplicación se comunica mediante el driver JDBC oficial (`org.postgresql.Driver`).

- **Integración con JPA y Hibernate:** Hibernate se utiliza como implementación de JPA para realizar el mapeo objeto-relacional y administrar las operaciones de persistencia sobre la base de datos.

---

## Requisitos Previos

- **Java Development Kit (JDK):** Versión 17 o superior.
- **Maven:** Utilizado para la gestión de dependencias y construcción del proyecto.
- **PostgreSQL:** Motor de base de datos instalado y ejecutándose localmente.
- **pgAdmin:** Herramienta recomendada para administrar PostgreSQL y verificar los datos generados.
- **IntelliJ IDEA:** Recomendado para abrir y ejecutar el proyecto.

---

## Guía de Configuración Paso a Paso

Para ejecutar este proyecto, es necesario tener una base de datos PostgreSQL local y vincular las credenciales correspondientes en la unidad de persistencia.

### 1. Creación de la Base de Datos en PostgreSQL

1. Iniciar PostgreSQL y abrir **pgAdmin**.
2. Crear una nueva base de datos.
3. Asignarle el nombre:

```text
facturacion
```

4. Mantener el puerto de PostgreSQL utilizado por la instalación local, que normalmente es:

```text
5432
```

La aplicación se encuentra configurada para conectarse mediante:

```text
jdbc:postgresql://localhost:5432/facturacion
```

---

### 2. Configuración de las Credenciales de Conexión

La aplicación utiliza las credenciales configuradas en la instalación local de PostgreSQL.

Los valores utilizados por defecto en el proyecto son:

- **Host:** `localhost`
- **Port:** `5432`
- **Database name:** `facturacion`
- **User:** `postgres`
- **Password:** La contraseña configurada en la instalación local de PostgreSQL.

Si las credenciales de PostgreSQL son diferentes, deberán modificarse en el archivo `persistence.xml`.

---

### 3. Configuración de la Unidad de Persistencia (`persistence.xml`)

Por motivos de seguridad, los datos de acceso confidenciales no deben incluirse en el repositorio.

El archivo de configuración se encuentra ubicado en:

```text
src/main/resources/META-INF/persistence.xml
```

Localizar las propiedades de conexión JDBC y reemplazar los valores correspondientes con las credenciales de PostgreSQL local:

```xml
<!-- Driver JDBC de PostgreSQL -->
<property name="jakarta.persistence.jdbc.driver"
          value="org.postgresql.Driver"/>

<!-- URL de conexión a PostgreSQL -->
<property name="jakarta.persistence.jdbc.url"
          value="jdbc:postgresql://localhost:5432/facturacion"/>

<!-- Usuario de PostgreSQL -->
<property name="jakarta.persistence.jdbc.user"
          value="postgres"/>

<!-- Contraseña de PostgreSQL -->
<property name="jakarta.persistence.jdbc.password"
          value="TU_PASSWORD_AQUI"/>
```

La unidad de persistencia utilizada por el proyecto es:

```xml
<persistence-unit name="FacturacionPU"
                  transaction-type="RESOURCE_LOCAL">
```

Además, Hibernate se encuentra configurado para mostrar las consultas SQL generadas y actualizar el esquema de la base de datos:

```xml
<property name="hibernate.show_sql"
          value="true"/>

<property name="hibernate.format_sql"
          value="true"/>

<property name="hibernate.hbm2ddl.auto"
          value="update"/>
```

La propiedad:

```text
hibernate.hbm2ddl.auto = update
```

permite que Hibernate actualice el esquema de la base de datos de acuerdo con las entidades JPA sin eliminar los datos existentes.

---

## Ejecución del Proyecto

Una vez configuradas las credenciales de PostgreSQL, abrir el proyecto desde **IntelliJ IDEA**.

La clase principal del proyecto se encuentra en:

```text
src/main/java/org/example/Main.java
```
Luego, el proyecto puede ejecutarse desde IntelliJ IDEA utilizando la clase `Main`.

---

## Comportamiento esperado durante la ejecución

**Generación automática del esquema:** Hibernate inspeccionará las clases anotadas con `@Entity` y actualizará automáticamente el esquema de PostgreSQL de acuerdo con las entidades y relaciones definidas.

**Carga de datos maestros:** Se crearán los registros requeridos para usuarios de auditoría, puntos de venta, artículos, rubros, marcas y listas de precios.

**Creación de la factura:** Se instanciará una cabecera `FacturaVenta` vinculada con un objeto `FacturaVentaDetalle`.

**Persistencia en Cascada:** El método `Main` agrega el detalle a la colección de detalles de la factura mediante:

```java
facturaVenta.addDetalle(detalle);
```

Posteriormente se realiza un único llamado:

```java
em.persist(facturaVenta);
```

No se realiza un llamado directo a:

```java
em.persist(detalle);
```

De esta manera, la persistencia del `FacturaVentaDetalle` se realiza mediante la configuración de cascada de la relación entre `FacturaVenta` y `FacturaVentaDetalle`, demostrando el funcionamiento de `CascadeType.ALL`.

**Confirmación de la transacción:** Una vez realizadas las operaciones, se confirma la transacción mediante:

```java
em.getTransaction().commit();
```

Los registros generados pueden ser verificados directamente desde **pgAdmin**, consultando las tablas correspondientes de la base de datos `facturacion`.

Por ejemplo:

```sql
SELECT *
FROM factura_venta;
```

Y:

```sql
SELECT *
FROM factura_venta_detalle;
```

También se puede verificar la relación entre la factura y sus detalles mediante:

```sql
SELECT
    fv.id AS factura_id,
    fvd.id AS detalle_id,
    fvd.descripcion,
    fvd.cantidad,
    fvd.precio_unitario
FROM factura_venta fv
JOIN factura_venta_detalle fvd
    ON fvd.factura_id = fv.id;
```

---

## Consideraciones

Al ejecutar varias veces la clase `Main`, los objetos creados mediante `em.persist()` pueden volver a insertarse en la base de datos, generando nuevos registros.

Esto sucede porque cada ejecución del programa crea nuevas instancias de las entidades y posteriormente las persiste.

Para este trabajo práctico esto no representa un inconveniente, ya que la finalidad es demostrar el funcionamiento de JPA, Hibernate, las relaciones entre entidades y la persistencia en cascada.

En una aplicación de mayor tamaño o en un entorno productivo, la carga de datos iniciales debería manejarse mediante mecanismos específicos que permitan evitar duplicados, como validaciones, restricciones `UNIQUE`, consultas previas o procesos de inicialización independientes.

---

## Estructura del Proyecto

La estructura principal del proyecto es:

```text
TP-ORM-JPA-FacturaArca/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── org/
│       │       └── example/
│       │           ├── Main.java
│       │           │
│       │           └── entidad/
│       │               ├── Articulo.java
│       │               ├── AuditoriaApp.java
│       │               ├── Cliente.java
│       │               ├── CondicionIva.java
│       │               ├── Contacto.java
│       │               ├── Domicilio.java
│       │               ├── EntityId.java
│       │               ├── FacturaVenta.java
│       │               ├── FacturaVentaDetalle.java
│       │               ├── ListaPrecio.java
│       │               ├── ListaPrecioArticulo.java
│       │               ├── Marca.java
│       │               ├── PuntoVenta.java
│       │               ├── Rubro.java
│       │               ├── TipoMoneda.java
│       │               └── Usuario.java
│       │
│       └── resources/
│           └── META-INF/
│               └── persistence.xml
│
├── .gitignore
├── pom.xml
└── README.md
```

---

## Conclusión

El proyecto permite demostrar el funcionamiento de **JPA y Hibernate** para realizar un mapeo objeto-relacional sobre una base de datos PostgreSQL.

Se implementaron entidades y relaciones entre clases, utilizando `EntityManager`, transacciones y persistencia en cascada.

El requisito principal relacionado con la factura consiste en crear una instancia de `FacturaVenta`, asociarle uno o más objetos `FacturaVentaDetalle` y realizar un único llamado a:

```java
em.persist(facturaVenta);
```

De esta manera, Hibernate administra la persistencia de los objetos relacionados mediante la configuración de las relaciones JPA y `CascadeType.ALL`.

Finalmente, los resultados de la ejecución pueden ser inspeccionados directamente en PostgreSQL utilizando pgAdmin.
