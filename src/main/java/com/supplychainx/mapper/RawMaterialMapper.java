package com.supplychainx.mapper;

import com.supplychainx.approvisionnement.dto.RawMaterialRequestDTO;
import com.supplychainx.approvisionnement.dto.RawMaterialResponseDTO;
import com.supplychainx.approvisionnement.entity.RawMaterial;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 * Mapper pour convertir entre l'entité RawMaterial et ses DTOs
 */
@Mapper(componentModel = "spring")
public interface RawMaterialMapper {

    /**
     * Convertit un RawMaterialRequestDTO en entité RawMaterial
     * @param dto Les données envoyées par le client
     * @return L'entité RawMaterial à sauvegarder en base
     */
    RawMaterial toEntity(RawMaterialRequestDTO dto);

    /**
     * Convertit une entité RawMaterial en RawMaterialResponseDTO
     * @param rawMaterial L'entité récupérée de la base de données
     * @return Le DTO à envoyer au client
     */
    RawMaterialResponseDTO toResponseDTO(RawMaterial rawMaterial);

    /**
     * Met à jour une entité RawMaterial existante avec les données du DTO
     * @param dto Les nouvelles données
     * @param rawMaterial L'entité existante à mettre à jour
     */
    void updateEntityFromDTO(RawMaterialRequestDTO dto, @MappingTarget RawMaterial rawMaterial);
    
    /**
     * Calcule le champ isCritical après le mapping
     * Cette méthode est appelée automatiquement par MapStruct après toResponseDTO
     */
    @AfterMapping
    default void calculateIsCritical(RawMaterial rawMaterial, @MappingTarget RawMaterialResponseDTO dto) {
        if (rawMaterial.getStock() != null && rawMaterial.getStockMin() != null) {
            dto.setIsCritical(rawMaterial.getStock() < rawMaterial.getStockMin());
        } else {
            dto.setIsCritical(false);
        }
    }
}
