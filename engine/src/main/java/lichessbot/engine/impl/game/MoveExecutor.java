package lichessbot.engine.impl.game;

import java.util.Objects;

import lichessbot.engine.IStatus;
import lichessbot.engine.impl.common.FieldConverter;
import lichessbot.engine.impl.common.SpecialMoveUtility;
import lichessbot.engine.impl.common.Status;

public class MoveExecutor {

  public static IStatus execute(Position position, String move) {
    int fromField = FieldConverter.toIndex(move.substring(0, 2));
    int toField = FieldConverter.toIndex(move.substring(2, 4));
    IStatus updateFigureDataStatus = updateFigureData(position, fromField, toField);
    if (updateFigureDataStatus.isOK()) {
      updatePlayerData(position, fromField, toField);
      MetaDataBitboard.setLastMove(position.getMetaDataBitboard(), move);
    }
    return updateFigureDataStatus;
  }

  private static IStatus updateFigureData(Position position, int fromField, int toField) {
    boolean isUpdatePending = !isSpecialMove(position, fromField, toField) && //
        !updateBitboard(position, position.getPawnBitboard(), fromField, toField)//
        && !updateBitboard(position, position.getCastelBitboard(), fromField, toField)//
        && !updateBitboard(position, position.getKnightBitboard(), fromField, toField) //
        && !updateBitboard(position, position.getBishopBitboard(), fromField, toField) //
        && !updateBitboard(position, position.getKingBitboard(), fromField, toField) //
        && !updateBitboard(position, position.getQueenBitboard(), fromField, toField);
    if (isUpdatePending) {
      return new Status(false, "Move denied", "");
    }
    return new Status(true, "Move accepted", "");
  }

  private static boolean isSpecialMove(Position position, int fromField, int toField) {
    if (SpecialMoveUtility.isAuPassant(position, fromField, toField)) {
      executeAuPassant(position, fromField, toField);
      return true;
    }
    return false;
  }

  private static void executeAuPassant(Position position, int fromField, int toField) {
    boolean[] pawnBitboard = position.getPawnBitboard();
    boolean[] whiteBitboard = position.getWhiteBitboard();

    pawnBitboard[fromField] = false;
    pawnBitboard[toField] = true;

    String lastMove = MetaDataBitboard.getLastMove(position.getMetaDataBitboard());
    String lastTarget = lastMove.substring(2);
    int lastTargetIndex = FieldConverter.toIndex(lastTarget);
    pawnBitboard[lastTargetIndex] = false;
    whiteBitboard[lastTargetIndex] = false;

  }

// TODO can be removed?
  private static void emptyField(Position position, int toField) {
    position.getPawnBitboard()[toField] = false;
    position.getCastelBitboard()[toField] = false;
    position.getKnightBitboard()[toField] = false;
    position.getBishopBitboard()[toField] = false;
    position.getQueenBitboard()[toField] = false;
    position.getKingBitboard()[toField] = false;
  }

  private static boolean updateBitboard(Position position, boolean[] figureBitboard, int fromField, int toField) {
    if (figureBitboard[fromField] && Objects.equals(MetaDataBitboard.isWhiteTurn(position.getMetaDataBitboard()), position.getWhiteBitboard()[fromField])) {
      emptyField(position, toField);
      figureBitboard[fromField] = false;
      figureBitboard[toField] = true;
      return true;
    }
    return false;
  }

  private static void updatePlayerData(Position position, int fromField, int toField) {
    boolean isWhiteTurn = MetaDataBitboard.isWhiteTurn(position.getMetaDataBitboard());
    boolean[] whiteBitboard = position.getWhiteBitboard();
    if (isWhiteTurn) {
      whiteBitboard[fromField] = false;
      whiteBitboard[toField] = true;
      MetaDataBitboard.setBlackTurn(position.getMetaDataBitboard());
    } else {
      whiteBitboard[fromField] = false;
      whiteBitboard[toField] = false;
      MetaDataBitboard.setWhiteTurn(position.getMetaDataBitboard());
    }
  }

}
