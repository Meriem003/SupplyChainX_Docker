package com.supplychainx.livraison.service;

import com.supplychainx.exception.BusinessRuleException;
import com.supplychainx.exception.ResourceNotFoundException;
import com.supplychainx.livraison.dto.OrderRequestDTO;
import com.supplychainx.livraison.dto.OrderResponseDTO;
import com.supplychainx.livraison.entity.Customer;
import com.supplychainx.livraison.entity.Order;
import com.supplychainx.livraison.enums.OrderStatus;
import com.supplychainx.livraison.repository.CustomerRepository;
import com.supplychainx.livraison.repository.OrderRepository;
import com.supplychainx.mapper.CustomerMapper;
import com.supplychainx.mapper.OrderMapper;
import com.supplychainx.mapper.ProductMapper;
import com.supplychainx.production.entity.Product;
import com.supplychainx.production.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final CustomerMapper customerMapper;
    private final ProductMapper productMapper;
    
    /**
     * US35 : Créer une commande client
     */
    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO dto) {
        // Vérifier que le client existe
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Client non trouvé avec l'ID: " + dto.getCustomerId()));
        
        // Vérifier que le produit existe
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Produit non trouvé avec l'ID: " + dto.getProductId()));
        
        // Créer la commande
        Order order = new Order();
        order.setCustomer(customer);
        order.setProduct(product);
        order.setQuantity(dto.getQuantity());
        order.setStatus(OrderStatus.valueOf(dto.getStatus()));
        
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toResponseDTO(savedOrder);
    }
    
    /**
     * US36 : Modifier une commande existante
     */
    @Transactional
    public OrderResponseDTO updateOrder(Long id, OrderRequestDTO dto) {
        // Vérifier que la commande existe
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Commande non trouvée avec l'ID: " + id));
        
        // Vérifier que le client existe
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Client non trouvé avec l'ID: " + dto.getCustomerId()));
        
        // Vérifier que le produit existe
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Produit non trouvé avec l'ID: " + dto.getProductId()));
        
        // Modifier la commande
        order.setCustomer(customer);
        order.setProduct(product);
        order.setQuantity(dto.getQuantity());
        order.setStatus(OrderStatus.valueOf(dto.getStatus()));
        
        Order updatedOrder = orderRepository.save(order);
        return orderMapper.toResponseDTO(updatedOrder);
    }
    
    /**
     * US37 : Annuler une commande si non expédiée
     */
    @Transactional
    public void cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Commande non trouvée avec l'ID: " + id));
        
        // Règle métier: on ne peut annuler que si le statut est EN_PREPARATION
        if (order.getStatus() != OrderStatus.EN_PREPARATION) {
            throw new BusinessRuleException(
                    "Impossible d'annuler la commande car elle a déjà été expédiée (statut: " + 
                    order.getStatus() + ")");
        }
        
        orderRepository.delete(order);
    }
    
    /**
     * US38 : Consulter la liste de toutes les commandes
     */
    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * US39 : Suivre le statut des commandes
     */
    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getOrdersByStatus(String status) {
        // Convertir le statut en Enum
        OrderStatus orderStatus = OrderStatus.valueOf(status);
        
        // Récupérer les commandes avec ce statut
        return orderRepository.findByStatus(orderStatus).stream()
                .map(orderMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
