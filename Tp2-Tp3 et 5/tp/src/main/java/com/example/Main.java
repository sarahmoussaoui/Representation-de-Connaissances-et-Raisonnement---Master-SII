/* ******************************************
CE TP EST CELUI DE LOGIQUE MODALE
****************************************** */
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

public class Main {
    public static void main(String[] args) throws ParserException, IOException {
        MlBeliefSet bs = new MlBeliefSet(); // Mlbeliefset est utilise pour stocker les formules modales
        MlParser parser = new MlParser(); // Mlparser est utilise pour parser les formules modales
        FolSignature sig = new FolSignature();  // FolSignature est utilise pour stocker les symboles de la logique du premier ordre
        // On ajoute les symboles de la logique du premier ordre dans la signature de la logique modale
        sig.add(new Predicate("p", 0));
        sig.add(new Predicate("u", 0));
        sig.add(new Predicate("b",0));
        //sig.add(new Predicate("n",0));
        //sig.add(new Predicate("t",1));
        //sig.add(new Predicate("s",0));
        // On ajoute la signature de la logique du premier ordre dans le parser de la logique modale
        parser.setSignature(sig);
        // On ajoute les formules modales dans la base de connaissances modale
        bs.add((RelationalFormula)parser.parseFormula("<>(p)"));
        bs.add((RelationalFormula)parser.parseFormula("[](!(p) || u)"));
        bs.add((RelationalFormula)parser.parseFormula("[](!b || (u))"));
        //bs.add((RelationalFormula) parser.parseFormula("[](t(X) && (X > 0 || s))"));

        // On affiche la base de connaissances modale
        System.out.println("Modal knowledge base: " + bs);
        //reasoner est utilise pour repondre aux requetes sur la base de connaissances modale par la methode query
        SimpleMlReasoner reasoner = new SimpleMlReasoner();
        System.out.println("<>(q)      " + reasoner.query(bs, (FolFormula)parser.parseFormula("<>(p)")) + "\n");
        System.out.println("<>(p && u)  " + reasoner.query(bs, (FolFormula)parser.parseFormula("[](!p || u)")) + "\n");
        System.out.println("!(u)  " + reasoner.query(bs, (FolFormula)parser.parseFormula("[](!n || b)")) + "\n");
    }
}
