# InventoryApp - Sistema de Gestión de Productos e Inventarios

Este proyecto es una aplicación de microservicios desarrollada con Spring Boot para la gestión de productos e inventarios. Está estructurado como un proyecto Maven multi-módulo que contiene dos microservicios independientes.

## 📋 Descripción General

La aplicación está compuesta por dos microservicios principales:
- **Product Service**: Gestión de productos
- **Inventory Service**: Gestión de inventarios

## 🏗️ Arquitectura del Proyecto

```
inventoryapp/
├── productservice/          # Microservicio de gestión de productos
├── inventoryservice/        # Microservicio de gestión de inventarios
└── pom.xml                 # POM padre del proyecto multi-módulo
```

## 🚀 Tecnologías Utilizadas

- **Java 24**
- **Spring Boot 3.5.4**
- **Spring Data JPA**
- **Spring Web**
- **Base de datos H2** (en memoria)
- **Maven** para gestión de dependencias

## 📦 Microservicios

### 1. Product Service (Puerto 8080)

Microservicio encargado de la gestión completa de productos.

#### Endpoints Disponibles:

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/products` | Crear un nuevo producto |
| GET | `/api/products` | Obtener todos los productos |
| GET | `/api/products/{id}` | Obtener un producto por ID |
| PUT | `/api/products/{id}` | Actualizar un producto |
| DELETE | `/api/products/{id}` | Eliminar un producto |

#### Modelo de Datos - Product:
```json
{
  "id": "Long (auto-generado)",
  "name": "String",
  "description": "String", 
  "price": "BigDecimal",
  "sku": "String",
  "createdAt": "LocalDateTime",
  "updatedAt": "LocalDateTime"
}
```

#### DTOs:
- **CreateProductDTO**: Para crear productos (name, description, price, sku)
- **UpdateProductDTO**: Para actualizar productos (name, description, price, sku)

### 2. Inventory Service (Puerto 8001)

Microservicio encargado de la gestión de inventarios de productos.

#### Endpoints Disponibles:

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/inventory` | Crear registro de inventario para un producto |
| GET | `/api/inventory` | Obtener todo el inventario |
| GET | `/api/inventory/{id}` | Obtener inventario por ID de producto |
| POST | `/api/inventory/increase/{id}` | Incrementar cantidad en inventario |
| POST | `/api/inventory/decrease/{id}` | Decrementar cantidad en inventario |
| DELETE | `/api/inventory/{id}` | Eliminar registro de inventario |

#### Modelo de Datos - Inventory:
```json
{
  "id": "Long (auto-generado)",
  "productId": "Long",
  "quantity": "int",
  "lastUpdate": "LocalDateTime"
}
```

#### DTOs:
- **CreateInventoryDTO**: Para crear inventario (productId, quantity)
- **QuantityDTO**: Para operaciones de incremento/decremento (quantity)

## 🚀 Cómo Ejecutar el Proyecto

### Prerrequisitos
- Java 24 o superior
- Maven 3.6 o superior

### Opción 1: Ejecutar desde la raíz del proyecto
```bash
# Compilar todo el proyecto
mvn clean compile

# Ejecutar el Product Service
cd productservice
mvn spring-boot:run

# En otra terminal, ejecutar el Inventory Service
cd inventoryservice
mvn spring-boot:run
```

### Opción 2: Ejecutar cada microservicio individualmente
```bash
# Product Service
cd productservice
mvn spring-boot:run

# Inventory Service  
cd inventoryservice
mvn spring-boot:run
```

## 📝 Ejemplos de Uso

### Crear un Producto
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop Dell",
    "description": "Laptop Dell XPS 13",
    "price": 1299.99,
    "sku": "DELL-XPS-13"
  }'
```

### Obtener todos los Productos
```bash
curl http://localhost:8080/api/products
```

### Crear Inventario para un Producto
```bash
curl -X POST http://localhost:8001/api/inventory \
  -H "Content-Type: application/json" \
  -d '{
    "productId": 1,
    "quantity": 50
  }'
```

### Incrementar Inventario
```bash
curl -X POST http://localhost:8001/api/inventory/increase/1 \
  -H "Content-Type: application/json" \
  -d '{"quantity": 10}'
```

### Decrementar Inventario
```bash
curl -X POST http://localhost:8001/api/inventory/decrease/1 \
  -H "Content-Type: application/json" \
  -d '{"quantity": 5}'
```

## 🛢️ Base de Datos

Ambos microservicios utilizan base de datos H2 en memoria. Las bases de datos se crean automáticamente al iniciar cada servicio y se pierden al detenerlos.

### Acceso a Consola H2:
- **Product Service**: http://localhost:8080/h2-console
- **Inventory Service**: http://localhost:8001/h2-console

## 📁 Estructura del Código

Cada microservicio sigue la arquitectura por capas estándar de Spring Boot:

```
src/main/java/com/vimofama/{service}/
├── controller/     # Controladores REST
├── dto/           # Data Transfer Objects
├── model/         # Entidades JPA
├── repository/    # Repositorios de datos
└── service/       # Lógica de negocio
```

## 🔧 Configuración

### Product Service
- Puerto: 8080
- Nombre: productservice

### Inventory Service  
- Puerto: 8001
- Nombre: inventoryservice


## 👥 Autor

- **Vimofama** - Desarrollo inicial

---

**Nota**: Este es un proyecto de demostración con base de datos en memoria. Para uso en producción, se recomienda configurar bases de datos persistentes y agregar medidas de seguridad adicionales.
