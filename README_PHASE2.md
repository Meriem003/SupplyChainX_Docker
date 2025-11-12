# 🚀 Guide Pas-à-Pas - Phase 2 : DevOps SupplyChainX

## 📋 Table des Matières
1. [Introduction](#introduction)
2. [Prérequis](#prérequis)
3. [Étape 1 : Préparation de l'Environnement](#étape-1--préparation-de-lenvironnement)
4. [Étape 2 : Tests Unitaires](#étape-2--tests-unitaires)
5. [Étape 3 : Tests d'Intégration](#étape-3--tests-dintégration)
6. [Étape 4 : Configuration JaCoCo](#étape-4--configuration-jacoco)
7. [Étape 5 : Conteneurisation Docker](#étape-5--conteneurisation-docker)
8. [Étape 6 : Docker Compose](#étape-6--docker-compose)
9. [Étape 7 : SonarQube](#étape-7--sonarqube)
10. [Étape 8 : Pipeline CI/CD](#étape-8--pipeline-cicd)
11. [Vérification Finale](#vérification-finale)

---

## 📖 Introduction

Bienvenue dans la Phase 2 du projet SupplyChainX ! 🎉

Dans cette phase, vous allez transformer votre application en un projet professionnel avec :
- ✅ Des tests automatisés
- 📊 Une analyse de qualité du code
- 🐳 Une conteneurisation Docker
- 🔄 Un pipeline CI/CD automatisé

**Durée estimée** : 3-5 jours
**Niveau** : Débutant avec accompagnement

---

## 🛠️ Prérequis

### Logiciels à installer

#### 1. **Docker Desktop** (obligatoire)
- 📥 Téléchargez depuis : https://www.docker.com/products/docker-desktop
- ✅ Vérifiez l'installation :
```powershell
docker --version
docker-compose --version
```

#### 2. **Git** (normalement déjà installé)
```powershell
git --version
```

#### 3. **Java 17** (normalement déjà installé)
```powershell
java -version
```

#### 4. **Maven** (normalement déjà installé)
```powershell
mvn -version
```

### Comptes en ligne

- ✅ Compte GitHub (pour le pipeline CI/CD)
- ✅ Accès au dépôt de votre projet

---

## 📝 Étape 1 : Préparation de l'Environnement

### 1.1 Vérifier la structure du projet

Ouvrez un terminal PowerShell dans votre projet et exécutez :

```powershell
cd "C:\Users\Youcode\Desktop\Breif's\Supply_docker"
ls
```

Vous devriez voir :
- ✅ `pom.xml`
- ✅ `src/main/java`
- ✅ `src/test/java`

### 1.2 Créer les répertoires nécessaires

```powershell
# Créer les dossiers pour Docker
New-Item -ItemType Directory -Force -Path "docker"
New-Item -ItemType Directory -Force -Path ".github/workflows"
```

### 1.3 Vérifier que l'application compile

```powershell
mvn clean compile
```

✅ **Checkpoint** : La compilation doit réussir sans erreur.

---

## 🧪 Étape 2 : Tests Unitaires

### 2.1 Comprendre les tests unitaires

**Qu'est-ce qu'un test unitaire ?**
Un test unitaire vérifie qu'une petite partie de votre code (une méthode) fonctionne correctement, **isolée du reste**.

**Exemple simple :**
```java
// Code à tester
public int additionner(int a, int b) {
    return a + b;
}

// Test unitaire
@Test
public void testAdditionner() {
    int resultat = additionner(2, 3);
    assertEquals(5, resultat); // Vérifie que 2+3 = 5
}
```

### 2.2 Vérifier les dépendances dans pom.xml

Ouvrez `pom.xml` et vérifiez que ces dépendances sont présentes :

```xml
<dependencies>
    <!-- Tests -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    
    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-core</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

### 2.3 Exemple de test unitaire pour un service

**Scénario** : Tester le service `SupplierService`

#### a) Créer le fichier de test

📁 Chemin : `src/test/java/com/supplychainx/approvisionnement/service/SupplierServiceTest.java`

```java
package com.supplychainx.approvisionnement.service;

import com.supplychainx.approvisionnement.dto.SupplierDTO;
import com.supplychainx.approvisionnement.entity.Supplier;
import com.supplychainx.approvisionnement.repository.SupplierRepository;
import com.supplychainx.mapper.SupplierMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SupplierServiceTest {

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private SupplierMapper supplierMapper;

    @InjectMocks
    private SupplierService supplierService;

    private Supplier supplier;
    private SupplierDTO supplierDTO;

    @BeforeEach
    void setUp() {
        // Préparer les données de test
        supplier = new Supplier();
        supplier.setId(1L);
        supplier.setName("Fournisseur Test");
        supplier.setEmail("test@supplier.com");

        supplierDTO = new SupplierDTO();
        supplierDTO.setId(1L);
        supplierDTO.setName("Fournisseur Test");
        supplierDTO.setEmail("test@supplier.com");
    }

    @Test
    void testCreateSupplier_Success() {
        // ARRANGE : Préparer les mocks
        when(supplierMapper.toEntity(any(SupplierDTO.class))).thenReturn(supplier);
        when(supplierRepository.save(any(Supplier.class))).thenReturn(supplier);
        when(supplierMapper.toDTO(any(Supplier.class))).thenReturn(supplierDTO);

        // ACT : Exécuter la méthode à tester
        SupplierDTO result = supplierService.createSupplier(supplierDTO);

        // ASSERT : Vérifier les résultats
        assertNotNull(result);
        assertEquals("Fournisseur Test", result.getName());
        assertEquals("test@supplier.com", result.getEmail());
        
        // Vérifier que les méthodes ont été appelées
        verify(supplierRepository, times(1)).save(any(Supplier.class));
    }

    @Test
    void testGetSupplierById_Success() {
        // ARRANGE
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(supplierMapper.toDTO(supplier)).thenReturn(supplierDTO);

        // ACT
        SupplierDTO result = supplierService.getSupplierById(1L);

        // ASSERT
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(supplierRepository, times(1)).findById(1L);
    }

    @Test
    void testGetSupplierById_NotFound() {
        // ARRANGE
        when(supplierRepository.findById(999L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(RuntimeException.class, () -> {
            supplierService.getSupplierById(999L);
        });
    }

    @Test
    void testDeleteSupplier_Success() {
        // ARRANGE
        when(supplierRepository.existsById(1L)).thenReturn(true);
        doNothing().when(supplierRepository).deleteById(1L);

        // ACT
        supplierService.deleteSupplier(1L);

        // ASSERT
        verify(supplierRepository, times(1)).deleteById(1L);
    }
}
```

### 2.4 Exécuter les tests unitaires

```powershell
mvn test
```

**Comprendre les résultats :**
- ✅ `Tests run: X, Failures: 0, Errors: 0` → Tout va bien !
- ❌ `Failures: 1` → Un test a échoué, lisez le message d'erreur

### 2.5 Créer des tests pour d'autres services

**À faire** : Créer des tests similaires pour :
- `RawMaterialService`
- `PurchaseOrderService`
- `ProductionOrderService`

💡 **Astuce** : Copiez le modèle ci-dessus et adaptez-le !

✅ **Checkpoint** : Tous vos tests unitaires doivent passer (couleur verte).

---

## 🔗 Étape 3 : Tests d'Intégration

### 3.1 Comprendre les tests d'intégration

**Différence avec tests unitaires :**
- Test unitaire : teste UNE méthode isolée
- Test d'intégration : teste PLUSIEURS composants ensemble (Controller + Service + Database)

### 3.2 Configurer H2 pour les tests

#### a) Ajouter la dépendance H2 dans pom.xml

```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>test</scope>
</dependency>
```

#### b) Créer application-test.properties

📁 Chemin : `src/test/resources/application-test.properties`

```properties
# Base de données H2 en mémoire pour les tests
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

# Désactiver Liquibase pour les tests
spring.liquibase.enabled=false

# Port aléatoire pour éviter les conflits
server.port=0
```

### 3.3 Exemple de test d'intégration

📁 Chemin : `src/test/java/com/supplychainx/approvisionnement/controller/SupplierControllerIntegrationTest.java`

```java
package com.supplychainx.approvisionnement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supplychainx.approvisionnement.dto.SupplierDTO;
import com.supplychainx.approvisionnement.entity.Supplier;
import com.supplychainx.approvisionnement.repository.SupplierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class SupplierControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SupplierRepository supplierRepository;

    @BeforeEach
    void setUp() {
        // Nettoyer la base de données avant chaque test
        supplierRepository.deleteAll();
    }

    @Test
    void testCreateSupplier_Success() throws Exception {
        // ARRANGE
        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setName("Nouveau Fournisseur");
        supplierDTO.setEmail("nouveau@supplier.com");
        supplierDTO.setPhone("0123456789");
        supplierDTO.setAddress("123 Rue Test");

        // ACT & ASSERT
        mockMvc.perform(post("/api/suppliers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(supplierDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Nouveau Fournisseur"))
                .andExpect(jsonPath("$.email").value("nouveau@supplier.com"));
    }

    @Test
    void testGetAllSuppliers_Success() throws Exception {
        // ARRANGE : Créer des données de test
        Supplier supplier1 = new Supplier();
        supplier1.setName("Fournisseur 1");
        supplier1.setEmail("f1@test.com");
        supplierRepository.save(supplier1);

        Supplier supplier2 = new Supplier();
        supplier2.setName("Fournisseur 2");
        supplier2.setEmail("f2@test.com");
        supplierRepository.save(supplier2);

        // ACT & ASSERT
        mockMvc.perform(get("/api/suppliers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Fournisseur 1"))
                .andExpect(jsonPath("$[1].name").value("Fournisseur 2"));
    }

    @Test
    void testGetSupplierById_Success() throws Exception {
        // ARRANGE
        Supplier supplier = new Supplier();
        supplier.setName("Test Supplier");
        supplier.setEmail("test@supplier.com");
        Supplier saved = supplierRepository.save(supplier);

        // ACT & ASSERT
        mockMvc.perform(get("/api/suppliers/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Supplier"));
    }

    @Test
    void testGetSupplierById_NotFound() throws Exception {
        // ACT & ASSERT
        mockMvc.perform(get("/api/suppliers/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateSupplier_Success() throws Exception {
        // ARRANGE
        Supplier supplier = new Supplier();
        supplier.setName("Original Name");
        supplier.setEmail("original@test.com");
        Supplier saved = supplierRepository.save(supplier);

        SupplierDTO updateDTO = new SupplierDTO();
        updateDTO.setName("Updated Name");
        updateDTO.setEmail("updated@test.com");

        // ACT & ASSERT
        mockMvc.perform(put("/api/suppliers/" + saved.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    void testDeleteSupplier_Success() throws Exception {
        // ARRANGE
        Supplier supplier = new Supplier();
        supplier.setName("To Delete");
        supplier.setEmail("delete@test.com");
        Supplier saved = supplierRepository.save(supplier);

        // ACT & ASSERT
        mockMvc.perform(delete("/api/suppliers/" + saved.getId()))
                .andExpect(status().isNoContent());

        // Vérifier que le fournisseur a bien été supprimé
        mockMvc.perform(get("/api/suppliers/" + saved.getId()))
                .andExpect(status().isNotFound());
    }
}
```

### 3.4 Exécuter les tests d'intégration

```powershell
mvn verify
```

✅ **Checkpoint** : Tous les tests (unitaires + intégration) doivent passer.

---

## 📊 Étape 4 : Configuration JaCoCo

### 4.1 Qu'est-ce que JaCoCo ?

JaCoCo mesure la **couverture de code** : quel pourcentage de votre code est testé ?

**Exemple :**
- Si vous avez 100 lignes de code
- Et vos tests exécutent 70 lignes
- → Couverture = 70%

### 4.2 Configurer JaCoCo dans pom.xml

Ajoutez ce plugin dans la section `<build><plugins>` :

```xml
<build>
    <plugins>
        <!-- Plugin JaCoCo -->
        <plugin>
            <groupId>org.jacoco</groupId>
            <artifactId>jacoco-maven-plugin</artifactId>
            <version>0.8.11</version>
            <executions>
                <!-- Préparation avant les tests -->
                <execution>
                    <id>prepare-agent</id>
                    <goals>
                        <goal>prepare-agent</goal>
                    </goals>
                </execution>
                
                <!-- Génération du rapport -->
                <execution>
                    <id>report</id>
                    <phase>test</phase>
                    <goals>
                        <goal>report</goal>
                    </goals>
                </execution>
                
                <!-- Vérification de la couverture minimale -->
                <execution>
                    <id>check</id>
                    <goals>
                        <goal>check</goal>
                    </goals>
                    <configuration>
                        <rules>
                            <rule>
                                <element>PACKAGE</element>
                                <limits>
                                    <limit>
                                        <counter>LINE</counter>
                                        <value>COVEREDRATIO</value>
                                        <minimum>0.60</minimum> <!-- 60% minimum -->
                                    </limit>
                                </limits>
                            </rule>
                        </rules>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

### 4.3 Générer le rapport JaCoCo

```powershell
mvn clean test
```

### 4.4 Voir le rapport

Le rapport HTML est généré dans :
📁 `target/site/jacoco/index.html`

**Pour l'ouvrir :**
```powershell
start target/site/jacoco/index.html
```

**Interpréter le rapport :**
- 🟢 Vert : Bon (> 80%)
- 🟡 Jaune : Moyen (50-80%)
- 🔴 Rouge : Faible (< 50%)

✅ **Checkpoint** : Votre couverture doit être > 60%.

---

## 🐳 Étape 5 : Conteneurisation Docker

### 5.1 Qu'est-ce que Docker ?

Docker permet d'empaqueter votre application avec tout ce dont elle a besoin (Java, dépendances, etc.) dans un **conteneur**.

**Avantage** : Ça fonctionne partout pareil !

### 5.2 Créer le Dockerfile

📁 Chemin : `Dockerfile` (à la racine du projet)

```dockerfile
# Étape 1 : Builder - Compiler l'application
FROM maven:3.9-eclipse-temurin-17 AS builder

# Définir le répertoire de travail
WORKDIR /app

# Copier les fichiers de configuration Maven
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Télécharger les dépendances (cache Docker)
RUN mvn dependency:go-offline -B

# Copier le code source
COPY src ./src

# Compiler et packager l'application (sans exécuter les tests)
RUN mvn clean package -DskipTests

# Étape 2 : Runtime - Image finale légère
FROM eclipse-temurin:17-jre-alpine

# Informations sur l'image
LABEL maintainer="votre-email@example.com"
LABEL description="SupplyChainX Backend Application"

# Créer un utilisateur non-root pour la sécurité
RUN addgroup -S spring && adduser -S spring -G spring

# Définir le répertoire de travail
WORKDIR /app

# Copier le JAR depuis l'étape builder
COPY --from=builder /app/target/*.jar app.jar

# Changer le propriétaire
RUN chown -R spring:spring /app

# Utiliser l'utilisateur non-root
USER spring:spring

# Port exposé
EXPOSE 8080

# Point d'entrée
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
```

### 5.3 Créer .dockerignore

📁 Chemin : `.dockerignore` (à la racine)

```
# Ignorer ces fichiers lors du build Docker
target/
.mvn/
*.md
.git/
.gitignore
.github/
*.log
*.iml
.idea/
.vscode/
```

### 5.4 Tester la construction de l'image

```powershell
# Construire l'image Docker
docker build -t supplychainx-backend:latest .
```

**Comprendre la commande :**
- `docker build` : Construire une image
- `-t supplychainx-backend:latest` : Nom et tag de l'image
- `.` : Utiliser le Dockerfile du répertoire actuel

### 5.5 Vérifier que l'image est créée

```powershell
docker images
```

Vous devriez voir `supplychainx-backend` dans la liste.

✅ **Checkpoint** : L'image Docker est construite sans erreur.

---

## 🎼 Étape 6 : Docker Compose

### 6.1 Qu'est-ce que Docker Compose ?

Docker Compose permet de gérer **plusieurs conteneurs** ensemble :
- Backend (votre application)
- Base de données (MySQL)
- SonarQube (analyse de code)

### 6.2 Créer docker-compose.yml

📁 Chemin : `docker-compose.yml` (à la racine)

```yaml
version: '3.8'

services:
  # Base de données MySQL
  mysql:
    image: mysql:8.0
    container_name: supplychainx-mysql
    environment:
      MYSQL_ROOT_PASSWORD: rootpassword
      MYSQL_DATABASE: supplychainx
      MYSQL_USER: supplychainx_user
      MYSQL_PASSWORD: supplychainx_pass
    ports:
      - "3306:3306"
    volumes:
      - mysql-data:/var/lib/mysql
      - ./src/main/resources/db/init.sql:/docker-entrypoint-initdb.d/init.sql
    networks:
      - supplychainx-network
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      timeout: 20s
      retries: 10

  # Application Backend Spring Boot
  backend:
    build:
      context: .
      dockerfile: Dockerfile
    container_name: supplychainx-backend
    depends_on:
      mysql:
        condition: service_healthy
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/supplychainx?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
      SPRING_DATASOURCE_USERNAME: supplychainx_user
      SPRING_DATASOURCE_PASSWORD: supplychainx_pass
      SPRING_JPA_HIBERNATE_DDL_AUTO: update
      SPRING_JPA_SHOW_SQL: "true"
    ports:
      - "8080:8080"
    networks:
      - supplychainx-network
    restart: unless-stopped

  # SonarQube pour l'analyse de code
  sonarqube:
    image: sonarqube:community
    container_name: supplychainx-sonarqube
    environment:
      SONAR_ES_BOOTSTRAP_CHECKS_DISABLE: "true"
    ports:
      - "9000:9000"
    volumes:
      - sonarqube-data:/opt/sonarqube/data
      - sonarqube-extensions:/opt/sonarqube/extensions
      - sonarqube-logs:/opt/sonarqube/logs
    networks:
      - supplychainx-network

networks:
  supplychainx-network:
    driver: bridge

volumes:
  mysql-data:
  sonarqube-data:
  sonarqube-extensions:
  sonarqube-logs:
```

### 6.3 Démarrer tous les services

```powershell
docker-compose up -d
```

**Comprendre la commande :**
- `docker-compose up` : Démarrer les services
- `-d` : Mode détaché (en arrière-plan)

### 6.4 Vérifier que tout fonctionne

```powershell
# Voir les conteneurs en cours d'exécution
docker-compose ps

# Voir les logs du backend
docker-compose logs -f backend

# Voir les logs de MySQL
docker-compose logs -f mysql
```

### 6.5 Tester l'application

Ouvrez votre navigateur :
- 🌐 Backend : http://localhost:8080
- 📊 SonarQube : http://localhost:9000 (admin/admin)

### 6.6 Arrêter les services

```powershell
docker-compose down
```

Pour tout supprimer (y compris les données) :
```powershell
docker-compose down -v
```

✅ **Checkpoint** : L'application, MySQL et SonarQube fonctionnent ensemble.

---

## 📈 Étape 7 : SonarQube

### 7.1 Configurer SonarQube

#### a) Se connecter à SonarQube

1. Ouvrez http://localhost:9000
2. Connexion : `admin` / `admin`
3. Changez le mot de passe (obligatoire)

#### b) Créer un projet

1. Cliquez sur "Create Project"
2. Nom : `SupplyChainX`
3. Key : `supplychainx`
4. Cliquez sur "Set Up"

#### c) Générer un token

1. Choisissez "Locally"
2. Générez un token
3. **Copiez-le** (vous ne pourrez plus le voir !)

### 7.2 Configurer Maven pour SonarQube

Ajoutez dans `pom.xml` (dans `<properties>`) :

```xml
<properties>
    <!-- ... autres propriétés ... -->
    
    <!-- SonarQube -->
    <sonar.host.url>http://localhost:9000</sonar.host.url>
    <sonar.projectKey>supplychainx</sonar.projectKey>
    <sonar.projectName>SupplyChainX</sonar.projectName>
    <sonar.coverage.jacoco.xmlReportPaths>
        ${project.build.directory}/site/jacoco/jacoco.xml
    </sonar.coverage.jacoco.xmlReportPaths>
</properties>
```

### 7.3 Lancer l'analyse SonarQube

```powershell
mvn clean verify sonar:sonar -Dsonar.login=VOTRE_TOKEN_ICI
```

**Remplacez** `VOTRE_TOKEN_ICI` par le token généré précédemment.

### 7.4 Voir les résultats

1. Retournez sur http://localhost:9000
2. Cliquez sur votre projet `SupplyChainX`
3. Analysez :
   - 🐛 Bugs
   - 🔒 Vulnérabilités
   - 💩 Code Smells
   - 📊 Couverture de code

### 7.5 Quality Gate

SonarQube utilise une **Quality Gate** (porte de qualité) :
- ✅ Passed : Code de bonne qualité
- ❌ Failed : Problèmes à corriger

**Objectif** : Atteindre "Passed" !

✅ **Checkpoint** : Analyse SonarQube effectuée, résultats visibles.

---

## 🔄 Étape 8 : Pipeline CI/CD

### 8.1 Qu'est-ce qu'un pipeline CI/CD ?

**CI (Intégration Continue)** : Tester automatiquement chaque modification
**CD (Déploiement Continu)** : Déployer automatiquement si les tests passent

**Flux :**
1. Vous faites un `git push`
2. Le pipeline se déclenche automatiquement
3. Il compile, teste, analyse
4. Si tout est OK → déploiement

### 8.2 Choisir GitHub Actions

Nous allons utiliser **GitHub Actions** (gratuit pour les dépôts publics).

### 8.3 Créer le workflow GitHub Actions

📁 Chemin : `.github/workflows/ci-cd.yml`

```yaml
name: CI/CD Pipeline

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  # Job 1 : Tests et Qualité
  build-and-test:
    name: Build, Test & Quality Analysis
    runs-on: ubuntu-latest

    steps:
      # Étape 1 : Récupérer le code
      - name: Checkout code
        uses: actions/checkout@v4
        with:
          fetch-depth: 0  # Pour SonarQube

      # Étape 2 : Configurer Java 17
      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: maven

      # Étape 3 : Compiler l'application
      - name: Build with Maven
        run: mvn clean compile

      # Étape 4 : Exécuter les tests unitaires
      - name: Run Unit Tests
        run: mvn test

      # Étape 5 : Exécuter les tests d'intégration
      - name: Run Integration Tests
        run: mvn verify

      # Étape 6 : Générer le rapport JaCoCo
      - name: Generate JaCoCo Report
        run: mvn jacoco:report

      # Étape 7 : Analyse SonarQube
      - name: SonarQube Scan
        env:
          SONAR_TOKEN: ${{ secrets.SONAR_TOKEN }}
        run: |
          mvn sonar:sonar \
            -Dsonar.projectKey=supplychainx \
            -Dsonar.host.url=${{ secrets.SONAR_HOST_URL }} \
            -Dsonar.login=${{ secrets.SONAR_TOKEN }}

      # Étape 8 : Upload des rapports de tests
      - name: Upload Test Reports
        if: always()
        uses: actions/upload-artifact@v3
        with:
          name: test-reports
          path: target/surefire-reports/

      # Étape 9 : Upload du rapport JaCoCo
      - name: Upload JaCoCo Report
        uses: actions/upload-artifact@v3
        with:
          name: jacoco-report
          path: target/site/jacoco/

  # Job 2 : Build Docker (seulement si tests OK)
  docker-build:
    name: Build Docker Image
    runs-on: ubuntu-latest
    needs: build-and-test
    if: github.ref == 'refs/heads/main'

    steps:
      - name: Checkout code
        uses: actions/checkout@v4

      - name: Set up Docker Buildx
        uses: docker/setup-buildx-action@v3

      - name: Build Docker Image
        run: docker build -t supplychainx-backend:latest .

      - name: Save Docker Image
        run: docker save supplychainx-backend:latest | gzip > supplychainx-backend.tar.gz

      - name: Upload Docker Image
        uses: actions/upload-artifact@v3
        with:
          name: docker-image
          path: supplychainx-backend.tar.gz
```

### 8.4 Configurer les secrets GitHub

1. Allez sur votre dépôt GitHub
2. Settings → Secrets and variables → Actions
3. Cliquez "New repository secret"

**Créer ces secrets :**

| Nom | Valeur |
|-----|--------|
| `SONAR_TOKEN` | Votre token SonarQube |
| `SONAR_HOST_URL` | `http://votre-ip:9000` (ou URL publique) |

**Note** : Pour SonarQube local, vous devrez utiliser une instance publique ou tunneling (ngrok).

### 8.5 Alternative : SonarCloud (recommandé pour débutants)

**SonarCloud** est gratuit pour les projets open source et plus simple !

#### a) Créer un compte SonarCloud

1. Allez sur https://sonarcloud.io
2. Connectez-vous avec GitHub
3. Cliquez "Analyze new project"
4. Sélectionnez votre dépôt `Supply_docker`
5. Générez un token

#### b) Modifier le workflow

Remplacez la partie SonarQube par :

```yaml
      - name: SonarCloud Scan
        env:
          SONAR_TOKEN: ${{ secrets.SONAR_TOKEN }}
        run: |
          mvn sonar:sonar \
            -Dsonar.projectKey=votre-org_Supply_docker \
            -Dsonar.organization=votre-org \
            -Dsonar.host.url=https://sonarcloud.io
```

### 8.6 Tester le pipeline

```powershell
git add .
git commit -m "Add CI/CD pipeline"
git push origin main
```

1. Allez sur GitHub → onglet "Actions"
2. Vous verrez votre workflow en cours d'exécution
3. Cliquez dessus pour voir les détails

**Couleurs :**
- 🟢 Vert : Succès
- 🔴 Rouge : Échec (cliquez pour voir les logs)
- 🟡 Jaune : En cours

✅ **Checkpoint** : Le pipeline s'exécute et passe avec succès.

---

## ✅ Vérification Finale

### Checklist complète

- [ ] **Tests Unitaires**
  - [ ] Au moins 3 services testés
  - [ ] Tous les tests passent (`mvn test`)
  - [ ] Utilisation de Mockito

- [ ] **Tests d'Intégration**
  - [ ] Au moins 2 controllers testés
  - [ ] Tests avec H2
  - [ ] Tous les tests passent (`mvn verify`)

- [ ] **JaCoCo**
  - [ ] Plugin configuré dans `pom.xml`
  - [ ] Rapport généré (`target/site/jacoco/index.html`)
  - [ ] Couverture > 60%

- [ ] **Docker**
  - [ ] `Dockerfile` créé
  - [ ] Image construite avec succès
  - [ ] `.dockerignore` présent

- [ ] **Docker Compose**
  - [ ] `docker-compose.yml` créé
  - [ ] Backend + MySQL + SonarQube fonctionnent
  - [ ] Application accessible sur http://localhost:8080

- [ ] **SonarQube**
  - [ ] Projet créé
  - [ ] Analyse exécutée
  - [ ] Quality Gate visible
  - [ ] Couverture reportée

- [ ] **CI/CD**
  - [ ] Workflow GitHub Actions créé
  - [ ] Secrets configurés
  - [ ] Pipeline s'exécute automatiquement
  - [ ] Tous les jobs passent

### Commandes de vérification rapide

```powershell
# 1. Tous les tests
mvn clean verify

# 2. Rapport JaCoCo
start target/site/jacoco/index.html

# 3. Build Docker
docker build -t supplychainx-backend:latest .

# 4. Lancer l'environnement complet
docker-compose up -d

# 5. Vérifier les services
docker-compose ps

# 6. Voir les logs
docker-compose logs -f backend
```

---

## 📚 Ressources Complémentaires

### Documentation

- 📖 [JUnit 5 Guide](https://junit.org/junit5/docs/current/user-guide/)
- 📖 [Mockito](https://site.mockito.org/)
- 📖 [JaCoCo](https://www.jacoco.org/jacoco/trunk/doc/)
- 📖 [Docker Documentation](https://docs.docker.com/)
- 📖 [GitHub Actions](https://docs.github.com/en/actions)
- 📖 [SonarQube](https://docs.sonarqube.org/)

### Tutoriels vidéo recommandés

- 🎥 Tests unitaires Spring Boot
- 🎥 Docker pour débutants
- 🎥 GitHub Actions CI/CD

### Aide et support

**En cas de problème :**
1. Lisez attentivement le message d'erreur
2. Vérifiez les logs : `docker-compose logs nom-du-service`
3. Consultez la documentation officielle
4. Cherchez sur Stack Overflow
5. Demandez de l'aide à votre formateur

---

## 🎯 User Stories - Validation

### US1 : Tests automatiques à chaque push
✅ **Critères d'acceptation :**
- GitHub Actions configuré
- Tests unitaires et d'intégration exécutés automatiquement
- Échec du pipeline si un test échoue

**Validation :**
```powershell
git add .
git commit -m "Test pipeline"
git push
# → Vérifier sur GitHub Actions
```

### US2 : Analyse de qualité après chaque build
✅ **Critères d'acceptation :**
- SonarQube/SonarCloud intégré au pipeline
- Rapports de couverture envoyés
- Quality Gate visible

**Validation :**
- Pipeline exécute `sonar:sonar`
- Rapport visible sur SonarCloud

### US3 : Déploiement automatique Dockerisé
✅ **Critères d'acceptation :**
- Dockerfile fonctionnel
- docker-compose.yml complet
- Image Docker construite dans le pipeline

**Validation :**
```powershell
docker-compose up -d
curl http://localhost:8080/api/suppliers
```

---

## 🏆 Félicitations !

Si vous êtes arrivé ici et que toutes les vérifications sont ✅, vous avez réussi la Phase 2 ! 🎉

**Vous savez maintenant :**
- ✅ Écrire des tests unitaires et d'intégration
- ✅ Mesurer la qualité du code
- ✅ Conteneuriser une application
- ✅ Créer un pipeline CI/CD complet

**Prochaines étapes :**
1. Améliorez la couverture de tests (objectif : 80%)
2. Corrigez les bugs et code smells détectés par SonarQube
3. Ajoutez des tests pour les cas limites (edge cases)
4. Explorez le déploiement sur le cloud (AWS, Azure, Heroku)

---

## 📝 Notes personnelles

_(Espace pour vos notes pendant la réalisation)_

**Problèmes rencontrés :**


**Solutions trouvées :**


**Améliorations futures :**


---

**Dernière mise à jour** : Novembre 2025
**Version** : 1.0
**Auteur** : Guide pour débutants SupplyChainX Phase 2
