package lichessbot.engine.impl.game;

import java.util.Objects;

import lichessbot.engine.IStatus;
import lichessbot.engine.impl.common.FieldConverter;
import lichessbot.engine.impl.common.SpecialMoveUtility;
import lichessbot.engine.impl.common.Status;

public class MoveExecutor {

  public static IStatus execute(Position position, String move) {
    boolean isWhiteTurn = MetaDataBitboard.isWhiteTurn(position.getMetaDataBitboard());
    int fromField = FieldConverter.toIndex(isWhiteTurn, move.substring(0, 2));
    int toField = FieldConverter.toIndex(isWhiteTurn, move.substring(2, move.length()));
    IStatus updateFigureDataStatus = null;
    if (move.length() == 4) {
      updateFigureDataStatus = updateFigureData(position, fromField, toField);
    } else {
      updateFigureDataStatus = handleComplexMove(position, move, fromField, toField);
    }
    if (updateFigureDataStatus.isOK()) {
      MetaDataBitboard.setLastMove(position.getMetaDataBitboard(), move);
    }
    return updateFigureDataStatus;
  }

  private static IStatus handleComplexMove(Position position, String move, int fromField, int toField) {
    boolean[] pawnBitboard = position.getPawnBitboard();
    boolean[] kingsBitboard = position.getKingBitboard();
    if (pawnBitboard[fromField]) {
      return transformPawn(position, pawnBitboard, move, fromField, toField);
    } else if (kingsBitboard[fromField]) {
      return castelKing(position, kingsBitboard, fromField, toField);
    }
    return new Status(false, "not implemented yet", "");
  }

  private static IStatus castelKing(Position position, boolean[] kingsBitboard, int fromField, int toField) {
    boolean[] whiteBitboard = position.getWhiteBitboard();
    boolean isWhite = MetaDataBitboard.isWhiteTurn(position.getMetaDataBitboard());
    int difference = toField - fromField;
    int direction = difference / Math.abs(difference);

    kingsBitboard[fromField] = false;
    whiteBitboard[fromField] = false;
    int kingsTarget = fromField + 2 * direction;
    kingsBitboard[kingsTarget] = true;
    whiteBitboard[kingsTarget] = isWhite;

    boolean[] castelBitboard = position.getCastelBitboard();
    castelBitboard[toField] = false;
    whiteBitboard[toField] = false;
    int castelTarget = fromField + direction;
    castelBitboard[castelTarget] = true;
    whiteBitboard[castelTarget] = isWhite;

    return new Status(true, "Castel executed", "");
  }

  private static IStatus transformPawn(Position position, boolean[] pawnBitboard, String move, int fromField, int toField) {
    char newFigure = move.charAt(4);
    pawnBitboard[fromField] = false;
    boolean[] targetTypeBitboard = null;
    switch (newFigure) {
    case 'q':
      targetTypeBitboard = position.getQueenBitboard();
      break;
    case 'b':
      targetTypeBitboard = position.getQueenBitboard();
      break;
    case 'c':
      targetTypeBitboard = position.getCastelBitboard();
      break;
    case 'k':
      targetTypeBitboard = position.getKnightBitboard();
      break;
    default:
      return new Status(false, "The given figure type is unknown", newFigure + "");
    }
    targetTypeBitboard[toField] = true;
    updatePlayerData(position, fromField, toField);
    return new Status(true, "The pawn was transformed successfully", "");
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
    updatePlayerData(position, fromField, toField);
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
    int lastTargetIndex = FieldConverter.toIndex(false, lastTarget);
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
