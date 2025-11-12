package com.supplychainx.mapper;

import com.supplychainx.common.dto.UserRequestDTO;
import com.supplychainx.common.dto.UserResponseDTO;
import com.supplychainx.common.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 * Mapper pour convertir entre l'entité User et ses DTOs
 * 
 * @Mapper indique à MapStruct de générer automatiquement le code de conversion
 * componentModel = "spring" permet d'utiliser ce mapper comme un bean Spring (@Component)
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Convertit un UserRequestDTO en entité User
     * @param dto Les données envoyées par le client
     * @return L'entité User à sauvegarder en base
     */
    User toEntity(UserRequestDTO dto);

    /**
     * Convertit une entité User en UserResponseDTO
     * @param user L'entité récupérée de la base de données
     * @return Le DTO à envoyer au client
     */
    UserResponseDTO toResponseDTO(User user);

    /**
     * Met à jour une entité User existante avec les données du DTO
     * @param dto Les nouvelles données
     * @param user L'entité existante à mettre à jour
     */
    void updateEntityFromDTO(UserRequestDTO dto, @MappingTarget User user);
}
