package be.fnord.DefaultLogic;

/**
 * Un exemple spécial pour une personne spéciale
 * 
 * Exploration spatiale
 * 
 * 
 * 
 * 
 * 
 * 
Étant donné le monde : 
	astronaute1_a_bord
Et les règles 
	[(astronaute1_a_bord):(vaisseau_pret) ==> (vaisseau_pret)]
	[(astronaute1_a_bord):(astronaute_en_mission) ==> (astronaute_en_mission)]
Extensions possibles
	 Ext: Th(W U (vaisseau_pret))
	= vaisseau_pret & astronaute1_a_bord
	 Ext: Th(W U (astronaute_en_mission))
	= astronaute_en_mission & astronaute1_a_bord
Nous recevons l'information selon laquelle l'astronaute1 effectue une EVA.
Étant donné le monde : 
	astronaute1_a_bord & eva_en_cours & (eva_en_cours -> ~astronaute_en_mission)
Et les règles 
	[(astronaute1_a_bord):(vaisseau_pret) ==> (vaisseau_pret)] , 
	[(astronaute1_a_bord):(astronaute_en_mission) ==> (astronaute_en_mission)] , 
	[(astronaute1_a_bord & eva_en_cours):(eva_en_cours) ==> (~astronaute_en_mission)]
Extensions possibles
	 Ext: Th(W U (vaisseau_pret))
	= vaisseau_pret & astronaute1_a_bord
	 Ext: Th(W U (astronaute_en_mission))
	= astronaute_en_mission & astronaute1_a_bord
	 Ext: Th(W U (eva_en_cours))
	= eva_en_cours & astronaute1_a_bord & eva_en_cours & (~astronaute_en_mission | ~eva_en_cours) 
 */


 import java.util.HashSet;
 
 import be.fnord.util.logic.DefaultReasoner;
 import be.fnord.util.logic.WFF;
 import be.fnord.util.logic.defaultLogic.DefaultRule;
 import be.fnord.util.logic.defaultLogic.RuleSet;
 import be.fnord.util.logic.defaultLogic.WorldSet;
 
 public class AstronauteExample {
 
     public static void example() {
         WorldSet myWorld = new WorldSet();
         // Commencer par croire que l'astronaute 1 est à bord
         myWorld.addFormula("astronaute1_a_bord");
 
         DefaultRule rule1 = new DefaultRule();
         rule1.setPrerequisite("astronaute1_a_bord");
         rule1.setJustificatoin("vaisseau_pret");
         rule1.setConsequence("vaisseau_pret");
 
         DefaultRule rule2 = new DefaultRule();
         rule2.setPrerequisite("astronaute1_a_bord");
         rule2.setJustificatoin("astronaute_en_mission");
         rule2.setConsequence("astronaute_en_mission");
 
         RuleSet myRules = new RuleSet();
         myRules.addRule(rule1);
         myRules.addRule(rule2);
 
         DefaultReasoner loader = new DefaultReasoner(myWorld, myRules);
         HashSet<String> extensions = loader.getPossibleScenarios();
 
         a.e.println("Étant donné le monde : \n\t" + myWorld.toString()
                 + "\n Et les règles \n\t" + myRules.toString());
 
         a.e.println("Extensions possibles");
         for (String c : extensions) {
             a.e.println("\t Ext: Th(W U (" + c + "))");
             a.e.incIndent();
             WFF world_and_ext = new WFF("(( " + myWorld.getWorld() + " ) & ("
                     + c + "))");
             a.e.println("= " + world_and_ext.getClosure());
             a.e.decIndent();
 
         }
 
     }
 
     public static void example2() {
        
         WorldSet myWorld = new WorldSet();
         // Commencez par croire que l'astronaute 1 est à bord
         myWorld.addFormula("astronaute1_a_bord");
         a.e.println("Nous recevons l'information selon laquelle l'astronaute1 effectue une EVA.");
         // Apprenez un jour que l'astronaute 1 effectue une EVA
         myWorld.addFormula("eva_en_cours");
         // Apprenez que si l'astronaute 1 est à bord et qu'une EVA est en cours, alors la mission de l'astronaute est terminée
         myWorld.addFormula("(astronaute1_a_bord & eva_en_cours " + a.e.IMPLIES + " " + a.e.NOT + "astronaute_en_mission)");
 
         DefaultRule rule1 = new DefaultRule();
         rule1.setPrerequisite("astronaute1_a_bord");
         rule1.setJustificatoin("vaisseau_pret");
         rule1.setConsequence("vaisseau_pret");
 
         DefaultRule rule2 = new DefaultRule();
         rule2.setPrerequisite("astronaute1_a_bord");
         rule2.setJustificatoin("astronaute_en_mission");
         rule2.setConsequence("astronaute_en_mission");
 
         DefaultRule rule3 = new DefaultRule();
         rule3.setPrerequisite("astronaute1_a_bord & eva_en_cours");
         rule3.setJustificatoin("eva_en_cours");
         rule3.setConsequence(a.e.NOT + "astronaute_en_mission");
 
         RuleSet myRules = new RuleSet();
         myRules.addRule(rule1);
         myRules.addRule(rule2);
         myRules.addRule(rule3);
 
         DefaultReasoner loader = new DefaultReasoner(myWorld, myRules);
         HashSet<String> extensions = loader.getPossibleScenarios();
 
         a.e.println("Étant donné le monde : \n\t" + myWorld.toString()
                 + "\n Et les règles \n\t" + myRules.toString());
 
         a.e.println("Extensions possibles");
         for (String c : extensions) {
             a.e.println("\t Ext: Th(W U (" + c + "))");
             a.e.incIndent();
             WFF world_and_ext = new WFF("(( " + myWorld.getWorld() + " ) & ("
                     + c + "))");
             a.e.println("= " + world_and_ext.getClosure());
             a.e.decIndent();
 
         }
 
     }
 
     public static void main(String[] args) {
         a.e.HIDE_EMPTY_EFFECTS_IN_PRINT = true;
 
         example();
         example2();
     }
 
 }


