# SAE S2.02 -- Rapport pour la ressource Graphes
===

# GAMBIRASIO Florian, DEHAINE Alexandre, PINTO Cristobal. Groupe C6


Version 1 : un seul moyen de transport
---

### Présentation d'un exemple

*Présenter un exemple concret de problème (données complètes pour la plateforme avec tous les moyens de transport, préférences de l'utilisatrices qui comprennent le moyen de transport choisi, le critère d'optimisation, et nombre d'itinéraires demandés).*
*Donner la solution du problème du point de vue de l'utilisatrice, càd quels sont les itinéraires possibles, quels sont les meilleurs itinéraires et pourquoi.*
*Pour l'instant on ne parle pas de graphes; on peut éventuellement faire des schémas.*

### Modèle pour l'exemple

*Donner le graphe modélisant l'exemple ci-dessus.*
*Donner la solution du problème (càd les meilleurs itinéraires) en tant que chemins dans le graphe.*

### Modélisation pour la Version 1 dans le cas général

*Expliquer de manière abstraite comment, étant donné un problème de recherche d'itinéraire (plateforme avec tous types de lignes, moyen de transport choisi, critère d'optimisation, nombre d'itinéraires demandés) on peut construire un graphe permettant de résoudre le problème de recherche d'itinéraire. C'est à dire:*
- *quels sont les sommets du graphe par rapport aux données du problème*, 
- *quelles sont ses arêtes, par rapport aux données du problème*, 
- *comment sont définis les poids des arêtes*,
- *quel algorithme sur les graphes permet de résoudre le problème d'itinéraire (nom de l'algorithme, arguments).*

*Utiliser un vocabulaire précis sur les graphes.*

Les sommets du graphe représentent les villes du réseau, tandis que les arêtes correspondent aux trajets allant d'un sommet A à un sommet B, avec comme poids la modalité choisie par l'utilisateur (prix, pollution, temps de trajet, etc.).
À partir de ce graphe, on peut alors déterminer le ou les trajets idéaux grâce à un algorithme des plus courts chemins, prenant en paramètre la ville de départ, la ville d'arrivée et les modalités choisies afin de générer le graphe nécessaire à la résolution du problème.


### Implémentation de la Version 1

*Écrire une classe de test qui reprend l'exemple, définit toutes les données de la plateforme, construit le graphe et calcule la solution.*
*Votre classe peut utiliser des assertions (test unitaire) ou bien afficher la solution.*
*Donner ici le **nom complet de la classe**, **la date et l'identifiant du commit à regarder** et un **lien vers la page de cette classe sur gitlab qui correspond au bon commit***.

*On insiste sur l'importance de spécifier le commit. En effet, quand vous commencerez la Version 2, le code utilisé pour le test de la Version 1 sera modifié. Il se peut que vous n'ayez pas le temps de finaliser la Version 2 et vous retrouver avec un code qui ne marche pas même pour la Version 1. C'est pourquoi il est important de rédiger le rapport au fur et à mesure et de donner ici un lien vers la version de votre code qui marche pour la Version 1 du projet.*


#### Lecture des arêtes

Afin de pouvoir lire les données fournis sous la forme:
`"villeA;villeB;Train;60;1.7;80",
 "villeB;villeD;Train;22;2.4;40", 
 "villeA;villeC;Train;42;1.4;50"`

Alexandre a créé la fonction `public ArrayList<ArrayList<String>> getInfo(String[] data);` qui prend en paramètre un tableau de string, afin de pouvoir prendres plusieurs arêtes en même temps, et qui, à l'aide de sa fonction `private String subSemiCol(String chaine, int prevIndex);` retourne une ArrayList d'ArrayList de String, la 1ère ArrayList contient plusieurs ArrayList qui eux même contiennent les String fournis décomposées et exploitable. Par exemple, si on prend le tableau de String fourni plus haut, si l'on veut la ville d'arrivé de la deuxième arête, cet à dire villeD, on écrit : variableArrayList.get(2).get(1), ce qui est égale à "villeD".

Pour faire ça, il utilise donc la fonction subSemiCol qui renvoie une fragementation de la chaine fournis, jusqu'au prochain ; en partant de l'index fournis en paramètre.

Cette fonction ne vérifie pas si les données fournies sont correctes, Cristobal a donc créé la fonction `checkPtVirgule(String[] data)` qui vérifie si les données sont au format attendu, il faut donc l'appeller avant d'utiliser `getInfo` pour éviter des erreurs si les données fournies sont invalide.

#### Transformation des données en graphes

Pour définir les sommets et les arêtes d'un graphe, Florian a écrit les classes **Arete**, qui implémente l'interface **Trancon**, et **Sommet**, qui implémente l'interface **Lieu**.
Une Arete est définie par un départ et une arrivée, tous deux de type **Lieu**, un mode de déplacement de type **ModaliteTransport**, et les différents poids possibles de l'arête dans une HashMap de type **HashMap<TypeCout,Double>**

Pour obtenir le graphe souhaité, Florian a écrit la classe **GenGraph** qui permet :
- La création d'une liste de sommets à partir de données de type String (en vérifiant qu'il n'y ait pas de doublons car il ne sait pas utiliser de Set), via la fonction `public ArrayList<Sommet> creatSommet(ArrayList<ArrayList<String>> data)` qui parcourt une liste de String, vérifie qu'il n'y a pas de doublons, puis transforme les String sélectionnées en Sommet.
- La création d'une liste d'arêtes à partir de données de type String, une liste de Sommet et le nombre de "poids" que les arêtes auront, via la fonction `public ArrayList<Arete> creatArete(ArrayList<ArrayList<String>> data, ArrayList<Sommet> listSommets, int nbModalite)`. Pour fonctionner, cette fonction nécessite que les données en paramètre contiennent au minimum 4 éléments, dont 3 avant les "poids". De plus, pour que les poids soient correctement placés dans la HashMap de l'arête, les valeurs des poids doivent être dans le même ordre que les valeurs de l'énum `TypeCout` (ici PRIX, CO2, TEMPS).
- La création de graphes de type `MultiGrapheOrienteValue` via la fonction `public MultiGrapheOrienteValue creationGraph(ArrayList<ArrayList<String>> data, int nbModalite, TypeCout modalite, ArrayList<Sommet> listeTotalSommet)` qui utilise la fonction de création de liste d'Arêtes la liste complète des Sommets (pour palier à leur suppression suite au filtrage).

#### Représentation des utilisateurs

Pour représenter les utilisateurs, Florian a écrit la classe **Voyageur** qui définit un voyageur par un nom, le critère qu'il cherche à optimiser en se déplaçant (Critère de type `TypeCout`) et son mode de déplacement de type `ModaliteTransport`. 

#### Filtrage des données

Pour filtrer les données afin de sélectionner un mode de déplacement choisi par l'utilisateur, Florian a écrit la fonction `private ArrayList<ArrayList<String>> filtreSelonTransport(ArrayList<ArrayList<String>> data, ModaliteTransport vehicule)` dans la classe **Plateforme** qui renvoie des données en excluant les lignes ne contenant pas le mode de transport spécifié en paramètre.
Le défaut de cette fonction est qu'après son exécution, les nouvelles données obtenues peuvent perdre des sommets s'ils sont uniquement reliés par un mode de transport différent de celui spécifié en paramètre.

La fonction `public MultiGrapheOrienteValue graphSelonMod(String[] data, int nbModalite, TypeCout modalChoisie, ModaliteTransport véhiculeChoisie)` permet de créer un graphe en utilisant un tableau de String qui sert de données, le nombre de "poids" que les arêtes auront, le critère priorisé par l'utilisateur, et son mode de déplacement.

#### Possibilité d'effectuer un voyage

La fonction `public boolean voyagePossible(MultiGrapheOrienteValue graphe, Sommet depart, Sommet arrivee)` permet de vérifier si un voyage est possible ou non entre 2 villes avec un moyen de transport sélectionné. Le choix du moyen de transport est effectué lors de la création du graphe passé en paramètre de cette fonction. Étant donné que le filtrage va supprimer les arêtes ne correspondant pas au moyen de transport de l'utilisateur, cette fonction va donc vérifier si les villes de départ et d'arrivée sont dans le graphe, et s'il existe ou non un plus court chemin allant du départ à l'arrivée.


Version 2 : multimodalité et prise en compte des correspondances
---

*Cette section explique la solution pour la Version 2 du projet.*

### Présentation d'un exemple

*Présenter un exemple concret (plateforme, couts de correspondance, critère d'optimalité).*
*Donner la solution du problème du point de vue de l'utilisatrice (quels sont les itinéraires possibles, lesquels sont optimaux et pourquoi).*
*Il est possible d'utiliser le même exemple que pour la Version 1 ou le modifier si pertinent.*

### Modèle pour l'exemple

*Donner le graphe modélisant l'exemple ci-dessus.*
*Donner la solution du problème (càd les meilleurs itinéraires) en tant que chemins dans le graphe.*

### Modélisation pour la Version 2 dans le cas général

*Mêmes questions que pour la section correspondante de la Version 1, mais cette fois-ci les données d'entrée contiennent aussi des couts de correspondance.*
*Vous pouvez expliquer l'entièreté de la solution pour la Version 2, ou bien indiquer **clairement** les différences par rapport à la solution proposée pour la Version 1.*

### Implémentation de la Version 2

*Écrire une classe de test qui reprend l'exemple, définit toutes les données de la plateforme, construit le graphe et calcule la solution.*
*Votre classe peut utiliser des assertions (test unitaire) ou bien afficher la solution.*
*Donner ici le **nom complet de la classe**, **la date et l'identifiant du commit à regarder** et un **lien vers la page de cette classe sur gitlab qui correspond au bon commit***.
*En particulier, il peut s'agir de la même classe que celle donnée pour la Version 1, mais un commit différent.*


Version 3 : optimisation multi-critères
---

*Suivre le même plan que pour les deux autres sections.*
*Pour l'exemple, veillez à spécifier toutes les données des problèmes. En particulier, on ajoute ici l'expression des préférences d'optimisation de l'utilisatrice.*
*Comme précédemment, il est possible d'utiliser le même exemple et simplement l'enrichir.*

----------------------------------------------------

**Fin du rapport**

### Barème sur 30 pts

Toute question sur le barème est à adresser à iovka.boneva@univ-lille.fr


- Rapport non rendu à temps -> note 0 
- **(7, décomposé comme suit)** Divers
  - **(1,5)** Respect de la structure du rapport
  - **(1,5)** Section Version 1 rendue pour le 18/05/2024. Cette version peut contenir les parties en italique.
  - **(1,5)** Section Version 2 rendue pour le 08/06/2024. Cette version peut contenir les parties en italique.
  - **(1)** Utilisation de vocabulaire précis sur les graphes (termes vu en cours, noms des algorithmes, etc.)
  - **(1,5)** Style d'écriture fluide et compréhensible

- **(8, décomposé comme suit)** Solution pour la Version 1
  - **(2)** Exemple pertinent (illustre tous les aspects du problème) et lisible (en particulier, ni trop grand ni trop petit, bien présenté)
  - **(4)** Le modèle de l'exemple permet de trouver la solution sur l'exemple. La modélisation pour le cas général permet de résoudre le problème posé
  - **(2)** L'implémentation de l'exemple est correcte et fonctionnelle

- **(6, décomposé comme suit)** Solution pour la Version 2
  - **(1)** Exemple pertinent
  - **(4)** le modèle de l'exemple permet de trouver la solution sur l'exemple. La modélisation pour le cas général permet de résoudre le problème posé
  - **(1)** L'implémentation de l'exemple est correcte et fonctionnelle

- **(3)** Qualité de la description de la solution (concerne les sections "Modèlisation dans le cas général" pour les Versions 1 et 2):
  - La modélisation pour le cas général est décrite de manière abstraite mais précise et complète. Pour vous donner une idée, un·e étudiant·e de BUT qui a validé les ressources Graphes et Dev devrait être en mesure d'implémenter votre solution d'après la description que vous en faites, sans avoir à trop réfléchir.

- **(6)** Solution pour la Version 3: mêmes critères que pour la Version 2