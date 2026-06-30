# MCommerce Microservices

Monorepo Docker pour la stack Spring Cloud + Angular :

- `config.server`
- `eureka.server`
- `api.gateway`
- `auth-service`
- `product-service`
- `cart-service`
- `payment-service`
- `ecom-front/mcommerce-front`

## Lancement Docker

```powershell
docker compose up --build
```

Pour que la stack Docker soit reproductible, `docker-compose.yml` monte la copie locale `config-repo/` dans `config-server` avec le profil Spring `native`. Le fichier `config.server/src/main/resources/application.properties` garde la configuration Git distante par défaut pour les lancements hors Docker.

Services exposés :

- Frontend : http://localhost:4200
- API Gateway : http://localhost:9000
- Config Server : http://localhost:8888
- Eureka : http://localhost:8761
- PostgreSQL : localhost:5432

## Lancement explicite avec le config repo local

Le dossier `config-repo/` est monté dans `config-server` et utilisé avec le profil Spring `native`.

```powershell
docker compose -f docker-compose.yml -f docker-compose.local-config.yml up --build
```

## Arrêt

```powershell
docker compose down
```

Pour supprimer aussi les volumes Docker :

```powershell
docker compose down -v
```

## Bases de données

Un seul conteneur PostgreSQL est utilisé, avec une base séparée par microservice :

- `auth_db`
- `product_db`
- `cart_db`
- `payment_db`

## Frontend

Angular est servi par Nginx. Les appels HTTP utilisent `/api`, qui est proxifié vers `api-gateway:9000`.

Les images produits utilisent `/uploads`, également proxifié vers l'API Gateway.
