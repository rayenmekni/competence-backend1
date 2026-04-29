# 🎓 Gestion des Compétences — Module PIDEV

> Module de gestion des compétences pour la plateforme **Smart Freelance Platform** — Projet PIDEV 4SAE5, ESPRIT 2026.

---

## 📋 Description

Ce module permet de gérer les **compétences (Skills)** des freelancers avec un système de **notation (Ratings)** et un **tableau de bord statistique**. Il se compose d'un backend Spring Boot et d'un frontend Angular.

### Fonctionnalités

- ✅ **CRUD Compétences** — Créer, lire, modifier, supprimer des compétences
- ✅ **Niveaux de compétence** — DEBUTANT, INTERMEDIAIRE, EXPERT
- ✅ **Système de notation** — Notes de 1 à 5 avec commentaires
- ✅ **Recherche & filtrage** — Par nom ou par niveau
- ✅ **Tableau de bord statistique** — Répartition par niveau (camembert), top 5 compétences par note moyenne (histogramme)

---

## 🏗️ Architecture

```
templateEXAMEN/
├── src/                          # Backend Spring Boot
│   └── main/java/tn/esprit/templateexamen/
│       ├── controller/           # REST Controllers
│       │   ├── SkillController.java
│       │   ├── RatingController.java
│       │   └── StatistiqueController.java
│       ├── entite/               # JPA Entities
│       │   ├── Skill.java
│       │   ├── Rating.java
│       │   └── NiveauSkill.java
│       ├── service/              # Business Logic
│       └── dto/                  # Data Transfer Objects
├── competence-frontend/          # Frontend Angular
│   └── src/app/
│       ├── components/           # UI Components
│       │   ├── skill-list/
│       │   ├── skill-form-dialog/
│       │   └── ratings-dialog/
│       ├── routes/statistiques/  # Dashboard
│       ├── services/             # API Services
│       └── models/               # TypeScript Models
└── pom.xml
```

---

## ⚙️ Tech Stack

| Couche | Technologie |
|--------|------------|
| **Backend** | Spring Boot 3.2, Java 21, Spring Data JPA |
| **Base de données** | MySQL |
| **Frontend** | Angular 18, Angular Material, Chart.js |
| **Service Discovery** | Eureka Client |

---

## 🚀 Démarrage rapide

### Prérequis

- Java 21+
- Node.js 18+
- MySQL 8+
- Maven

### 1. Base de données

```sql
CREATE DATABASE IF NOT EXISTS Competence;
```

### 2. Backend

```bash
# Depuis la racine du projet
./mvnw spring-boot:run
```

Le backend démarre sur **http://localhost:8089**

### 3. Frontend

```bash
cd competence-frontend
npm install
ng serve
```

Le frontend démarre sur **http://localhost:4200**

---

## 🔌 API Endpoints

### Compétences (`/competence`)

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| `GET` | `/competence/retrieve-all-competence` | Lister toutes les compétences |
| `GET` | `/competence/retrieve-skill/{id}` | Récupérer une compétence |
| `POST` | `/competence/add-skill` | Ajouter une compétence |
| `PUT` | `/competence/modify-skill` | Modifier une compétence |
| `DELETE` | `/competence/remove-skill/{id}` | Supprimer une compétence |
| `GET` | `/competence/retrieve-competence-by-niveau/{niveau}` | Filtrer par niveau |
| `GET` | `/competence/search-competence-by-name/{nom}` | Rechercher par nom |

### Notations (`/api/ratings`)

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| `GET` | `/api/ratings/retrieve-all-ratings` | Lister toutes les notes |
| `GET` | `/api/ratings/retrieve-ratings-by-skill/{id}` | Notes par compétence |
| `POST` | `/api/ratings/add-rating/{skill-id}` | Ajouter une note |
| `PUT` | `/api/ratings/modify-rating` | Modifier une note |
| `DELETE` | `/api/ratings/remove-rating/{id}` | Supprimer une note |
| `GET` | `/api/ratings/average-rating/{skill-id}` | Note moyenne |
| `GET` | `/api/ratings/count-ratings/{skill-id}` | Nombre de notes |

### Statistiques (`/api/statistiques`)

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| `GET` | `/api/statistiques/global` | Statistiques globales |
| `GET` | `/api/statistiques/competences/{id}/notes` | Stats par compétence |

---

## 👥 Équipe

**Projet PIDEV 4SAE5** — ESPRIT 2026

---
