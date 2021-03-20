package lichessbot.engine.impl.game;

public class Position {

  private boolean[] _pawnBitboard;
  private boolean[] _castelBitboard;
  private boolean[] _whiteBitboard;
  private boolean[] _kingBitboard;
  private boolean[] _queenBitboard;
  private boolean[] _bishopBitboard;
  private boolean[] _knightBitboard;
  private boolean[] _metaDataBitboard;

  public void setPawnBitBoard(boolean[] pawnBitboard) {
    _pawnBitboard = pawnBitboard;
  }

  public void setCastelBitBoard(boolean[] castelBitboard) {
    _castelBitboard = castelBitboard;
  }

  public void setKnightBitBoard(boolean[] knightBitboard) {
    _knightBitboard = knightBitboard;
  }

  public void setBishopBitBoard(boolean[] bishopBitboard) {
    _bishopBitboard = bishopBitboard;
  }

  public void setQueenBitBoard(boolean[] queenBitboard) {
    _queenBitboard = queenBitboard;
  }

  public void setKingBitBoard(boolean[] kingBitboard) {
    _kingBitboard = kingBitboard;
  }

  public void setWhiteBitBoard(boolean[] whiteBitboard) {
    _whiteBitboard = whiteBitboard;
  }

  public void setMetaDataBitboard(boolean[] metaDataBitboard) {
    _metaDataBitboard = metaDataBitboard;
  }

  public boolean[] getPawnBitboard() {
    return _pawnBitboard;
  }

  public boolean[] getCastelBitboard() {
    return _castelBitboard;
  }

  public boolean[] getWhiteBitboard() {
    return _whiteBitboard;
  }

  public boolean[] getKingBitboard() {
    return _kingBitboard;
  }

  public boolean[] getQueenBitboard() {
    return _queenBitboard;
  }

  public boolean[] getBishopBitboard() {
    return _bishopBitboard;
  }

  public boolean[] getKnightBitboard() {
    return _knightBitboard;
  }

  public boolean[] getMetaDataBitboard() {
    return _metaDataBitboard;
  }
}
