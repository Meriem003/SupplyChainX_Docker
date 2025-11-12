package com.supplychainx.mapper;

import com.supplychainx.production.dto.ProductRequestDTO;
import com.supplychainx.production.dto.ProductResponseDTO;
import com.supplychainx.production.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 * Mapper pour convertir entre l'entité Product et ses DTOs
 */
@Mapper(componentModel = "spring")
public interface ProductMapper {

    /**
     * Convertit un ProductRequestDTO en entité Product
     * @param dto Les données envoyées par le client
     * @return L'entité Product à sauvegarder en base
     */
    Product toEntity(ProductRequestDTO dto);

    /**
     * Convertit une entité Product en ProductResponseDTO
     * @param product L'entité récupérée de la base de données
     * @return Le DTO à envoyer au client
     */
    ProductResponseDTO toResponseDTO(Product product);

    /**
     * Met à jour une entité Product existante avec les données du DTO
     * @param dto Les nouvelles données
     * @param product L'entité existante à mettre à jour
     */
    void updateEntityFromDTO(ProductRequestDTO dto, @MappingTarget Product product);
}
