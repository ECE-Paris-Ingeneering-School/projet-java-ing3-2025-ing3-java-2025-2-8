-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Hôte : localhost:8889
-- Généré le : sam. 26 avr. 2025 à 14:15
-- Version du serveur : 5.7.39
-- Version de PHP : 8.2.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `ShoppingDB`
--

-- --------------------------------------------------------

--
-- Structure de la table `Commande`
--

CREATE TABLE `Commande` (
  `idCommande` int(11) NOT NULL,
  `dateCommande` datetime DEFAULT CURRENT_TIMESTAMP,
  `idUtilisateur` int(11) NOT NULL,
  `total` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `Commande`
--

INSERT INTO `Commande` (`idCommande`, `dateCommande`, `idUtilisateur`, `total`) VALUES
(1, '2025-04-02 14:07:23', 1, '50.00'),
(2, '2025-04-02 14:07:23', 2, '75.00'),
(3, '2025-04-02 14:07:23', 1, '30.00'),
(4, '2025-04-02 14:07:23', 3, '100.00'),
(5, '2025-04-02 14:07:23', 4, '65.00'),
(6, '2025-04-02 14:07:23', 2, '85.00'),
(7, '2025-04-14 14:53:18', 1, '50.00'),
(8, '2025-04-15 11:43:36', 1, '15.00'),
(9, '2025-04-15 16:58:15', 8, '90.00'),
(10, '2025-04-15 16:58:51', 8, '90.00'),
(11, '2025-04-15 17:03:16', 1, '16.00'),
(12, '2025-04-15 17:16:02', 8, '220.00'),
(13, '2025-04-15 17:22:24', 8, '220.00'),
(14, '2025-04-15 17:24:45', 8, '50.00'),
(15, '2025-04-15 21:49:31', 8, '180.00'),
(16, '2025-04-15 22:07:00', 8, '180.00'),
(17, '2025-04-26 10:10:47', 8, '185.00');

-- --------------------------------------------------------

--
-- Structure de la table `LigneCommande`
--

CREATE TABLE `LigneCommande` (
  `idLigne` int(11) NOT NULL,
  `idCommande` int(11) NOT NULL,
  `idProduit` int(11) NOT NULL,
  `quantite` int(11) NOT NULL,
  `prixUnitaire` decimal(10,2) NOT NULL,
  `sousTotal` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `LigneCommande`
--

INSERT INTO `LigneCommande` (`idLigne`, `idCommande`, `idProduit`, `quantite`, `prixUnitaire`, `sousTotal`) VALUES
(1, 1, 1, 12, '0.50', '5.00'),
(2, 1, 2, 1, '80.00', '80.00'),
(3, 2, 3, 4, '20.00', '80.00'),
(4, 3, 1, 10, '0.50', '4.00'),
(6, 4, 4, 2, '25.00', '50.00'),
(7, 5, 5, 3, '15.00', '45.00'),
(8, 6, 6, 1, '45.00', '45.00'),
(9, 7, 4, 2, '25.00', '50.00'),
(11, 9, 6, 2, '45.00', '90.00'),
(12, 10, 11, 1, '90.00', '90.00'),
(13, 11, 5, 1, '15.00', '15.00'),
(14, 11, 1, 2, '0.50', '1.00'),
(15, 12, 12, 1, '220.00', '220.00'),
(16, 13, 12, 1, '220.00', '220.00'),
(17, 14, 4, 2, '25.00', '50.00'),
(18, 15, 11, 2, '90.00', '180.00'),
(19, 16, 11, 2, '90.00', '180.00'),
(20, 17, 11, 1, '90.00', '90.00'),
(21, 17, 10, 1, '50.00', '50.00'),
(22, 17, 6, 1, '45.00', '45.00');

-- --------------------------------------------------------

--
-- Structure de la table `Marque`
--

CREATE TABLE `Marque` (
  `idMarque` int(11) NOT NULL,
  `nom` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `Marque`
--

INSERT INTO `Marque` (`idMarque`, `nom`) VALUES
(2, 'Adidas'),
(6, 'New Balance'),
(1, 'Nike'),
(7, 'ON Cloud'),
(3, 'Puma'),
(4, 'Reebok'),
(5, 'Under Armour');

-- --------------------------------------------------------

--
-- Structure de la table `Produit`
--

CREATE TABLE `Produit` (
  `idProduit` int(11) NOT NULL,
  `nom` varchar(100) NOT NULL,
  `prixUnitaire` decimal(10,2) NOT NULL,
  `stock` int(11) NOT NULL,
  `idMarque` int(11) NOT NULL,
  `qteLotPromo` int(11) DEFAULT NULL,
  `prixLotPromo` decimal(10,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `Produit`
--

INSERT INTO `Produit` (`idProduit`, `nom`, `prixUnitaire`, `stock`, `idMarque`, `qteLotPromo`, `prixLotPromo`) VALUES
(1, 'Briquet', '0.50', 100, 1, 10, '4.00'),
(2, 'Chaussure de sport', '80.00', 50, 2, NULL, NULL),
(3, 'T-shirt', '20.00', 200, 3, 5, '90.00'),
(4, 'Short de sport', '25.00', 150, 4, NULL, NULL),
(5, 'Casquette', '15.00', 120, 5, NULL, NULL),
(6, 'Sac de sport', '45.00', 70, 6, NULL, NULL),
(10, 'Hoodie', '50.00', 60, 1, 5, '50.00'),
(11, 'Chaussure running', '90.00', 100, 7, 0, '100.00'),
(12, 'Tante', '220.00', 10, 1, 10, '200.00');

-- --------------------------------------------------------

--
-- Structure de la table `Reduction`
--

CREATE TABLE `Reduction` (
  `idReduction` int(11) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `typeReduction` enum('pourcentage','fixe') DEFAULT NULL,
  `valeur` decimal(10,2) DEFAULT NULL,
  `dateDebut` date DEFAULT NULL,
  `dateFin` date DEFAULT NULL,
  `idProduit` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `Reduction`
--

INSERT INTO `Reduction` (`idReduction`, `description`, `typeReduction`, `valeur`, `dateDebut`, `dateFin`, `idProduit`) VALUES
(1, 'Promo -20% sur Briquets', 'pourcentage', '20.00', '2025-01-01', '2025-01-31', 1),
(2, 'Promo -10% sur T-shirts', 'pourcentage', '10.00', '2025-02-01', '2025-02-28', 3),
(3, 'Promo -5€ sur Chaussures', 'fixe', '5.00', '2025-03-01', '2025-03-15', 2),
(4, 'Promo -15% sur Casquettes', 'pourcentage', '15.00', '2025-04-01', '2025-04-30', 5),
(5, 'Promo -25% sur Shorts', 'pourcentage', '25.00', '2025-05-01', '2025-05-31', 4),
(6, 'Promo -30% sur Sacs de sport', 'pourcentage', '30.00', '2025-06-01', '2025-06-30', 6);

-- --------------------------------------------------------

--
-- Structure de la table `Utilisateur`
--

CREATE TABLE `Utilisateur` (
  `idUtilisateur` int(11) NOT NULL,
  `nom` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `mot_de_passe` varchar(100) NOT NULL,
  `type` enum('client','admin') NOT NULL,
  `date_inscription` datetime DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `Utilisateur`
--

INSERT INTO `Utilisateur` (`idUtilisateur`, `nom`, `email`, `mot_de_passe`, `type`, `date_inscription`) VALUES
(1, 'Jean Dupont', 'jean.dupont@example.com', 'password123', 'client', '2025-04-02 14:07:23'),
(2, 'Marie Durand', 'marie.durand@example.com', 'password123', 'client', '2025-04-02 14:07:23'),
(3, 'Paul Martin', 'paul.martin@example.com', 'password123', 'client', '2025-04-02 14:07:23'),
(4, 'Alice Petit', 'alice.petit@example.com', 'password123', 'client', '2025-04-02 14:07:23'),
(5, 'Admin One', 'admin1@example.com', 'adminpass', 'admin', '2025-04-02 14:07:23'),
(6, 'Admin Two', 'admin2@example.com', 'adminpass', 'admin', '2025-04-02 14:07:23'),
(7, 'Jean Test', 'jean@example.com', '1234', 'client', '2025-04-14 13:33:16'),
(8, 'jean haj', 'jean.haj@icloud.com', 'jeanhaj24', 'client', '2025-04-15 12:31:06');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `Commande`
--
ALTER TABLE `Commande`
  ADD PRIMARY KEY (`idCommande`),
  ADD KEY `idUtilisateur` (`idUtilisateur`);

--
-- Index pour la table `LigneCommande`
--
ALTER TABLE `LigneCommande`
  ADD PRIMARY KEY (`idLigne`),
  ADD KEY `idCommande` (`idCommande`),
  ADD KEY `lignecommande_ibfk_2` (`idProduit`);

--
-- Index pour la table `Marque`
--
ALTER TABLE `Marque`
  ADD PRIMARY KEY (`idMarque`),
  ADD UNIQUE KEY `nom` (`nom`);

--
-- Index pour la table `Produit`
--
ALTER TABLE `Produit`
  ADD PRIMARY KEY (`idProduit`),
  ADD KEY `idMarque` (`idMarque`);

--
-- Index pour la table `Reduction`
--
ALTER TABLE `Reduction`
  ADD PRIMARY KEY (`idReduction`),
  ADD KEY `fk_reduction_produit` (`idProduit`);

--
-- Index pour la table `Utilisateur`
--
ALTER TABLE `Utilisateur`
  ADD PRIMARY KEY (`idUtilisateur`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `Commande`
--
ALTER TABLE `Commande`
  MODIFY `idCommande` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT pour la table `LigneCommande`
--
ALTER TABLE `LigneCommande`
  MODIFY `idLigne` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT pour la table `Marque`
--
ALTER TABLE `Marque`
  MODIFY `idMarque` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT pour la table `Produit`
--
ALTER TABLE `Produit`
  MODIFY `idProduit` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT pour la table `Reduction`
--
ALTER TABLE `Reduction`
  MODIFY `idReduction` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT pour la table `Utilisateur`
--
ALTER TABLE `Utilisateur`
  MODIFY `idUtilisateur` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `Commande`
--
ALTER TABLE `Commande`
  ADD CONSTRAINT `commande_ibfk_1` FOREIGN KEY (`idUtilisateur`) REFERENCES `Utilisateur` (`idUtilisateur`);

--
-- Contraintes pour la table `LigneCommande`
--
ALTER TABLE `LigneCommande`
  ADD CONSTRAINT `lignecommande_ibfk_1` FOREIGN KEY (`idCommande`) REFERENCES `Commande` (`idCommande`) ON DELETE CASCADE,
  ADD CONSTRAINT `lignecommande_ibfk_2` FOREIGN KEY (`idProduit`) REFERENCES `Produit` (`idProduit`) ON DELETE CASCADE;

--
-- Contraintes pour la table `Produit`
--
ALTER TABLE `Produit`
  ADD CONSTRAINT `produit_ibfk_1` FOREIGN KEY (`idMarque`) REFERENCES `Marque` (`idMarque`);

--
-- Contraintes pour la table `Reduction`
--
ALTER TABLE `Reduction`
  ADD CONSTRAINT `fk_reduction_produit` FOREIGN KEY (`idProduit`) REFERENCES `Produit` (`idProduit`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
