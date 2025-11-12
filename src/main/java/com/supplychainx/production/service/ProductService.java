package com.supplychainx.production.service;

import com.supplychainx.exception.BusinessRuleException;
import com.supplychainx.exception.ResourceNotFoundException;
import com.supplychainx.production.dto.ProductCreateDTO;
import com.supplychainx.production.dto.ProductResponseDTO;
import com.supplychainx.production.dto.ProductUpdateDTO;
import com.supplychainx.production.entity.Product;
import com.supplychainx.production.entity.ProductionOrder;
import com.supplychainx.production.repository.ProductRepository;
import com.supplychainx.production.repository.ProductionOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service pour gérer les produits finis
 * US18: Ajouter un produit
 * US19: Modifier un produit
 * US20: Supprimer un produit (s'il n'existe aucun ordre associé)
 * US21: Consulter tous les produits
 * US22: Rechercher un produit par nom
 */
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductionOrderRepository productionOrderRepository;

    /**
     * US18: Ajouter un produit fini avec toutes ses informations
     */
    @Transactional
    public ProductResponseDTO createProduct(ProductCreateDTO dto) {
        // Créer le produit
        Product product = new Product();
        product.setName(dto.getName());
        product.setProductionTime(dto.getProductionTime());
        product.setCost(dto.getCost());
        product.setStock(dto.getStock());

        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }

    /**
     * US19: Modifier un produit existant
     */
    @Transactional
    public ProductResponseDTO updateProduct(Long id, ProductUpdateDTO dto) {
        // Vérifier que le produit existe
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Produit non trouvé avec l'ID: " + id));

        // Mettre à jour les informations
        product.setName(dto.getName());
        product.setProductionTime(dto.getProductionTime());
        product.setCost(dto.getCost());
        product.setStock(dto.getStock());

        Product updatedProduct = productRepository.save(product);
        return convertToDTO(updatedProduct);
    }

    /**
     * US20: Supprimer un produit (s'il n'existe aucun ordre associé)
     */
    @Transactional
    public void deleteProduct(Long id) {
        // Vérifier que le produit existe
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Produit non trouvé avec l'ID: " + id));

        // Règle métier: vérifier qu'il n'y a pas d'ordre de production associé
        List<ProductionOrder> orders = productionOrderRepository.findByProduct(product);
        if (!orders.isEmpty()) {
            throw new BusinessRuleException(
                    "Impossible de supprimer le produit car il a " + orders.size() + 
                    " ordre(s) de production associé(s)");
        }

        productRepository.delete(product);
    }

    /**
     * US21: Consulter la liste de tous les produits
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * US22: Rechercher un produit par nom
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Conversion d'une entité Product en DTO
     */
    private ProductResponseDTO convertToDTO(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setIdProduct(product.getIdProduct());
        dto.setName(product.getName());
        dto.setProductionTime(product.getProductionTime());
        dto.setCost(product.getCost());
        dto.setStock(product.getStock());
        return dto;
    }
}
