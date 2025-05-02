/* ******************************************
CE TP EST CELUI DE LOGIQUE DU PREMIER ORDRE
****************************************** */

package com.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.tweetyproject.commons.ParserException;
import org.tweetyproject.logics.commons.syntax.Constant;
import org.tweetyproject.logics.commons.syntax.Functor;
import org.tweetyproject.logics.commons.syntax.Predicate;
import org.tweetyproject.logics.commons.syntax.Sort;
import org.tweetyproject.logics.fol.parser.FolParser;
import org.tweetyproject.logics.fol.syntax.FolBeliefSet;
import org.tweetyproject.logics.fol.syntax.FolSignature;

public class Main2 {
    /**
     *
     * @param args arguments
     * @throws ParserException ParserException
     * @throws IOException     IOException
     */
    public static void main(String[] args) throws ParserException, IOException {

        FolSignature sig = new FolSignature();
        Sort person = new Sort("person");
        sig.add(person);
        Constant Sarah = new Constant("Sarah", person); // Sarah est une personne
        sig.add(Sarah);
        Constant Hayat = new Constant("Hayat", person); // Hayat est une personne
        sig.add(Hayat);
        Constant david = new Constant("David", person); // David est une personne
        sig.add(david);

        Sort activity = new Sort("activity");
        sig.add(activity);
        Constant hiking = new Constant("hiking", activity); // hiking est une activité
        sig.add(hiking);
        Constant swimming = new Constant("swimming", activity); // swimming est une activité
        sig.add(swimming);
        Constant painting = new Constant("painting", activity); // painting est une activité
        sig.add(painting);

        List<Sort> l = new ArrayList<Sort>();
        l.add(person);
        Predicate participates = new Predicate("Participates", l); // création du prédicat Participates d'arité 1
        sig.add(participates);
        l = new ArrayList<Sort>();
        l.add(person);
        l.add(activity);
        Predicate organizes = new Predicate("Organizes", l); // création du prédicat Organizes qui est d'arité 2
        sig.add(organizes);
        l = new ArrayList<Sort>();
        l.add(person);
        // isFriendWith est une fonction qui prend un terme qui est de type personne
        Functor knows = new Functor("knows", l, person);
        sig.add(knows);

        FolBeliefSet b = new FolBeliefSet();
        b.setSignature(sig);
        FolParser parser = new FolParser();
        parser.setSignature(sig);

        b.add(parser.parseFormula("forall X:(Participates(X) => (exists Y:(Organizes(X,Y))))"));
        // si X participe à une activité, alors knows(X) participe également à cette
        // activité
        b.add(parser.parseFormula("forall X:(Participates(X) => Participates(knows(X)))"));
        b.add(parser.parseFormula("Participates(Sarah)")); // Sarah participe à une activité
        b.add(parser.parseFormula("Organizes(Hayat, hiking)")); // Hayat organise une randonnée

        // imprimer dans la console
        System.out.println(b.toArray()[0]);
        System.out.println(b.toArray()[1]);
        System.out.println(b.toArray()[2]);
        System.out.println(b.toArray()[3]);

    }
}
