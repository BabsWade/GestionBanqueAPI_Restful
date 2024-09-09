-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : lun. 09 sep. 2024 à 15:22
-- Version du serveur :  10.4.14-MariaDB
-- Version de PHP : 7.4.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `banking_db`
--

-- --------------------------------------------------------

--
-- Structure de la table `bank_accounts`
--

CREATE TABLE `bank_accounts` (
  `id` bigint(20) NOT NULL,
  `account_number` varchar(20) NOT NULL,
  `account_holder_name` varchar(100) NOT NULL,
  `balance` decimal(38,2) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `bank_accounts`
--

INSERT INTO `bank_accounts` (`id`, `account_number`, `account_holder_name`, `balance`, `created_at`, `updated_at`) VALUES
(1, '1234567890', 'Babs WADE', '149500.00', '2024-09-08 18:16:51', '2024-09-09 01:26:59'),
(2, '0987654321', 'Zocoder Ndiaye', '200500.00', '2024-09-08 18:16:51', '2024-09-09 01:26:59');

-- --------------------------------------------------------

--
-- Structure de la table `transactions`
--

CREATE TABLE `transactions` (
  `id` bigint(20) NOT NULL,
  `account_id` bigint(20) NOT NULL,
  `transaction_type` enum('DEPOSIT','WITHDRAWAL') NOT NULL,
  `amount` decimal(38,2) NOT NULL,
  `transaction_date` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `transactions`
--

INSERT INTO `transactions` (`id`, `account_id`, `transaction_type`, `amount`, `transaction_date`) VALUES
(1, 1, 'DEPOSIT', '200.00', '2024-09-08 18:16:51'),
(2, 1, 'WITHDRAWAL', '50.00', '2024-09-08 18:16:51'),
(3, 2, 'DEPOSIT', '100.00', '2024-09-08 18:16:51'),
(4, 1, 'DEPOSIT', '-5.00', '2024-09-09 01:17:51'),
(5, 1, 'DEPOSIT', '5.00', '2024-09-09 01:17:52'),
(6, 1, 'DEPOSIT', '-5.00', '2024-09-09 01:18:42'),
(7, 2, 'DEPOSIT', '5.00', '2024-09-09 01:18:42'),
(8, 1, 'DEPOSIT', '-500.00', '2024-09-09 01:26:59'),
(9, 2, 'DEPOSIT', '500.00', '2024-09-09 01:26:59');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `bank_accounts`
--
ALTER TABLE `bank_accounts`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `account_number` (`account_number`);

--
-- Index pour la table `transactions`
--
ALTER TABLE `transactions`
  ADD PRIMARY KEY (`id`),
  ADD KEY `account_id` (`account_id`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `bank_accounts`
--
ALTER TABLE `bank_accounts`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `transactions`
--
ALTER TABLE `transactions`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `transactions`
--
ALTER TABLE `transactions`
  ADD CONSTRAINT `transactions_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `bank_accounts` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
