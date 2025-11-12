package com.supplychainx.approvisionnement.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO pour créer un fournisseur (US3)
 * Contient toutes les informations nécessaires pour créer un nouveau fournisseur
 */
@Data
public class SupplierCreateDTO {

    /**
     * Nom du fournisseur (obligatoire)
     */
    @NotBlank(message = "Le nom du fournisseur est obligatoire")
    private String name;

    /**
     * Informations de contact (email, téléphone, adresse)
     */
    @NotBlank(message = "Les informations de contact sont obligatoires")
    private String contact;

    /**
     * Score de fiabilité du fournisseur (0.0 à 5.0)
     */
    @NotNull(message = "Le score de fiabilité est obligatoire")
    @Min(value = 0, message = "Le score doit être positif")
    private Double rating;

    /**
     * Délai moyen de livraison en jours
     */
    @NotNull(message = "Le délai de livraison est obligatoire")
    @Min(value = 1, message = "Le délai doit être au minimum 1 jour")
    private Integer leadTime;
}
