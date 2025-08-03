# Inventory Management API

Esta API de gestión de inventarios está desarrollada con Spring Boot y contiene dos microservicios independientes: uno para la gestión de productos y otro para la gestión de inventarios.

## Estructura del Proyecto

El proyecto utiliza Maven como herramienta de construcción y está organizado como un proyecto multi-módulo que contiene:

- **Product Service** (Puerto 8080): Gestión de productos
- **Inventory Service** (Puerto 8001): Gestión de inventarios

## Tecnologías Utilizadas

- Java 24
- Spring Boot 3.5.4
- Spring Data JPA
- H2 Database (Base de datos en memoria)
- Maven (Multi-módulo)
- Jakarta Validation
- JUnit 5 & AssertJ (Testing)

## Requisitos

- Java 24 o superior
- Maven 3.6+ (opcional, se incluye wrapper)

## Instalación y Ejecución

#### Compilar el proyecto completo
```bash
./mvnw clean compile
```

#### Ejecutar Product Service (Puerto 8080)
```bash
./mvnw spring-boot:run -pl productservice
```

#### Ejecutar Inventory Service (Puerto 8001)
```bash
./mvnw spring-boot:run -pl inventoryservice
```

### Opción 3: Ejecutar Tests

```bash
# Ejecutar tests de un servicio específico
./mvnw test -pl productservice
```

## Testing

El proyecto incluye pruebas completas de integración para ambos controladores:

### ProductControllerTests
- ✅ Creación exitosa de productos
- ✅ Validación de datos de entrada
- ✅ Obtener todos los productos
- ✅ Obtener producto por ID
- ✅ Actualización de productos
- ✅ Eliminación de productos
- ✅ Manejo de productos no encontrados

Las pruebas utilizan `@SpringBootTest` con `TestRestTemplate` para probar los endpoints de forma integral.

## Manejo de Excepciones

Ambos microservicios incluyen un `GlobalExceptionHandler` que maneja automáticamente:

- **Validaciones de entrada**: Errores 400 con mensajes descriptivos
- **Recursos no encontrados**: Errores 404 personalizados
- **Errores de formato**: Manejo especial para valores como precio con formato incorrecto
- **Errores internos**: Respuestas consistentes para errores del servidor

Los manejadores de excepciones se activan automáticamente mediante anotaciones de Spring Boot sin necesidad de configuración manual.

## Product Service API

Base URL: `http://localhost:8080/api/products`

### Endpoints Disponibles

#### 1. Crear Producto
- **POST** `/api/products`
- **Descripción**: Crea un nuevo producto
- **Request Body**:
```json
{
    "name": "Laptop Dell",
    "description": "Laptop Dell Inspiron 15 con 8GB RAM",
    "price": 899.99,
    "sku": "DELL-INS-15-001"
}
```
- **Response** (201 Created):
```json
{
    "id": 1,
    "name": "Laptop Dell",
    "description": "Laptop Dell Inspiron 15 con 8GB RAM",
    "price": 899.99,
    "sku": "DELL-INS-15-001",
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
}
```

#### 2. Obtener Todos los Productos
- **GET** `/api/products`
- **Descripción**: Retorna la lista de todos los productos
- **Response** (200 OK):
```json
[
    {
        "id": 1,
        "name": "Laptop Dell",
        "description": "Laptop Dell Inspiron 15 con 8GB RAM",
        "price": 899.99,
        "sku": "DELL-INS-15-001",
        "createdAt": "2024-01-15T10:30:00",
        "updatedAt": "2024-01-15T10:30:00"
    }
]
```

#### 3. Obtener Producto por ID
- **GET** `/api/products/{id}`
- **Descripción**: Retorna un producto específico por su ID
- **Response** (200 OK):
```json
{
    "id": 1,
    "name": "Laptop Dell",
    "description": "Laptop Dell Inspiron 15 con 8GB RAM",
    "price": 899.99,
    "sku": "DELL-INS-15-001",
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
}
```

#### 4. Actualizar Producto
- **PUT** `/api/products/{id}`
- **Descripción**: Actualiza un producto existente (campos opcionales)
- **Request Body**:
```json
{
    "name": "Laptop Dell Actualizada",
    "description": "Laptop Dell Inspiron 15 con 16GB RAM",
    "price": 999.99,
    "sku": "DELL-INS-15-002"
}
```
- **Response** (200 OK):
```json
{
    "id": 1,
    "name": "Laptop Dell Actualizada",
    "description": "Laptop Dell Inspiron 15 con 16GB RAM",
    "price": 999.99,
    "sku": "DELL-INS-15-002",
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T11:45:00"
}
```

#### 5. Eliminar Producto
- **DELETE** `/api/products/{id}`
- **Descripción**: Elimina un producto
- **Response** (200 OK):
```json
{
    "message": "Producto eliminado exitosamente",
    "deleted": true,
    "product": {
        "id": 1,
        "name": "Laptop Dell",
        "description": "Laptop Dell Inspiron 15 con 8GB RAM",
        "price": 899.99,
        "sku": "DELL-INS-15-001",
        "createdAt": "2024-01-15T10:30:00",
        "updatedAt": "2024-01-15T10:30:00"
    }
}
```

### Validaciones del Product Service

Para el endpoint de creación de productos, se validan los siguientes campos:
- `name`: Requerido, no puede ser null, no puede estar vacío, mínimo 3 caracteres
- `description`: Requerido, no puede ser null, no puede estar vacío, mínimo 3 caracteres
- `price`: Requerido, no puede ser null y debe ser positivo
- `sku`: Requerido, no puede ser null, no puede estar vacío, mínimo 3 caracteres

**Ejemplos de errores de validación**:

*Error por campos vacíos:*
```json
{
    "timestamp": "2024-01-15T10:30:00",
    "status": 400,
    "error": "Validation Failed",
    "message": "Los datos enviados no son válidos",
    "errors": {
        "name": "El nombre del producto es obligatorio",
        "price": "El precio del producto es obligatorio"
    }
}
```

*Error por tamaño mínimo:*
```json
{
    "timestamp": "2024-01-15T10:30:00",
    "status": 400,
    "error": "Validation Failed",
    "message": "Los datos enviados no son válidos",
    "errors": {
        "name": "El nombre del producto debe tener al menos 3 caracteres",
        "sku": "El SKU del producto debe tener al menos 3 caracteres"
    }
}
```

*Error por precio inválido:*
```json
{
    "timestamp": "2024-01-15T10:30:00",
    "status": 400,
    "error": "Validation Failed",
    "message": "Los datos enviados no son válidos",
    "errors": {
        "price": "El precio del producto debe ser mayor a 0"
    }
}
```

*Error por formato de datos incorrecto (ej: precio con coma):*
```json
{
    "timestamp": "2024-01-15T10:30:00",
    "status": 400,
    "error": "Bad Request",
    "message": "El formato del precio no es válido. Use punto (.) como separador decimal (ejemplo: 19.99)",
    "details": "Verifique el formato de los datos enviados"
}
```

## Inventory Service API

Base URL: `http://localhost:8001/api/inventory`

### Endpoints Disponibles

#### 1. Crear Inventario
- **POST** `/api/inventory`
- **Descripción**: Crea un nuevo registro de inventario para un producto
- **Request Body**:
```json
{
    "productId": 1,
    "quantity": 50
}
```
- **Response** (201 Created):
```json
{
    "id": 1,
    "productId": 1,
    "quantity": 50,
    "lastUpdate": "2024-01-15T10:30:00"
}
```

#### 2. Obtener Todo el Inventario
- **GET** `/api/inventory`
- **Descripción**: Retorna la lista de todos los registros de inventario
- **Response** (200 OK):
```json
[
    {
        "id": 1,
        "productId": 1,
        "quantity": 50,
        "lastUpdate": "2024-01-15T10:30:00"
    }
]
```

#### 3. Obtener Inventario por ID de Producto
- **GET** `/api/inventory/{productId}`
- **Descripción**: Retorna el inventario de un producto específico
- **Response** (200 OK):
```json
{
    "id": 1,
    "productId": 1,
    "quantity": 50,
    "lastUpdate": "2024-01-15T10:30:00"
}
```

#### 4. Incrementar Cantidad
- **POST** `/api/inventory/increase/{productId}`
- **Descripción**: Incrementa la cantidad en inventario de un producto
- **Request Body**:
```json
{
    "quantity": 10
}
```
- **Response** (200 OK):
```json
{
    "id": 1,
    "productId": 1,
    "quantity": 60,
    "lastUpdate": "2024-01-15T11:00:00"
}
```

#### 5. Decrementar Cantidad
- **POST** `/api/inventory/decrease/{productId}`
- **Descripción**: Decrementa la cantidad en inventario de un producto
- **Request Body**:
```json
{
    "quantity": 5
}
```
- **Response** (200 OK):
```json
{
    "id": 1,
    "productId": 1,
    "quantity": 55,
    "lastUpdate": "2024-01-15T11:15:00"
}
```

#### 6. Eliminar Inventario
- **DELETE** `/api/inventory/{productId}`
- **Descripción**: Elimina un registro de inventario
- **Response** (204 No Content)

### Validaciones del Inventory Service

Para todos los endpoints que requieren request body, se aplican las siguientes validaciones:

**CreateInventoryDTO**:
- `productId`: Requerido, no puede ser null
- `quantity`: Requerido, no puede ser null, debe ser mayor o igual a 0

**QuantityDTO**:
- `quantity`: Requerido, no puede ser null y debe ser positivo (mayor a 0)

**Ejemplos de errores de validación**:

*Error por cantidad negativa en incremento/decremento:*
```json
{
    "timestamp": "2024-01-15T10:30:00",
    "status": 400,
    "error": "Validation Failed",
    "message": "Los datos enviados no son válidos",
    "errors": {
        "quantity": "La cantidad debe ser un número positivo mayor a 0"
    }
}
```

*Error por campos obligatorios:*
```json
{
    "timestamp": "2024-01-15T10:30:00",
    "status": 400,
    "error": "Validation Failed",
    "message": "Los datos enviados no son válidos",
    "errors": {
        "productId": "El ID del producto es obligatorio",
        "quantity": "La cantidad inicial es obligatoria"
    }
}
```

*Error por formato de datos incorrecto (ej: cantidad como texto):*
```json
{
    "timestamp": "2024-01-15T10:30:00",
    "status": 400,
    "error": "Bad Request",
    "message": "El formato de la cantidad no es válido. Debe ser un número entero (ejemplo: 10)",
    "details": "Verifique el formato de los datos enviados"
}
```

## Códigos de Estado HTTP

- **200 OK**: Operación exitosa
- **201 Created**: Recurso creado exitosamente
- **204 No Content**: Recurso eliminado exitosamente
- **400 Bad Request**: Error en los datos enviados o validación fallida
- **404 Not Found**: Recurso no encontrado

## Ejemplos de Uso con cURL

### Product Service

```bash
# Crear producto
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop Dell",
    "description": "Laptop Dell Inspiron 15",
    "price": 899.99,
    "sku": "DELL-INS-15-001"
  }'

# Ejemplo de error - nombre muy corto
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "PC",
    "description": "PC",
    "price": -100,
    "sku": "PC"
  }'

# Obtener todos los productos
curl -X GET http://localhost:8080/api/products

# Obtener producto por ID
curl -X GET http://localhost:8080/api/products/1
```

### Inventory Service

```bash
# Crear inventario
curl -X POST http://localhost:8001/api/inventory \
  -H "Content-Type: application/json" \
  -d '{
    "productId": 1,
    "quantity": 50
  }'

# Incrementar cantidad
curl -X POST http://localhost:8001/api/inventory/increase/1 \
  -H "Content-Type: application/json" \
  -d '{
    "quantity": 10
  }'

# Ejemplo de error - cantidad negativa
curl -X POST http://localhost:8001/api/inventory/increase/1 \
  -H "Content-Type: application/json" \
  -d '{
    "quantity": -5
  }'

# Decrementar cantidad
curl -X POST http://localhost:8001/api/inventory/decrease/1 \
  -H "Content-Type: application/json" \
  -d '{
    "quantity": 5
  }'

# Obtener inventario
curl -X GET http://localhost:8001/api/inventory/1
```

## Notas Adicionales

- Ambos microservicios utilizan H2 Database en memoria, por lo que los datos se pierden al reiniciar las aplicaciones
- Los microservicios funcionan de forma independiente y pueden ejecutarse por separado
- El proyecto incluye validaciones automáticas con mensajes personalizados en español
- Todas las validaciones de strings requieren un mínimo de 3 caracteres
- Los mensajes de error son descriptivos y facilitan la depuración
- Asegúrate de que los puertos 8080 y 8001 estén disponibles antes de ejecutar los servicios
