# MCommerce Microservices

MCommerce est une application e-commerce construite avec une architecture microservices. Le projet est organise en monorepo et regroupe les services backend Spring Boot, une configuration centralisee Spring Cloud Config, un registre Eureka, une API Gateway, une base PostgreSQL et un frontend Angular servi par Nginx.

Le projet est entierement dockerise afin de pouvoir lancer toute la stack avec une seule commande.

## Table des matieres

- [Architecture generale](#architecture-generale)
- [Technologies](#technologies)
- [Structure du projet](#structure-du-projet)
- [Microservices](#microservices)
  - [config.server](#configserver)
  - [eureka.server](#eurekaserver)
  - [api.gateway](#apigateway)
  - [auth-service](#auth-service)
  - [product-service](#product-service)
  - [cart-service](#cart-service)
  - [payment-service](#payment-service)
  - [frontend Angular](#frontend-angular)
- [Configuration centralisee](#configuration-centralisee)
- [Bases de donnees](#bases-de-donnees)
- [Stockage des images produits](#stockage-des-images-produits)
- [Dockerisation](#dockerisation)
- [Lancement du projet](#lancement-du-projet)
- [Endpoints utiles](#endpoints-utiles)

## Architecture generale

La communication entre les services repose sur Spring Cloud :

- `config.server` centralise les configurations des microservices.
- `eureka.server` sert de registre de decouverte de services.
- `api.gateway` expose une entree unique vers les microservices.
- Les services metier s'enregistrent dans Eureka et recuperent leur configuration depuis le Config Server.
- Le frontend Angular appelle l'API Gateway via Nginx.
- PostgreSQL stocke les donnees applicatives, avec une base separee par microservice.

Flux simplifie :

```text
Frontend Angular
       |
       | /api, /uploads
       v
API Gateway
       |
       | lb:// via Eureka
       v
auth-service / product-service / cart-service / payment-service
       |
       v
PostgreSQL
```

## Technologies

- Java 21
- Spring Boot
- Spring Cloud Config
- Spring Cloud Netflix Eureka
- Spring Cloud Gateway
- Spring Data JPA
- Spring Security
- OpenFeign
- PostgreSQL
- Angular
- Nginx
- Docker et Docker Compose

## Structure du projet

```text
J2EE_Controle/
  api.gateway/
  auth-service/
  cart-service/
  config-repo/
  config.server/
  docker/
  ecom-front/
  eureka.server/
  payment-service/
  product-service/
  docker-compose.yml
  docker-compose.local-config.yml
  .env.example
  README.md
```

## Microservices

### config.server

Service Spring Cloud Config charge de fournir la configuration centralisee aux autres microservices.

Role principal :

- Exposer les fichiers de configuration contenus dans `config-repo/`.
- Fournir les proprietes de chaque service au demarrage.
- Permettre une configuration centralisee sans dupliquer les fichiers `application.properties`.

Port par defaut :

```text
8888
```

En Docker, ce service utilise le profil `native` et lit la configuration depuis le dossier local monte :

```text
config-repo/
```

Hors Docker, le fichier `config.server/src/main/resources/application.properties` conserve une configuration Git distante :

```properties
spring.cloud.config.server.git.uri=${CONFIG_REPO_URI:https://github.com/ZedKaS/config-repo.git}
```

### eureka.server

Service Eureka Server utilise comme registre de decouverte.

Role principal :

- Recevoir l'enregistrement des microservices.
- Permettre a l'API Gateway et aux services de localiser les autres services par leur nom logique.
- Eviter les URLs fixes entre microservices.

Port par defaut :

```text
8761
```

Interface Eureka :

```text
http://localhost:8761
```

### api.gateway

API Gateway Spring Cloud Gateway. C'est le point d'entree principal du backend.

Role principal :

- Exposer les routes publiques vers les microservices.
- Router les requetes vers les services via Eureka.
- Centraliser une partie de la securite, notamment la verification JWT.
- Gerer le CORS pour le frontend.

Port par defaut :

```text
9000
```

Routes principales :

```text
/auth/**
/admin/**
/products/**
/uploads/**
/carts/**
/payments/**
```

### auth-service

Microservice d'authentification et de gestion des utilisateurs.

Role principal :

- Inscription des utilisateurs.
- Connexion.
- Generation et validation des tokens JWT.
- Gestion des utilisateurs par un administrateur.
- Initialisation eventuelle d'un utilisateur administrateur.

Base de donnees :

```text
auth_db
```

Port par defaut :

```text
8084
```

### product-service

Microservice de gestion des produits.

Role principal :

- Creation de produits.
- Modification de produits.
- Suppression logique ou desactivation.
- Consultation et pagination des produits.
- Upload et exposition des images produits.

Base de donnees :

```text
product_db
```

Port par defaut :

```text
8081
```

Les images produits sont servies via :

```text
/uploads/**
```

### cart-service

Microservice de gestion du panier.

Role principal :

- Creer et recuperer le panier d'un utilisateur.
- Ajouter un produit au panier.
- Modifier les quantites.
- Supprimer un article du panier.
- Vider le panier.
- Communiquer avec `product-service` via OpenFeign pour recuperer les informations produit.

Base de donnees :

```text
cart_db
```

Port par defaut :

```text
8082
```

### payment-service

Microservice de paiement et d'historique des transactions.

Role principal :

- Lancer un paiement depuis le panier courant.
- Enregistrer les transactions.
- Gerer le statut et la methode de paiement.
- Consulter l'historique des paiements.
- Communiquer avec `cart-service` via OpenFeign.

Base de donnees :

```text
payment_db
```

Port par defaut :

```text
8083
```

### Frontend Angular

Application frontend situee dans :

```text
ecom-front/mcommerce-front
```

Role principal :

- Interface utilisateur e-commerce.
- Consultation des produits.
- Authentification.
- Gestion du panier.
- Paiement.
- Administration des produits, utilisateurs et paiements.

En Docker, Angular est compile puis servi par Nginx.

Port expose :

```text
4200
```

URL :

```text
http://localhost:4200
```

Le frontend appelle le backend via des chemins relatifs :

```text
/api
/uploads
```

Nginx redirige ensuite ces appels vers `api-gateway:9000`.

## Configuration centralisee

Les fichiers de configuration sont regroupes dans :

```text
config-repo/
```

Fichiers principaux :

```text
api.gateway.properties
auth-service.properties
cart-service.properties
eureka.server.properties
payment-service.properties
product-service.properties
```

En Docker, `config.server` utilise ce dossier local grace au profil `native` :

```yaml
SPRING_PROFILES_ACTIVE: native
CONFIG_NATIVE_SEARCH_LOCATIONS: file:/config-repo
```

Le dossier est monte dans le conteneur :

```yaml
./config-repo:/config-repo:ro
```

Cela rend le lancement Docker reproductible, meme si le repo Git distant n'est pas a jour ou contient des valeurs locales comme `localhost`.

## Bases de donnees

Le projet utilise un seul conteneur PostgreSQL, mais chaque microservice possede sa propre base.

Bases creees automatiquement au premier demarrage :

```text
auth_db
product_db
cart_db
payment_db
```

Le script d'initialisation se trouve ici :

```text
docker/postgres/init-databases.sql
```

Les donnees PostgreSQL sont conservees dans un volume Docker :

```text
postgres-data
```

## Stockage des images produits

Les images produits ne sont pas stockees dans un chemin local configure dans `config-repo/product-service.properties`.

En Docker, le chemin est fourni par `docker-compose.yml` :

```text
--file.upload-dir=/data/uploads
```

Le dossier `/data/uploads` est monte sur un volume Docker persistant :

```text
product-uploads
```

Cela permet de conserver les images meme apres redemarrage du conteneur `product-service`.

## Dockerisation

Chaque service backend possede son propre `Dockerfile` multi-stage :

- build Maven avec Java 21
- image runtime Java 21
- exposition du port du service
- healthcheck via Actuator dans Docker Compose

Le frontend possede egalement son `Dockerfile` :

- build Angular avec Node
- service des fichiers statiques avec Nginx
- proxy `/api` vers `api-gateway:9000`
- proxy `/uploads` vers `api-gateway:9000/uploads`

Le fichier principal d'orchestration est :

```text
docker-compose.yml
```

Services lances par Docker Compose :

```text
postgres
config-server
eureka-server
auth-service
product-service
cart-service
payment-service
api-gateway
frontend
```

Volumes Docker :

```text
postgres-data
product-uploads
config-git-cache
```

## Lancement du projet

Prerequis :

- Docker Desktop
- Docker Compose
- Ports disponibles : `4200`, `9000`, `8888`, `8761`, `5432`, `8081`, `8082`, `8083`, `8084`

Lancer toute la stack :

```powershell
docker compose up --build
```

Lancer en arriere-plan :

```powershell
docker compose up -d --build
```

Verifier les conteneurs :

```powershell
docker compose ps
```

Voir les logs :

```powershell
docker compose logs -f
```

Arreter les conteneurs :

```powershell
docker compose down
```

Arreter et supprimer les volumes :

```powershell
docker compose down -v
```

## Endpoints utiles

Frontend :

```text
http://localhost:4200
```

API Gateway :

```text
http://localhost:9000
```

Config Server :

```text
http://localhost:8888
```

Eureka Dashboard :

```text
http://localhost:8761
```

Healthchecks :

```text
http://localhost:8888/actuator/health
http://localhost:9000/actuator/health
http://localhost:8081/actuator/health
http://localhost:8082/actuator/health
http://localhost:8083/actuator/health
http://localhost:8084/actuator/health
```

Exemple de configuration exposee par le Config Server :

```text
http://localhost:8888/product-service/default
```