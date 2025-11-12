package com.supplychainx.mapper;

import com.supplychainx.approvisionnement.dto.SupplierRequestDTO;
import com.supplychainx.approvisionnement.dto.SupplierResponseDTO;
import com.supplychainx.approvisionnement.entity.Supplier;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 * Mapper pour convertir entre l'entité Supplier et ses DTOs
 */
@Mapper(componentModel = "spring")
public interface SupplierMapper {

    /**
     * Convertit un SupplierRequestDTO en entité Supplier
     * @param dto Les données envoyées par le client
     * @return L'entité Supplier à sauvegarder en base
     */
    Supplier toEntity(SupplierRequestDTO dto);

    /**
     * Convertit une entité Supplier en SupplierResponseDTO
     * @param supplier L'entité récupérée de la base de données
     * @return Le DTO à envoyer au client
     */
    SupplierResponseDTO toResponseDTO(Supplier supplier);

    /**
     * Met à jour une entité Supplier existante avec les données du DTO
     * @param dto Les nouvelles données
     * @param supplier L'entité existante à mettre à jour
     */
    void updateEntityFromDTO(SupplierRequestDTO dto, @MappingTarget Supplier supplier);
}
