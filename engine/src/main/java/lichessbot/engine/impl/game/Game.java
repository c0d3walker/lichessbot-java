package lichessbot.engine.impl.game;

import java.util.List;

import lichessbot.engine.IGame;
import lichessbot.engine.IMoveEvaluator;
import lichessbot.engine.IStatus;
import lichessbot.engine.impl.common.FieldConverter;
import lichessbot.engine.impl.common.Status;

public class Game implements IGame {

  private Position _position;
  IMoveEvaluator _moveEvaluator;

  public Game(Position position, IMoveEvaluator moveEvaluator) {
    _position = position;
    _moveEvaluator = moveEvaluator;
  }

  @Override
  public IStatus getMove() {
    boolean isWhite = MetaDataBitboard.isWhiteTurn(_position.getMetaDataBitboard());
    List<String> moves = MoveCollector.collectAllPossibleMoves(_position, isWhite);
    return _moveEvaluator.evaluateAndFindBestMove(_position, moves);
  }

  @Override
  public IStatus executeMove(String move) {
    int fromField = FieldConverter.toIndex(move.substring(0, 2));
    int toField = FieldConverter.toIndex(move.substring(2, 4));
    IStatus updateFigureDataStatus = updateFigureData(fromField, toField);
    if (updateFigureDataStatus.isOK()) {
      updatePlayerData(fromField, toField);
    }
    return updateFigureDataStatus;
  }

  private IStatus updateFigureData(int fromField, int toField) {
    boolean isUpdatePending = !updateBitboard(_position,_position.getPawnBitboard(), fromField, toField)//
        && !updateBitboard(_position,_position.getCastelBitboard(), fromField, toField)//
        && !updateBitboard(_position,_position.getKnightBitboard(), fromField, toField) //
        && !updateBitboard(_position,_position.getBishopBitboard(), fromField, toField) //
        && !updateBitboard(_position,_position.getKingBitboard(), fromField, toField) //
        && !updateBitboard(_position,_position.getQueenBitboard(), fromField, toField);
    if (isUpdatePending) {
      return new Status(false, "Move denied", "");
    }
    return new Status(true, "Move accepted", "");
  }

  private void emptyField(Position position, int toField) {
    position.getPawnBitboard()[toField] = false;
    position.getCastelBitboard()[toField] = false;
    position.getKnightBitboard()[toField] = false;
    position.getBishopBitboard()[toField] = false;
    position.getQueenBitboard()[toField] = false;
    position.getKingBitboard()[toField] = false;
  }

  private boolean updateBitboard(Position position,boolean[] figureBitboard, int fromField, int toField) {
    if (figureBitboard[fromField]) {
      emptyField(position, toField);
      figureBitboard[fromField] = false;
      figureBitboard[toField] = true;
      return true;
    }
    return false;
  }

  private void updatePlayerData(int fromField, int toField) {
    boolean isWhiteTurn = MetaDataBitboard.isWhiteTurn(_position.getMetaDataBitboard());
    boolean[] whiteBitboard = _position.getWhiteBitboard();
    if (isWhiteTurn) {
      whiteBitboard[fromField] = false;
      whiteBitboard[toField] = true;
      MetaDataBitboard.setBlackTurn(_position.getMetaDataBitboard());
    } else {
      whiteBitboard[fromField] = false;
      whiteBitboard[toField] = false;
      MetaDataBitboard.setWhiteTurn(_position.getMetaDataBitboard());
    }
  }

}
