package com.supplychainx.security;

import com.supplychainx.common.entity.User;
import com.supplychainx.common.enums.UserRole;
import com.supplychainx.common.repository.UserRepository;
import com.supplychainx.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service pour gérer l'authentification des utilisateurs
 * Vérifie l'email et le mot de passe fournis dans les headers HTTP
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Pour vérifier les mots de passe cryptés

    /**
     * Authentifie un utilisateur avec son email et mot de passe
     * @param email Email de l'utilisateur
     * @param password Mot de passe de l'utilisateur
     * @return L'utilisateur authentifié
     * @throws UnauthorizedException Si les identifiants sont incorrects
     */
    public User authenticate(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            throw new UnauthorizedException("Email est requis dans le header");
        }
        
        if (password == null || password.trim().isEmpty()) {
            throw new UnauthorizedException("Mot de passe est requis dans le header");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Email ou mot de passe incorrect"));

        // Vérification du mot de passe crypté avec BCrypt
        // passwordEncoder.matches() compare le mot de passe en clair avec le hash BCrypt stocké en base
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UnauthorizedException("Email ou mot de passe incorrect");
        }

        return user;
    }

    /**
     * Vérifie si un utilisateur a l'un des rôles requis
     * @param user L'utilisateur à vérifier
     * @param requiredRoles Les rôles autorisés
     * @throws UnauthorizedException Si l'utilisateur n'a pas les permissions
     */
    public void checkRole(User user, UserRole[] requiredRoles) {
        // L'admin a accès à tout
        if (user.getRole() == UserRole.ADMIN) {
            return;
        }

        // Vérifier si le rôle de l'utilisateur est dans la liste des rôles autorisés
        for (UserRole role : requiredRoles) {
            if (user.getRole() == role) {
                return;
            }
        }

        throw new UnauthorizedException("Vous n'avez pas la permission d'accéder à cette ressource");
    }
}
