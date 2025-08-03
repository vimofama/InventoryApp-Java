package com.vimofama.productservice.controller;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.vimofama.productservice.dto.CreateProductDTO;
import com.vimofama.productservice.dto.UpdateProductDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.net.URI;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductControllerTests {

    @Autowired
    TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    void shouldCreateProductSuccessfully() {
        CreateProductDTO createProductDTO = new CreateProductDTO(
                "Laptop Dell",
                "Laptop Dell XPS 13",
                new BigDecimal("899.99"),
                "DELL-XPS-13"
        );

        ResponseEntity<String> response = restTemplate.postForEntity("/api/products", createProductDTO, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        URI location = response.getHeaders().getLocation();
        ResponseEntity<String> getResponse = restTemplate.getForEntity(location, String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
        Number id = documentContext.read("$.id");
        String name = documentContext.read("$.name");
        String description = documentContext.read("$.description");
        BigDecimal price = new BigDecimal(documentContext.read("$.price").toString());
        String sku = documentContext.read("$.sku");

        assertThat(id).isNotNull();
        assertThat(name).isEqualTo("Laptop Dell");
        assertThat(description).isEqualTo("Laptop Dell XPS 13");
        assertThat(price).isEqualTo(new BigDecimal("899.99"));
        assertThat(sku).isEqualTo("DELL-XPS-13");
    }

    @Test
    void shouldReturnValidationErrorWhenCreateProductWithInvalidData() {
        CreateProductDTO invalidProduct = new CreateProductDTO(
                "", // nombre vacío
                "ab", // descripción muy corta
                new BigDecimal("-10"), // precio negativo
                ""
        );

        ResponseEntity<String> response = restTemplate.postForEntity("/api/products", invalidProduct, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldGetAllProducts() {
        // Crear algunos productos primero
        CreateProductDTO product1 = new CreateProductDTO("Product 1", "Description 1", new BigDecimal("10.00"), "SKU1");
        CreateProductDTO product2 = new CreateProductDTO("Product 2", "Description 2", new BigDecimal("20.00"), "SKU2");

        restTemplate.postForEntity("/api/products", product1, String.class);
        restTemplate.postForEntity("/api/products", product2, String.class);

        ResponseEntity<String> response = restTemplate.getForEntity("/api/products", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        int productCount = documentContext.read("$.length()");
        assertThat(productCount).isEqualTo(2);

        String firstProductName = documentContext.read("$[0].name");
        String secondProductName = documentContext.read("$[1].name");
        assertThat(firstProductName).isEqualTo("Product 1");
        assertThat(secondProductName).isEqualTo("Product 2");
    }

    @Test
    void shouldGetProductById() {
        CreateProductDTO createProductDTO = new CreateProductDTO(
                "Test Product",
                "Test Description",
                new BigDecimal("15.99"),
                "TEST-SKU"
        );

        ResponseEntity<String> createResponse = restTemplate.postForEntity("/api/products", createProductDTO, String.class);
        URI location = createResponse.getHeaders().getLocation();

        ResponseEntity<String> response = restTemplate.getForEntity(location, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        String name = documentContext.read("$.name");
        assertThat(name).isEqualTo("Test Product");
    }

    @Test
    void shouldReturnNotFoundWhenGetProductByInvalidId() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/products/999", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldUpdateProductSuccessfully() {
        // Crear producto primero
        CreateProductDTO createProductDTO = new CreateProductDTO(
                "Original Product",
                "Original Description",
                new BigDecimal("99.99"),
                "ORIG-SKU"
        );

        ResponseEntity<String> createResponse = restTemplate.postForEntity("/api/products", createProductDTO, String.class);
        URI location = createResponse.getHeaders().getLocation();
        Assertions.assertNotNull(location);
        String productId = location.getPath().split("/")[3];

        // Actualizar producto
        UpdateProductDTO updateProductDTO = new UpdateProductDTO(
                "Updated Product",
                "Updated Description",
                null,
                "UPD-SKU"
        );

        HttpEntity<UpdateProductDTO> requestEntity = new HttpEntity<>(updateProductDTO);
        ResponseEntity<String> updateResponse = restTemplate.exchange(
                "/api/products/" + productId,
                HttpMethod.PUT,
                requestEntity,
                String.class
        );

        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(updateResponse.getBody());
        String updatedName = documentContext.read("$.name");
        String updatedDescription = documentContext.read("$.description");
        BigDecimal updatedPrice = new BigDecimal(documentContext.read("$.price").toString());
        String updatedSku = documentContext.read("$.sku");

        assertThat(updatedName).isEqualTo("Updated Product");
        assertThat(updatedDescription).isEqualTo("Updated Description");
        assertThat(updatedPrice).isEqualTo(new BigDecimal("99.99"));
        assertThat(updatedSku).isEqualTo("UPD-SKU");
    }

    @Test
    void shouldReturnNotFoundWhenUpdateNonExistentProduct() {
        UpdateProductDTO updateProductDTO = new UpdateProductDTO(
                "Non Existent",
                "Description",
                new BigDecimal("100.00"),
                "SKU"
        );

        HttpEntity<UpdateProductDTO> requestEntity = new HttpEntity<>(updateProductDTO);
        ResponseEntity<String> response = restTemplate.exchange(
                "/api/products/999",
                HttpMethod.PUT,
                requestEntity,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldDeleteProductSuccessfully() {
        // Crear producto primero
        CreateProductDTO createProductDTO = new CreateProductDTO(
                "Product to Delete",
                "Will be deleted",
                new BigDecimal("50.00"),
                "DEL-SKU"
        );

        ResponseEntity<String> createResponse = restTemplate.postForEntity("/api/products", createProductDTO, String.class);
        URI location = createResponse.getHeaders().getLocation();
        Assertions.assertNotNull(location);
        String productId = location.getPath().split("/")[3];

        // Eliminar producto
        ResponseEntity<String> deleteResponse = restTemplate.exchange(
                "/api/products/" + productId,
                HttpMethod.DELETE,
                null,
                String.class
        );

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(deleteResponse.getBody());
        String message = documentContext.read("$.message");
        Boolean deleted = documentContext.read("$.deleted");
        assertThat(message).isEqualTo("Producto eliminado exitosamente");
        assertThat(deleted).isTrue();

        // Verificar que el producto ya no existe
        ResponseEntity<String> getResponse = restTemplate.getForEntity("/api/products/" + productId, String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldReturnNotFoundWhenDeleteNonExistentProduct() {
        ResponseEntity<String> response = restTemplate.exchange(
                "/api/products/999",
                HttpMethod.DELETE,
                null,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
