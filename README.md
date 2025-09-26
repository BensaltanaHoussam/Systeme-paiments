# Système de gestion des abonnements et paiements

## Contexte

La gestion des abonnements (personnels ou professionnels) est devenue complexe avec la multiplication des services. Ce projet vise à centraliser le suivi des abonnements, à détecter rapidement les paiements manqués et à générer des rapports pour mieux anticiper le budget.

## Objectif

Fournir une application console Java permettant :
- La gestion centralisée des abonnements (avec ou sans engagement)
- Le suivi des échéances et des paiements
- La détection des impayés
- La génération de rapports financiers synthétiques

## Structure de l’application

- **UI (Menu)** : Interface textuelle pour la navigation et la saisie utilisateur
- **Services** : Logique métier (génération d’échéances, détection des impayés, rapports)
- **Entities** : Objets métiers persistants (`Abonnement`, `Paiement`)
- **DAO** : Accès aux données via JDBC (PostgreSQL ou MySQL)
- **Utils** : Outils de gestion des dates, validations, etc.

## Modèle de données

- **Abonnement** (abstrait) : `id`, `nomService`, `montantMensuel`, `dateDebut`, `dateFin`, `statut` (enum), `typeAbonnement`
    - **AbonnementAvecEngagement** : + `dureeEngagementMois`
    - **AbonnementSansEngagement**
- **Paiement** : `idPaiement`, `idAbonnement`, `dateEcheance`, `datePaiement`, `typePaiement`, `statut` (enum)

## Fonctionnalités principales

- Création, modification, suppression d’abonnements (avec/sans engagement)
- Liste et recherche d’abonnements
- Génération automatique des échéances mensuelles
- Paiement d’une échéance, modification/suppression d’un paiement
- Liste des paiements d’un abonnement
- Affichage des impayés et du montant total impayé
- Calcul du total déjà payé pour un abonnement
- Affichage des 5 derniers paiements
- Rapports financiers mensuels et annuels

## Base de données

- **Abonnement** : (id, nomService, montantMensuel, dateDebut, dateFin, statut, typeAbonnement, dureeEngagementMois)
- **Paiement** : (idPaiement, idAbonnement, dateEcheance, datePaiement, typePaiement, statut)
- Relation 1..n entre Abonnement et Paiement

## Exigences techniques

- Java 8 minimum (utilisation de Stream API, lambda, Optional, Collectors)
- JDBC (PostgreSQL ou MySQL)
- Architecture en couches (UI, services, DAO, utilitaires)
- Gestion des exceptions avec messages clairs
- Contrôle de version avec Git

## Lancement

1. Cloner le dépôt :  
   `git clone git@github.com:BensaltanaHoussam/Systeme-paiments.git`
2. Configurer la base de données (PostgreSQL ou MySQL)
3. Compiler le projet avec un JDK 8+
4. Exécuter la classe principale :  
   `ui.Main`

## Exemple d’utilisation

- Naviguer dans le menu principal pour gérer abonnements et paiements
- Créer un abonnement, générer ses échéances, enregistrer les paiements
- Consulter les rapports pour suivre les paiements et impayés

## Auteur

Projet développé par BensaltanaHoussam

---

*Ce projet est un exemple pédagogique de gestion d’abonnements et de paiements en Java, architecture MVC simplifiée, interface console.*
