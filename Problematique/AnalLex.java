package app5;

/** @author Ahmed Khoumsi */

/** Cette classe effectue l'analyse lexicale
 */
public class AnalLex {

// Attributs
  String s;
  int lecturePtr;
  etatLex etat;
  String prochainTerminal;
//  Enum
  enum etatLex {
    INITIAL,
    OPERATEUR,
    VARIABLE,
    VERIFUNDERSCORE,
    NOMBRE
  }
/** Constructeur pour l'initialisation d'attribut(s)
 */
  public AnalLex(String s) {  // arguments possibles
    this.s = s.replaceAll("\\s+","");
    this.s = this.s.replaceAll("//.*", "") + ' ';
    lecturePtr = 0;
  }


/** resteTerminal() retourne :
      false  si tous les terminaux de l'expression arithmetique ont ete retournes
      true s'il reste encore au moins un terminal qui n'a pas ete retourne 
 */
  public boolean resteTerminal( ) {
    return (lecturePtr < (s.length() - 1));
  }
  
  
/** prochainTerminal() retourne le prochain terminal
      Cette methode est une implementation d'un AEF
 */  
  public Terminal prochainTerminal( ) {
    prochainTerminal = "";
    etat = etatLex.INITIAL;
    Terminal retour = null;
    while (retour == null){
      char c = s.charAt(lecturePtr++);
      prochainTerminal += c;

      switch (etat) {

        case INITIAL : {
          etatLexInitial(c);
          break;
        }

        case OPERATEUR : {
          retour = etatLexOperateur();
          break;
        }

        case VARIABLE : {
          Terminal isNotNull = etatLexVariable(c);
          if (isNotNull != null) retour = isNotNull;
          break;
        }

        case VERIFUNDERSCORE: {
          etatLexVerifUnderscore(c);
          break;
        }

        case NOMBRE: {
          Terminal isNotNull = etatLexNombre(c);
          if (isNotNull != null) retour = isNotNull;
          break;
        }
      }
    }
    return retour;
  }

  private void etatLexInitial(char c) {
    if (Character.isLetter(c)){
      if (Character.isLowerCase(c)){
        ErreurLex("Caractere initial est une minuscule");
      }
      etat = etatLex.VARIABLE;
    }
    else if (Character.isDigit(c)) {
      etat = etatLex.NOMBRE;
    }
    else if (c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')') {
      etat = etatLex.OPERATEUR;
    }
    else if (c == '_') {
      ErreurLex("Premier caractere est un underscore");
    }
    else {
      ErreurLex("Caractere invalide");
    }
  }

  private Terminal etatLexOperateur(){
    switch (s.charAt(--lecturePtr -1)) {
      case '*', '/' -> {
        return new Terminal(prochainTerminal.substring(0, prochainTerminal.length()-1), Terminal.Type.OPERATEUR1);
      }
      case '+', '-' -> {
        return new Terminal(prochainTerminal.substring(0, prochainTerminal.length()-1), Terminal.Type.OPERATEUR2);
      }
      case '(' -> {
        return new Terminal(prochainTerminal.substring(0, prochainTerminal.length()-1), Terminal.Type.PARENTHESEO);
      }
      case ')' -> {
        return new Terminal(prochainTerminal.substring(0, prochainTerminal.length()-1), Terminal.Type.PARENTHESEF);
      }
      default -> {
        ErreurLex("Caractere incompatible");
      }
    }
    return null;
  }

  private Terminal etatLexVariable(char c){
    if (c == '_') {
      etat = etatLex.VERIFUNDERSCORE;
    } else if (!Character.isLetter(c)){
      lecturePtr--;
      return new Terminal(prochainTerminal.substring(0, prochainTerminal.length()-1), Terminal.Type.VARIABLE);
    } else if (!resteTerminal()) {
      return new Terminal(prochainTerminal, Terminal.Type.VARIABLE);
    }
    return null;
  }

  private void etatLexVerifUnderscore(char c) {
    if (Character.isLetter(c)) {
      etat = etatLex.VARIABLE;
    } else if (c == '_') {
      ErreurLex("Caractere invalide double __");
    } else {
      ErreurLex("Caractere invalide impossible de finir avec un _");
    }
  }

  private Terminal etatLexNombre(char c) {
    if (!Character.isDigit(c)) {
      lecturePtr--;
      return new Terminal(prochainTerminal.substring(0, prochainTerminal.length()-1),Terminal.Type.NOMBRE);
    }

    if (resteTerminal()) {
      return new Terminal(prochainTerminal, Terminal.Type.NOMBRE);
    }

    return null;
  }
/** ErreurLex() envoie un message d'erreur lexicale
 */ 
  public void ErreurLex(String s) {
     throw new Error("Erreur lexicale au niveau du caractere " + lecturePtr + " : " + s);
  }

  
  //Methode principale a lancer pour tester l'analyseur lexical
  public static void main(String[] args) {
    String toWrite = "";
    System.out.println("Debut d'analyse lexicale");
    if (args.length == 0){
    args = new String [2];
            args[0] = "ExpArith.txt";
            args[1] = "ResultatLexical.txt";
    }
    Reader r = new Reader(args[0]);

    AnalLex lexical = new AnalLex(r.toString()); // Creation de l'analyseur lexical

    // Execution de l'analyseur lexical
    Terminal t = null;
    while(lexical.resteTerminal()){
      t = lexical.prochainTerminal();
      toWrite += t.getType()+ " : " +  t.getChaine() + "\n" ;  // toWrite contient le resultat
    }				   //    d'analyse lexicale
    System.out.println(toWrite); 	// Ecriture de toWrite sur la console
    Writer w = new Writer(args[1],toWrite); // Ecriture de toWrite dans fichier args[1]
    System.out.println("Fin d'analyse lexicale");
  }
}
