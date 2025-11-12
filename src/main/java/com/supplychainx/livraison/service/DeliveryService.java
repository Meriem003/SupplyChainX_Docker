package com.supplychainx.livraison.service;

import com.supplychainx.exception.ResourceNotFoundException;
import com.supplychainx.livraison.dto.DeliveryRequestDTO;
import com.supplychainx.livraison.dto.DeliveryResponseDTO;
import com.supplychainx.livraison.entity.Delivery;
import com.supplychainx.livraison.entity.Order;
import com.supplychainx.livraison.enums.DeliveryStatus;
import com.supplychainx.livraison.repository.DeliveryRepository;
import com.supplychainx.livraison.repository.OrderRepository;
import com.supplychainx.mapper.CustomerMapper;
import com.supplychainx.mapper.DeliveryMapper;
import com.supplychainx.mapper.OrderMapper;
import com.supplychainx.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliveryService {
    
    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;
    private final DeliveryMapper deliveryMapper;
    private final OrderMapper orderMapper;
    private final CustomerMapper customerMapper;
    private final ProductMapper productMapper;
    
    /**
     * US40 : Créer une livraison pour une commande et calculer son coût total
     */
    @Transactional
    public DeliveryResponseDTO createDelivery(DeliveryRequestDTO dto) {
        // Vérifier que la commande existe
        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Commande non trouvée avec l'ID: " + dto.getOrderId()));
        
        // Créer la livraison
        Delivery delivery = new Delivery();
        delivery.setOrder(order);
        delivery.setVehicle(dto.getVehicle());
        delivery.setDriver(dto.getDriver());
        delivery.setStatus(DeliveryStatus.valueOf(dto.getStatus()));
        delivery.setDeliveryDate(dto.getDeliveryDate());
        
        // Calculer le coût total de la livraison
        Double calculatedCost = calculateDeliveryCost(order, dto.getCost());
        delivery.setCost(calculatedCost);
        
        Delivery savedDelivery = deliveryRepository.save(delivery);
        return deliveryMapper.toResponseDTO(savedDelivery);
    }
    
    /**
     * Calculer le coût total de la livraison
     * Formule simple : coût de base fourni (ou coût du produit * quantité si non fourni)
     */
    private Double calculateDeliveryCost(Order order, Double baseCost) {
        if (baseCost != null && baseCost > 0) {
            // Si un coût est fourni, on l'utilise
            return baseCost;
        }
        
        // Sinon, on calcule un coût basé sur le produit et la quantité
        // Coût = coût unitaire du produit × quantité × facteur de livraison (1.1 pour 10% de frais)
        Double productCost = order.getProduct().getCost();
        Integer quantity = order.getQuantity();
        Double deliveryFactor = 1.1; // 10% de frais de livraison
        
        return productCost * quantity * deliveryFactor;
    }
}
