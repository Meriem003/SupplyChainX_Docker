package com.supplychainx.mapper;

import com.supplychainx.approvisionnement.dto.SupplyOrderRequestDTO;
import com.supplychainx.approvisionnement.dto.SupplyOrderResponseDTO;
import com.supplychainx.approvisionnement.entity.SupplyOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper pour convertir entre l'entité SupplyOrder et ses DTOs
 * 
 * uses = {SupplierMapper, RawMaterialMapper} indique que ce mapper 
 * utilise d'autres mappers pour convertir les relations
 */
@Mapper(componentModel = "spring", uses = {SupplierMapper.class, RawMaterialMapper.class})
public interface SupplyOrderMapper {

    /**
     * Convertit un SupplyOrderRequestDTO en entité SupplyOrder
     * 
     * @Mapping ignore les champs complexes qui seront gérés dans le service
     * - supplier sera récupéré via supplierId dans le service
     * - materials sera récupéré via materialIds dans le service
     */
    @Mapping(target = "supplier", ignore = true)
    @Mapping(target = "materials", ignore = true)
    SupplyOrder toEntity(SupplyOrderRequestDTO dto);

    /**
     * Convertit une entité SupplyOrder en SupplyOrderResponseDTO
     * MapStruct utilisera automatiquement SupplierMapper et RawMaterialMapper
     * pour convertir les relations supplier et materials
     */
    SupplyOrderResponseDTO toResponseDTO(SupplyOrder supplyOrder);

    /**
     * Met à jour une entité SupplyOrder existante avec les données du DTO
     */
    @Mapping(target = "supplier", ignore = true)
    @Mapping(target = "materials", ignore = true)
    void updateEntityFromDTO(SupplyOrderRequestDTO dto, @MappingTarget SupplyOrder supplyOrder);
}
