# Projet maven Thymeleaf pour le front de l'application Square Game 

# 🎮 Square Games — Front

Application web frontend de **Square Games**, développée avec **Spring Boot** et **Thymeleaf**.

Le frontend permet aux utilisateurs de :

* créer un compte ;
* se connecter ;
* consulter leurs parties ;
* créer des parties ;
* jouer aux différents jeux ;
* recevoir les mises à jour d'une partie en temps réel grâce aux WebSockets ;
* se déconnecter.

Une interface d'administration est également disponible pour les utilisateurs possédant le rôle `ROLE_ADMIN`.

---

## 🛠️ Technologies utilisées

* Java
* Spring Boot
* Spring MVC
* Thymeleaf
* Spring Security 6
* JWT
* REST API
* WebSocket
* STOMP
* HTML / CSS
* JavaScript
* Font Awesome
* Maven

---

## 🏗️ Architecture

Le frontend communique avec deux APIs distinctes :

```text
                    ┌─────────────────────┐
                    │     Navigateur      │
                    │                     │
                    │  Thymeleaf / JS     │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │   Front Spring Boot │
                    │      Port 8082      │
                    └───────┬───────┬─────┘
                            │       │
               REST + JWT   │       │   REST + JWT
                            ▼       ▼
                 ┌──────────────┐ ┌──────────────┐
                 │ API Users    │ │ API Games    │
                 │ Port 8081    │ │ Port 8080    │
                 └──────────────┘ └──────┬───────┘
                                         │
                                         │ WebSocket
                                         ▼
                                  Mises à jour
                                  en temps réel
```

### APIs utilisées

| Application        |   Port | Rôle                                         |
| ------------------ | -----: | -------------------------------------------- |
| Front Square Games | `8082` | Interface utilisateur                        |
| API Users          | `8081` | Gestion des utilisateurs et authentification |
| API Games          | `8080` | Gestion des parties et jeux                  |

---

# 🔐 Authentification

L'authentification est basée sur un **JWT** fourni par l'API Users.

Lors de la connexion :

1. Le frontend envoie le username et le mot de passe à l'API Users.
2. L'API vérifie les identifiants.
3. L'API renvoie un JWT.
4. Le frontend décode le payload du JWT.
5. Le frontend récupère notamment :

    * l'identifiant utilisateur ;
    * le username ;
    * le rôle.
6. Un `CustomUserDetails` est créé.
7. Le `SecurityContext` de Spring Security est enregistré en session.

Le JWT est ensuite utilisé pour communiquer avec les APIs protégées.

---

## 👤 Gestion des rôles

Deux rôles sont utilisés :

```text
ROLE_USER
ROLE_ADMIN
```

Le rôle est également présent dans le `CustomUserDetails`.

Les autorités Spring Security sont créées avec :

```java
new SimpleGrantedAuthority(role)
```

### Utilisateur classique

Un utilisateur avec `ROLE_USER` peut accéder aux fonctionnalités normales de l'application.

### Administrateur

Un utilisateur avec `ROLE_ADMIN` peut accéder à l'espace d'administration.

La sécurité du frontend contient notamment :

```java
.requestMatchers("/admin/**").hasRole("ADMIN")
```

L'accès à `/admin` est donc réservé aux administrateurs.

---

# 🚦 Redirection après connexion

Après une connexion réussie, le frontend vérifie le rôle présent dans le JWT.

```text
ROLE_ADMIN
    ↓
/admin
```

et :

```text
ROLE_USER
    ↓
/home
```

Cela permet à l'administrateur d'arriver directement sur son espace d'administration.

---

# 🛡️ Espace administrateur

L'espace `/admin` permet actuellement de gérer les utilisateurs.

L'administrateur peut consulter :

* le username ;
* l'adresse email ;
* le rôle.

Il peut également supprimer un utilisateur.

La suppression est effectuée par le frontend auprès de l'API Users.

Architecture :

```text
Frontend
   │
   │ DELETE /users/{id}
   │ Authorization: Bearer <JWT>
   ▼
API Users
   │
   ▼
Suppression de l'utilisateur
```

L'accès à l'administration est protégé à deux niveaux :

### Frontend

```java
.requestMatchers("/admin/**").hasRole("ADMIN")
```

### API Users

Les opérations sensibles sont également protégées côté API avec `@PreAuthorize`.

---

# 🎮 Gestion des jeux

Le frontend permet de jouer aux différents jeux proposés par l'API Games :

* Morpion (`tictactoe`)
* Puissance 4 (`connect4`)
* Taquin (`15 puzzle`)

Les parties sont récupérées depuis l'API Games.

Le frontend utilise Thymeleaf pour générer la page initiale puis JavaScript pour gérer les interactions avec le plateau.

---

# 🔄 Communication REST

Les actions principales sont effectuées via l'API Games.

Par exemple, lorsqu'un joueur joue un coup :

```text
Navigateur
    │
    │ POST /games/{gameId}/moves
    │ Authorization: Bearer <JWT>
    ▼
API Games
    │
    ▼
Validation du coup
    │
    ▼
Mise à jour de la partie
```

Le frontend n'utilise donc pas directement la logique métier des jeux.

La logique de jeu reste dans l'API Games.

---

# ⚡ WebSocket et STOMP

Les WebSockets permettent de recevoir les changements d'une partie en temps réel.

Le frontend utilise **STOMP** pour communiquer avec le serveur WebSocket.

Connexion :

```javascript
const client = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/ws',
    reconnectDelay: 5000
});
```

Le frontend s'abonne ensuite au topic correspondant à la partie :

```text
/topic/games/{gameId}
```

---

## 🔄 Fonctionnement d'un coup avec WebSocket

Lorsqu'un joueur joue :

```text
1. Joueur A
      │
      │ POST /games/{id}/moves
      ▼
2. API Games
      │
      │ Mise à jour de la partie
      ▼
3. GameWebSocketService
      │
      │ /topic/games/{id}
      ▼
4. Joueur A + Joueur B
      │
      ▼
5. Mise à jour du plateau
```

Cela permet au deuxième joueur de voir le changement sans avoir besoin de recharger manuellement la page.

---

# 🏆 Fin de partie

Lorsque l'API Games indique que la partie est terminée :

```text
status = TERMINATED
```

le frontend recharge la page.

Cela permet notamment de réafficher la fenêtre de victoire générée par Thymeleaf.

La page affiche alors la popup de victoire avec une animation de confettis.

---

# 🖥️ Interface

L'interface utilise un thème graphique commun avec :

* couleurs bordeaux ;
* boutons personnalisés ;
* cartes de jeux ;
* navigation commune ;
* page d'administration ;
* popup de victoire ;
* affichage responsive.

Les éléments communs sont regroupés dans des fragments Thymeleaf.

Par exemple, le head est réutilisable :

```html
<head th:replace="fragments/head :: head"></head>
```


Le header est réutilisable :

```html
<header th:replace="fragments/header :: header"></header>
```


Le footer est également réutilisable :

```html
<footer th:replace="fragments/footer :: footer"></footer>
```

---

# 📁 Structure du projet

La structure principale est organisée de cette manière :

```text
src/
└── main/
    ├── java/
    │   └── com.square_game.front_square_game/
    │       ├── configuration/
    │       │   ├── RestClienConfig
    │       │   └── SecurityConfig
    │       │
    │       ├── controllers/
    │       │   ├── LoginController
    │       │   ├── AdminController
    │       │   └── ...
    │       │
    │       ├── dto/
    │       │
    │       ├── security/
    │       │   └── CustomUserDetails
    │       │
    │       └── services/
    │           ├── GameApiService
    │           └── UserApiService
    │
    └── resources/
        ├── static/
        │   ├── css/
        │   │   └── style.css
        │   │
        │   └── js/
        │       └── game.js
        │
        └── templates/
            ├── fragments/
            │   ├── head.html
            │   ├── header.html
            │   └── footer.html
            │
            ├── admin.html
            ├── home.html
            ├── login.html
            ├── register.html
            ├── game.html
            └── ...
```

---

# ⚙️ Configuration

Les URLs des APIs sont configurées dans les propriétés Spring.

Exemple :

```properties
api.users.url=http://localhost:8081
api.games.url=http://localhost:8080
```

Le frontend utilise ces URLs pour communiquer avec les différentes APIs.

---

# 🚀 Installation

## 1. Cloner le projet

```bash
git clone <URL_DU_REPOSITORY>
```

Puis :

```bash
cd front-square-game
```

---

## 2. Vérifier les APIs

Avant de démarrer le frontend, vérifier que les deux APIs sont disponibles :

```text
API Users → http://localhost:8081
API Games → http://localhost:8080
```

---

## 3. Démarrer le frontend

Avec Maven :

```bash
./mvnw spring-boot:run
```

Sous Windows :

```bash
mvnw.cmd spring-boot:run
```

Ou lancer directement la classe principale depuis IntelliJ IDEA.

---

# 🌐 Accès à l'application

Une fois l'application démarrée :

```text
http://localhost:8082
```

---

# 🔑 Parcours utilisateur

## Utilisateur non connecté

```text
Accueil
  │
  ├── Se connecter
  │
  └── S'inscrire
```

## Utilisateur connecté

```text
Connexion
   │
   ▼
ROLE_USER
   │
   ▼
/home
   │
   └── Jeux
```

## Administrateur

```text
Connexion
   │
   ▼
ROLE_ADMIN
   │
   ▼
/admin
   │
   ├── Gestion des utilisateurs
   │
   └── Suppression des utilisateurs
```

---

# 🔒 Sécurité

Le frontend utilise Spring Security avec une politique de session.

Le JWT reçu lors de la connexion est associé au `CustomUserDetails`.

Les appels aux APIs protégées transmettent le JWT avec :

```http
Authorization: Bearer <token>
```

Exemple :

```http
GET /users

Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

Les endpoints sensibles restent protégés côté backend.

La sécurité du frontend ne remplace donc pas celle des APIs.

---

# 🧪 Tests

Pour tester l'application :

### 1. Créer un utilisateur

Depuis :

```text
/register
```

### 2. Se connecter

Depuis :

```text
/login
```

### 3. Tester un utilisateur classique

Vérifier que l'utilisateur arrive sur :

```text
/home
```

### 4. Tester un administrateur

Se connecter avec un compte possédant :

```text
ROLE_ADMIN
```

L'utilisateur doit être redirigé vers :

```text
/admin
```

### 5. Tester l'administration

Vérifier :

* l'affichage des utilisateurs ;
* l'affichage des rôles ;
* la suppression d'un utilisateur ;
* la protection de `/admin`.

### 6. Tester les jeux

Créer une partie puis vérifier :

* l'affichage du plateau ;
* les mouvements ;
* le changement de joueur ;
* les mises à jour WebSocket ;
* la fin de partie ;
* l'affichage de la victoire.

---

# 📌 Points importants

Le frontend est responsable de :

* l'affichage ;
* la navigation ;
* l'expérience utilisateur ;
* l'authentification côté interface ;
* la communication avec les APIs ;
* l'affichage des mises à jour en temps réel.

Les APIs restent responsables de :

* la logique métier ;
* la gestion des utilisateurs ;
* la validation des JWT ;
* la gestion des parties ;
* les règles des jeux ;
* la persistance des données.

Cette séparation permet de conserver une architecture composée de plusieurs applications indépendantes.

---

# 📚 Projet

**Square Games**

Projet réalisé dans le cadre de la formation de développeur.

Architecture :

```text
┌─────────────────────┐
│   Front Thymeleaf   │
│       :8082         │
└──────────┬──────────┘
           │
     ┌─────┴─────┐
     │           │
     ▼           ▼
┌──────────┐ ┌──────────┐
│   Users  │ │   Games  │
│   :8081  │ │   :8080  │
└──────────┘ └──────────┘
```

---

## 👩‍💻 Technologies

```text
Java
Spring Boot
Spring MVC
Thymeleaf
Spring Security
JWT
REST
WebSocket
STOMP
JavaScript
HTML
CSS
Maven
```


