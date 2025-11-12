package com.supplychainx.mapper;

import com.supplychainx.production.dto.BillOfMaterialRequestDTO;
import com.supplychainx.production.dto.BillOfMaterialResponseDTO;
import com.supplychainx.production.entity.BillOfMaterial;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper pour convertir entre l'entité BillOfMaterial (BOM) et ses DTOs
 * 
 * uses = {ProductMapper, RawMaterialMapper} indique que ce mapper 
 * utilise d'autres mappers pour convertir les relations
 */
@Mapper(componentModel = "spring", uses = {ProductMapper.class, RawMaterialMapper.class})
public interface BillOfMaterialMapper {

    /**
     * Convertit un BillOfMaterialRequestDTO en entité BillOfMaterial
     * 
     * @Mapping ignore les champs complexes qui seront gérés dans le service
     * - product sera récupéré via productId dans le service
     * - material sera récupéré via materialId dans le service
     */
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "material", ignore = true)
    BillOfMaterial toEntity(BillOfMaterialRequestDTO dto);

    /**
     * Convertit une entité BillOfMaterial en BillOfMaterialResponseDTO
     * MapStruct utilisera automatiquement ProductMapper et RawMaterialMapper
     * pour convertir les relations product et material
     */
    BillOfMaterialResponseDTO toResponseDTO(BillOfMaterial billOfMaterial);

    /**
     * Met à jour une entité BillOfMaterial existante avec les données du DTO
     */
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "material", ignore = true)
    void updateEntityFromDTO(BillOfMaterialRequestDTO dto, @MappingTarget BillOfMaterial billOfMaterial);
}
