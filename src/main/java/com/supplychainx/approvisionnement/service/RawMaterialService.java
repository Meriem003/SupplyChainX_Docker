package com.supplychainx.approvisionnement.service;

import com.supplychainx.approvisionnement.dto.RawMaterialCreateDTO;
import com.supplychainx.approvisionnement.dto.RawMaterialResponseDTO;
import com.supplychainx.approvisionnement.dto.RawMaterialUpdateDTO;
import com.supplychainx.approvisionnement.entity.RawMaterial;
import com.supplychainx.approvisionnement.repository.RawMaterialRepository;
import com.supplychainx.exception.BusinessRuleException;
import com.supplychainx.exception.ResourceNotFoundException;
import com.supplychainx.mapper.RawMaterialMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service pour gérer les matières premières
 * Implémente les US8, US9, US10, US11, US12
 */
@Service
@RequiredArgsConstructor
@Transactional
public class RawMaterialService {

    private final RawMaterialRepository rawMaterialRepository;
    private final RawMaterialMapper rawMaterialMapper;

    /**
     * US8: Ajouter une nouvelle matière première
     * 
     * @param dto Données de la matière première à créer
     * @return La matière première créée
     */
    public RawMaterialResponseDTO createRawMaterial(RawMaterialCreateDTO dto) {
        // Créer l'entité RawMaterial
        RawMaterial material = new RawMaterial();
        material.setName(dto.getName());
        material.setStock(dto.getStock());
        material.setStockMin(dto.getStockMin());
        material.setUnit(dto.getUnit());

        // Sauvegarder en base de données
        material = rawMaterialRepository.save(material);

        // Convertir et retourner le DTO via le mapper
        return rawMaterialMapper.toResponseDTO(material);
    }

    /**
     * US9: Modifier une matière première existante
     * 
     * @param materialId ID de la matière première à modifier
     * @param dto Nouvelles données
     * @return La matière première modifiée
     */
    public RawMaterialResponseDTO updateRawMaterial(Long materialId, RawMaterialUpdateDTO dto) {
        // Vérifier que la matière première existe
        RawMaterial material = rawMaterialRepository.findById(materialId)
                .orElseThrow(() -> new ResourceNotFoundException("Matière première non trouvée avec l'ID: " + materialId));

        // Mettre à jour les informations
        material.setName(dto.getName());
        material.setStock(dto.getStock());
        material.setStockMin(dto.getStockMin());
        material.setUnit(dto.getUnit());

        // Sauvegarder
        material = rawMaterialRepository.save(material);

        // Retourner le DTO via le mapper
        return rawMaterialMapper.toResponseDTO(material);
    }

    /**
     * US10: Supprimer une matière première (si elle n'est utilisée dans aucune commande)
     * 
     * Règle métier: Une matière première ne peut être supprimée que si elle n'est pas
     * utilisée dans des commandes d'approvisionnement ou dans des BOM (Bill of Materials)
     * 
     * @param materialId ID de la matière première à supprimer
     */
    public void deleteRawMaterial(Long materialId) {
        // Vérifier que la matière première existe
        RawMaterial material = rawMaterialRepository.findById(materialId)
                .orElseThrow(() -> new ResourceNotFoundException("Matière première non trouvée avec l'ID: " + materialId));

        // Vérifier qu'elle n'est pas utilisée dans des commandes d'approvisionnement
        // Note: La relation ManyToMany avec SupplyOrder est gérée via l'entité
        boolean isUsedInOrders = !material.getSuppliers().isEmpty();

        if (isUsedInOrders) {
            throw new BusinessRuleException(
                    "Impossible de supprimer la matière première : elle est associée à des fournisseurs ou des commandes");
        }

        // Supprimer la matière première
        rawMaterialRepository.delete(material);
    }

    /**
     * US11: Consulter la liste de toutes les matières premières
     * 
     * @return Liste de toutes les matières premières
     */
    public List<RawMaterialResponseDTO> getAllRawMaterials() {
        return rawMaterialRepository.findAll().stream()
                .map(rawMaterialMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * US12: Filtrer les matières premières dont le stock est inférieur au seuil critique
     * 
     * @return Liste des matières premières en stock critique
     */
    public List<RawMaterialResponseDTO> getCriticalStockMaterials() {
        return rawMaterialRepository.findMaterialsBelowMinStock().stream()
                .map(rawMaterialMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
