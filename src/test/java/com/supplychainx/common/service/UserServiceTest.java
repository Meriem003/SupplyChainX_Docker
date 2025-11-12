package com.supplychainx.common.service;

import com.supplychainx.common.dto.UpdateRoleDTO;
import com.supplychainx.common.dto.UserCreateDTO;
import com.supplychainx.common.dto.UserResponseDTO;
import com.supplychainx.common.entity.User;
import com.supplychainx.common.enums.UserRole;
import com.supplychainx.common.repository.UserRepository;
import com.supplychainx.exception.BusinessRuleException;
import com.supplychainx.exception.ResourceNotFoundException;
import com.supplychainx.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour UserService
 * Teste les US1 et US2
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserCreateDTO createDTO;
    private UpdateRoleDTO updateRoleDTO;
    private UserResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        // Initialisation de l'utilisateur de test
        user = new User();
        user.setIdUser(1L);
        user.setFirstName("Jean");
        user.setLastName("Dupont");
        user.setEmail("jean.dupont@supplychainx.com");
        user.setPassword("$2a$10$encodedPassword");
        user.setRole(UserRole.CHEF_PRODUCTION);

        // DTO de création
        createDTO = new UserCreateDTO();
        createDTO.setFirstName("Marie");
        createDTO.setLastName("Martin");
        createDTO.setEmail("marie.martin@supplychainx.com");
        createDTO.setPassword("password123");
        createDTO.setRole(UserRole.GESTIONNAIRE_APPROVISIONNEMENT);

        // DTO de mise à jour du rôle
        updateRoleDTO = new UpdateRoleDTO();
        updateRoleDTO.setRole(UserRole.ADMIN);

        // DTO de réponse
        responseDTO = new UserResponseDTO();
        responseDTO.setIdUser(1L);
        responseDTO.setFirstName("Jean");
        responseDTO.setLastName("Dupont");
        responseDTO.setEmail("jean.dupont@supplychainx.com");
        responseDTO.setRole("CHEF_PRODUCTION");
    }

    // ==================== US1: Créer un utilisateur ====================
    
    @Test
    @DisplayName("US1 - Créer un utilisateur avec succès")
    void testCreateUser_Success() {
        // Given
        when(userRepository.findByEmail(createDTO.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(createDTO.getPassword())).thenReturn("$2a$10$encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toResponseDTO(user)).thenReturn(responseDTO);

        // When
        UserResponseDTO result = userService.createUser(createDTO);

        // Then
        assertNotNull(result);
        assertEquals(responseDTO.getEmail(), result.getEmail());
        verify(userRepository, times(1)).findByEmail(createDTO.getEmail());
        verify(passwordEncoder, times(1)).encode(createDTO.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
        verify(userMapper, times(1)).toResponseDTO(user);
    }

    @Test
    @DisplayName("US1 - Créer un utilisateur avec un email déjà existant doit échouer")
    void testCreateUser_EmailAlreadyExists_ShouldFail() {
        // Given
        when(userRepository.findByEmail(createDTO.getEmail())).thenReturn(Optional.of(user));

        // When & Then
        assertThrows(BusinessRuleException.class, () -> {
            userService.createUser(createDTO);
        });
        verify(userRepository, times(1)).findByEmail(createDTO.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("US1 - Créer un utilisateur avec le rôle ADMIN")
    void testCreateUser_WithAdminRole() {
        // Given
        createDTO.setRole(UserRole.ADMIN);
        when(userRepository.findByEmail(createDTO.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(createDTO.getPassword())).thenReturn("$2a$10$encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toResponseDTO(user)).thenReturn(responseDTO);

        // When
        UserResponseDTO result = userService.createUser(createDTO);

        // Then
        assertNotNull(result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("US1 - Créer un utilisateur avec le rôle CHEF_PRODUCTION")
    void testCreateUser_WithChefProductionRole() {
        // Given
        createDTO.setRole(UserRole.CHEF_PRODUCTION);
        when(userRepository.findByEmail(createDTO.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(createDTO.getPassword())).thenReturn("$2a$10$encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toResponseDTO(user)).thenReturn(responseDTO);

        // When
        UserResponseDTO result = userService.createUser(createDTO);

        // Then
        assertNotNull(result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("US1 - Créer un utilisateur avec le rôle GESTIONNAIRE_APPROVISIONNEMENT")
    void testCreateUser_WithGestionnaireApprovisionnementRole() {
        // Given
        createDTO.setRole(UserRole.GESTIONNAIRE_APPROVISIONNEMENT);
        when(userRepository.findByEmail(createDTO.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(createDTO.getPassword())).thenReturn("$2a$10$encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toResponseDTO(user)).thenReturn(responseDTO);

        // When
        UserResponseDTO result = userService.createUser(createDTO);

        // Then
        assertNotNull(result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("US1 - Créer un utilisateur avec le rôle GESTIONNAIRE_COMMERCIAL")
    void testCreateUser_WithGestionnaireCommercialRole() {
        // Given
        createDTO.setRole(UserRole.GESTIONNAIRE_COMMERCIAL);
        when(userRepository.findByEmail(createDTO.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(createDTO.getPassword())).thenReturn("$2a$10$encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toResponseDTO(user)).thenReturn(responseDTO);

        // When
        UserResponseDTO result = userService.createUser(createDTO);

        // Then
        assertNotNull(result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("US1 - Vérifier que le mot de passe est crypté lors de la création")
    void testCreateUser_PasswordIsEncoded() {
        // Given
        String plainPassword = "password123";
        String encodedPassword = "$2a$10$encodedPassword";
        
        createDTO.setPassword(plainPassword);
        when(userRepository.findByEmail(createDTO.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(plainPassword)).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toResponseDTO(user)).thenReturn(responseDTO);

        // When
        userService.createUser(createDTO);

        // Then
        verify(passwordEncoder, times(1)).encode(plainPassword);
        verify(userRepository, times(1)).save(any(User.class));
    }

    // ==================== US2: Modifier le rôle d'un utilisateur ====================
    
    @Test
    @DisplayName("US2 - Modifier le rôle d'un utilisateur avec succès")
    void testUpdateUserRole_Success() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toResponseDTO(user)).thenReturn(responseDTO);

        // When
        UserResponseDTO result = userService.updateUserRole(1L, updateRoleDTO);

        // Then
        assertNotNull(result);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).toResponseDTO(user);
    }

    @Test
    @DisplayName("US2 - Modifier le rôle d'un utilisateur inexistant doit lever une exception")
    void testUpdateUserRole_UserNotFound() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.updateUserRole(999L, updateRoleDTO);
        });
        verify(userRepository, times(1)).findById(999L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("US2 - Changer le rôle de CHEF_PRODUCTION vers ADMIN")
    void testUpdateUserRole_FromChefProductionToAdmin() {
        // Given
        user.setRole(UserRole.CHEF_PRODUCTION);
        updateRoleDTO.setRole(UserRole.ADMIN);
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toResponseDTO(user)).thenReturn(responseDTO);

        // When
        UserResponseDTO result = userService.updateUserRole(1L, updateRoleDTO);

        // Then
        assertNotNull(result);
        verify(userRepository, times(1)).save(user);
        assertEquals(UserRole.ADMIN, user.getRole());
    }

    @Test
    @DisplayName("US2 - Changer le rôle de GESTIONNAIRE_APPROVISIONNEMENT vers GESTIONNAIRE_COMMERCIAL")
    void testUpdateUserRole_FromGestionnaireApprovisionnementToCommercial() {
        // Given
        user.setRole(UserRole.GESTIONNAIRE_APPROVISIONNEMENT);
        updateRoleDTO.setRole(UserRole.GESTIONNAIRE_COMMERCIAL);
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toResponseDTO(user)).thenReturn(responseDTO);

        // When
        UserResponseDTO result = userService.updateUserRole(1L, updateRoleDTO);

        // Then
        assertNotNull(result);
        verify(userRepository, times(1)).save(user);
        assertEquals(UserRole.GESTIONNAIRE_COMMERCIAL, user.getRole());
    }

    @Test
    @DisplayName("US2 - Changer le rôle d'un ADMIN vers CHEF_PRODUCTION")
    void testUpdateUserRole_FromAdminToChefProduction() {
        // Given
        user.setRole(UserRole.ADMIN);
        updateRoleDTO.setRole(UserRole.CHEF_PRODUCTION);
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toResponseDTO(user)).thenReturn(responseDTO);

        // When
        UserResponseDTO result = userService.updateUserRole(1L, updateRoleDTO);

        // Then
        assertNotNull(result);
        verify(userRepository, times(1)).save(user);
        assertEquals(UserRole.CHEF_PRODUCTION, user.getRole());
    }

    @Test
    @DisplayName("US2 - Changer vers le rôle GESTIONNAIRE_APPROVISIONNEMENT")
    void testUpdateUserRole_ToGestionnaireApprovisionnement() {
        // Given
        updateRoleDTO.setRole(UserRole.GESTIONNAIRE_APPROVISIONNEMENT);
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toResponseDTO(user)).thenReturn(responseDTO);

        // When
        UserResponseDTO result = userService.updateUserRole(1L, updateRoleDTO);

        // Then
        assertNotNull(result);
        verify(userRepository, times(1)).save(user);
        assertEquals(UserRole.GESTIONNAIRE_APPROVISIONNEMENT, user.getRole());
    }
}
