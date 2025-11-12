package com.supplychainx.approvisionnement.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO pour modifier une matière première existante (US9)
 */
@Data
public class RawMaterialUpdateDTO {

    /**
     * Nom de la matière première (obligatoire)
     */
    @NotBlank(message = "Le nom de la matière première est obligatoire")
    private String name;

    /**
     * Quantité en stock
     */
    @NotNull(message = "Le stock est obligatoire")
    @Min(value = 0, message = "Le stock doit être positif ou nul")
    private Integer stock;

    /**
     * Seuil minimum critique
     */
    @NotNull(message = "Le seuil minimum est obligatoire")
    @Min(value = 0, message = "Le seuil minimum doit être positif ou nul")
    private Integer stockMin;

    /**
     * Unité de mesure
     */
    @NotBlank(message = "L'unité de mesure est obligatoire")
    private String unit;
}
