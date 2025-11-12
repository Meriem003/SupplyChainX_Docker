package com.supplychainx.approvisionnement.service;

import com.supplychainx.approvisionnement.dto.SupplierCreateDTO;
import com.supplychainx.approvisionnement.dto.SupplierResponseDTO;
import com.supplychainx.approvisionnement.dto.SupplierUpdateDTO;
import com.supplychainx.approvisionnement.entity.Supplier;
import com.supplychainx.approvisionnement.enums.SupplyOrderStatus;
import com.supplychainx.approvisionnement.repository.SupplierRepository;
import com.supplychainx.exception.BusinessRuleException;
import com.supplychainx.exception.ResourceNotFoundException;
import com.supplychainx.mapper.SupplierMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service pour gérer les fournisseurs
 * Implémente les US3, US4, US5, US6, US7
 */
@Service
@RequiredArgsConstructor
@Transactional
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    /**
     * US3: Ajouter un fournisseur avec toutes ses informations
     * 
     * @param dto Données du fournisseur à créer
     * @return Le fournisseur créé
     */
    public SupplierResponseDTO createSupplier(SupplierCreateDTO dto) {
        // Créer l'entité Supplier
        Supplier supplier = new Supplier();
        supplier.setName(dto.getName());
        supplier.setContact(dto.getContact());
        supplier.setRating(dto.getRating());
        supplier.setLeadTime(dto.getLeadTime());

        // Sauvegarder en base de données
        supplier = supplierRepository.save(supplier);

        // Convertir et retourner le DTO de réponse via le mapper
        return supplierMapper.toResponseDTO(supplier);
    }

    /**
     * US4: Modifier un fournisseur existant
     * 
     * @param supplierId ID du fournisseur à modifier
     * @param dto Nouvelles données du fournisseur
     * @return Le fournisseur modifié
     */
    public SupplierResponseDTO updateSupplier(Long supplierId, SupplierUpdateDTO dto) {
        // Vérifier que le fournisseur existe
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Fournisseur non trouvé avec l'ID: " + supplierId));

        // Mettre à jour les informations
        supplier.setName(dto.getName());
        supplier.setContact(dto.getContact());
        supplier.setRating(dto.getRating());
        supplier.setLeadTime(dto.getLeadTime());

        // Sauvegarder
        supplier = supplierRepository.save(supplier);

        // Retourner le DTO via le mapper
        return supplierMapper.toResponseDTO(supplier);
    }

    /**
     * US5: Supprimer un fournisseur (s'il n'a aucune commande active)
     * 
     * Règle métier: Un fournisseur ne peut être supprimé que s'il n'a aucune commande active
     * (statut EN_ATTENTE ou EN_COURS)
     * 
     * @param supplierId ID du fournisseur à supprimer
     */
    public void deleteSupplier(Long supplierId) {
        // Vérifier que le fournisseur existe
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Fournisseur non trouvé avec l'ID: " + supplierId));

        // Vérifier qu'il n'a pas de commandes actives
        boolean hasActiveOrders = supplier.getOrders().stream()
                .anyMatch(order -> order.getStatus() == SupplyOrderStatus.EN_ATTENTE 
                        || order.getStatus() == SupplyOrderStatus.EN_COURS);

        if (hasActiveOrders) {
            throw new BusinessRuleException(
                    "Impossible de supprimer le fournisseur : il a des commandes actives (EN_ATTENTE ou EN_COURS)");
        }

        // Supprimer le fournisseur
        supplierRepository.delete(supplier);
    }

    /**
     * US6: Consulter la liste de tous les fournisseurs
     * 
     * @return Liste de tous les fournisseurs
     */
    public List<SupplierResponseDTO> getAllSuppliers() {
        return supplierRepository.findAll().stream()
                .map(supplierMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * US7: Rechercher un fournisseur par nom
     * 
     * @param name Nom du fournisseur (recherche partielle insensible à la casse)
     * @return Liste des fournisseurs correspondants
     */
    public List<SupplierResponseDTO> searchSuppliersByName(String name) {
        return supplierRepository.findByNameContainingIgnoreCase(name).stream()
                .map(supplierMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
