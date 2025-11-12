package com.supplychainx.common.service;

import com.supplychainx.common.dto.UpdateRoleDTO;
import com.supplychainx.common.dto.UserCreateDTO;
import com.supplychainx.common.dto.UserResponseDTO;
import com.supplychainx.common.entity.User;
import com.supplychainx.common.repository.UserRepository;
import com.supplychainx.exception.BusinessRuleException;
import com.supplychainx.exception.ResourceNotFoundException;
import com.supplychainx.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service pour gérer les utilisateurs
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Pour crypter les mots de passe
    private final UserMapper userMapper; // Mapper pour les conversions Entity <-> DTO

    /**
     * Crée un nouveau compte utilisateur
     * US1: En tant qu'administrateur, je veux créer un compte utilisateur avec un rôle spécifique
     */
    public UserResponseDTO createUser(UserCreateDTO dto) {
        // Vérifier que l'email n'est pas déjà utilisé
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new BusinessRuleException("Un utilisateur avec cet email existe déjà");
        }

        // Créer l'utilisateur
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // Cryptage BCrypt du mot de passe
        user.setRole(dto.getRole());

        // Sauvegarder
        user = userRepository.save(user);

        // Retourner le DTO via le mapper
        return userMapper.toResponseDTO(user);
    }

    /**
     * Modifie le rôle d'un utilisateur existant
     * US2: En tant qu'administrateur, je veux modifier le rôle d'un utilisateur existant
     */
    public UserResponseDTO updateUserRole(Long userId, UpdateRoleDTO dto) {
        // Récupérer l'utilisateur
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID: " + userId));

        // Mettre à jour le rôle
        user.setRole(dto.getRole());

        // Sauvegarder
        user = userRepository.save(user);

        // Retourner le DTO via le mapper
        return userMapper.toResponseDTO(user);
    }
}