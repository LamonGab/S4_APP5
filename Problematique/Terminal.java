package app5;

/** Cette classe identifie les terminaux reconnus et retournes par
 *  l'analyseur lexical
 */
public class Terminal {

  // Constantes et attributs
  //  ....
  private String chaine;
  private Type type;

  enum Type {
    VARIABLE,
    NOMBRE,
    OPERATEUR1, //Opérateur avec grande priorité (multiplication et division)
    OPERATEUR2, //Opérateur avec petite priorité (addition et soustraction)
    PARENTHESEO,
    PARENTHESEF,
    INVALIDE
  }

  /** Un ou deux constructeurs (ou plus, si vous voulez)
   *   pour l'initialisation d'attributs
   */
  public Terminal(String chaine, Type type) {   // arguments possibles
    this.chaine = chaine;
    this.type = type;
  }

  public String getChaine(){
     return chaine;
  }

  public Type getType() {
    return type;
  }
}
