Démarrez l'application et la base de données en une seule commande :

```bash
docker-compose up -d
```

## 📊 Commandes Utiles

### Vérifier l'état des conteneurs
```bash
docker-compose ps
```
### Arrêter les conteneurs
```bash
docker-compose down
```

### Arrêter et supprimer les volumes
```bash
docker-compose down -v
```

### Reconstruire l'image après modification du code
```bash
docker-compose up -d --build
```

### Accéder au conteneur
```bash
# Application
docker exec -it supplychainx-app sh

# MySQL
docker exec -it supplychainx-mysql mysql -uroot -proot
```

## 🛠️ Structure des Fichiers Docker

```
SupplyChainX_Docker/
├── Dockerfile              # Instructions pour construire l'image de l'application
├── docker-compose.yml      # Orchestration des services (app + MySQL)
├── .dockerignore          # Fichiers à exclure de l'image Docker
└── DOCKER_GUIDE.md        # Ce fichier
```

## 📦 Architecture Docker

```
┌─────────────────────────────────────┐
│     Docker Network                   │
│     (supplychainx-network)          │
│                                     │
│  ┌──────────────┐  ┌─────────────┐ │
│  │    MySQL     │  │  Spring App │ │
│  │  Container   │  │  Container  │ │
│  │              │  │             │ │
│  │  Port: 3306  │  │  Port: 8080 │ │
│  └──────────────┘  └─────────────┘ │
└─────────────────────────────────────┘
         │                  │
         │                  │
    Host: 3306         Host: 8080
```