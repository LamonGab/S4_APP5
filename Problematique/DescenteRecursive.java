package app5;

/** @author Ahmed Khoumsi */

/** Cette classe effectue l'analyse syntaxique
 */
public class DescenteRecursive {

  // Attributs

  private Terminal dernierTerminal;
  private AnalLex analLex;
  int lecteurPtr;

/** Constructeur de DescenteRecursive :
      - recoit en argument le nom du fichier contenant l'expression a analyser
      - pour l'initalisation d'attribut(s)
 */
public DescenteRecursive(String in) {
  Reader r = new Reader(in);
  analLex = new AnalLex(r.toString());
  lecteurPtr = 0;
}

/** AnalSynt() effectue l'analyse syntaxique et construit l'AST.
 *    Elle retourne une reference sur la racine de l'AST construit
 */
public ElemAST AnalSynt( ) {
  dernierTerminal = analLex.prochainTerminal();
  lecteurPtr++;
  return E();
}

// Methode pour chaque symbole non-terminal de la grammaire retenue
// ...
// ...
private ElemAST E(){
  ElemAST b1 = null;
  ElemAST b2 = null;

  b1 = T();

  if (dernierTerminal.getType() == Terminal.Type.OPERATEUR2) {
    Terminal operateur = dernierTerminal;
    if (analLex.resteTerminal()){
      dernierTerminal = analLex.prochainTerminal();
      lecteurPtr++;
    }
    b2 = E();
    return new NoeudAST(operateur, b1, b2);
  }
  else if (!analLex.resteTerminal() || dernierTerminal.getType() == Terminal.Type.PARENTHESEF){
    return b1;
  }
  else {
    ErreurSynt("Erreur de syntaxe (Niveau E) " + dernierTerminal.getType().toString());
    return new FeuilleAST(new Terminal("Invalide", Terminal.Type.INVALIDE));
  }
}

private ElemAST T(){

  ElemAST b1 = null;
  ElemAST b2 = null;

  b1 = F();
  if (dernierTerminal.getType() == Terminal.Type.OPERATEUR1) {
    Terminal operateur = dernierTerminal;
    if (analLex.resteTerminal()){
      dernierTerminal = analLex.prochainTerminal();
      lecteurPtr++;

    }
    b2 = T();
    return new NoeudAST(operateur, b1, b2);
  }
  else if (dernierTerminal.getType() == Terminal.Type.OPERATEUR2 || !analLex.resteTerminal() || dernierTerminal.getType() == Terminal.Type.PARENTHESEF) {
    return b1;
  }
  else {
    ErreurSynt("Erreur de syntaxe (Niveau T) " + dernierTerminal.getType().toString());
    return new FeuilleAST(new Terminal("Invalide", Terminal.Type.INVALIDE));
  }
}

private ElemAST F() {
  ElemAST b1;
  switch (dernierTerminal.getType()){
    case NOMBRE, VARIABLE -> {
      b1 = new FeuilleAST(dernierTerminal);
      if (analLex.resteTerminal()){
        dernierTerminal = analLex.prochainTerminal();
        lecteurPtr++;
      }
      return b1;
    }
    case PARENTHESEO -> {
      if (analLex.resteTerminal()){
        dernierTerminal = analLex.prochainTerminal();
        lecteurPtr++;
      }
      b1 = E();
      if (dernierTerminal.getType() != Terminal.Type.PARENTHESEF){
        ErreurSynt("Erreur de syntaxe (Niveau F), parenthese manquante");
      }
      if (analLex.resteTerminal()){
        dernierTerminal = analLex.prochainTerminal();
        lecteurPtr++;
      }
      return b1;
    }
    default -> {
      ErreurSynt("Erreur de syntaxe (Niveau F) " + dernierTerminal.getType().toString());
    }
  }

  ErreurSynt("Erreur de syntaxe (Niveau F) " + dernierTerminal.getType().toString());
  return new FeuilleAST(new Terminal("Invalide", Terminal.Type.INVALIDE));
}


/** ErreurSynt() envoie un message d'erreur syntaxique
 */
public void ErreurSynt(String s)
{
    throw new Error("Erreur de syntaxe a la position : " + lecteurPtr + " " + "Suite : " + s);
}



  //Methode principale a lancer pour tester l'analyseur syntaxique 
  public static void main(String[] args) {
    String toWriteLect = "";
    String toWriteEval = "";

    System.out.println("Debut d'analyse syntaxique");
    if (args.length == 0){
      args = new String [2];
      args[0] = "ExpArith.txt";
      args[1] = "ResultatSyntaxique.txt";
    }
    DescenteRecursive dr = new DescenteRecursive(args[0]);
    try {
      ElemAST RacineAST = dr.AnalSynt();
      toWriteLect += "Lecture de l'AST trouve : " + RacineAST.LectAST() + "\n";
      System.out.println(toWriteLect);
      System.out.println("Postfix : " + RacineAST.LectPostfixAST() + "\n");
      toWriteEval += "Evaluation de l'AST trouve : " + RacineAST.EvalAST() + "\n";
      System.out.println(toWriteEval);
      Writer w = new Writer(args[1],toWriteLect+toWriteEval); // Ecriture de toWrite 
                                                              // dans fichier args[1]
    } catch (Exception e) {
      System.out.println(e);
      e.printStackTrace();
      System.exit(51);
    }
    System.out.println("Analyse syntaxique terminee");
  }

}

