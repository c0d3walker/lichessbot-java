package lichessbot.engine.impl;

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

}
