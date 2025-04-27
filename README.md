# 🛍️ Projet Java Shopping

Bienvenue dans mon projet **Java Shopping**, développé dans le cadre du module de Programmation Avancée Java 2025.

Ce projet simule une **application e-commerce** simple avec :

- Authentification client / administrateur
- Gestion du catalogue de produits
- Gestion du panier d'achat
- Passage de commande
- Module d'administration pour les produits
- Module de reporting/statistiques

---

## 🔧 Technologies utilisées

- **Java SE** (JDK 17)
- **Swing** pour l'interface graphique
- **MySQL** pour la base de données
- **JDBC** pour la communication entre Java et la base de données
- **Git** pour le versioning

---

## 🛠️ Fonctionnalités principales

- **Client :**
  - Connexion et inscription
  - Recherche de produits
  - Filtrage par marque
  - Ajout au panier
  - Passage de commande avec récapitulatif
  - Historique de commandes

- **Administrateur :**
  - Ajout, modification, suppression de produits
  - Visualisation des commandes des clients
  - Statistiques (ventes totales, nombre de commandes, produit le plus vendu)

---

## 🗄️ Structure du projet

| Dossier/Fichier | Description |
|-----------------|-------------|
| `modele/`        | Classes modèles (`Produit`, `Commande`, `Utilisateur`, etc.) |
| `dao/`           | Classes DAO pour l'accès à la base de données |
| `vue/`           | Interfaces graphiques Swing |
| `controleur/`    | Gestion des actions utilisateur (LoginControleur) |
| `util/`          | Utilitaire de connexion à la base de données |

---

## 📚 Base de données

- Le schéma comprend les tables :
  - `Utilisateur`
  - `Produit`
  - `Marque`
  - `Commande`
  - `LigneCommande`

> ⚡ Les relations entre commandes et lignes de commande sont gérées par clés étrangères (`idCommande`, `idProduit`).

---

## 🚀 Lancer l'application

1. Cloner le projet :
   ```bash
   git clone https://github.com/JeanElHaj/java-shopping.git

