package lichessbot.engine.impl.common;

public class FieldConverter {

  private static final String ZERO = "0";

  public static int toIndex(boolean isWhite, String field) {
    if (field.startsWith(ZERO)) {
      return castelingIndex(field, isWhite);
    }
    return moveIndex(field);
  }

  private static int moveIndex(String field) {
    char letter = field.charAt(0);
    char number = field.charAt(1);

    if (letter == '0' && number == '-') {
      return 4;
    }

    return (number - 49) * 8 + letter - 97;
  }

  private static int castelingIndex(String field, boolean isWhite) {
    int fieldIndex = -1;
    switch (field) {
    case "0-":
      fieldIndex = isWhite ? 4 : 60;
      break;
    case "0":
      fieldIndex = isWhite ? 7 : 63;
      break;
    case "0-0":
      fieldIndex = isWhite ? 0 : 56;
      break;
    default:
      System.err.print("Unknown move sequence: " + field);
    }
    return fieldIndex;
  }

  public static String toField(int field) {
    char letter = (char) (field % 8 + 97);
    char number = (char) (field / 8 + 49);
    return new StringBuilder().append(letter).append(number).toString();
  }

}
