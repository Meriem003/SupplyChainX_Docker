package com.supplychainx.security;

import com.supplychainx.common.entity.User;
import com.supplychainx.common.enums.UserRole;
import com.supplychainx.common.repository.UserRepository;
import com.supplychainx.exception.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour AuthenticationService
 */
@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder; // Mock du PasswordEncoder

    @InjectMocks
    private AuthenticationService authenticationService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setIdUser(1L);
        testUser.setFirstName("Jean");
        testUser.setLastName("Dupont");
        testUser.setEmail("jean.dupont@supplychainx.com");
        testUser.setPassword("$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy"); // Hash BCrypt de "password123"
        testUser.setRole(UserRole.CHEF_PRODUCTION);
    }

    @Test
    void testAuthenticate_WithValidCredentials_ShouldReturnUser() {
        // Given
        when(userRepository.findByEmail("jean.dupont@supplychainx.com"))
                .thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", testUser.getPassword()))
                .thenReturn(true); // Simule que le mot de passe correspond

        // When
        User result = authenticationService.authenticate("jean.dupont@supplychainx.com", "password123");

        // Then
        assertNotNull(result);
        assertEquals("Jean", result.getFirstName());
        assertEquals(UserRole.CHEF_PRODUCTION, result.getRole());
        verify(userRepository, times(1)).findByEmail("jean.dupont@supplychainx.com");
        verify(passwordEncoder, times(1)).matches("password123", testUser.getPassword());
    }

    @Test
    void testAuthenticate_WithInvalidEmail_ShouldThrowException() {
        // Given
        when(userRepository.findByEmail("wrong@email.com"))
                .thenReturn(Optional.empty());

        // When & Then
        assertThrows(UnauthorizedException.class, () -> {
            authenticationService.authenticate("wrong@email.com", "password123");
        });
    }

    @Test
    void testAuthenticate_WithInvalidPassword_ShouldThrowException() {
        // Given
        when(userRepository.findByEmail("jean.dupont@supplychainx.com"))
                .thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrongpassword", testUser.getPassword()))
                .thenReturn(false); // Simule que le mot de passe ne correspond pas

        // When & Then
        assertThrows(UnauthorizedException.class, () -> {
            authenticationService.authenticate("jean.dupont@supplychainx.com", "wrongpassword");
        });
        verify(passwordEncoder, times(1)).matches("wrongpassword", testUser.getPassword());
    }

    @Test
    void testAuthenticate_WithNullEmail_ShouldThrowException() {
        // When & Then
        assertThrows(UnauthorizedException.class, () -> {
            authenticationService.authenticate(null, "password123");
        });
    }

    @Test
    void testAuthenticate_WithEmptyPassword_ShouldThrowException() {
        // When & Then
        assertThrows(UnauthorizedException.class, () -> {
            authenticationService.authenticate("jean.dupont@supplychainx.com", "");
        });
    }

    @Test
    void testCheckRole_WithValidRole_ShouldNotThrowException() {
        // Given
        UserRole[] requiredRoles = {UserRole.CHEF_PRODUCTION, UserRole.ADMIN};

        // When & Then
        assertDoesNotThrow(() -> {
            authenticationService.checkRole(testUser, requiredRoles);
        });
    }

    @Test
    void testCheckRole_WithAdminUser_ShouldAlwaysPass() {
        // Given
        testUser.setRole(UserRole.ADMIN);
        UserRole[] requiredRoles = {UserRole.CHEF_PRODUCTION}; // L'admin n'est pas dans la liste

        // When & Then
        assertDoesNotThrow(() -> {
            authenticationService.checkRole(testUser, requiredRoles);
        });
    }

    @Test
    void testCheckRole_WithInvalidRole_ShouldThrowException() {
        // Given
        UserRole[] requiredRoles = {UserRole.GESTIONNAIRE_COMMERCIAL}; // Rôle différent

        // When & Then
        assertThrows(UnauthorizedException.class, () -> {
            authenticationService.checkRole(testUser, requiredRoles);
        });
    }
}
