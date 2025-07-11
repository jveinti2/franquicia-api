# Franquicia API

API REST para gestión de franquicias, sucursales y productos con Spring Boot y DynamoDB.

## Instalación Rápida

```bash
git clone https://github.com/usuario/franquicia-api.git
cd franquicia-api
mvn clean install
mvn spring-boot:run
```

## Configuración

### Base de Datos Local (DynamoDB Local)
```bash
# Instalar DynamoDB Local
wget https://s3.us-west-2.amazonaws.com/dynamodb-local/dynamodb_local_latest.tar.gz
tar -xzf dynamodb_local_latest.tar.gz
java -Djava.library.path=./DynamoDBLocal_lib -jar DynamoDBLocal.jar -sharedDb

# Crear tabla
aws dynamodb create-table --cli-input-json file://create-table.json --endpoint-url http://localhost:8000
```

## Uso

### Endpoints Principales

**Franquicias**
```bash
# Crear franquicia
POST /api/franquicias
{
  "nombre": "McDonald's"
}

# Obtener franquicias
GET /api/franquicias

# Actualizar nombre
PUT /api/franquicias/{id}
{
  "nombre": "Nuevo Nombre"
}
```

**Sucursales**
```bash
# Crear sucursal
POST /api/franquicias/{franquiciaId}/sucursales
{
  "nombre": "Sucursal Centro"
}

# Obtener sucursales
GET /api/franquicias/{franquiciaId}/sucursales
```

**Productos**
```bash
# Crear producto
POST /api/franquicias/{franquiciaId}/sucursales/{sucursalId}/productos
{
  "nombre": "Big Mac",
  "stock": 50
}

# Actualizar stock
PUT /api/franquicias/{franquiciaId}/sucursales/{sucursalId}/productos/{productoId}/stock
{
  "stock": 75
}

# Eliminar producto
DELETE /api/franquicias/{franquiciaId}/sucursales/{sucursalId}/productos/{productoId}
```

**Endpoint Especial**
```bash
# Producto con mayor stock por sucursal
GET /api/productos/mayor-stock
```

### Documentación Interactiva

Accede a Swagger UI: `http://localhost:8080/api/swagger-ui.html`

## Estructura del Proyecto

```
src/main/java/com/franchise/
├── config/           # Configuraciones (DynamoDB, OpenAPI)
├── controller/       # Controladores REST
├── dto/             # DTOs de request y response
├── exception/       # Manejo de excepciones
├── model/           # Entidades DynamoDB
├── repository/      # Acceso a datos
├── service/         # Lógica de negocio
└── FranchiseApplication.java
```

## Tecnologías Usadas

- **Spring Boot 3.2.0** - Framework principal
- **Spring Web** - API REST
- **Spring Validation** - Validación de datos
- **AWS SDK DynamoDB** - Base de datos NoSQL
- **SpringDoc OpenAPI** - Documentación automática
- **Lombok** - Reducción de código boilerplate
- **Java 17** - Lenguaje de programación

## Características

### Funcionalidades Implementadas

- CRUD completo de franquicias
- CRUD completo de sucursales por franquicia
- CRUD completo de productos por sucursal
- Actualización de stock de productos
- Endpoint para consultar producto con mayor stock por sucursal
- Actualización de nombres de franquicias y sucursales
- Validaciones de negocio
- Documentación automática con Swagger

### Modelo de Datos DynamoDB

- **Partition Key (PK)**: FRANQUICIA#{id}
- **Sort Key (SK)**: FRANQUICIA#{id} | SUCURSAL#{id} | SUCURSAL#{id}#PRODUCTO#{id}
- **GSI**: SucursalIndex para consultas por sucursal

## Variables de Entorno

```properties
# Servidor
server.port=8080
server.servlet.context-path=/api

# AWS DynamoDB
aws.region=us-east-1
aws.dynamodb.endpoint=http://localhost:8000
aws.accesskey=dummy
aws.secretkey=dummy
```

## Cómo Contribuir

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit tus cambios (`git commit -m 'Agregar nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Abre un Pull Request

## Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para detalles.

## Autor

Desarrollado como prueba técnica para demostrar habilidades en Spring Boot, DynamoDB y APIs REST.
