# Mini Projet MNIST - Reconnaissance de Chiffres

## Description
Programme Java de reconnaissance automatique des chiffres manuscrits 3 et 5
en utilisant le Machine Learning (Weka).

## Technologies utilisees
- Java 21
- Apache POI 5.5.1 (fichiers Excel)
- Weka 3.8.6 (Machine Learning)
- Dataset MNIST (60 000 images)

## Structure du projet
src/mnist/Util.java             - Traitement des fichiers MNIST
src/mnist/Test.java             - Tests des methodes
src/mnist/MachineLearning.java  - Modeles ML
lib/                            - Bibliotheques JAR
data/                           - Fichiers MNIST binaires

## Fonctionnalites
- Lecture des fichiers binaires MNIST
- Generation de chiffres.txt avec 800 lignes (400 trois + 400 cinq)
- Conversion image PNG vers fichier texte et vice-versa
- Creation fichier Excel avec Apache POI
- Generation fichier ARFF pour Weka

## Resultats Machine Learning
| Modele        | Precision |
|---------------|-----------|
| Naive Bayes   |   88.13%  |
| Random Forest |   97.25%  |

Meilleur modele : Random Forest avec 97.25%

## Comment lancer
1. Cloner : git clone https://github.com/slimaniyasmine-gif/MiniProjetMNIST.git
2. Importer dans Eclipse
3. Ajouter les JAR dans lib/ au Build Path
4. Lancer Test.java pour la Partie 1
5. Lancer MachineLearning.java pour la Partie 2
