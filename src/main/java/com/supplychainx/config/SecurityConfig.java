package com.supplychainx.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration pour la sécurité
 * 
 * Cette classe configure le cryptage des mots de passe avec BCrypt
 */
@Configuration
public class SecurityConfig {

    /**
     * Bean pour crypter et vérifier les mots de passe
     * 
     * BCrypt est un algorithme de hachage sécurisé :
     * - Il ajoute du "sel" automatiquement (sécurité renforcée)
     * - Il est lent exprès (empêche les attaques par force brute)
     * - Chaque cryptage donne un résultat différent (même pour le même mot de passe)
     * 
     * Exemple :
     * - password123 → $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
     * - password123 → $2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi (différent !)
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
