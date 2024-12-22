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

public class Main3 {
    public static void main(String[] args) throws ParserException, IOException {
        MlBeliefSet bs = new MlBeliefSet(); // Mlbeliefset est utilise pour stocker les formules modales
        MlParser parser = new MlParser(); // Mlparser est utilise pour parser les formules modales
        FolSignature sig = new FolSignature();  // FolSignature est utilise pour stocker les symboles de la logique du premier ordre
        // On ajoute les symboles de la logique du premier ordre dans la signature de la logique modale
        sig.add(new Predicate("fenetres_fermees", 0));
        sig.add(new Predicate("porte_verrouillee", 0));
        // On ajoute la signature de la logique du premier ordre dans le parser de la logique modale
        parser.setSignature(sig);
        // On ajoute les formules modales dans la base de connaissances modale
        bs.add((RelationalFormula)parser.parseFormula("<>(fenetres_fermees && porte_verrouillee)"));
        bs.add((RelationalFormula)parser.parseFormula("[](!fenetres_fermees || porte_verrouillee)"));
        bs.add((RelationalFormula)parser.parseFormula("[](porte_verrouillee && <>(!porte_verrouillee))"));
        // On affiche la base de connaissances modale
        System.out.println("Modal knowledge base: " + bs);
        //reasoner est utilise pour repondre aux requetes sur la base de connaissances modale par la methode query
        SimpleMlReasoner reasoner = new SimpleMlReasoner();
        System.out.println("[](!fenetres_fermees)      " + reasoner.query(bs, (FolFormula)parser.parseFormula("[](!fenetres_fermees)")) + "\n");
        System.out.println("<>(fenetres_fermees && porte_verrouillee)  " + reasoner.query(bs, (FolFormula)parser.parseFormula("<>(fenetres_fermees && porte_verrouillee)")) + "\n");
        System.out.println("!(porte_verrouillee)  " + reasoner.query(bs, (FolFormula)parser.parseFormula("!(porte_verrouillee)")) + "\n");
    }
}


