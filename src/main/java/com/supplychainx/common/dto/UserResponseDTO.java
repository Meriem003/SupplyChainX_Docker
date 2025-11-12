package com.supplychainx.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour retourner les informations d'un utilisateur
 * (sans exposer le mot de passe)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private Long idUser;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
}
