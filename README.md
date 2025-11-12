# 🚀 SupplyChainX - Système de Gestion de la Supply Chain

## 📖 Description

**SupplyChainX** est une application monolithique Spring Boot qui gère l'ensemble de la chaîne d'approvisionnement, de l'achat des matières premières jusqu'à la livraison des produits finis aux clients.

### 🎯 Modules Principaux

1. **📦 Approvisionnement** - Gestion des fournisseurs, matières premières et commandes
2. **🏭 Production** - Gestion des produits finis, nomenclatures (BOM) et ordres de production
3. **🚚 Livraison** - Gestion des clients, commandes clients et livraisons

---

## 🚀 Démarrage Rapide (3 étapes)

### 1️⃣ Créer la base de données
Ouvrez MySQL et exécutez :
```sql
CREATE DATABASE supply_chainx_db;
```

### 2️⃣ Configurer le mot de passe
Ouvrez : `src/main/resources/application.properties`

Changez cette ligne :
```properties
spring.datasource.password=VOTRE_MOT_DE_PASSE
```

### 3️⃣ Démarrer l'application
Double-cliquez sur : **`start.bat`**

Ou utilisez la commande :
```bash
.\mvnw.cmd spring-boot:run
```

✅ **C'est tout !** L'application démarre sur : http://localhost:8080

---

## 📚 Guide Complet

Lisez le fichier **[GUIDE_SIMPLE.md](GUIDE_SIMPLE.md)** pour apprendre à :
- Créer vos premières entités (tables)
- Créer des API REST
- Comprendre la structure du projet

---

## � Structure du Projet

```
src/main/java/com/supplychainx/
├── security/         → 🔒 Système de sécurité AOP (NOUVEAU)
│   ├── RequiresAuth.java
│   ├── RequiresRole.java
│   ├── SecurityAspect.java
│   └── AuthenticationService.java
├── exception/        → Gestion des erreurs
├── common/           → Utilisateurs et entités communes
│   ├── entity/       → User
│   ├── repository/   → UserRepository
│   ├── service/      → UserService
│   └── controller/   → UserController
├── approvisionnement/→ Module Approvisionnement
│   ├── entity/       → Supplier, RawMaterial, SupplyOrder
│   ├── repository/   → Accès base de données
│   ├── service/      → Logique métier
│   └── controller/   → API REST
├── production/       → Module Production
│   ├── entity/       → Product, BillOfMaterial, ProductionOrder
│   ├── repository/
│   ├── service/
│   └── controller/
└── livraison/        → Module Livraison
    ├── entity/       → Customer, Order, Delivery
    ├── repository/
    ├── service/
    └── controller/
```

---

## 🔧 Technologies

- **Java 17**
- **Spring Boot 3.5.7** (Framework)
- **MySQL** (Base de données)
- **Lombok** (Simplifier le code)

---