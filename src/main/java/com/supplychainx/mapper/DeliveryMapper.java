package com.supplychainx.mapper;

import com.supplychainx.livraison.dto.DeliveryRequestDTO;
import com.supplychainx.livraison.dto.DeliveryResponseDTO;
import com.supplychainx.livraison.entity.Delivery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper pour convertir entre l'entité Delivery et ses DTOs
 * 
 * uses = {OrderMapper} indique que ce mapper utilise OrderMapper
 * pour convertir la relation order
 */
@Mapper(componentModel = "spring", uses = {OrderMapper.class})
public interface DeliveryMapper {

    /**
     * Convertit un DeliveryRequestDTO en entité Delivery
     * 
     * @Mapping ignore le champ order qui sera géré dans le service
     * - order sera récupéré via orderId dans le service
     */
    @Mapping(target = "order", ignore = true)
    Delivery toEntity(DeliveryRequestDTO dto);

    /**
     * Convertit une entité Delivery en DeliveryResponseDTO
     * MapStruct utilisera automatiquement OrderMapper
     * pour convertir la relation order
     */
    DeliveryResponseDTO toResponseDTO(Delivery delivery);

    /**
     * Met à jour une entité Delivery existante avec les données du DTO
     */
    @Mapping(target = "order", ignore = true)
    void updateEntityFromDTO(DeliveryRequestDTO dto, @MappingTarget Delivery delivery);
}
