package lichessbot.engine.impl.common;

import static lichessbot.engine.impl.common.BitBoardFactory.GeneralUsage.createEmptyBitboard;

/**
 * This class creates the bitboards used for representing the game board. The
 * structure is following:
 * 
 * @formatter:off
 * 
 * 56 > a8 b8 c8 d8 e8 f8 g8 h8 < 63
 * 48 > a7 b7 c7 d7 e7 f7 g7 h7 < 55
 * 40 > a6 b6 c6 d6 e6 f6 g6 h6 < 47
 * 32 > a5 b5 c5 d5 e5 f5 g5 h5 < 39
 * 24 > a4 b4 c4 d4 e4 f4 g4 h4 < 31
 * 16 > a3 b3 c3 d3 e3 f3 g3 h3 < 23
 * 8  > a2 b2 c2 d2 e2 f2 g2 h2 < 15
 * 0  > a1 b1 c1 d1 e1 f1 g1 h1 <  7
 * 
 * @formatter:on
 * 
 * The index 0 is a1.
 * 
 * @author lu
 *
 */
public class BitBoardFactory {

  private BitBoardFactory() {
    // do nothing
  }

  public static class GeneralUsage {
    public static boolean[] createEmptyBitboard() {
      return new boolean[64];
    }

    public static boolean[] copyBitboard(boolean[] bitboard) {
      boolean[] copy = new boolean[bitboard.length];
      for (int fieldIndex = 0; fieldIndex < bitboard.length; fieldIndex++) {
        copy[fieldIndex] = bitboard[fieldIndex];
      }
      return copy;
    }

    public static boolean[] merge(boolean[]... bitboards) {
      boolean[] merged = copyBitboard(bitboards[0]);
      for (int fieldIndex = 0; fieldIndex < merged.length; fieldIndex++) {
        for (int boardIndex = 1; !merged[fieldIndex]&&boardIndex<bitboards.length; boardIndex++) {
          merged[fieldIndex] = bitboards[boardIndex][fieldIndex];
        }
      }
      return merged;
    }

    public static void printBitboard(boolean[] bitboard) {
      for (int row = 0; row < 8; row++) {
        int startField = 56 - row * 8;
        for (int field = startField; field < startField + 8; field++) {
          System.out.print(bitboard[field] ? 1 : 0);
        }
        System.out.print('\n');
      }
    }

  }

  public static class DefaultFigurePosition {

    public static boolean[] createWhiteBitboard() {
      boolean[] bitboard = createEmptyBitboard();
      for (int fieldIndex = 0; fieldIndex < 16; fieldIndex++) {
        bitboard[fieldIndex] = true;
      }
      return bitboard;
    }

    public static boolean[] createPawnBitboard() {
      boolean[] bitboard = createEmptyBitboard();
      for (int fieldIndex = 8; fieldIndex <= 15; fieldIndex++) {
        bitboard[fieldIndex] = true;
      }
      for (int fieldIndex = 48; fieldIndex <= 55; fieldIndex++) {
        bitboard[fieldIndex] = true;
      }
      return bitboard;
    }

    public static boolean[] createCastelBitboard() {
      boolean[] bitboard = createEmptyBitboard();
      bitboard[0] = true;
      bitboard[7] = true;
      bitboard[56] = true;
      bitboard[63] = true;
      return bitboard;
    }

    public static boolean[] createKnightBitboard() {
      boolean[] bitboard = createEmptyBitboard();
      bitboard[1] = true;
      bitboard[6] = true;
      bitboard[57] = true;
      bitboard[62] = true;
      return bitboard;
    }

    public static boolean[] createBishopBitboard() {
      boolean[] bitboard = createEmptyBitboard();
      bitboard[2] = true;
      bitboard[5] = true;
      bitboard[58] = true;
      bitboard[61] = true;
      return bitboard;
    }

    public static boolean[] createQueenBitboard() {
      boolean[] bitboard = createEmptyBitboard();
      bitboard[3] = true;
      bitboard[59] = true;
      return bitboard;
    }

    public static boolean[] createKingBitboard() {
      boolean[] bitboard = createEmptyBitboard();
      bitboard[4] = true;
      bitboard[60] = true;
      return bitboard;
    }
  }

  public static class FigureMovementBitboard {
    public static boolean[][] createPawnMovementBitboard() {
      boolean[][] movementBitboards = new boolean[64][];

      // normal move behavior
      for (int fieldIndex = 8; fieldIndex <= 55; fieldIndex++) {
        movementBitboards[fieldIndex] = createEmptyBitboard();
        movementBitboards[fieldIndex][fieldIndex+8] = true;
        pawnTakeMoves(movementBitboards, fieldIndex);
      }
      // double jump at beginning
      for (int fieldIndex = 8; fieldIndex <= 15; fieldIndex++) {
        movementBitboards[fieldIndex][fieldIndex + 16] = true;
      }
      return movementBitboards;
    }

    private static void pawnTakeMoves(boolean[][] movementBitboards, int fieldIndex) {
      int takeLeft = fieldIndex + 7;
      int column=getColumn(fieldIndex);
      if (isInColumn(takeLeft, column-1)) {
        movementBitboards[fieldIndex][takeLeft] = true;
      }
      int takeRight = fieldIndex + 9;
      if (isInColumn(takeRight, column+1)) {
        movementBitboards[fieldIndex][takeRight] = true;
      }
    }

    public static boolean[][] createCastelMovementBitboard() {
      boolean[][] movementBitboards = new boolean[64][];

      for (int fieldIndex = 0; fieldIndex <= 63; fieldIndex++) {
        movementBitboards[fieldIndex] = createEmptyBitboard();
        insertStreightLineFields(movementBitboards, fieldIndex);
      }
      return movementBitboards;
    }

    private static void insertStreightLineFields(boolean[][] movementBitboards, int fieldIndex) {
      int row = getRow(fieldIndex);
      for (int rowField = row * 8; rowField < (row + 1) * 8; rowField++) {
        if (rowField != fieldIndex) {
          movementBitboards[fieldIndex][rowField] = true;
        }
      }
      for (int moveColumn = getColumn(fieldIndex); moveColumn <= 63; moveColumn += 8) {
        if (moveColumn != fieldIndex) {
          movementBitboards[fieldIndex][moveColumn] = true;
        }
      }
    }

    private static int getRow(int fieldIndex) {
      return fieldIndex / 8;
    }

    private static int getColumn(int fieldIndex) {
      return fieldIndex % 8;
    }

    public static boolean[][] createKnightMovementBitboard() {
      boolean[][] movementBitboards = new boolean[64][];

      for (int fieldIndex = 0; fieldIndex <= 63; fieldIndex++) {
        movementBitboards[fieldIndex] = createEmptyBitboard();

        int column = getColumn(fieldIndex);
        int row = getRow(fieldIndex);

        int topLeft = fieldIndex + 15;
        if (isValidField(topLeft) && isInColumn(topLeft, column - 1) && isInRow(topLeft, row + 2)) {
          movementBitboards[fieldIndex][topLeft] = true;
        }
        int topRight = fieldIndex + 17;
        if (isValidField(topRight) && isInColumn(topRight, column + 1) && isInRow(topRight, row + 2)) {
          movementBitboards[fieldIndex][topRight] = true;
        }
        int bottomLeft = fieldIndex - 17;
        if (isValidField(bottomLeft) && isInColumn(bottomLeft, column - 1) && isInRow(bottomLeft, row - 2)) {
          movementBitboards[fieldIndex][bottomLeft] = true;
        }
        int bottomRight = fieldIndex - 15;
        if (isValidField(bottomRight) && isInColumn(bottomRight, column + 1) && isInRow(bottomRight, row - 2)) {
          movementBitboards[fieldIndex][bottomRight] = true;
        }
        int leftTop = fieldIndex + 6;
        if (isValidField(leftTop) && isInColumn(leftTop, column - 2) && isInRow(leftTop, row + 1)) {
          movementBitboards[fieldIndex][leftTop] = true;
        }
        int rightTop = fieldIndex + 10;
        if (isValidField(rightTop) && isInColumn(rightTop, column + 2) && isInRow(rightTop, row + 1)) {
          movementBitboards[fieldIndex][rightTop] = true;
        }
        int leftDown = fieldIndex - 10;
        if (isValidField(leftDown) && isInColumn(leftDown, column - 2) && isInRow(leftDown, row - 1)) {
          movementBitboards[fieldIndex][leftDown] = true;
        }
        int rightDown = fieldIndex - 6;
        if (isValidField(rightDown) && isInColumn(rightDown, column + 2) && isInRow(rightDown, row - 1)) {
          movementBitboards[fieldIndex][rightDown] = true;
        }
      }

      return movementBitboards;
    }

    private static boolean isValidField(int field) {
      return field >= 0 && field < 64;
    }

    public static boolean[][] createBishopMovementBitboard() {
      boolean[][] movementBitboards = new boolean[64][];

      for (int fieldIndex = 0; fieldIndex <= 63; fieldIndex++) {
        movementBitboards[fieldIndex] = createEmptyBitboard();
        insertDiagonalFields(movementBitboards, fieldIndex);
      }

      return movementBitboards;
    }

    public static boolean[][] createQueenMovementBitboard() {
      boolean[][] movementBitboards = new boolean[64][];

      for (int fieldIndex = 0; fieldIndex <= 63; fieldIndex++) {
        movementBitboards[fieldIndex] = createEmptyBitboard();
        insertStreightLineFields(movementBitboards, fieldIndex);
        insertDiagonalFields(movementBitboards, fieldIndex);
      }

      return movementBitboards;
    }

    public static boolean[][] createKingMovementBitboard() {
      boolean[][] movementBitboards = new boolean[64][];

      for (int fieldIndex = 0; fieldIndex <= 63; fieldIndex++) {
        movementBitboards[fieldIndex] = createEmptyBitboard();
        int col = getColumn(fieldIndex);
        int row = getRow(fieldIndex);
        int leftDown = fieldIndex - 9;
        if (leftDown > 0 && isInColumn(leftDown, col - 1) && isInRow(leftDown, row - 1)) {
          movementBitboards[fieldIndex][leftDown] = true;
        }
        int middleDown = fieldIndex - 8;
        if (middleDown > 0 && isInColumn(middleDown, col) && isInRow(middleDown, row - 1)) {
          movementBitboards[fieldIndex][middleDown] = true;
        }
        int rightDown = fieldIndex - 7;
        if (rightDown > 0 && isInColumn(rightDown, col + 1) && isInRow(rightDown, row - 1)) {
          movementBitboards[fieldIndex][rightDown] = true;
        }
        int leftMiddle = fieldIndex - 1;
        if (leftMiddle > 0 && isInColumn(leftMiddle, col - 1) && isInRow(leftMiddle, row)) {
          movementBitboards[fieldIndex][leftMiddle] = true;
        }
        int rightMiddle = fieldIndex + 1;
        if (rightMiddle < 64 && isInColumn(rightMiddle, col + 1) && isInRow(rightMiddle, row)) {
          movementBitboards[fieldIndex][rightMiddle] = true;
        }
        int leftTop = fieldIndex + 7;
        if (leftTop < 64 && isInColumn(leftTop, col - 1) && isInRow(leftTop, row + 1)) {
          movementBitboards[fieldIndex][leftTop] = true;
        }
        int middleTop = fieldIndex + 8;
        if (middleTop < 64 && isInColumn(middleTop, col) && isInRow(middleTop, row + 1)) {
          movementBitboards[fieldIndex][middleTop] = true;
        }
        int rightTop = fieldIndex + 9;
        if (rightTop < 64 && isInColumn(rightTop, col + 1) && isInRow(rightTop, row + 1)) {
          movementBitboards[fieldIndex][rightTop] = true;
        }

      }

      return movementBitboards;
    }

    private static void insertDiagonalFields(boolean[][] movementBitboards, int fieldIndex) {
      int row = getRow(fieldIndex);
      int col = getColumn(fieldIndex);

      for (int diagonaleField = 1; fieldIndex + diagonaleField * 9 <= 63; diagonaleField++) {
        int field = fieldIndex + diagonaleField * 9;
        if (isInColumn(field, col + diagonaleField) && isInRow(field, row + diagonaleField)) {
          movementBitboards[fieldIndex][field] = true;
        } else {
          break;
        }
      }
      for (int diagonaleField = 1; fieldIndex + diagonaleField * 7 <= 63; diagonaleField++) {
        int field = fieldIndex + diagonaleField * 7;
        if (isInColumn(field, col - diagonaleField) && isInRow(field, row + diagonaleField)) {
          movementBitboards[fieldIndex][field] = true;
        } else {
          break;
        }
      }
      for (int diagonaleField = 1; fieldIndex - diagonaleField * 9 >= 0; diagonaleField++) {
        int field = fieldIndex - diagonaleField * 9;
        if (isInColumn(field, col - diagonaleField) && isInRow(field, row - diagonaleField)) {
          movementBitboards[fieldIndex][field] = true;
        } else {
          break;
        }
      }
      for (int diagonaleField = 1; fieldIndex - diagonaleField * 7 >= 0; diagonaleField++) {
        int field = fieldIndex - diagonaleField * 7;
        if (isInColumn(field, col + diagonaleField) && isInRow(field, row - diagonaleField)) {
          movementBitboards[fieldIndex][field] = true;
        } else {
          break;
        }
      }
    }

    private static boolean isInColumn(int fieldIndex, int columnIndex) {
      return getColumn(fieldIndex) == columnIndex;
    }

    private static boolean isInRow(int fieldIndex, int rowIndex) {
      return getRow(fieldIndex) == rowIndex;
    }
  }

}
