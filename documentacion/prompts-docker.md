# Prompts de Docker
- Prompts para configurar Docker Compose
- Prompts para resolver problemas de containerización
- Prompts para optimizar la configuración
- Aprendizajes sobre DevOps y containerización

---

## Prompt 1: [Comandos Docker]

### Prompt Utilizado:
```
Podes explicarme para que son y como funcionan estos comandos?

🐳 Instrucciones para Docker
Levantar Bases de Datos
# Levantar MySQL y PostgreSQL
docker compose up -d

# Verificar que los contenedores estén corriendo
docker compose ps

# Ver logs de los contenedores
docker compose logs -f
Ejecutar Aplicación con Diferentes Profiles
# Con H2 (desarrollo)
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# Con MySQL
./mvnw spring-boot:run -Dspring-boot.run.profiles=mysql

# Con PostgreSQL
./mvnw spring-boot:run -Dspring-boot.run.profiles=postgres
Detener Bases de Datos
# Detener contenedores
docker compose down

# Detener y eliminar volúmenes
docker compose down -v
```

### Respuesta Recibida:
```
Claro, te explico cada parte paso a paso y con ejemplos simples para que entiendas  estas instrucciones con Docker y Spring Boot. 🧠🐳

🧩 ¿Cuál es la idea general?
Tu proyecto usa Spring Boot y puede funcionar con distintas bases de datos:
- H2 (en memoria, para desarrollo),
- MySQL (común en producción o testing),
- PostgreSQL (otra opción popular).

Y para no tener que instalar estas bases en tu máquina, tu profesor te propone usar Docker para levantar los contenedores de base de datos y cambiar el entorno usando los perfiles de Spring Boot (dev, mysql, postgres).

🐳 PARTE 1 – Levantar las bases de datos con Docker
docker compose up -d

- Levanta los contenedores definidos en el archivo docker-compose.yml.
- En este caso, seguramente se levantan MySQL y PostgreSQL como servicios.
- "-d" significa detached, o sea que corre en segundo plano.

✅ Verificar que están corriendo:
docker compose ps

- Te muestra una tabla con los contenedores que están funcionando, por ejemplo:
| NAME                | STATUS  | PORTS        |
| ------------------- | ------- | ------------ |
| empleados\_mysql    | running | 0.0.0.0:3306 |
| empleados\_postgres | running | 0.0.0.0:5432 |

📜 Ver logs (opcional para debug)
docker compose logs -f

- Muestra los mensajes que están saliendo desde los contenedores.
- "-f" hace que los veas "en vivo" como si fueran logs en consola.

🚀 PARTE 2 – Correr tu aplicación con diferentes perfiles
Cuando ejecutás tu aplicación con:
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

Le estás diciendo a Spring Boot:
👉 “Usá el perfil llamado dev”

Y Spring busca el archivo de configuración:
application-dev.properties

Lo mismo con estos:
| Comando                               | Usa archivo                       |
| ------------------------------------- | --------------------------------- |
| `-Dspring-boot.run.profiles=mysql`    | `application-mysql.properties`    |
| `-Dspring-boot.run.profiles=postgres` | `application-postgres.properties` |

🛑 PARTE 3 – Detener los contenedores de base de datos
docker compose down
- Detiene y elimina los contenedores 

docker compose down -v
- También elimina los volúmenes de datos (¡cuidado! borra la info de las bases).

🧪 ¿Para qué te sirve todo esto?
Te permite:
1. Probar tu app con distintas bases sin tener que instalarlas manualmente.
2. Tener un entorno de desarrollo más ordenado y reproducible.
3. Cambiar de configuración fácilmente usando los perfiles de Spring Boot.
```

### Modificaciones Realizadas:
- Ninguna modificación al código: La respuesta proporcionada por la IA fue completa y precisa


### Explicación del Prompt:
El prompt fue efectivo porque:
- Directo y específico: Solicitó explicación de comandos concretos sin ambigüedades
- Alcance claro: Pidió tanto el "para qué" como el "cómo funcionan", cubriendo propósito y mecanismo

### Aprendizajes Obtenidos:
- Arquitectura multi-perfil: Comprendí cómo Spring Boot maneja diferentes entornos (dev, mysql, postgres) mediante perfiles de configuración
- Docker Compose workflow: Entendí el flujo completo: up -d → ps → logs -f → down/down -v
Separación de responsabilidades: Docker maneja la infraestructura (bases de datos) mientras Spring Boot maneja la lógica de aplicación
- Gestión de datos: Diferencia crítica entre docker compose down (preserva datos) y docker compose down -v (elimina datos)
- Eficiencia en desarrollo: No necesidad de instalar múltiples bases de datos localmente, usar contenedores según necesidad

---

## Prompt n: [Descripción breve]

### Prompt Utilizado:
```
[Prompt completo aquí]
```

### Respuesta Recibida:
```
[Respuesta completa aquí]
```

### Modificaciones Realizadas:
- [Lista de cambios realizados al código generado]
- [Explicación de por qué se modificó]

### Explicación del Prompt:
[Explicar por qué se usó este prompt específico]

### Aprendizajes Obtenidos:
- [Lista de aprendizajes]
- [Conceptos nuevos comprendidos]
- [Buenas prácticas identificadas]

---
