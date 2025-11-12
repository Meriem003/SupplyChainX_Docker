package com.supplychainx.production.service;

import com.supplychainx.exception.BusinessRuleException;
import com.supplychainx.exception.ResourceNotFoundException;
import com.supplychainx.mapper.ProductMapper;
import com.supplychainx.mapper.ProductionOrderMapper;
import com.supplychainx.production.dto.*;
import com.supplychainx.production.entity.Product;
import com.supplychainx.production.entity.ProductionOrder;
import com.supplychainx.production.enums.ProductionOrderStatus;
import com.supplychainx.production.repository.ProductRepository;
import com.supplychainx.production.repository.ProductionOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service pour gérer les ordres de production
 * US23: Créer un ordre de production
 * US24: Modifier un ordre existant
 * US25: Annuler un ordre si non commencé
 * US26: Consulter tous les ordres
 * US27: Suivre le statut des ordres
 */
@Service
@RequiredArgsConstructor
public class ProductionOrderService {

    private final ProductionOrderRepository productionOrderRepository;
    private final ProductRepository productRepository;
    private final ProductionOrderMapper productionOrderMapper;
    private final ProductMapper productMapper;

    /**
     * US23: Créer un ordre de production
     */
    @Transactional
    public ProductionOrderResponseDTO createProductionOrder(ProductionOrderCreateDTO dto) {
        // Vérifier que le produit existe
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Produit non trouvé avec l'ID: " + dto.getProductId()));

        // Créer l'ordre de production
        ProductionOrder order = new ProductionOrder();
        order.setProduct(product);
        order.setQuantity(dto.getQuantity());
        order.setStatus(ProductionOrderStatus.valueOf(dto.getStatus()));
        order.setStartDate(dto.getStartDate());
        order.setEndDate(dto.getEndDate());

        ProductionOrder savedOrder = productionOrderRepository.save(order);
        return productionOrderMapper.toResponseDTO(savedOrder);
    }

    /**
     * US24: Modifier un ordre existant
     */
    @Transactional
    public ProductionOrderResponseDTO updateProductionOrder(Long id, ProductionOrderUpdateDTO dto) {
        // Vérifier que l'ordre existe
        ProductionOrder order = productionOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Ordre de production non trouvé avec l'ID: " + id));

        // Vérifier que le produit existe
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Produit non trouvé avec l'ID: " + dto.getProductId()));

        // Mettre à jour l'ordre
        order.setProduct(product);
        order.setQuantity(dto.getQuantity());
        order.setStatus(ProductionOrderStatus.valueOf(dto.getStatus()));
        order.setStartDate(dto.getStartDate());
        order.setEndDate(dto.getEndDate());

        ProductionOrder updatedOrder = productionOrderRepository.save(order);
        return productionOrderMapper.toResponseDTO(updatedOrder);
    }

    /**
     * US25: Annuler un ordre si non commencé
     */
    @Transactional
    public void cancelProductionOrder(Long id) {
        // Vérifier que l'ordre existe
        ProductionOrder order = productionOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Ordre de production non trouvé avec l'ID: " + id));

        // Règle métier: on ne peut annuler que si le statut est EN_ATTENTE
        if (order.getStatus() != ProductionOrderStatus.EN_ATTENTE) {
            throw new BusinessRuleException(
                    "Impossible d'annuler l'ordre car il a déjà commencé (statut: " + 
                    order.getStatus() + ")");
        }

        productionOrderRepository.delete(order);
    }

    /**
     * US26: Consulter tous les ordres de production
     */
    @Transactional(readOnly = true)
    public List<ProductionOrderResponseDTO> getAllProductionOrders() {
        return productionOrderRepository.findAll().stream()
                .map(productionOrderMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * US27: Suivre le statut des ordres (filtrage par statut)
     */
    @Transactional(readOnly = true)
    public List<ProductionOrderResponseDTO> getProductionOrdersByStatus(String status) {
        // Convertir le statut en Enum
        ProductionOrderStatus orderStatus = ProductionOrderStatus.valueOf(status);
        
        // Récupérer les ordres avec ce statut
        return productionOrderRepository.findByStatus(orderStatus).stream()
                .map(productionOrderMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
