package lichessbot.engine.impl.game;

public class MetaDataBitboard {

  public static void setWhiteTurn(boolean[] metaDataBitboard) {
    metaDataBitboard[0] = true;
  }

  public static void setBlackTurn(boolean[] metaDataBitboard) {
    metaDataBitboard[0] = false;
  }

  public static boolean isWhiteTurn(boolean[] metaDataBitboard) {
    return metaDataBitboard[0];
  }

  public static String getLastMove(boolean[] metaDataBitboard) {
    if (metaDataBitboard[1]) {
      return readCastelingMove(metaDataBitboard);
    }
    return readMove(metaDataBitboard);
  }

  private static String readMove(boolean[] metaDataBitboard) {
    StringBuilder move = new StringBuilder(4);
    move.append(readChar(metaDataBitboard, 2, 'a')).append(readChar(metaDataBitboard, 5, '0'));
    move.append(readChar(metaDataBitboard, 8, 'a')).append(readChar(metaDataBitboard, 11, '0'));
    return move.toString();
  }

  private static char readChar(boolean[] metaDataBitboard, int offset, char toCompare) {
    char symbol = toCompare;
    symbol+=1;
    if (metaDataBitboard[offset]) {
      symbol += 0x4;
    }
    if (metaDataBitboard[offset + 1]) {
      symbol += 0x2;
    }
    if (metaDataBitboard[offset + 2]) {
      symbol += 0x1;
    }
    return symbol;
  }

  private static String readCastelingMove(boolean[] metaDataBitboard) {
    // TODO Auto-generated method stub
    return null;
  }

  public static void setLastMove(boolean[] metaDataBitboard, String move) {
    char[] moveChars = move.toCharArray();
    if (moveChars[0] == '0') {
      setCasteling(metaDataBitboard, moveChars);
    } else {
      setMove(metaDataBitboard, moveChars);
    }
  }

  private static void setMove(boolean[] metaDataBitboard, char[] moveChars) {
    metaDataBitboard[1] = false;
    setForSymbol(metaDataBitboard, moveChars[0], 'a', 2);
    setForSymbol(metaDataBitboard, moveChars[1], '0', 5);
    setForSymbol(metaDataBitboard, moveChars[2], 'a', 8);
    setForSymbol(metaDataBitboard, moveChars[3], '0', 11);
  }

  private static void setForSymbol(boolean[] metaDataBitboard, char moveChar, char toCompareChar, int offset) {
    int position = moveChar - toCompareChar-1;
    metaDataBitboard[offset] = (position & 0x4) == 0x4;
    metaDataBitboard[offset + 1] = (position & 0x2) == 0x2;
    metaDataBitboard[offset + 2] = (position & 0x1) == 0x1;
  }

  private static void setCasteling(boolean[] metaDataBitboard, char[] moveChars) {
    metaDataBitboard[1] = true;
    metaDataBitboard[2] = moveChars.length > 3;
  }

}
