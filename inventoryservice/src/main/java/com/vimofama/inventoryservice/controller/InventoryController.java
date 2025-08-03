package com.vimofama.inventoryservice.controller;

import com.vimofama.inventoryservice.dto.CreateInventoryDTO;
import com.vimofama.inventoryservice.dto.QuantityDTO;
import com.vimofama.inventoryservice.model.Inventory;
import com.vimofama.inventoryservice.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<List<Inventory>> getAllInventory() {
        return ResponseEntity.ok(inventoryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventory> getInventoryByProductId(@PathVariable Long id) {
        var inventory = inventoryService.findByProductId(id);
        if (inventory == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(inventory);
    }

    @PostMapping
    public ResponseEntity<Inventory> createInventory(@Valid @RequestBody CreateInventoryDTO  createInventoryDTO, UriComponentsBuilder ubc) {
        var inventory = inventoryService.save(createInventoryDTO);
        if (inventory == null) {
            return ResponseEntity.badRequest().build();
        }
        URI uri = ubc.path("/api/inventory/{id}").buildAndExpand(inventory.getProductId()).toUri();
        return ResponseEntity.created(uri).body(inventory);
    }

    @PostMapping("/increase/{id}")
    public ResponseEntity<Inventory> increaseQuantity(@PathVariable Long id, @Valid @RequestBody QuantityDTO quantityDTO) {
        var inventory = inventoryService.increaseQuantity(id, quantityDTO.quantity());
        if (inventory == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(inventory);
    }

    @PostMapping("/decrease/{id}")
    public ResponseEntity<Inventory> decreaseQuantity(@PathVariable Long id, @Valid @RequestBody QuantityDTO quantityDTO) {
        var inventory = inventoryService.decreaseQuantity(id, quantityDTO.quantity());
        if (inventory == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(inventory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInventory(@PathVariable Long id) {
        var inventory = inventoryService.deleteById(id);
        if (!inventory) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }
}
