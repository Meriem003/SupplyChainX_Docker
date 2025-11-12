package com.supplychainx.livraison.controller;

import com.supplychainx.common.enums.UserRole;
import com.supplychainx.livraison.dto.OrderRequestDTO;
import com.supplychainx.livraison.dto.OrderResponseDTO;
import com.supplychainx.livraison.service.OrderService;
import com.supplychainx.security.RequiresRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Gestion des commandes clients")
public class OrderController {
    
    private final OrderService orderService;
    
    /**
     * US35 : Créer une commande client
     */
    @PostMapping
    @RequiresRole(UserRole.GESTIONNAIRE_COMMERCIAL)
    @Operation(summary = "Créer une commande client",
            description = "Permet de créer une nouvelle commande pour un client avec produit et quantité")
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO dto) {
        OrderResponseDTO order = orderService.createOrder(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
    
    /**
     * US36 : Modifier une commande existante
     */
    @PutMapping("/{id}")
    @RequiresRole(UserRole.GESTIONNAIRE_COMMERCIAL)
    @Operation(summary = "Modifier une commande",
            description = "Permet de modifier toutes les informations d'une commande existante")
    public ResponseEntity<OrderResponseDTO> updateOrder(
            @PathVariable Long id,
            @Valid @RequestBody OrderRequestDTO dto) {
        OrderResponseDTO order = orderService.updateOrder(id, dto);
        return ResponseEntity.ok(order);
    }
    
    /**
     * US37 : Annuler une commande si non expédiée
     */
    @DeleteMapping("/{id}")
    @RequiresRole(UserRole.GESTIONNAIRE_COMMERCIAL)
    @Operation(summary = "Annuler une commande",
            description = "Permet d'annuler une commande uniquement si elle n'a pas été expédiée (statut EN_PREPARATION)")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * US38 : Consulter la liste de toutes les commandes
     */
    @GetMapping
    @RequiresRole(UserRole.SUPERVISEUR_LIVRAISONS)
    @Operation(summary = "Consulter toutes les commandes",
            description = "Retourne la liste de toutes les commandes clients")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        List<OrderResponseDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }
    
    /**
     * US39 : Suivre le statut des commandes
     */
    @GetMapping("/status/{status}")
    @RequiresRole(UserRole.SUPERVISEUR_LIVRAISONS)
    @Operation(summary = "Suivre le statut des commandes",
            description = "Permet de filtrer les commandes par statut (EN_PREPARATION, EN_ROUTE, LIVREE)")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByStatus(@PathVariable String status) {
        List<OrderResponseDTO> orders = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(orders);
    }
}
