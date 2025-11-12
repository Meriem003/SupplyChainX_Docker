package com.supplychainx.production.service;

import com.supplychainx.exception.ResourceNotFoundException;
import com.supplychainx.production.dto.MaterialAvailabilityDTO;
import com.supplychainx.production.dto.ProductionAvailabilityResponseDTO;
import com.supplychainx.production.dto.ProductionTimeResponseDTO;
import com.supplychainx.production.entity.BillOfMaterial;
import com.supplychainx.production.entity.Product;
import com.supplychainx.production.repository.BillOfMaterialRepository;
import com.supplychainx.production.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanningService {
    
    private final ProductRepository productRepository;
    private final BillOfMaterialRepository billOfMaterialRepository;
    
    /**
     * US28 : Vérifier la disponibilité des matières premières avant de lancer un ordre
     */
    @Transactional(readOnly = true)
    public ProductionAvailabilityResponseDTO checkMaterialAvailability(Long productId, Integer quantity) {
        // Vérifier que le produit existe
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Produit non trouvé avec l'ID: " + productId));
        
        // Récupérer la liste des matières nécessaires (BOM)
        List<BillOfMaterial> bom = billOfMaterialRepository.findByProduct(product);
        
        // Vérifier la disponibilité de chaque matière
        List<MaterialAvailabilityDTO> materialsStatus = new ArrayList<>();
        boolean canProduce = true;
        
        for (BillOfMaterial bomItem : bom) {
            Integer requiredQuantity = bomItem.getQuantity() * quantity;
            Integer availableStock = bomItem.getMaterial().getStock();
            boolean isAvailable = availableStock >= requiredQuantity;
            
            materialsStatus.add(new MaterialAvailabilityDTO(
                    bomItem.getMaterial().getIdMaterial(),
                    bomItem.getMaterial().getName(),
                    requiredQuantity,
                    availableStock,
                    isAvailable
            ));
            
            if (!isAvailable) {
                canProduce = false;
            }
        }
        
        return new ProductionAvailabilityResponseDTO(
                product.getIdProduct(),
                product.getName(),
                quantity,
                canProduce,
                materialsStatus
        );
    }
    
    /**
     * US29 : Calculer le temps estimé de production
     */
    @Transactional(readOnly = true)
    public ProductionTimeResponseDTO calculateProductionTime(Long productId, Integer quantity) {
        // Vérifier que le produit existe
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Produit non trouvé avec l'ID: " + productId));
        
        // Calculer le temps total
        Integer unitProductionTime = product.getProductionTime();
        Integer totalProductionTime = unitProductionTime * quantity;
        
        return new ProductionTimeResponseDTO(
                product.getIdProduct(),
                product.getName(),
                quantity,
                unitProductionTime,
                totalProductionTime
        );
    }
}
