package app5;

/** @author Ahmed Khoumsi */

/** Classe representant une feuille d'AST
 */
public class NoeudAST extends ElemAST {

  // Attributs
  private ElemAST noeudDroit;
  private ElemAST noeudGauche;
  private Terminal terminal;

  /** Constructeur pour l'initialisation d'attributs
   */
  public NoeudAST(Terminal terminal, ElemAST noeudGauche, ElemAST noeudDroit) { // avec arguments
    //
    this.terminal = terminal;
    this.noeudDroit = noeudDroit;
    this.noeudGauche = noeudGauche;
  }
  /** Evaluation de noeud d'AST
   */
  public int EvalAST( ) {
    return switch (terminal.getChaine()) {
      case "+" -> noeudGauche.EvalAST() + noeudDroit.EvalAST();
      case "-" -> noeudGauche.EvalAST() - noeudDroit.EvalAST();
      case "*" -> noeudGauche.EvalAST() * noeudDroit.EvalAST();
      case "/" -> noeudGauche.EvalAST() / noeudDroit.EvalAST();
      default -> 0;
    };
  }


  /** Lecture de noeud d'AST
   */
  public String LectAST( ) {
    return "( " + noeudGauche.LectAST() + " " + terminal.getChaine() + " " + noeudDroit.LectAST() + " )"; // temp
     //
  }

  public String LectPostfixAST() {
    return noeudGauche.LectPostfixAST() + " " + noeudDroit.LectPostfixAST() + " " + terminal.getChaine() + " " ;  }
}


