package com.supplychainx.approvisionnement.service;

import com.supplychainx.approvisionnement.dto.SupplierCreateDTO;
import com.supplychainx.approvisionnement.dto.SupplierResponseDTO;
import com.supplychainx.approvisionnement.dto.SupplierUpdateDTO;
import com.supplychainx.approvisionnement.entity.Supplier;
import com.supplychainx.approvisionnement.entity.SupplyOrder;
import com.supplychainx.approvisionnement.enums.SupplyOrderStatus;
import com.supplychainx.approvisionnement.repository.SupplierRepository;
import com.supplychainx.exception.BusinessRuleException;
import com.supplychainx.exception.ResourceNotFoundException;
import com.supplychainx.mapper.SupplierMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour SupplierService
 * Teste les US3, US4, US5, US6, US7
 */
@ExtendWith(MockitoExtension.class)
class SupplierServiceTest {

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private SupplierMapper supplierMapper;

    @InjectMocks
    private SupplierService supplierService;

    private Supplier supplier;
    private SupplierCreateDTO createDTO;
    private SupplierUpdateDTO updateDTO;
    private SupplierResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        // Initialisation du fournisseur de test
        supplier = new Supplier();
        supplier.setIdSupplier(1L);
        supplier.setName("Fournisseur Test");
        supplier.setContact("contact@test.com");
        supplier.setRating(4.5);
        supplier.setLeadTime(5);
        supplier.setOrders(new ArrayList<>());

        // DTO de création
        createDTO = new SupplierCreateDTO();
        createDTO.setName("Nouveau Fournisseur");
        createDTO.setContact("nouveau@test.com");
        createDTO.setRating(4.0);
        createDTO.setLeadTime(7);

        // DTO de mise à jour
        updateDTO = new SupplierUpdateDTO();
        updateDTO.setName("Fournisseur Modifié");
        updateDTO.setContact("modifie@test.com");
        updateDTO.setRating(4.8);
        updateDTO.setLeadTime(3);

        // DTO de réponse
        responseDTO = new SupplierResponseDTO();
        responseDTO.setIdSupplier(1L);
        responseDTO.setName("Fournisseur Test");
        responseDTO.setContact("contact@test.com");
        responseDTO.setRating(4.5);
        responseDTO.setLeadTime(5);
    }

    // ==================== US3: Créer un fournisseur ====================
    
    @Test
    @DisplayName("US3 - Créer un fournisseur avec succès")
    void testCreateSupplier_Success() {
        // Given
        when(supplierRepository.save(any(Supplier.class))).thenReturn(supplier);
        when(supplierMapper.toResponseDTO(supplier)).thenReturn(responseDTO);

        // When
        SupplierResponseDTO result = supplierService.createSupplier(createDTO);

        // Then
        assertNotNull(result);
        assertEquals(responseDTO.getName(), result.getName());
        verify(supplierRepository, times(1)).save(any(Supplier.class));
        verify(supplierMapper, times(1)).toResponseDTO(supplier);
    }

    @Test
    @DisplayName("US3 - Créer un fournisseur avec toutes les informations obligatoires")
    void testCreateSupplier_WithAllRequiredFields() {
        // Given
        when(supplierRepository.save(any(Supplier.class))).thenReturn(supplier);
        when(supplierMapper.toResponseDTO(supplier)).thenReturn(responseDTO);

        // When
        SupplierResponseDTO result = supplierService.createSupplier(createDTO);

        // Then
        assertNotNull(result);
        assertNotNull(result.getName());
        assertNotNull(result.getContact());
        assertNotNull(result.getRating());
        assertNotNull(result.getLeadTime());
        verify(supplierRepository, times(1)).save(any(Supplier.class));
    }

    // ==================== US4: Modifier un fournisseur ====================
    
    @Test
    @DisplayName("US4 - Modifier un fournisseur existant avec succès")
    void testUpdateSupplier_Success() {
        // Given
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(supplierRepository.save(any(Supplier.class))).thenReturn(supplier);
        when(supplierMapper.toResponseDTO(supplier)).thenReturn(responseDTO);

        // When
        SupplierResponseDTO result = supplierService.updateSupplier(1L, updateDTO);

        // Then
        assertNotNull(result);
        verify(supplierRepository, times(1)).findById(1L);
        verify(supplierRepository, times(1)).save(supplier);
        verify(supplierMapper, times(1)).toResponseDTO(supplier);
    }

    @Test
    @DisplayName("US4 - Modifier un fournisseur inexistant doit lever une exception")
    void testUpdateSupplier_NotFound() {
        // Given
        when(supplierRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            supplierService.updateSupplier(999L, updateDTO);
        });
        verify(supplierRepository, times(1)).findById(999L);
        verify(supplierRepository, never()).save(any(Supplier.class));
    }

    // ==================== US5: Supprimer un fournisseur ====================
    
    @Test
    @DisplayName("US5 - Supprimer un fournisseur sans commandes actives")
    void testDeleteSupplier_WithoutActiveOrders_Success() {
        // Given
        SupplyOrder completedOrder = new SupplyOrder();
        completedOrder.setStatus(SupplyOrderStatus.RECUE);
        supplier.getOrders().add(completedOrder);

        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        doNothing().when(supplierRepository).delete(supplier);

        // When
        supplierService.deleteSupplier(1L);

        // Then
        verify(supplierRepository, times(1)).findById(1L);
        verify(supplierRepository, times(1)).delete(supplier);
    }

    @Test
    @DisplayName("US5 - Supprimer un fournisseur avec commandes EN_ATTENTE doit échouer")
    void testDeleteSupplier_WithPendingOrders_ShouldFail() {
        // Given
        SupplyOrder pendingOrder = new SupplyOrder();
        pendingOrder.setStatus(SupplyOrderStatus.EN_ATTENTE);
        supplier.getOrders().add(pendingOrder);

        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));

        // When & Then
        assertThrows(BusinessRuleException.class, () -> {
            supplierService.deleteSupplier(1L);
        });
        verify(supplierRepository, times(1)).findById(1L);
        verify(supplierRepository, never()).delete(any(Supplier.class));
    }

    @Test
    @DisplayName("US5 - Supprimer un fournisseur avec commandes EN_COURS doit échouer")
    void testDeleteSupplier_WithInProgressOrders_ShouldFail() {
        // Given
        SupplyOrder inProgressOrder = new SupplyOrder();
        inProgressOrder.setStatus(SupplyOrderStatus.EN_COURS);
        supplier.getOrders().add(inProgressOrder);

        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));

        // When & Then
        assertThrows(BusinessRuleException.class, () -> {
            supplierService.deleteSupplier(1L);
        });
        verify(supplierRepository, never()).delete(any(Supplier.class));
    }

    @Test
    @DisplayName("US5 - Supprimer un fournisseur inexistant doit lever une exception")
    void testDeleteSupplier_NotFound() {
        // Given
        when(supplierRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            supplierService.deleteSupplier(999L);
        });
        verify(supplierRepository, never()).delete(any(Supplier.class));
    }

    // ==================== US6: Consulter tous les fournisseurs ====================
    
    @Test
    @DisplayName("US6 - Récupérer la liste de tous les fournisseurs")
    void testGetAllSuppliers_Success() {
        // Given
        Supplier supplier2 = new Supplier();
        supplier2.setIdSupplier(2L);
        supplier2.setName("Fournisseur 2");
        
        List<Supplier> suppliers = Arrays.asList(supplier, supplier2);
        
        SupplierResponseDTO responseDTO2 = new SupplierResponseDTO();
        responseDTO2.setIdSupplier(2L);
        responseDTO2.setName("Fournisseur 2");
        
        when(supplierRepository.findAll()).thenReturn(suppliers);
        when(supplierMapper.toResponseDTO(supplier)).thenReturn(responseDTO);
        when(supplierMapper.toResponseDTO(supplier2)).thenReturn(responseDTO2);

        // When
        List<SupplierResponseDTO> result = supplierService.getAllSuppliers();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(supplierRepository, times(1)).findAll();
        verify(supplierMapper, times(2)).toResponseDTO(any(Supplier.class));
    }

    @Test
    @DisplayName("US6 - Récupérer une liste vide si aucun fournisseur")
    void testGetAllSuppliers_EmptyList() {
        // Given
        when(supplierRepository.findAll()).thenReturn(new ArrayList<>());

        // When
        List<SupplierResponseDTO> result = supplierService.getAllSuppliers();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(supplierRepository, times(1)).findAll();
    }

    // ==================== US7: Rechercher un fournisseur par nom ====================
    
    @Test
    @DisplayName("US7 - Rechercher un fournisseur par nom avec succès")
    void testSearchSuppliersByName_Success() {
        // Given
        List<Supplier> suppliers = Arrays.asList(supplier);
        when(supplierRepository.findByNameContainingIgnoreCase("Test")).thenReturn(suppliers);
        when(supplierMapper.toResponseDTO(supplier)).thenReturn(responseDTO);

        // When
        List<SupplierResponseDTO> result = supplierService.searchSuppliersByName("Test");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Fournisseur Test", result.get(0).getName());
        verify(supplierRepository, times(1)).findByNameContainingIgnoreCase("Test");
    }

    @Test
    @DisplayName("US7 - Rechercher un fournisseur - aucun résultat")
    void testSearchSuppliersByName_NoResults() {
        // Given
        when(supplierRepository.findByNameContainingIgnoreCase("Inexistant")).thenReturn(new ArrayList<>());

        // When
        List<SupplierResponseDTO> result = supplierService.searchSuppliersByName("Inexistant");

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(supplierRepository, times(1)).findByNameContainingIgnoreCase("Inexistant");
    }

    @Test
    @DisplayName("US7 - Rechercher un fournisseur - insensible à la casse")
    void testSearchSuppliersByName_CaseInsensitive() {
        // Given
        List<Supplier> suppliers = Arrays.asList(supplier);
        when(supplierRepository.findByNameContainingIgnoreCase("test")).thenReturn(suppliers);
        when(supplierMapper.toResponseDTO(supplier)).thenReturn(responseDTO);

        // When
        List<SupplierResponseDTO> result = supplierService.searchSuppliersByName("test");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(supplierRepository, times(1)).findByNameContainingIgnoreCase("test");
    }
}
