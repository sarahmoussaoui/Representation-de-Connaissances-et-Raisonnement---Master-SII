package com.example;

import org.tweetyproject.commons.ParserException;
import org.tweetyproject.logics.commons.syntax.Predicate;
import org.tweetyproject.logics.commons.syntax.RelationalFormula;
import org.tweetyproject.logics.fol.syntax.FolFormula;
import org.tweetyproject.logics.fol.syntax.FolSignature;
import org.tweetyproject.logics.ml.parser.MlParser;
import org.tweetyproject.logics.ml.reasoner.SimpleMlReasoner;
import org.tweetyproject.logics.ml.syntax.MlBeliefSet;

import java.io.IOException;

public class tp3_modale_ex {
        public static void main(String[] args) throws ParserException, IOException {
                MlBeliefSet bs = new MlBeliefSet(); // Base de croyances modale
                MlParser parser = new MlParser();
                FolSignature sig = new FolSignature(); // Signature de la logique du premier ordre

                // Déclaration des prédicats (faits atomiques)
                sig.add(new Predicate("lumiere_allumee", 0));
                sig.add(new Predicate("chauffage_en_marche", 0));

                parser.setSignature(sig);

                // Formules modales
                bs.add((RelationalFormula) parser.parseFormula("<>(lumiere_allumee && chauffage_en_marche)"));
                bs.add((RelationalFormula) parser.parseFormula("[](!lumiere_allumee || chauffage_en_marche)"));
                bs.add((RelationalFormula) parser.parseFormula("[](chauffage_en_marche && <>(!chauffage_en_marche))"));

                // Affichage de la base de connaissances
                System.out.println("Modal knowledge base: " + bs);

                // Raisonneur logique
                SimpleMlReasoner reasoner = new SimpleMlReasoner();

                System.out.println("[](!lumiere_allumee)      "
                                + reasoner.query(bs, (FolFormula) parser.parseFormula("[](!lumiere_allumee)")) + "\n");

                System.out.println("<>(lumiere_allumee && chauffage_en_marche)  "
                                + reasoner.query(bs, (FolFormula) parser.parseFormula(
                                                "<>(lumiere_allumee && chauffage_en_marche)"))
                                + "\n");

                System.out.println("!(chauffage_en_marche)  "
                                + reasoner.query(bs, (FolFormula) parser.parseFormula("!(chauffage_en_marche)"))
                                + "\n");
        }
}
