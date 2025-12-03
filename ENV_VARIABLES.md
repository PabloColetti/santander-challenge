# Variables de Entorno - Santander Challenge

Este documento describe todas las variables de entorno configurables para el proyecto.

<a id="tabla-contenidos"></a>

## Tabla de Contenidos

- [Configuración de Servicios](#configuracion-servicios)
- [Base de Datos](#base-datos)
- [JPA/Hibernate](#jpa-hibernate)
- [Uso en Desarrollo](#uso-desarrollo)
- [Uso en Producción](#uso-produccion)
- [GitHub Actions](#github-actions)
- [Notas Importantes](#notas-importantes)

<a id="configuracion-servicios" ></a>

## Configuración de Servicios

### Config Server (ms-config)

| Variable                 | Descripción                                 | Default     | Requerido |
| ------------------------ | ------------------------------------------- | ----------- | --------- |
| `CONFIG_SERVER_NAME`     | Nombre del servicio Config Server           | `ms-config` | No        |
| `CONFIG_SERVER_PORT`     | Puerto del Config Server                    | `8888`      | No        |
| `SPRING_PROFILES_ACTIVE` | Perfil activo de Spring (native, git, etc.) | `native`    | No        |

> [!NOTE] NOTA
> El Config Server debe iniciarse primero, ya que otros servicios dependen de él para obtener la configuración.

### Eureka Server (ms-eureka)

| Variable             | Descripción                | Default     | Requerido |
| -------------------- | -------------------------- | ----------- | --------- |
| `EUREKA_SERVER_NAME` | Nombre del servicio Eureka | `ms-eureka` | No        |
| `EUREKA_SERVER_PORT` | Puerto del Eureka Server   | `8761`      | No        |

> [!TIP] TIPS
> El puerto 8761 es el estándar para Eureka. Solo cambiarlo si hay conflictos de puertos.

### API Gateway (api-consumer)

| Variable            | Descripción            | Default | Requerido |
| ------------------- | ---------------------- | ------- | --------- |
| `API_CONSUMER_PORT` | Puerto del API Gateway | `8080`  | No        |

### Microservicios

| Variable                            | Descripción                                    | Default       | Requerido |
| ----------------------------------- | ---------------------------------------------- | ------------- | --------- |
| `BANKS_SERVER_PORT`                 | Puerto del microservicio ms-banks              | `8090`        | No        |
| `ACCOUNTS_SERVER_PORT`              | Puerto del microservicio ms-accounts           | `9090`        | No        |
| `EUREKA_BANKS_INSTANCE_HOSTNAME`    | Hostname de la instancia ms-banks en Eureka    | `ms-banks`    | No        |
| `EUREKA_ACCOUNTS_INSTANCE_HOSTNAME` | Hostname de la instancia ms-accounts en Eureka | `ms-accounts` | No        |

<a id="base-datos"></a>

## Base de Datos

### PostgreSQL - Banks Database

| Variable                       | Descripción                             | Default          | Requerido           |
| ------------------------------ | --------------------------------------- | ---------------- | ------------------- |
| `POSTGRES_BANKS_DB`            | Nombre de la base de datos              | `ms_banks_db`    | No                  |
| `POSTGRES_BANKS_USER`          | Usuario de la base de datos             | `root`           | **Sí (Producción)** |
| `POSTGRES_BANKS_PASSWORD`      | Contraseña de la base de datos          | `root`           | **Sí (Producción)** |
| `POSTGRES_BANKS_HOST`          | Host de la base de datos                | `postgres-banks` | No                  |
| `POSTGRES_BANKS_PORT`          | Puerto interno de PostgreSQL            | `5432`           | No                  |
| `POSTGRES_BANKS_EXTERNAL_PORT` | Puerto externo para conexión desde host | `5433`           | No                  |

> [!IMPORTANT] IMPORTANTE
> En producción, siempre configurar las credenciales de la base de datos con valores seguros. El usuario y contraseña por defecto (`root`/`root`) son solo para desarrollo.

### PostgreSQL - Accounts Database

| Variable                          | Descripción                             | Default             | Requerido           |
| --------------------------------- | --------------------------------------- | ------------------- | ------------------- |
| `POSTGRES_ACCOUNTS_DB`            | Nombre de la base de datos              | `ms_accounts_db`    | No                  |
| `POSTGRES_ACCOUNTS_USER`          | Usuario de la base de datos             | `root`              | **Sí (Producción)** |
| `POSTGRES_ACCOUNTS_PASSWORD`      | Contraseña de la base de datos          | `root`              | **Sí (Producción)** |
| `POSTGRES_ACCOUNTS_HOST`          | Host de la base de datos                | `postgres-accounts` | No                  |
| `POSTGRES_ACCOUNTS_PORT`          | Puerto interno de PostgreSQL            | `5432`              | No                  |
| `POSTGRES_ACCOUNTS_EXTERNAL_PORT` | Puerto externo para conexión desde host | `5434`              | No                  |

> [!TIP] TIPS
> Los puertos externos (`*_EXTERNAL_PORT`) son solo para conexión desde el host, La comunicación interna entre contenedores usa los puertos internos.

<a id="conexion-dbeaver"></a>
**Conexión con DBeaver:**

- **Banks DB:** `localhost:5433` (usuario: `root`, password: `root`, database: `ms_banks_db`)
- **Accounts DB:** `localhost:5434` (usuario: `root`, password: `root`, database: `ms_accounts_db`)

<a id="jpa-hibernate"></a>

## JPA/Hibernate

| Variable                        | Descripción                       | Valores Posibles                       | Default  | Requerido |
| ------------------------------- | --------------------------------- | -------------------------------------- | -------- | --------- |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | Estrategia de gestión del esquema | `create`, `update`, `validate`, `none` | `create` | No        |

**Valores explicados:**

- **`create`**: Crea las tablas al iniciar. Solo para desarrollo, borra datos existentes.
- **`update`**: Actualiza el esquema si hay cambios.

> [!CAUTION] PRECAUCIÓN
> Usar `update` con precaución, ya que puede causar cambios inesperados en el esquema de la base de datos.

- **`validate`**: Valida que el esquema coincida con las entidades, recomendado para producción.
- **`none`**: No hace nada con el esquema, recomendado para producción.

> [!WARNING] ADVERTENCIA
> En producción, usa `validate` o `none`. Nunca uses `create` en producción, ya que eliminará todos los datos existentes.

<a id="uso-desarrollo"></a>

## Uso en Desarrollo

### Archivo `.env`

1. Copiar el contenido del archivo `.env.example` a `.env`
2. Editar `.env` con los valores locales (opcional, los defaults funcionan)
3. Docker Compose leerá automáticamente el archivo `.env`

> [!NOTE] NOTA
> El archivo `.env` está en `.gitignore` y no se commitea al repositorio, usar `.env.example` como plantilla.

<a id="uso-produccion"></a>

## Uso en Producción

### Variables Críticas para Producción

> [!IMPORTANT] IMPORTANTE
> En producción, SIEMPRE configurar las siguientes variables:

1. **Credenciales de Base de Datos:**

   - `POSTGRES_BANKS_USER`
   - `POSTGRES_BANKS_PASSWORD`
   - `POSTGRES_ACCOUNTS_USER`
   - `POSTGRES_ACCOUNTS_PASSWORD`

2. **JPA DDL Auto:**

   - `SPRING_JPA_HIBERNATE_DDL_AUTO=validate` (o `none`)

3. **Puertos:** Ajustar según la infraestructura

### Ejemplo de `.env` para Producción

```text
# Producción - Valores seguros

POSTGRES_BANKS_USER=prod_user
POSTGRES_BANKS_PASSWORD=SecurePassword123!
POSTGRES_ACCOUNTS_USER=prod_user
POSTGRES_ACCOUNTS_PASSWORD=SecurePassword123!
SPRING_JPA_HIBERNATE_DDL_AUTO=validate
```

<a id="github-actions"></a>

## GitHub Actions

Para usar en GitHub Actions, configura los secrets en:
**Settings → Secrets and variables → Actions**

<a id="notas-importantes"></a>

## Notas Importantes

1. **Nunca commitear el archivo `.env`** con credenciales reales
2. El archivo `.env.example` está en el repositorio como plantilla
3. Los valores por defecto funcionan para desarrollo local
4. En producción, usar secretos gestionados (GitHub Secrets, AWS Secrets Manager, etc.)
5. Los puertos externos de PostgreSQL (`*_EXTERNAL_PORT`) son solo para conexión desde el host, no afectan la comunicación interna entre contenedores

## Enlaces Útiles

- [Docker Compose Environment Variables](https://docs.docker.com/compose/environment-variables/)
- [Spring Boot Externalized Configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config)
- [GitHub Actions Secrets](https://docs.github.com/en/actions/security-guides/encrypted-secrets)
