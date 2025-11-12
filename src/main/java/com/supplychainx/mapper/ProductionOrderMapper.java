package com.supplychainx.mapper;

import com.supplychainx.production.dto.ProductionOrderRequestDTO;
import com.supplychainx.production.dto.ProductionOrderResponseDTO;
import com.supplychainx.production.entity.ProductionOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper pour convertir entre l'entité ProductionOrder et ses DTOs
 * 
 * uses = {ProductMapper} indique que ce mapper utilise ProductMapper
 * pour convertir la relation product
 */
@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface ProductionOrderMapper {

    /**
     * Convertit un ProductionOrderRequestDTO en entité ProductionOrder
     * 
     * @Mapping ignore le champ product qui sera géré dans le service
     * - product sera récupéré via productId dans le service
     */
    @Mapping(target = "product", ignore = true)
    ProductionOrder toEntity(ProductionOrderRequestDTO dto);

    /**
     * Convertit une entité ProductionOrder en ProductionOrderResponseDTO
     * MapStruct utilisera automatiquement ProductMapper
     * pour convertir la relation product
     */
    ProductionOrderResponseDTO toResponseDTO(ProductionOrder productionOrder);

    /**
     * Met à jour une entité ProductionOrder existante avec les données du DTO
     */
    @Mapping(target = "product", ignore = true)
    void updateEntityFromDTO(ProductionOrderRequestDTO dto, @MappingTarget ProductionOrder productionOrder);
}
