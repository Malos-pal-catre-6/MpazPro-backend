# mpazpro-backend

Backend REST (Spring Boot 3 + Postgres/Neon) para la app MpazPro.

## 1. Configura Neon

Edita `src/main/resources/application-local.properties` con los datos reales
de tu proyecto Neon (Dashboard -> Connection Details):

```
spring.datasource.url=jdbc:postgresql://TU_HOST_NEON/TU_BASE_DE_DATOS?sslmode=require
spring.datasource.username=TU_USUARIO_NEON
spring.datasource.password=TU_PASSWORD_NEON
```

`spring.jpa.hibernate.ddl-auto=update` ya esta configurado, asi que las
tablas (`usuarios`, `contenidos`, `preguntas`, `intentos`) se crean solas la
primera vez que levantas el backend. No necesitas escribir SQL a mano.

## 2. Correr en local

Este zip no trae el Maven Wrapper (`mvnw`). Dos formas de correrlo:

- **Mas facil**: abre la carpeta `mpazpro-backend` como proyecto Maven en
  IntelliJ IDEA (Open -> selecciona la carpeta, detecta el `pom.xml` solo) y
  ejecuta `MpazproBackendApplication.main()` con el boton de Run.
- **Por consola**: instala Maven (`choco install maven` en Windows o
  `sudo apt install maven` en Linux) y corre:
  ```bash
  mvn spring-boot:run
  ```

El backend queda escuchando en `http://localhost:8080`.

## 3. Probar rapido con curl

```bash
# Registrar un docente
curl -X POST http://localhost:8080/auth/registro -H "Content-Type: application/json" \
  -d '{"nombre":"Profe Ana","usuario":"ana","password":"123456","rol":"DOCENTE"}'

# Login
curl -X POST http://localhost:8080/auth/login -H "Content-Type: application/json" \
  -d '{"usuario":"ana","password":"123456"}'
```

El login devuelve un `token` JWT. Para los endpoints protegidos:

```bash
curl http://localhost:8080/contenidos/mios -H "Authorization: Bearer EL_TOKEN"
```

## 4. Conectar desde Android

- **Emulador**: `http://10.0.2.2:8080/`
- **Celular fisico** (misma WiFi que tu PC): `http://TU_IP_LAN:8080/`

## Endpoints

| Metodo | Ruta | Rol | Que hace |
|---|---|---|---|
| POST | /auth/registro | publico | crea usuario |
| POST | /auth/login | publico | devuelve JWT |
| POST | /contenidos | DOCENTE | crea contenido (nace EN_REVISION) |
| GET | /contenidos/mios | DOCENTE | lista su contenido |
| PATCH | /contenidos/{id}/max-intentos | DOCENTE | define intentos maximos |
| GET | /contenidos/pendientes | UTP | contenido EN_REVISION |
| PATCH | /contenidos/{id}/estado | UTP | autoriza/rechaza |
| GET | /contenidos/autorizados | ESTUDIANTE | contenido visible |
| POST | /intentos | ESTUDIANTE | registra resultado de actividad |
| GET | /intentos/mios/{contenidoId} | ESTUDIANTE | sus intentos |
| GET | /intentos/contenido/{contenidoId} | DOCENTE | intentos de su contenido |
| GET | /informe/export | DOCENTE | descarga CSV del informe pedagogico |
