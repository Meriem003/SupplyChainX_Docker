package com.supplychainx.approvisionnement.controller;

import com.supplychainx.approvisionnement.dto.RawMaterialCreateDTO;
import com.supplychainx.approvisionnement.dto.RawMaterialResponseDTO;
import com.supplychainx.approvisionnement.dto.RawMaterialUpdateDTO;
import com.supplychainx.approvisionnement.service.RawMaterialService;
import com.supplychainx.common.enums.UserRole;
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
 * Controller REST pour gérer les matières premières
 * Implémente les US8, US9, US10, US11, US12
 */
@RestController
@RequestMapping("/api/raw-materials")
@RequiredArgsConstructor
@Tag(name = "Matières Premières", description = "Gestion des matières premières (Module Approvisionnement)")
public class RawMaterialController {

    private final RawMaterialService rawMaterialService;

    /**
     * US8: Ajouter une nouvelle matière première
     * Accessible uniquement au GESTIONNAIRE_APPROVISIONNEMENT
     */
    @PostMapping
    @RequiresRole(UserRole.GESTIONNAIRE_APPROVISIONNEMENT)
    @Operation(summary = "Créer une matière première", description = "Ajoute une nouvelle matière première (US8)")
    public ResponseEntity<RawMaterialResponseDTO> createRawMaterial(@Valid @RequestBody RawMaterialCreateDTO dto) {
        RawMaterialResponseDTO created = rawMaterialService.createRawMaterial(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * US9: Modifier une matière première existante
     * Accessible uniquement au GESTIONNAIRE_APPROVISIONNEMENT
     */
    @PutMapping("/{id}")
    @RequiresRole(UserRole.GESTIONNAIRE_APPROVISIONNEMENT)
    @Operation(summary = "Modifier une matière première", description = "Met à jour une matière première existante (US9)")
    public ResponseEntity<RawMaterialResponseDTO> updateRawMaterial(
            @PathVariable Long id,
            @Valid @RequestBody RawMaterialUpdateDTO dto) {
        RawMaterialResponseDTO updated = rawMaterialService.updateRawMaterial(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * US10: Supprimer une matière première (si elle n'est utilisée dans aucune commande)
     * Accessible uniquement au GESTIONNAIRE_APPROVISIONNEMENT
     */
    @DeleteMapping("/{id}")
    @RequiresRole(UserRole.GESTIONNAIRE_APPROVISIONNEMENT)
    @Operation(summary = "Supprimer une matière première", 
               description = "Supprime une matière première si elle n'est pas utilisée (US10)")
    public ResponseEntity<Void> deleteRawMaterial(@PathVariable Long id) {
        rawMaterialService.deleteRawMaterial(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * US11: Consulter la liste de toutes les matières premières
     * Accessible au SUPERVISEUR_LOGISTIQUE
     */
    @GetMapping
    @RequiresRole(UserRole.SUPERVISEUR_LOGISTIQUE)
    @Operation(summary = "Liste des matières premières", 
               description = "Consulte la liste de toutes les matières premières (US11)")
    public ResponseEntity<List<RawMaterialResponseDTO>> getAllRawMaterials() {
        List<RawMaterialResponseDTO> materials = rawMaterialService.getAllRawMaterials();
        return ResponseEntity.ok(materials);
    }

    /**
     * US12: Filtrer les matières premières dont le stock est inférieur au seuil critique
     * Accessible au SUPERVISEUR_LOGISTIQUE
     */
    @GetMapping("/critical")
    @RequiresRole(UserRole.SUPERVISEUR_LOGISTIQUE)
    @Operation(summary = "Matières en stock critique", 
               description = "Filtre les matières dont le stock < seuil minimum (US12)")
    public ResponseEntity<List<RawMaterialResponseDTO>> getCriticalStockMaterials() {
        List<RawMaterialResponseDTO> criticalMaterials = rawMaterialService.getCriticalStockMaterials();
        return ResponseEntity.ok(criticalMaterials);
    }
}
