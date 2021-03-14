package lichessbot.engine.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class MoveCollector {

  private static final boolean[][] KNIGHT_MOVE_MAP = BitBoardFactory.FigureMovementBitboard.createKnightMovementBitboard();
  private static final boolean[][] KING_MOVE_MAP = BitBoardFactory.FigureMovementBitboard.createKingMovementBitboard();
  private static final boolean[][] PAWN_MOVE_MAP = BitBoardFactory.FigureMovementBitboard.createPawnMovementBitboard();

  public static List<String> collectAllPossibleMoves(Position position, boolean isWhite) {
    boolean[] gameField = getGameField(position);
    List<String> moves = new ArrayList<>();
    boolean[] whiteBitboard = position.getWhiteBitboard();
    moves.addAll(collectKnightMoves(position, isWhite, gameField, whiteBitboard));
    moves.addAll(collectKingMoves(position, isWhite, gameField, whiteBitboard));
    moves.addAll(collectPawnMoves(position, isWhite, gameField, whiteBitboard));
    return moves;
  }

  private static Collection<? extends String> collectPawnMoves(Position position, boolean isWhite, boolean[] gameField, boolean[] whiteBitboard) {
    boolean[] pawnBitboard = position.getPawnBitboard();
    List<Integer> figuresForPlayer = findFiguresForPlayer(isWhite, whiteBitboard, pawnBitboard);
    return findPawnMoves(isWhite, whiteBitboard, gameField, figuresForPlayer);
  }

  private static Collection<? extends String> findPawnMoves(boolean isWhite, boolean[] whiteBitboard, boolean[] gameField, List<Integer> figuresForPlayer) {
    List<String> moves = new ArrayList<>();
    int moveDirection = isWhite ? 1 : -1;
    for (Integer figure : figuresForPlayer) {
      boolean[] moveMap = PAWN_MOVE_MAP[figure];
      int target = figure + moveDirection * 8;
      if (!gameField[target]) {
        moves.add(createMove(figure, target));
      }
      if (figure / 8 == 6 && !isWhite || figure / 8 == 1 && isWhite) {
        target = figure + moveDirection * 16;
        if (!gameField[target]) {
          moves.add(createMove(figure, target));
        }
      }
      target = figure + moveDirection * 7;
      if (canPawnTake(isWhite, whiteBitboard, gameField, moveMap, target)) {
        moves.add(createMove(figure, target));
      }
      target = figure + moveDirection * 9;
      if (canPawnTake(isWhite, whiteBitboard, gameField, moveMap, target)) {
        moves.add(createMove(figure, target));
      }

    }
    return moves;
  }

  private static boolean canPawnTake(boolean isWhite, boolean[] whiteBitboard, boolean[] gameField, boolean[] moveMap, int target) {
    return target >= 0 && target < 64 && moveMap[target] && gameField[target] && !Objects.equals(isWhite, whiteBitboard[target]);
  }

  private static String createMove(int fromField, int toField) {
    String from = FieldConverter.toField(fromField);
    String to = FieldConverter.toField(toField);
    return from + to;
  }

  private static Collection<? extends String> collectKingMoves(Position position, boolean isWhite, boolean[] gameField, boolean[] whiteBitboard) {
    boolean[] kingBitboard = position.getKingBitboard();
    List<Integer> figuresForPlayer = findFiguresForPlayer(isWhite, whiteBitboard, kingBitboard);
    return findJumpMoves(isWhite, whiteBitboard, gameField, KING_MOVE_MAP, figuresForPlayer);
  }

  private static Collection<String> collectKnightMoves(Position position, boolean isWhite, boolean[] gameField, boolean[] whiteBitboard) {
    boolean[] knightBitboard = position.getKnightBitboard();
    List<Integer> figuresForPlayer = findFiguresForPlayer(isWhite, whiteBitboard, knightBitboard);
    return findJumpMoves(isWhite, whiteBitboard, gameField, KNIGHT_MOVE_MAP, figuresForPlayer);
  }

  private static Collection<String> findJumpMoves(boolean isWhite, boolean[] whiteBitboard, boolean[] gameField, boolean[][] jumpMap, List<Integer> figuresForPlayer) {
    List<String> moves = new ArrayList<>(2);

    for (Integer figureField : figuresForPlayer) {
      boolean[] jumpTargets = jumpMap[figureField];
      for (int targetField = 0; targetField < 64; targetField++) {
        if (jumpTargets[targetField] && (!gameField[targetField] || !Objects.equals(whiteBitboard[targetField], isWhite))) {
          moves.add(createMove(figureField, targetField));
        }
      }
    }

    return moves;
  }

  private static List<Integer> findFiguresForPlayer(boolean isWhite, boolean[] whiteBitboard, boolean[] bitboard) {
    List<Integer> startFields = new ArrayList<>();
    for (int field = 0; field < 64; field++) {
      if (bitboard[field]) {
        if (Objects.equals(isWhite, whiteBitboard[field])) {
          startFields.add(field);
        }
      }
    }
    return startFields;
  }

  private static boolean[] getGameField(Position position) {
    return BitBoardFactory.GeneralUsage.merge(//
        position.getPawnBitboard(), //
        position.getCastelBitboard(), //
        position.getKnightBitboard(), //
        position.getBishopBitboard(), //
        position.getKingBitboard(), //
        position.getQueenBitboard());
  }

}