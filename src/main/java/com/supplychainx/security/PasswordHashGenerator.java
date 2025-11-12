package com.supplychainx.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Utilitaire pour générer des hashs BCrypt de mots de passe
 * 
 * Utilisation :
 * 1. Exécuter cette classe comme un programme Java (main method)
 * 2. Le hash du mot de passe sera affiché dans la console
 * 3. Copier ce hash pour l'insérer dans la base de données
 */
public class PasswordHashGenerator {

    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        
        // Générer le hash pour le mot de passe admin
        String adminPassword = "admin123";
        String adminHash = passwordEncoder.encode(adminPassword);
        
        System.out.println("=================================================");
        System.out.println("GÉNÉRATEUR DE HASH BCRYPT");
        System.out.println("=================================================");
        System.out.println();
        System.out.println("Mot de passe : " + adminPassword);
        System.out.println("Hash BCrypt  : " + adminHash);
        System.out.println();
        System.out.println("=================================================");
        System.out.println("SQL à exécuter pour créer l'admin :");
        System.out.println("=================================================");
        System.out.println();
        System.out.println("INSERT INTO users (first_name, last_name, email, password, role)");
        System.out.println("VALUES ('Admin', 'SupplyChainX', 'admin@supplychainx.com', '" + adminHash + "', 'ADMIN');");
        System.out.println();
        System.out.println("=================================================");
        System.out.println();
        
        // Générer quelques autres exemples
        System.out.println("Autres exemples de hashs :");
        System.out.println("=================================================");
        
        String[] passwords = {"password123", "test1234", "securepass"};
        for (String password : passwords) {
            String hash = passwordEncoder.encode(password);
            System.out.println("Mot de passe : " + password);
            System.out.println("Hash         : " + hash);
            System.out.println();
        }
        
        // Démonstration que le même mot de passe donne un hash différent à chaque fois
        System.out.println("=================================================");
        System.out.println("DÉMONSTRATION : Même mot de passe → Hashs différents");
        System.out.println("=================================================");
        System.out.println();
        System.out.println("Mot de passe : admin123");
        for (int i = 1; i <= 3; i++) {
            String hash = passwordEncoder.encode("admin123");
            System.out.println("Hash " + i + "       : " + hash);
        }
        System.out.println();
        System.out.println("Note : BCrypt ajoute un 'sel' aléatoire, donc chaque hash est unique");
        System.out.println("      même pour le même mot de passe. C'est normal et sécurisé !");
    }
}
