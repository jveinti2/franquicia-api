# Franquicia API

API REST para gestión de franquicias, sucursales y productos con Spring Boot y DynamoDB.

## Setup Local

```bash
# Clonar proyecto
git clone https://github.com/usuario/franquicia-api.git
cd franquicia-api

# Levantar DynamoDB Local
java -Djava.library.path=./DynamoDBLocal_lib -jar DynamoDBLocal.jar -sharedDb

# Crear tabla
aws dynamodb create-table --cli-input-json file://create-table.json --endpoint-url http://localhost:8000

# Ejecutar aplicación
.\mvnw spring-boot:run
```

## Docker

```bash
# Construir imagen
docker build -t franquicia-api .

# Ejecutar contenedor
docker run -p 8080:8080 --network host franquicia-api

# O usar docker-compose (incluye DynamoDB local)
docker-compose up
```

## Infraestructura como Código

```bash
aws cloudformation deploy --template-file infrastructure/cloudformation.yaml --stack-name franquicia-data-infrastructure --parameter-overrides Environment=dev ApplicationName=franquicia-system --capabilities CAPABILITY_NAMED_IAM --region us-east-1
```

## Documentación

Swagger UI: `http://localhost:8080/api/swagger-ui.html`
