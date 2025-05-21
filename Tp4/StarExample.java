package be.fnord.DefaultLogic;

/**
 * Un exemple spécial pour une personne spéciale
 * 
 * Considérations cosmologiques sur les étoiles
 * 
 * 
 * 
 * 
 * 
 * 
Given the world: 
	star_is_shining
And the rules 
	[(star_is_shining):(star_is_visible) ==> (star_is_visible)]
Possible Extensions
	 Ext: Th(W U (star_is_visible))
	= star_is_visible & star_is_shining 
One day we discover that the star is obscured by clouds.
Given the world: 
	star_is_shining & clouds_covering_star & (clouds_covering_star -> ~star_is_visible)
And the rules 
	[(star_is_shining):(star_is_visible) ==> (star_is_visible)] , [(star_is_shining & clouds_covering_star):(star_is_obscured) ==> (~star_is_visible)]
Possible Extensions
	 Ext: Th(W U (~star_is_visible))
	= ~star_is_visible & star_is_shining & clouds_covering_star & (~star_is_visible | ~clouds_covering_star) 
 */



import java.util.HashSet;

import be.fnord.util.logic.DefaultReasoner;
import be.fnord.util.logic.WFF;
import be.fnord.util.logic.defaultLogic.DefaultRule;
import be.fnord.util.logic.defaultLogic.RuleSet;
import be.fnord.util.logic.defaultLogic.WorldSet;

public class StarExample {

    // Example 2.9 From
    // J. P. Delgrande, T. Schaub, and W. K. Jackson (1994). Alternative
    // approaches to default logic. Artificial Intelligence, 70:167-237.

    public static void example() {
        WorldSet myWorld = new WorldSet();
        // Start by believing that the star is shining
        myWorld.addFormula("star_is_shining"); // We think that the star is shining

        DefaultRule rule1 = new DefaultRule();
        rule1.setPrerequisite("star_is_shining");
        rule1.setJustificatoin("star_is_visible");
        rule1.setConsequence("star_is_visible");

        RuleSet myRules = new RuleSet();
        myRules.addRule(rule1);

        DefaultReasoner loader = new DefaultReasoner(myWorld, myRules);
        HashSet<String> extensions = loader.getPossibleScenarios();

        a.e.println("Given the world: \n\t" + myWorld.toString()
                + "\n And the rules \n\t" + myRules.toString());

        a.e.println("Possible Extensions");
        for (String c : extensions) {
            a.e.println("\t Ext: Th(W U (" + c + "))");
            // Added closure operator
            a.e.incIndent();
            WFF world_and_ext = new WFF("(( " + myWorld.getWorld() + " ) & ("
                    + c + "))");
            a.e.println("= " + world_and_ext.getClosure());
            a.e.decIndent();

        }

    }

    public static void example2() {
        WorldSet myWorld = new WorldSet();
        // Start by believing that the star is shining
        myWorld.addFormula("star_is_shining"); // We think that the star is shining
        a.e.println("One day we discover that the star is obscured by clouds.");
        // Learn one day that the star is obscured by clouds 
        myWorld.addFormula("clouds_covering_star");
        // Learn that if clouds are covering the star,
        // then the star is obscured
        myWorld.addFormula("(star_is_shining & clouds_covering_star " + a.e.IMPLIES + " " + a.e.NOT + "star_is_visible)"); 

        DefaultRule rule1 = new DefaultRule();
        rule1.setPrerequisite("star_is_shining");
        rule1.setJustificatoin("star_is_visible");
        rule1.setConsequence("star_is_visible");

        DefaultRule rule2 = new DefaultRule();
        rule2.setPrerequisite("star_is_shining & clouds_covering_star");
        rule2.setJustificatoin("star_is_obscured");
        rule2.setConsequence(a.e.NOT + "star_is_visible");

        RuleSet myRules = new RuleSet();
        myRules.addRule(rule1);
        myRules.addRule(rule2);

        DefaultReasoner loader = new DefaultReasoner(myWorld, myRules);
        HashSet<String> extensions = loader.getPossibleScenarios();

        a.e.println("Given the world: \n\t" + myWorld.toString()
                + "\n And the rules \n\t" + myRules.toString());

        a.e.println("Possible Extensions");
        for (String c : extensions) {
            a.e.println("\t Ext: Th(W U (" + c + "))");
            // Added closure operator
            a.e.incIndent();
            WFF world_and_ext = new WFF("(( " + myWorld.getWorld() + " ) & ("
                    + c + "))");
            a.e.println("= " + world_and_ext.getClosure());
            a.e.decIndent();

        }

    }

    public static void main(String[] args) {
        // Load example
        // Turn on the removal of empty effects from print statements
        a.e.HIDE_EMPTY_EFFECTS_IN_PRINT = true;

        example();
        example2();
    }

}

