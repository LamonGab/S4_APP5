package app5;

/** @author Ahmed Khoumsi */

/** Classe representant une feuille d'AST
 */
public class FeuilleAST extends ElemAST {

  // Attribut(s)
    private Terminal terminal;

/**Constructeur pour l'initialisation d'attribut(s)
 */
  public FeuilleAST(Terminal terminal) {  // avec arguments
      this.terminal = terminal;
  }


  /** Evaluation de feuille d'AST
   */
  public int EvalAST( ) {
      if (terminal.getType() == Terminal.Type.NOMBRE) {
          return Integer.parseInt(terminal.getChaine());
      } else if (terminal.getType() == Terminal.Type.VARIABLE) {
          throw new Error("Evalutation impossible car variable");
      }
      return 0;
  }


 /** Lecture de chaine de caracteres correspondant a la feuille d'AST
  */
  public String LectAST( ) {
      return terminal.getChaine();
  }

    @Override
    public String LectPostfixAST() {
        return terminal.getChaine();
    }


}
