package com.supplychainx.approvisionnement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour retourner les informations d'une matière première
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RawMaterialResponseDTO {

    private Long idMaterial;
    private String name;
    private Integer stock;
    private Integer stockMin;
    private String unit;
    
    /**
     * Indique si le stock est critique (inférieur au seuil minimum)
     * Utile pour US12
     */
    private Boolean isCritical;
}
