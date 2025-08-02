package com.vimofama.productservice.controller;

import com.vimofama.productservice.dto.CreateProductDTO;
import com.vimofama.productservice.dto.UpdateProductDTO;
import com.vimofama.productservice.model.Product;
import com.vimofama.productservice.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductDTO createProductDTO, UriComponentsBuilder ubc) {
        var product = productService.create(createProductDTO);
        URI url = ubc.path("/products/{id}").buildAndExpand(product.getId()).toUri();
        return ResponseEntity.created(url).body(product);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        var product = productService.getById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody UpdateProductDTO updateProductDTO) {
        var product = productService.update(id, updateProductDTO);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        var product = productService.getById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        productService.delete(id);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Producto eliminado exitosamente");
        response.put("deleted", true);
        response.put("product", product);
        return ResponseEntity.ok(response);
    }

}
