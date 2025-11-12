package com.supplychainx.mapper;

import com.supplychainx.livraison.dto.OrderRequestDTO;
import com.supplychainx.livraison.dto.OrderResponseDTO;
import com.supplychainx.livraison.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper pour convertir entre l'entité Order (commande client) et ses DTOs
 * 
 * uses = {CustomerMapper, ProductMapper} indique que ce mapper 
 * utilise d'autres mappers pour convertir les relations
 */
@Mapper(componentModel = "spring", uses = {CustomerMapper.class, ProductMapper.class})
public interface OrderMapper {

    /**
     * Convertit un OrderRequestDTO en entité Order
     * 
     * @Mapping ignore les champs complexes qui seront gérés dans le service
     * - customer sera récupéré via customerId dans le service
     * - product sera récupéré via productId dans le service
     */
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "product", ignore = true)
    Order toEntity(OrderRequestDTO dto);

    /**
     * Convertit une entité Order en OrderResponseDTO
     * MapStruct utilisera automatiquement CustomerMapper et ProductMapper
     * pour convertir les relations customer et product
     */
    OrderResponseDTO toResponseDTO(Order order);

    /**
     * Met à jour une entité Order existante avec les données du DTO
     */
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "product", ignore = true)
    void updateEntityFromDTO(OrderRequestDTO dto, @MappingTarget Order order);
}
