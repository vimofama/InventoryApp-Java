package com.vimofama.inventoryservice.service;

import com.vimofama.inventoryservice.dto.CreateInventoryDTO;
import com.vimofama.inventoryservice.model.Inventory;
import com.vimofama.inventoryservice.repository.InventoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InventoryService {

    private RestTemplate restTemplate = new RestTemplate();
    private InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public InventoryRepository getInventoryRepository() {
        return inventoryRepository;
    }

    public List<Inventory> findAll() {
        return inventoryRepository.findAll();
    }

    public Inventory findByProductId(Long id) {
        return inventoryRepository.findByProductId(id).orElse(null);
    }

    public Inventory save(CreateInventoryDTO createInventoryDTO) {
        var id = createInventoryDTO.productId();
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080/api/products/" + id, String.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            return null;
        }
        Inventory inventory = new Inventory(
                id,
                createInventoryDTO.quantity(),
                LocalDateTime.now()
        );
        return inventoryRepository.save(inventory);
    }

    public Inventory increaseQuantity(Long productId, int quantity) {
        var inventory = findByProductId(productId);
        if (inventory == null) {
            return null;
        }
        inventory.setQuantity(inventory.getQuantity() + quantity);
        inventory.setLastUpdate(LocalDateTime.now());
        return inventoryRepository.save(inventory);
    }

    public Inventory decreaseQuantity(Long productId, int quantity) {
        var inventory = findByProductId(productId);
        if (inventory == null) {
            return null;
        }
        inventory.setQuantity(inventory.getQuantity() - quantity);
        inventory.setLastUpdate(LocalDateTime.now());
        return inventoryRepository.save(inventory);
    }

    public boolean deleteById(Long id) {
        var inventory = findByProductId(id);
        if (inventory == null) {
            return false;
        }
        inventoryRepository.delete(inventory);
        return true;
    }
}
