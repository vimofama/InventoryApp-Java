package com.vimofama.productservice.service;

import com.vimofama.productservice.dto.CreateProductDTO;
import com.vimofama.productservice.dto.UpdateProductDTO;
import com.vimofama.productservice.model.Product;
import com.vimofama.productservice.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product create(CreateProductDTO createProductDTO) {
        Product product = new Product(
                createProductDTO.name(),
                createProductDTO.description(),
                createProductDTO.price(),
                createProductDTO.sku()
        );
        return productRepository.save(product);
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Product getById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.orElse(null);
    }

    public Product update(Long id, UpdateProductDTO updateProductDTO) {
        Product product = getById(id);
        if (product == null) {
            return null;
        }
        if (updateProductDTO.name() != null) product.setName(updateProductDTO.name());
        if (updateProductDTO.description() != null) product.setDescription(updateProductDTO.description());
        if (updateProductDTO.price() != null) product.setPrice(updateProductDTO.price());
        if (updateProductDTO.sku() != null) product.setSku(updateProductDTO.sku());

        return productRepository.save(product);
    }

    public boolean delete(Long id) {
        Product product = getById(id);
        if (product == null) {
            return false;
        }
        productRepository.delete(product);
        return true;
    }
}
