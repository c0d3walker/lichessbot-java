package lichessbot.engine.impl;

import java.util.List;

import lichessbot.engine.IGame;
import lichessbot.engine.IStatus;

public class Game implements IGame {

  private Position _position;
  private int _aheadMoves;

  public Game(Position position, int aheadMoves) {
    _position = position;
    _aheadMoves = aheadMoves;
  }

  @Override
  public IStatus getMove() {
    boolean isWhite = MetaDataBitboard.isWhiteTurn(_position.getMetaDataBitboard());

    List<String> moves = MoveCollector.collectAllPossibleMoves(_position, isWhite);
    return new Status(false, "Not implemented", "");
  }

  @Override
  public IStatus executeMove(String move) {
    int fromField = FieldConverter.toIndex(move.substring(0, 1));
    int toField = FieldConverter.toIndex(move.substring(2, 3));
    IStatus updateFigureData = updateFigureData(fromField, toField);
    if (updateFigureData.isOK()) {
      updatePlayerData(fromField, toField);
    }
    return new Status(false, "Not implemented", "");
  }

  private IStatus updateFigureData(int fromField, int toField) {
    boolean isUpdatePending = !updateBitboard(_position.getPawnBitboard(), fromField, toField)//
        && !updateBitboard(_position.getCastelBitboard(), fromField, toField)//
        && !updateBitboard(_position.getKnightBitboard(), fromField, toField) //
        && !updateBitboard(_position.getBishopBitboard(), fromField, toField) //
        && !updateBitboard(_position.getKingBitboard(), fromField, toField) //
        && !updateBitboard(_position.getQueenBitboard(), fromField, toField);
    if (isUpdatePending) {
      return new Status(false, "Move denied", "");
    }
    return new Status(true, "Move accepted", "");
  }

  private boolean updateBitboard(boolean[] bitboard, int fromField, int toField) {
    if (bitboard[fromField]) {
      bitboard[fromField] = false;
      bitboard[toField] = true;
      return true;
    }
    return false;
  }

  private void updatePlayerData(int fromField, int toField) {
    boolean isWhiteTurn = MetaDataBitboard.isWhiteTurn(_position.getMetaDataBitboard());
    if (isWhiteTurn) {
      boolean[] whiteBitboard = _position.getWhiteBitboard();
      whiteBitboard[fromField] = false;
      whiteBitboard[toField] = true;
      MetaDataBitboard.setBlackTurn(_position.getMetaDataBitboard());
    } else {
      MetaDataBitboard.setWhiteTurn(_position.getMetaDataBitboard());
    }
  }

}
