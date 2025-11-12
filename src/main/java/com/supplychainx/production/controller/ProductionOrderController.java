package com.supplychainx.production.controller;

import com.supplychainx.common.enums.UserRole;
import com.supplychainx.production.dto.ProductionOrderCreateDTO;
import com.supplychainx.production.dto.ProductionOrderResponseDTO;
import com.supplychainx.production.dto.ProductionOrderUpdateDTO;
import com.supplychainx.production.service.ProductionOrderService;
import com.supplychainx.security.RequiresRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST pour gérer les ordres de production
 * US23: Créer un ordre de production
 * US24: Modifier un ordre existant
 * US25: Annuler un ordre si non commencé
 * US26: Consulter tous les ordres
 * US27: Suivre le statut des ordres
 */
@RestController
@RequestMapping("/api/production-orders")
@RequiredArgsConstructor
@Tag(name = "Ordres de Production", description = "Gestion des ordres de production (Module Production)")
public class ProductionOrderController {

    private final ProductionOrderService productionOrderService;

    /**
     * US23: Créer un ordre de production
     * Rôle: CHEF_PRODUCTION
     */
    @PostMapping
    @RequiresRole(UserRole.CHEF_PRODUCTION)
    @Operation(summary = "Créer un ordre de production", 
               description = "US23: Permet au chef de production de créer un nouvel ordre")
    public ResponseEntity<ProductionOrderResponseDTO> createProductionOrder(@Valid @RequestBody ProductionOrderCreateDTO dto) {
        ProductionOrderResponseDTO created = productionOrderService.createProductionOrder(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * US24: Modifier un ordre existant
     * Rôle: CHEF_PRODUCTION
     */
    @PutMapping("/{id}")
    @RequiresRole(UserRole.CHEF_PRODUCTION)
    @Operation(summary = "Modifier un ordre de production", 
               description = "US24: Permet au chef de production de modifier un ordre existant")
    public ResponseEntity<ProductionOrderResponseDTO> updateProductionOrder(
            @PathVariable Long id,
            @Valid @RequestBody ProductionOrderUpdateDTO dto) {
        ProductionOrderResponseDTO updated = productionOrderService.updateProductionOrder(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * US25: Annuler un ordre si non commencé
     * Rôle: CHEF_PRODUCTION
     */
    @DeleteMapping("/{id}")
    @RequiresRole(UserRole.CHEF_PRODUCTION)
    @Operation(summary = "Annuler un ordre de production", 
               description = "US25: Permet au chef de production d'annuler un ordre non commencé")
    public ResponseEntity<Void> cancelProductionOrder(@PathVariable Long id) {
        productionOrderService.cancelProductionOrder(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * US26: Consulter tous les ordres de production
     * Rôle: SUPERVISEUR_PRODUCTION
     */
    @GetMapping
    @RequiresRole(UserRole.SUPERVISEUR_PRODUCTION)
    @Operation(summary = "Consulter tous les ordres", 
               description = "US26: Permet au superviseur production de consulter tous les ordres")
    public ResponseEntity<List<ProductionOrderResponseDTO>> getAllProductionOrders() {
        List<ProductionOrderResponseDTO> orders = productionOrderService.getAllProductionOrders();
        return ResponseEntity.ok(orders);
    }

    /**
     * US27: Suivre le statut des ordres (filtrage par statut)
     * Rôle: SUPERVISEUR_PRODUCTION
     */
    @GetMapping("/status/{status}")
    @RequiresRole(UserRole.SUPERVISEUR_PRODUCTION)
    @Operation(summary = "Filtrer les ordres par statut", 
               description = "US27: Permet au superviseur production de suivre les ordres selon leur statut (EN_ATTENTE, EN_PRODUCTION, TERMINE, BLOQUE)")
    public ResponseEntity<List<ProductionOrderResponseDTO>> getProductionOrdersByStatus(@PathVariable String status) {
        List<ProductionOrderResponseDTO> orders = productionOrderService.getProductionOrdersByStatus(status);
        return ResponseEntity.ok(orders);
    }
}
