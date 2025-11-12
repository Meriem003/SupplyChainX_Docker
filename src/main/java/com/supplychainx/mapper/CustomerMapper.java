package com.supplychainx.mapper;

import com.supplychainx.livraison.dto.CustomerRequestDTO;
import com.supplychainx.livraison.dto.CustomerResponseDTO;
import com.supplychainx.livraison.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 * Mapper pour convertir entre l'entité Customer et ses DTOs
 */
@Mapper(componentModel = "spring")
public interface CustomerMapper {

    /**
     * Convertit un CustomerRequestDTO en entité Customer
     * @param dto Les données envoyées par le client
     * @return L'entité Customer à sauvegarder en base
     */
    Customer toEntity(CustomerRequestDTO dto);

    /**
     * Convertit une entité Customer en CustomerResponseDTO
     * @param customer L'entité récupérée de la base de données
     * @return Le DTO à envoyer au client
     */
    CustomerResponseDTO toResponseDTO(Customer customer);

    /**
     * Met à jour une entité Customer existante avec les données du DTO
     * @param dto Les nouvelles données
     * @param customer L'entité existante à mettre à jour
     */
    void updateEntityFromDTO(CustomerRequestDTO dto, @MappingTarget Customer customer);
}
