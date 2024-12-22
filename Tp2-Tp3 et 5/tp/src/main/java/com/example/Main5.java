/* ******************************************
CE TP EST CELUI RESEAU SEMANTIQUE
****************************************** */
package com.example;

import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Edge;
import org.graphstream.ui.view.Viewer.CloseFramePolicy;
import java.util.*;

// Classe représentant un concept musical
class ConceptMusical {
    String nom;
    List<RelationMusical> relations;

    ConceptMusical(String nom) {
        this.nom = nom;
        this.relations = new ArrayList<>();
    }

    void ajouterRelation(RelationMusical relation) {
        relations.add(relation);
    }

    @Override
    public String toString() {
        return nom;
    }
}

// Classe représentant une relation musicale entre deux concepts
class RelationMusical {
    ConceptMusical source;
    ConceptMusical cible;
    String type;

    RelationMusical(ConceptMusical source, ConceptMusical cible, String type) {
        this.source = source;
        this.cible = cible;
        this.type = type;
    }

    @Override
    public String toString() {
        return source.nom + " " + type + " " + cible.nom;
    }
}

// Classe représentant le réseau sémantique musical
class ReseauSemantiqueMusicale {
    Map<String, ConceptMusical> concepts;

    ReseauSemantiqueMusicale() {
        this.concepts = new HashMap<>();
    }

    void ajouterConcept(ConceptMusical concept) {
        concepts.put(concept.nom, concept);
    }

    void ajouterRelation(RelationMusical relation) {
        concepts.get(relation.source.nom).ajouterRelation(relation);
    }

    // Méthode pour répondre à une question musicale en propageant les marqueurs
    List<ConceptMusical> repondreQuestionMusicale(String question) {
        // Traitement de la question pour extraire les concepts clés
        String[] mots = question.split(" ");
        List<ConceptMusical> conceptsQuestion = new ArrayList<>();
        for (String mot : mots) {
            if (concepts.containsKey(mot)) {
                conceptsQuestion.add(concepts.get(mot));
            }
        }

        // Si aucun concept n'est trouvé dans la question, retourner une liste vide
        if (conceptsQuestion.isEmpty()) {
            return Collections.emptyList();
        }

        // Propagation des marqueurs à partir des concepts de la question
        Set<ConceptMusical> conceptsReponse = new HashSet<>();
        for (ConceptMusical concept : conceptsQuestion) {
            propagerMarqueurs(concept, conceptsReponse);
        }

        return new ArrayList<>(conceptsReponse);
    }

    // Méthode récursive pour propager les marqueurs
    private void propagerMarqueurs(ConceptMusical concept, Set<ConceptMusical> conceptsReponse) {
        if (!conceptsReponse.contains(concept)) {
            conceptsReponse.add(concept);
            for (RelationMusical relation : concept.relations) {
                propagerMarqueurs(relation.cible, conceptsReponse);
            }
        }
    }

    // Méthode pour afficher le réseau (pour déboguer)
    void afficherReseau() {
        for (ConceptMusical concept : concepts.values()) {
            System.out.println("Concept: " + concept.nom);
            for (RelationMusical relation : concept.relations) {
                System.out.println("  " + relation);
            }
        }
    }

    // Méthode pour générer un graphe GraphStream à partir du réseau sémantique musical
    Graph genererGraphe() {
        Graph graph = new SingleGraph("ReseauMusical");

        // Ajouter les nœuds pour chaque concept
        for (ConceptMusical concept : concepts.values()) {
            Node node = graph.addNode(concept.nom);
            node.setAttribute("ui.label", concept.nom);
            node.setAttribute("ui.style", "size: 40px; fill-color: blue; text-color: white;text-size: 14px;");
        }

        // Ajouter les arêtes pour chaque relation
        for (ConceptMusical concept : concepts.values()) {
            for (RelationMusical relation : concept.relations) {
                Edge edge = graph.addEdge(concept.nom + "-" + relation.cible.nom, concept.nom, relation.cible.nom, true);
                edge.setAttribute("ui.label", relation.type);
                edge.setAttribute("ui.style", "arrow-size: 20px, 3px;");
            }
        }

        return graph;
    }

    // Méthode pour visualiser le réseau sémantique musical
    void visualiserReseau() {
        // Créer une instance de Graph
        Graph graph = genererGraphe();

        // Ajouter une feuille de style CSS pour personnaliser le graphe
        /* graph.setAttribute("ui.stylesheet", "node { fill-color: blue; size: 20px; text-color: white; text-size: 15px; } " +
                                           "edge { text-size: 12px; }"); */
        graph.setAttribute("ui.stylesheet", "node { size: 30px; fill-color: blue; text-color: white; text-size: 18px; text-alignment: center; } " +
                                           "edge { text-size: 12px; } " +
                                           "node.marked { fill-color: red; } " +
                                           "edge.marked { fill-color: red; }");
     
     

        // Créer une instance de Viewer
        Viewer viewer = graph.display();

        // Définir la stratégie de fermeture de la fenêtre
        viewer.setCloseFramePolicy(CloseFramePolicy.CLOSE_VIEWER);
    }

    // Méthode pour visualiser le réseau sémantique musical avec les concepts de la question en couleur
    void visualiserReseauAvecMarqueurs(List<ConceptMusical> conceptsMarques) {
        // Créer une instance de Graph
        Graph graph = genererGraphe();

        // Ajouter une feuille de style CSS pour personnaliser le graphe
        graph.setAttribute("ui.stylesheet", "node { fill-color: blue; size: 20px; text-color: white; text-size: 15px; } " +
                                           "edge { text-size: 12px; } " +
                                           "node.marked { fill-color: red; } " +
                                           "edge.marked { fill-color: red; }");

        // Marquer les nœuds et les arêtes liés aux concepts de la question
        for (ConceptMusical concept : conceptsMarques) {
            Node node = graph.getNode(concept.nom);
            if (node != null) {
                node.setAttribute("ui.class", "marked");
                for (RelationMusical relation : concept.relations) {
                    Edge edge = graph.getEdge(concept.nom + "-" + relation.cible.nom);
                    if (edge != null) {
                        edge.setAttribute("ui.class", "marked");
                    }
                }
            }
        }

        // Créer une instance de Viewer
        Viewer viewer = graph.display();

        // Définir la stratégie de fermeture de la fenêtre
        viewer.setCloseFramePolicy(CloseFramePolicy.CLOSE_VIEWER);
    }
}

// Exemple d'utilisation dans le domaine musical
public class Main5 {
    public static void main(String[] args) {
        // Définir le package UI à utiliser
        System.setProperty("org.graphstream.ui", "swing");

        ReseauSemantiqueMusicale reseauMusical = new ReseauSemantiqueMusicale();

        // Création des concepts musicaux
        ConceptMusical instrument = new ConceptMusical("instrument");
        ConceptMusical percussion = new ConceptMusical("percussion");
        ConceptMusical cordes = new ConceptMusical("cordes");
        ConceptMusical bois = new ConceptMusical("bois");
        ConceptMusical cuivres = new ConceptMusical("cuivres");
        ConceptMusical piano = new ConceptMusical("piano");
        ConceptMusical guitare = new ConceptMusical("guitare");
        ConceptMusical violon = new ConceptMusical("violon");
        ConceptMusical flute = new ConceptMusical("flûte");
        ConceptMusical trompette = new ConceptMusical("trompette");
        ConceptMusical rythme = new ConceptMusical("rythme");
        ConceptMusical melodie = new ConceptMusical("mélodie");
        ConceptMusical harmonie = new ConceptMusical("harmonie");
        ConceptMusical tempo = new ConceptMusical("tempo");
        ConceptMusical tonalite = new ConceptMusical("tonalité");
        ConceptMusical orchestre = new ConceptMusical("orchestre");
        ConceptMusical symphonie = new ConceptMusical("symphonie");

        // Ajout des concepts au réseau musical
        reseauMusical.ajouterConcept(instrument);
        reseauMusical.ajouterConcept(percussion);
        reseauMusical.ajouterConcept(cordes);
        reseauMusical.ajouterConcept(bois);
        reseauMusical.ajouterConcept(cuivres);
        reseauMusical.ajouterConcept(piano);
        reseauMusical.ajouterConcept(guitare);
        reseauMusical.ajouterConcept(violon);
        reseauMusical.ajouterConcept(flute);
        reseauMusical.ajouterConcept(trompette);
        reseauMusical.ajouterConcept(rythme);
        reseauMusical.ajouterConcept(melodie);
        reseauMusical.ajouterConcept(harmonie);
        reseauMusical.ajouterConcept(tempo);
        reseauMusical.ajouterConcept(tonalite);
        reseauMusical.ajouterConcept(orchestre);
        reseauMusical.ajouterConcept(symphonie);

        // Création des relations musicales
        RelationMusical r1 = new RelationMusical(instrument, percussion, "est un");
        RelationMusical r2 = new RelationMusical(instrument, cordes, "est un");
        RelationMusical r3 = new RelationMusical(instrument, bois, "est un");
        RelationMusical r4 = new RelationMusical(instrument, cuivres, "est un");
        RelationMusical r5 = new RelationMusical(percussion, piano, "est un");
        RelationMusical r6 = new RelationMusical(cordes, guitare, "est un");
        RelationMusical r7 = new RelationMusical(cordes, violon, "est un");
        RelationMusical r8 = new RelationMusical(bois, flute, "est un");
        RelationMusical r9 = new RelationMusical(cuivres, trompette, "est un");
        RelationMusical r10 = new RelationMusical(rythme, tempo, "inclut");
        RelationMusical r11 = new RelationMusical(melodie, tonalite, "a une");
        RelationMusical r12 = new RelationMusical(harmonie, tonalite, "a une");
        RelationMusical r13 = new RelationMusical(orchestre, symphonie, "joue une");
        RelationMusical r14 = new RelationMusical(symphonie, harmonie, "inclut");
        RelationMusical r15 = new RelationMusical(symphonie, melodie, "inclut");
        // Création des relations entre les sous-réseaux
        RelationMusical r16 = new RelationMusical(instrument, rythme, "inclut");
        RelationMusical r17 = new RelationMusical(instrument, orchestre, "joue");
        RelationMusical r18 = new RelationMusical(rythme, orchestre, "inclut");




        // Ajout des relations au réseau musical
        reseauMusical.ajouterRelation(r1);
        reseauMusical.ajouterRelation(r2);
        reseauMusical.ajouterRelation(r3);
        reseauMusical.ajouterRelation(r4);
        reseauMusical.ajouterRelation(r5);
        reseauMusical.ajouterRelation(r6);
        reseauMusical.ajouterRelation(r7);
        reseauMusical.ajouterRelation(r8);
        reseauMusical.ajouterRelation(r9);
        reseauMusical.ajouterRelation(r10);
        reseauMusical.ajouterRelation(r11);
        reseauMusical.ajouterRelation(r12);
        reseauMusical.ajouterRelation(r13);
        reseauMusical.ajouterRelation(r14);
        reseauMusical.ajouterRelation(r15);
        // Ajout des relations entre les sous-réseaux au réseau musical
        reseauMusical.ajouterRelation(r16);
        reseauMusical.ajouterRelation(r17);
        reseauMusical.ajouterRelation(r18);

        // Afficher le réseau pour déboguer
        reseauMusical.afficherReseau();

        // Répondre à des questions musicales
        String question1 = "Quels sont les instruments de musique ?";
        List<ConceptMusical> reponses1 = reseauMusical.repondreQuestionMusicale(question1);
        System.out.println("Réponse à la question musicale \"" + question1 + "\" :");
        if (reponses1.isEmpty()) {
            System.out.println("Aucune réponse trouvée pour la question. Manque de connaissances.");
        } else {
            for (ConceptMusical concept : reponses1) {
                System.out.println("- " + concept.nom);
            }
        }

        String question2 = "Que joue un orchestre ?";
        List<ConceptMusical> reponses2 = reseauMusical.repondreQuestionMusicale(question2);
        System.out.println("Réponse à la question musicale \"" + question2 + "\" :");
        if (reponses2.isEmpty()) {
            System.out.println("Aucune réponse trouvée pour la question. Manque de connaissances.");
        } else {
            for (ConceptMusical concept : reponses2) {
                System.out.println("- " + concept.nom);
            }
        }

        String question3 = "Quels sont les éléments d'une symphonie ?";
        List<ConceptMusical> reponses3 = reseauMusical.repondreQuestionMusicale(question3);
        System.out.println("Réponse à la question musicale \"" + question3 + "\" :");
        if (reponses3.isEmpty()) {
            System.out.println("Aucune réponse trouvée pour la question. Manque de connaissances.");
        } else {
            for (ConceptMusical concept : reponses3) {
                System.out.println("- " + concept.nom);
            }
        }

        String question4 = "Quels sont les instruments à vent ?";
        List<ConceptMusical> reponses4 = reseauMusical.repondreQuestionMusicale(question4);
        System.out.println("Réponse à la question musicale \"" + question4 + "\" :");
        if (reponses4.isEmpty()) {
            System.out.println("Aucune réponse trouvée pour la question. Manque de connaissances.");
        } else {
            for (ConceptMusical concept : reponses4) {
                System.out.println("- " + concept.nom);
            }
        }

        // Visualiser le réseau sémantique musical
        reseauMusical.visualiserReseau();

        // Visualiser le réseau sémantique musical avec les concepts de la question en couleur
        reseauMusical.visualiserReseauAvecMarqueurs(reponses1);
    }
}
