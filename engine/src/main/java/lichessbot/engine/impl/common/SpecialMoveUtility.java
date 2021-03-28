package lichessbot.engine.impl.common;

import java.util.regex.Pattern;

import lichessbot.engine.impl.game.MetaDataBitboard;
import lichessbot.engine.impl.game.Position;

public class SpecialMoveUtility {

  private static final Pattern PAWN_MOVE_PATTERN = Pattern.compile("[a-h][1-8][a-h][1-8]+?");

  public static boolean isAuPassant(Position position, int fromField, int toField) {
    boolean[] pawnBitboard = position.getPawnBitboard();
    boolean isWhite = MetaDataBitboard.isWhiteTurn(position.getMetaDataBitboard());
    int auPassantRow = isWhite ? 4 : 3;
    if (pawnBitboard[fromField] && fromField / 8 == auPassantRow) {
      int auPassantColumn = findAuPassantColumn(position, pawnBitboard);
      return toField % 8 == auPassantColumn;
    }
    return false;
  }

  private static int findAuPassantColumn(Position position, boolean[] pawnBitboard) {
    String lastMove = MetaDataBitboard.getLastMove(position.getMetaDataBitboard());
    if (PAWN_MOVE_PATTERN.matcher(lastMove).matches()) {
      int target = FieldConverter.toIndex(false,lastMove.substring(2, 4));
      if (pawnBitboard[target]) {
        int from = FieldConverter.toIndex(false,lastMove.substring(0, 2));
        if (Math.abs(target - from) == 16) {
          return target % 8;
        }
      }
    }

    return -2;
  }

}
