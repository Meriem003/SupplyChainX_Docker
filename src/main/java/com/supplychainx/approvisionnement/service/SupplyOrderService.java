package com.supplychainx.approvisionnement.service;

import com.supplychainx.approvisionnement.dto.*;
import com.supplychainx.approvisionnement.entity.RawMaterial;
import com.supplychainx.approvisionnement.entity.Supplier;
import com.supplychainx.approvisionnement.entity.SupplyOrder;
import com.supplychainx.approvisionnement.enums.SupplyOrderStatus;
import com.supplychainx.approvisionnement.repository.RawMaterialRepository;
import com.supplychainx.approvisionnement.repository.SupplierRepository;
import com.supplychainx.approvisionnement.repository.SupplyOrderRepository;
import com.supplychainx.exception.BusinessRuleException;
import com.supplychainx.exception.ResourceNotFoundException;
import com.supplychainx.mapper.RawMaterialMapper;
import com.supplychainx.mapper.SupplierMapper;
import com.supplychainx.mapper.SupplyOrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service pour gérer les commandes d'approvisionnement
 * US13: Créer une commande
 * US14: Modifier une commande
 * US15: Supprimer une commande si elle n'a pas été livrée
 * US16: Consulter toutes les commandes
 */
@Service
@RequiredArgsConstructor
public class SupplyOrderService {

    private final SupplyOrderRepository supplyOrderRepository;
    private final SupplierRepository supplierRepository;
    private final RawMaterialRepository rawMaterialRepository;
    private final SupplyOrderMapper supplyOrderMapper;
    private final SupplierMapper supplierMapper;
    private final RawMaterialMapper rawMaterialMapper;

    /**
     * US13: Créer une commande d'approvisionnement
     */
    @Transactional
    public SupplyOrderResponseDTO createSupplyOrder(SupplyOrderCreateDTO dto) {
        // Vérifier que le fournisseur existe
        Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Fournisseur non trouvé avec l'ID: " + dto.getSupplierId()));

        // Vérifier que les matières premières existent
        List<RawMaterial> materials = rawMaterialRepository.findAllById(dto.getMaterialIds());
        if (materials.size() != dto.getMaterialIds().size()) {
            throw new ResourceNotFoundException("Certaines matières premières n'existent pas");
        }

        // Créer la commande
        SupplyOrder order = new SupplyOrder();
        order.setSupplier(supplier);
        order.setMaterials(materials);
        order.setOrderDate(dto.getOrderDate());
        order.setStatus(SupplyOrderStatus.valueOf(dto.getStatus()));

        SupplyOrder savedOrder = supplyOrderRepository.save(order);
        return supplyOrderMapper.toResponseDTO(savedOrder);
    }

    /**
     * US14: Modifier une commande existante
     */
    @Transactional
    public SupplyOrderResponseDTO updateSupplyOrder(Long id, SupplyOrderUpdateDTO dto) {
        // Vérifier que la commande existe
        SupplyOrder order = supplyOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Commande non trouvée avec l'ID: " + id));

        // Vérifier que le fournisseur existe
        Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Fournisseur non trouvé avec l'ID: " + dto.getSupplierId()));

        // Vérifier que les matières premières existent
        List<RawMaterial> materials = rawMaterialRepository.findAllById(dto.getMaterialIds());
        if (materials.size() != dto.getMaterialIds().size()) {
            throw new ResourceNotFoundException("Certaines matières premières n'existent pas");
        }

        // Mettre à jour la commande
        order.setSupplier(supplier);
        order.setMaterials(materials);
        order.setOrderDate(dto.getOrderDate());
        order.setStatus(SupplyOrderStatus.valueOf(dto.getStatus()));

        SupplyOrder updatedOrder = supplyOrderRepository.save(order);
        return supplyOrderMapper.toResponseDTO(updatedOrder);
    }

    /**
     * US15: Supprimer une commande si elle n'a pas été livrée
     */
    @Transactional
    public void deleteSupplyOrder(Long id) {
        // Vérifier que la commande existe
        SupplyOrder order = supplyOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Commande non trouvée avec l'ID: " + id));

        // Règle métier: on ne peut supprimer que si le statut n'est pas RECUE
        if (order.getStatus() == SupplyOrderStatus.RECUE) {
            throw new BusinessRuleException(
                    "Impossible de supprimer une commande déjà livrée (statut RECUE)");
        }

        supplyOrderRepository.delete(order);
    }

    /**
     * US16: Consulter toutes les commandes d'approvisionnement
     */
    @Transactional(readOnly = true)
    public List<SupplyOrderResponseDTO> getAllSupplyOrders() {
        return supplyOrderRepository.findAll().stream()
                .map(supplyOrderMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * US17: Suivre le statut des commandes (filtrage par statut)
     */
    @Transactional(readOnly = true)
    public List<SupplyOrderResponseDTO> getSupplyOrdersByStatus(String status) {
        // Convertir le statut en Enum
        SupplyOrderStatus orderStatus = SupplyOrderStatus.valueOf(status);
        
        // Récupérer les commandes avec ce statut
        return supplyOrderRepository.findByStatus(orderStatus).stream()
                .map(supplyOrderMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
