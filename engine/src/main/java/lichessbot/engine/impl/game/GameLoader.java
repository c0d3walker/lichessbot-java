package lichessbot.engine.impl.game;

import lichessbot.engine.impl.common.BitBoardFactory;

public class GameLoader {

  public static Position createDefaultStartPosition() {
    Position position = new Position();
    position.setPawnBitBoard(BitBoardFactory.DefaultFigurePosition.createPawnBitboard());
    position.setCastelBitBoard(BitBoardFactory.DefaultFigurePosition.createCastelBitboard());
    position.setKnightBitBoard(BitBoardFactory.DefaultFigurePosition.createKnightBitboard());
    position.setBishopBitBoard(BitBoardFactory.DefaultFigurePosition.createBishopBitboard());
    position.setQueenBitBoard(BitBoardFactory.DefaultFigurePosition.createQueenBitboard());
    position.setKingBitBoard(BitBoardFactory.DefaultFigurePosition.createKingBitboard());
    position.setWhiteBitBoard(BitBoardFactory.DefaultFigurePosition.createWhiteBitboard());
    position.setMetaDataBitboard(createDefaultMetaData());
    return position;
  }

  public static Position createEmptyPosition() {
    Position position = new Position();
    position.setPawnBitBoard(BitBoardFactory.GeneralUsage.createEmptyBitboard());
    position.setCastelBitBoard(BitBoardFactory.GeneralUsage.createEmptyBitboard());
    position.setKnightBitBoard(BitBoardFactory.GeneralUsage.createEmptyBitboard());
    position.setBishopBitBoard(BitBoardFactory.GeneralUsage.createEmptyBitboard());
    position.setQueenBitBoard(BitBoardFactory.GeneralUsage.createEmptyBitboard());
    position.setKingBitBoard(BitBoardFactory.GeneralUsage.createEmptyBitboard());
    position.setWhiteBitBoard(BitBoardFactory.GeneralUsage.createEmptyBitboard());
    position.setMetaDataBitboard(createDefaultMetaData());
    return position;
  }
  
  public static Position clonePosition(Position other) {
    Position position=new Position();
    position.setPawnBitBoard(BitBoardFactory.GeneralUsage.copyBitboard(other.getPawnBitboard()));
    position.setCastelBitBoard(BitBoardFactory.GeneralUsage.copyBitboard(other.getCastelBitboard()));
    position.setKnightBitBoard(BitBoardFactory.GeneralUsage.copyBitboard(other.getKnightBitboard()));
    position.setBishopBitBoard(BitBoardFactory.GeneralUsage.copyBitboard(other.getBishopBitboard()));
    position.setQueenBitBoard(BitBoardFactory.GeneralUsage.copyBitboard(other.getQueenBitboard()));
    position.setKingBitBoard(BitBoardFactory.GeneralUsage.copyBitboard(other.getKingBitboard()));
    position.setWhiteBitBoard(BitBoardFactory.GeneralUsage.copyBitboard(other.getWhiteBitboard()));
    position.setMetaDataBitboard(BitBoardFactory.GeneralUsage.copyBitboard(other.getMetaDataBitboard()));
    return position;
  }

  private static boolean[] createDefaultMetaData() {
    boolean[] metaDataBitboard = BitBoardFactory.GeneralUsage.createEmptyBitboard();
    MetaDataBitboard.setWhiteTurn(metaDataBitboard);
    return metaDataBitboard;
  }

  public static void setPawn(Position position, int field, boolean isWhite) {
    position.getPawnBitboard()[field] = true;
    markAsWhiteFigure(position, field, isWhite);
  }
  
  public static void setCastel(Position position, int field, boolean isWhite) {
    position.getCastelBitboard()[field] = true;
    markAsWhiteFigure(position, field, isWhite);
  }
  
  public static void setKnight(Position position, int field, boolean isWhite) {
    position.getKnightBitboard()[field] = true;
    markAsWhiteFigure(position, field, isWhite);
  }
  
  public static void setBishop(Position position, int field, boolean isWhite) {
    position.getBishopBitboard()[field] = true;
    markAsWhiteFigure(position, field, isWhite);
  }
  
  public static void setKing(Position position, int field, boolean isWhite) {
    position.getKingBitboard()[field] = true;
    markAsWhiteFigure(position, field, isWhite);
  }
  
  public static void setQueen(Position position, int field, boolean isWhite) {
    position.getQueenBitboard()[field] = true;
    markAsWhiteFigure(position, field, isWhite);
  }

  public static boolean[] getGameField(Position position) {
    return BitBoardFactory.GeneralUsage.merge(//
        position.getPawnBitboard(), //
        position.getCastelBitboard(), //
        position.getKnightBitboard(), //
        position.getBishopBitboard(), //
        position.getKingBitboard(), //
        position.getQueenBitboard());
  }
  
  private static void markAsWhiteFigure(Position position, int field, boolean isWhite) {
    if (isWhite) {
      position.getWhiteBitboard()[field] = true;
    }
  }
}
