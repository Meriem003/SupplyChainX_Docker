package com.supplychainx.production.service;

import com.supplychainx.approvisionnement.entity.RawMaterial;
import com.supplychainx.approvisionnement.repository.RawMaterialRepository;
import com.supplychainx.exception.ResourceNotFoundException;
import com.supplychainx.mapper.BillOfMaterialMapper;
import com.supplychainx.production.dto.BillOfMaterialRequestDTO;
import com.supplychainx.production.dto.BillOfMaterialResponseDTO;
import com.supplychainx.production.entity.BillOfMaterial;
import com.supplychainx.production.entity.Product;
import com.supplychainx.production.repository.BillOfMaterialRepository;
import com.supplychainx.production.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service pour gérer les nomenclatures (BOM - Bill of Materials)
 * Permet d'associer des matières premières aux produits finis
 * Utilisé pour la planification (US28) et le calcul des besoins en matières
 */
@Service
@RequiredArgsConstructor
public class BillOfMaterialService {

    private final BillOfMaterialRepository billOfMaterialRepository;
    private final ProductRepository productRepository;
    private final RawMaterialRepository rawMaterialRepository;
    private final BillOfMaterialMapper billOfMaterialMapper;

    /**
     * Ajouter une ligne de nomenclature (associer une matière première à un produit)
     * 
     * @param dto Données de la nomenclature
     * @return La nomenclature créée
     */
    @Transactional
    public BillOfMaterialResponseDTO createBillOfMaterial(BillOfMaterialRequestDTO dto) {
        // Vérifier que le produit existe
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Produit non trouvé avec l'ID: " + dto.getProductId()));

        // Vérifier que la matière première existe
        RawMaterial material = rawMaterialRepository.findById(dto.getMaterialId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Matière première non trouvée avec l'ID: " + dto.getMaterialId()));

        // Créer la nomenclature via le mapper
        BillOfMaterial bom = billOfMaterialMapper.toEntity(dto);
        bom.setProduct(product);
        bom.setMaterial(material);

        BillOfMaterial savedBom = billOfMaterialRepository.save(bom);
        return billOfMaterialMapper.toResponseDTO(savedBom);
    }

    /**
     * Modifier une ligne de nomenclature existante
     * 
     * @param id Identifiant de la nomenclature
     * @param dto Nouvelles données
     * @return La nomenclature mise à jour
     */
    @Transactional
    public BillOfMaterialResponseDTO updateBillOfMaterial(Long id, BillOfMaterialRequestDTO dto) {
        // Vérifier que la nomenclature existe
        BillOfMaterial bom = billOfMaterialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Nomenclature non trouvée avec l'ID: " + id));

        // Vérifier que le produit existe
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Produit non trouvé avec l'ID: " + dto.getProductId()));

        // Vérifier que la matière première existe
        RawMaterial material = rawMaterialRepository.findById(dto.getMaterialId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Matière première non trouvée avec l'ID: " + dto.getMaterialId()));

        // Mettre à jour via le mapper
        billOfMaterialMapper.updateEntityFromDTO(dto, bom);
        bom.setProduct(product);
        bom.setMaterial(material);

        BillOfMaterial updatedBom = billOfMaterialRepository.save(bom);
        return billOfMaterialMapper.toResponseDTO(updatedBom);
    }

    /**
     * Supprimer une ligne de nomenclature
     * 
     * @param id Identifiant de la nomenclature
     */
    @Transactional
    public void deleteBillOfMaterial(Long id) {
        BillOfMaterial bom = billOfMaterialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Nomenclature non trouvée avec l'ID: " + id));

        billOfMaterialRepository.delete(bom);
    }

    /**
     * Consulter toutes les lignes de nomenclature
     * 
     * @return Liste de toutes les nomenclatures
     */
    @Transactional(readOnly = true)
    public List<BillOfMaterialResponseDTO> getAllBillOfMaterials() {
        return billOfMaterialRepository.findAll().stream()
                .map(billOfMaterialMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Consulter la nomenclature (BOM) d'un produit spécifique
     * Utile pour US28: vérifier les matières nécessaires avant de lancer un ordre
     * 
     * @param productId Identifiant du produit
     * @return Liste des matières nécessaires pour fabriquer ce produit
     */
    @Transactional(readOnly = true)
    public List<BillOfMaterialResponseDTO> getBillOfMaterialsByProduct(Long productId) {
        // Vérifier que le produit existe
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Produit non trouvé avec l'ID: " + productId));

        return billOfMaterialRepository.findByProduct(product).stream()
                .map(billOfMaterialMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
