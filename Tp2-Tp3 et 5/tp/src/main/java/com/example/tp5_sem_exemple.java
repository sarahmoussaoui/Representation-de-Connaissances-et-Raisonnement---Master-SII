package com.example;

import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Edge;
import org.graphstream.ui.view.Viewer.CloseFramePolicy;
import java.util.*;

// Classe représentant un concept informatique
class ConceptInformatique {
    String nom;
    List<RelationInformatique> relations;

    ConceptInformatique(String nom) {
        this.nom = nom;
        this.relations = new ArrayList<>();
    }

    void ajouterRelation(RelationInformatique relation) {
        relations.add(relation);
    }

    @Override
    public String toString() {
        return nom;
    }
}

// Classe représentant une relation informatique entre deux concepts
class RelationInformatique {
    ConceptInformatique source;
    ConceptInformatique cible;
    String type;

    RelationInformatique(ConceptInformatique source, ConceptInformatique cible, String type) {
        this.source = source;
        this.cible = cible;
        this.type = type;
    }

    @Override
    public String toString() {
        return source.nom + " " + type + " " + cible.nom;
    }
}

// Classe représentant le réseau sémantique informatique
class ReseauSemantiqueInformatique {
    Map<String, ConceptInformatique> concepts;

    ReseauSemantiqueInformatique() {
        this.concepts = new HashMap<>();
    }

    void ajouterConcept(ConceptInformatique concept) {
        concepts.put(concept.nom, concept);
    }

    void ajouterRelation(RelationInformatique relation) {
        concepts.get(relation.source.nom).ajouterRelation(relation);
    }

    // Méthode récursive pour propager les marqueurs
    private void propagerMarqueurs(ConceptInformatique concept, Set<ConceptInformatique> conceptsReponse,
            Set<ConceptInformatique> conceptsVisites, String verbe) {
        if (!conceptsVisites.contains(concept)) {
            conceptsVisites.add(concept);
            conceptsReponse.add(concept);
            for (RelationInformatique relation : concept.relations) {
                // Propager les marqueurs pour les relations spécifiées dans la question
                if (relation.type.equals("est un") || relation.type.equals(verbe)) {
                    propagerMarqueurs(relation.cible, conceptsReponse, conceptsVisites, verbe);
                }
            }
        }
    }

    // Méthode pour vérifier si un concept est mentionné dans la question
    private boolean estMentionneDansQuestion(ConceptInformatique concept, String[] mots) {
        for (String mot : mots) {
            if (mot.equalsIgnoreCase(concept.nom)) {
                return true;
            }
        }
        return false;
    }

    // Méthode de wrapper pour appeler la méthode récursive
    List<ConceptInformatique> repondreQuestionInformatique(String question, String verbe) {
        // Traitement de la question pour extraire les concepts clés
        String[] mots = question.split(" ");
        List<ConceptInformatique> conceptsQuestion = new ArrayList<>();
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
        Set<ConceptInformatique> conceptsReponse = new HashSet<>();
        for (ConceptInformatique concept : conceptsQuestion) {
            propagerMarqueurs(concept, conceptsReponse, new HashSet<>(), verbe);
        }

        // Filtrer les concepts trouvés pour exclure ceux mentionnés dans la question
        List<ConceptInformatique> resultats = new ArrayList<>();
        for (ConceptInformatique concept : conceptsReponse) {
            if (!estMentionneDansQuestion(concept, mots)) {
                resultats.add(concept);
            }
        }

        // Retourner les concepts marqués en tant que réponse
        return resultats;
    }

    // Méthode pour afficher le réseau (pour déboguer)
    void afficherReseau() {
        for (ConceptInformatique concept : concepts.values()) {
            System.out.println("Concept: " + concept.nom);
            for (RelationInformatique relation : concept.relations) {
                System.out.println("  " + relation);
            }
        }
    }

    // Méthode pour générer un graphe GraphStream à partir du réseau sémantique
    // informatique
    Graph genererGraphe() {
        Graph graph = new SingleGraph("ReseauInformatique");

        // Ajouter les nœuds pour chaque concept
        for (ConceptInformatique concept : concepts.values()) {
            Node node = graph.addNode(concept.nom);
            node.setAttribute("ui.label", concept.nom);
            node.setAttribute("ui.style", "size: 40px; fill-color: blue; text-color: Black;text-size: 14px;");
        }

        // Ajouter les arêtes pour chaque relation
        for (ConceptInformatique concept : concepts.values()) {
            for (RelationInformatique relation : concept.relations) {
                Edge edge = graph.addEdge(concept.nom + "-" + relation.cible.nom, concept.nom, relation.cible.nom,
                        true);
                edge.setAttribute("ui.label", relation.type);
                edge.setAttribute("ui.style", "arrow-size: 35px, 4px;");
            }
        }

        return graph;
    }

    // Méthode pour visualiser le réseau sémantique informatique
    void visualiserReseau() {
        // Créer une instance de Graph
        Graph graph = genererGraphe();

        // Ajouter une feuille de style CSS pour personnaliser le graphe
        graph.setAttribute("ui.stylesheet",
                "node { size: 30px; fill-color: blue; text-color: white; text-size: 18px; text-alignment: center; } " +
                        "edge { text-size: 12px; } " +
                        "node.marked { fill-color: red; } " +
                        "edge.marked { fill-color: red; }" +
                        "node.response {fill-color: green;}" +
                        "edge.response {fill-color: green;}");

        // Créer une instance de Viewer
        Viewer viewer = graph.display();

        // Définir la stratégie de fermeture de la fenêtre
        viewer.setCloseFramePolicy(CloseFramePolicy.CLOSE_VIEWER);
    }

    void visualiserReseauAvecMarqueurs(List<ConceptInformatique> conceptsMarques,
            List<ConceptInformatique> conceptsReponse, int questionId) {
        // Créer une instance de Graph
        Graph graph = genererGraphe();

        // Générer des classes CSS dynamiques pour chaque question
        String marqueurNodeClass = "marked_" + questionId;
        String marqueurEdgeClass = "marked_edge_" + questionId;
        String reponseNodeClass = "response_" + questionId;
        String reponseEdgeClass = "response_edge_" + questionId;

        // Ajouter une feuille de style CSS pour personnaliser le graphe
        graph.setAttribute("ui.stylesheet",
                "node { fill-color: blue; size: 20px; text-color: white; text-size: 15px; } " +
                        "edge { text-size: 12px; } " +
                        "node." + marqueurNodeClass + " { fill-color: red; } " +
                        "edge." + marqueurEdgeClass + " { fill-color: red; } " +
                        "node." + reponseNodeClass + " { fill-color: green; } " +
                        "edge." + reponseEdgeClass + " { fill-color: green; } ");

        // Marquer les nœuds et les arêtes liés aux concepts de la question
        for (ConceptInformatique concept : conceptsMarques) {
            Node node = graph.getNode(concept.nom);
            if (node != null) {
                node.setAttribute("ui.class", marqueurNodeClass);
                for (RelationInformatique relation : concept.relations) {
                    Edge edge = graph.getEdge(concept.nom + "-" + relation.cible.nom);
                    if (edge != null) {
                        edge.setAttribute("ui.class", marqueurEdgeClass);
                    }
                }
            }
        }

        // Marquer les nœuds et les arêtes liés aux concepts de la réponse
        for (ConceptInformatique concept : conceptsReponse) {
            Node node = graph.getNode(concept.nom);
            if (node != null) {
                node.setAttribute("ui.class", reponseNodeClass);
                for (RelationInformatique relation : concept.relations) {
                    Edge edge = graph.getEdge(concept.nom + "-" + relation.cible.nom);
                    if (edge != null) {
                        edge.setAttribute("ui.class", reponseEdgeClass);
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

// Exemple d'utilisation dans le domaine informatique
public class tp5_sem_exemple {
    public static void main(String[] args) {
        // Définir le package UI à utiliser
        System.setProperty("org.graphstream.ui", "swing");

        // Créer une instance de ReseauSemantiqueInformatique
        ReseauSemantiqueInformatique reseauInformatique = new ReseauSemantiqueInformatique();

        // Création des concepts informatiques
        ConceptInformatique langage = new ConceptInformatique("langage");
        ConceptInformatique python = new ConceptInformatique("Python");
        ConceptInformatique java = new ConceptInformatique("Java");
        ConceptInformatique cpp = new ConceptInformatique("C++");
        ConceptInformatique ruby = new ConceptInformatique("Ruby");
        ConceptInformatique outil = new ConceptInformatique("outil");
        ConceptInformatique application = new ConceptInformatique("application");
        ConceptInformatique entreprise = new ConceptInformatique("entreprise");
        ConceptInformatique systemeExploitation = new ConceptInformatique("système d'exploitation");
        ConceptInformatique assistantVirtuel = new ConceptInformatique("assistant virtuel");
        ConceptInformatique developpeur = new ConceptInformatique("développeur logiciel");
        ConceptInformatique bibliothequeOpenSource = new ConceptInformatique("bibliothèque open-source");
        ConceptInformatique analyseDonnees = new ConceptInformatique("analyse de données");

        // Ajout des concepts au réseau informatique
        reseauInformatique.ajouterConcept(langage);
        reseauInformatique.ajouterConcept(python);
        reseauInformatique.ajouterConcept(java);
        reseauInformatique.ajouterConcept(cpp);
        reseauInformatique.ajouterConcept(ruby);
        reseauInformatique.ajouterConcept(outil);
        reseauInformatique.ajouterConcept(application);
        reseauInformatique.ajouterConcept(entreprise);
        reseauInformatique.ajouterConcept(systemeExploitation);
        reseauInformatique.ajouterConcept(assistantVirtuel);
        reseauInformatique.ajouterConcept(developpeur);
        reseauInformatique.ajouterConcept(bibliothequeOpenSource);
        reseauInformatique.ajouterConcept(analyseDonnees);

        // Création des relations informatiques

        RelationInformatique r1 = new RelationInformatique(langage, python, "est un");
        RelationInformatique r2 = new RelationInformatique(langage, java, "est un");
        RelationInformatique r3 = new RelationInformatique(langage, cpp, "est un");
        RelationInformatique r4 = new RelationInformatique(langage, ruby, "est un");
        RelationInformatique r5 = new RelationInformatique(python, application, "conduit à");
        RelationInformatique r6 = new RelationInformatique(python, bibliothequeOpenSource, "conduit à");
        RelationInformatique r7 = new RelationInformatique(developpeur, java, "utilise");
        RelationInformatique r8 = new RelationInformatique(java, entreprise, "travaille dans");
        RelationInformatique r9 = new RelationInformatique(java, systemeExploitation, "concerne");
        RelationInformatique r10 = new RelationInformatique(ruby, developpeur, "doute");
        RelationInformatique r11 = new RelationInformatique(ruby, application, "doute");
        RelationInformatique r12 = new RelationInformatique(entreprise, application, "utilise");
        RelationInformatique r13 = new RelationInformatique(systemeExploitation, assistantVirtuel, "concerne");
        RelationInformatique r14 = new RelationInformatique(systemeExploitation, application, "concerne");
        RelationInformatique r15 = new RelationInformatique(developpeur, analyseDonnees, "utilise");
        RelationInformatique r16 = new RelationInformatique(python, analyseDonnees, "utilisé dans");
        RelationInformatique r17 = new RelationInformatique(java, analyseDonnees, "utilisé dans");
        RelationInformatique r18 = new RelationInformatique(cpp, analyseDonnees, "utilisé dans");
        RelationInformatique r19 = new RelationInformatique(outil, langage, "est un");

        // Ajout des relations au réseau informatique
        reseauInformatique.ajouterRelation(r1);
        reseauInformatique.ajouterRelation(r2);
        reseauInformatique.ajouterRelation(r3);
        reseauInformatique.ajouterRelation(r4);
        reseauInformatique.ajouterRelation(r5);
        reseauInformatique.ajouterRelation(r6);
        reseauInformatique.ajouterRelation(r7);
        reseauInformatique.ajouterRelation(r8);
        reseauInformatique.ajouterRelation(r9);
        reseauInformatique.ajouterRelation(r10);
        reseauInformatique.ajouterRelation(r11);
        reseauInformatique.ajouterRelation(r12);
        reseauInformatique.ajouterRelation(r13);
        reseauInformatique.ajouterRelation(r14);
        reseauInformatique.ajouterRelation(r15);
        reseauInformatique.ajouterRelation(r16);
        reseauInformatique.ajouterRelation(r17);
        reseauInformatique.ajouterRelation(r18);
        reseauInformatique.ajouterRelation(r19);

        // Afficher le réseau pour déboguer
        reseauInformatique.afficherReseau();

        // Répondre à la première question
        String question1 = "Quels sont les langage de programmation ?";
        List<ConceptInformatique> conceptsMarques1 = reseauInformatique.repondreQuestionInformatique(question1,
                "est un");
        List<ConceptInformatique> reponses1 = new ArrayList<>(conceptsMarques1); // Copie des réponses

        // Afficher la réponse à la première question
        System.out.println("Réponse à la question informatique \"" + question1 + "\" :");
        if (reponses1.isEmpty()) {
            System.out.println("Aucune réponse trouvée pour la question. Manque de connaissances.");
        } else {
            for (ConceptInformatique concept : reponses1) {
                System.out.println("- " + concept.nom);
            }
        }

        // Visualiser le réseau pour la première question
        reseauInformatique.visualiserReseauAvecMarqueurs(conceptsMarques1, reponses1, 1);

        // Répondre à la deuxième question
        String question2 = "Qu'est-ce que Python conduit à ?";
        List<ConceptInformatique> conceptsMarques2 = reseauInformatique.repondreQuestionInformatique(question2,
                "conduit à");
        List<ConceptInformatique> reponses2 = new ArrayList<>(conceptsMarques2); // Copie des réponses

        // Afficher la réponse à la deuxième question
        System.out.println("Réponse à la question informatique \"" + question2 + "\" :");
        if (reponses2.isEmpty()) {
            System.out.println("Aucune réponse trouvée pour la question. Manque de connaissances.");
        } else {
            for (ConceptInformatique concept : reponses2) {
                System.out.println("- " + concept.nom);
            }
        }

        // Visualiser le réseau pour la deuxième question
        reseauInformatique.visualiserReseauAvecMarqueurs(conceptsMarques2, reponses2, 2);

        // Répondre à la troisième question
        String question3 = "Qu'est-ce que Python est utilisé dans ?";
        // String question3 = "Qu'est-ce que java concerne ?";
        List<ConceptInformatique> conceptsMarques3 = reseauInformatique.repondreQuestionInformatique(question3,
                "utilisé dans");
        List<ConceptInformatique> reponses3 = new ArrayList<>(conceptsMarques3); // Copie des réponses

        // Afficher la réponse à la troisième question
        System.out.println("Réponse à la question informatique \"" + question3 + "\" :");
        if (reponses3.isEmpty()) {
            System.out.println("Aucune réponse trouvée pour la question. Manque de connaissances.");
        } else {
            for (ConceptInformatique concept : reponses3) {
                System.out.println("- " + concept.nom);
            }
        }

        // Visualiser le réseau pour la troisième question
        reseauInformatique.visualiserReseauAvecMarqueurs(conceptsMarques3, reponses3, 3);

        String question4 = "Qu'est-ce que Python utilise ?";
        // String question3 = "Qu'est-ce que python utilise ?";
        List<ConceptInformatique> conceptsMarques4 = reseauInformatique.repondreQuestionInformatique(question4,
                "utilise");
        List<ConceptInformatique> reponses4 = new ArrayList<>(conceptsMarques4); // Copie des réponses

        // Afficher la réponse à la troisième question
        System.out.println("Réponse à la question informatique \"" + question4 + "\" :");
        if (reponses4.isEmpty()) {
            System.out.println("Aucune réponse trouvée pour la question. Manque de connaissances.");
        } else {
            for (ConceptInformatique concept : reponses4) {
                System.out.println("- " + concept.nom);
            }
        }
        // Visualiser le réseau pour la troisième question
        reseauInformatique.visualiserReseauAvecMarqueurs(conceptsMarques4, reponses4, 4);

    }

}
