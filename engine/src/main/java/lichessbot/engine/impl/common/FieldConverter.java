package lichessbot.engine.impl.common;

public class FieldConverter {

  public static int toIndex(String field) {
    char letter = field.charAt(0);
    char number = field.charAt(1);
    
    return (number-49)*8+letter-97;
  }
  
  public static String toField(int field) {
    char letter=(char)(field%8+97);
    char number=(char)(field/8+49);
    return new StringBuilder().append(letter).append(number).toString();
  }

}
