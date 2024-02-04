# CosmoApp
Cosmo Device Explorer - Application Android
Vision Globale
L'application Android Cosmo Device Explorer est conçue pour permettre aux utilisateurs de visualiser et explorer les données des appareils disponibles via l'API Cosmo. Elle offre une interface utilisateur conviviale basée sur Jetpack Compose pour une expérience fluide et moderne. L'application est structurée en modules distincts, suivant les principes de l'architecture propre (Clean Architecture), avec une séparation claire des responsabilités entre les couches de données, de domaine et de présentation.
Stack Technique : Jetpack Compose , Koin , kotlin 
Modules
Module Data
Ce module est responsable de la communication avec l'API Cosmo pour récupérer les données des appareils. Il comprend des classes telles que Device, ApiService, DeviceResponse, et DeviceRepositoryImpl.

Module Domain
Ce module contient les entités métier de l'application en liaison avec le bluetooth, ainsi que les cas d'utilisation et les interfaces de repository définis pour l'accès aux données.

Module Présentation
Ce module abrite l'interface utilisateur de l'application, construite en utilisant Jetpack Compose. Il contient les ViewModels, les composants d'interface utilisateur, et les couches nécessaires pour présenter les données de manière conviviale.

Module principal App
Ce module est le point d'entrée de l'application(MyActivity and MyApp(koin)). Il agrège les modules de données, de domaine et de présentation pour former une application Android fonctionnelle.

*****************************************

Construction et Exécution
Suivez ces étapes pour construire et exécuter l'application sur votre périphérique Android ou un émulateur Android :

Clonez ce dépôt GitHub sur votre machine locale en utilisant la commande suivante :

git clone https://github.com/AhmedFekiIng/CosmoApp.git
Ouvrez Android Studio.

Dans Android Studio, sélectionnez "Open an existing project", puis naviguez vers le répertoire où vous avez cloné le dépôt.

Une fois le projet ouvert dans Android Studio, attendez que les dépendances soient synchronisées et que la construction du projet soit terminée.

Connectez votre périphérique Android à votre ordinateur via un câble USB, ou configurez un émulateur Android.

Sélectionnez votre périphérique Android ou émulateur Android dans la liste des périphériques cibles, puis cliquez sur le bouton "Run" pour "app" dans Android Studio pour installer et exécuter l'application.