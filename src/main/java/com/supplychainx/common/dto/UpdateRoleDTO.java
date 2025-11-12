package com.supplychainx.common.dto;

import com.supplychainx.common.enums.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour mettre à jour le rôle d'un utilisateur
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRoleDTO {

    @NotNull(message = "Le rôle est requis")
    private UserRole role;
}
