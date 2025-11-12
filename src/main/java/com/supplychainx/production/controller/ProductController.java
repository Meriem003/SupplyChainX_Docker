package com.supplychainx.production.controller;

import com.supplychainx.common.enums.UserRole;
import com.supplychainx.production.dto.ProductCreateDTO;
import com.supplychainx.production.dto.ProductResponseDTO;
import com.supplychainx.production.dto.ProductUpdateDTO;
import com.supplychainx.production.service.ProductService;
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
 * Controller REST pour gérer les produits finis
 * US18: Ajouter un produit
 * US19: Modifier un produit
 * US20: Supprimer un produit
 * US21: Consulter tous les produits
 * US22: Rechercher un produit par nom
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Produits Finis", description = "Gestion des produits finis (Module Production)")
public class ProductController {

    private final ProductService productService;

    /**
     * US18: Ajouter un produit fini avec toutes ses informations
     * Rôle: CHEF_PRODUCTION
     */
    @PostMapping
    @RequiresRole(UserRole.CHEF_PRODUCTION)
    @Operation(summary = "Créer un produit fini", 
               description = "US18: Permet au chef de production d'ajouter un nouveau produit")
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductCreateDTO dto) {
        ProductResponseDTO created = productService.createProduct(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * US19: Modifier un produit existant
     * Rôle: CHEF_PRODUCTION
     */
    @PutMapping("/{id}")
    @RequiresRole(UserRole.CHEF_PRODUCTION)
    @Operation(summary = "Modifier un produit fini", 
               description = "US19: Permet au chef de production de modifier un produit existant")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductUpdateDTO dto) {
        ProductResponseDTO updated = productService.updateProduct(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * US20: Supprimer un produit (s'il n'existe aucun ordre associé)
     * Rôle: CHEF_PRODUCTION
     */
    @DeleteMapping("/{id}")
    @RequiresRole(UserRole.CHEF_PRODUCTION)
    @Operation(summary = "Supprimer un produit fini", 
               description = "US20: Permet au chef de production de supprimer un produit sans ordre associé")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * US21: Consulter la liste de tous les produits
     * Rôle: SUPERVISEUR_PRODUCTION
     */
    @GetMapping
    @RequiresRole(UserRole.SUPERVISEUR_PRODUCTION)
    @Operation(summary = "Consulter tous les produits", 
               description = "US21: Permet au superviseur production de consulter tous les produits")
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        List<ProductResponseDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * US22: Rechercher un produit par nom
     * Rôle: SUPERVISEUR_PRODUCTION
     */
    @GetMapping("/search")
    @RequiresRole(UserRole.SUPERVISEUR_PRODUCTION)
    @Operation(summary = "Rechercher un produit par nom", 
               description = "US22: Permet au superviseur production de rechercher un produit par nom")
    public ResponseEntity<List<ProductResponseDTO>> searchProductsByName(@RequestParam String name) {
        List<ProductResponseDTO> products = productService.searchProductsByName(name);
        return ResponseEntity.ok(products);
    }
}
