package lichessbot.engine.impl;

import static java.lang.Math.min;

public class MoveByteMapFactory {

  public static byte[][] createCastleMap() {
    byte[][] map = createEmptyMap();

    for (byte field = 0; field < 64; field++) {
      map[field] = new byte[17];
      fillCastleFields(map, field);
    }

    return map;
  }

  private static int fillCastleFields(byte[][] map, byte field) {
    byte toLeft = toLeft(field);
    byte moveIndex = toLeft;

    for (byte target = toLeft; target > 0; target--) {
      map[field][moveIndex -target] = (byte) (field - (toLeft-target)-1);
    }
    map[field][moveIndex] = -1;
    moveIndex++;

    int toRight = 8 - toLeft - 2;
    for (int target = 0; target <= toRight; target++) {
      map[field][moveIndex] = (byte) (field + 1 + target);
      moveIndex++;
    }
    map[field][moveIndex] = -1;
    moveIndex++;

    for (byte target = (byte) (field - 8); target >= 0; target -= 8) {
      map[field][moveIndex] = target;
      moveIndex++;
    }
    map[field][moveIndex] = -1;
    moveIndex++;

    for (byte target = (byte) (field + 8); target < 64; target += 8) {
      map[field][moveIndex] = target;
      moveIndex++;
    }
    return moveIndex;
  }

  private static byte toLeft(int field) {
    return (byte) (field % 8);
  }

  private static byte[][] createEmptyMap() {
    return new byte[64][];
  }

  public static byte[][] createBishopMap() {
    byte[][] map = createEmptyMap();

    for (int field = 0; field < 64; field++) {
      int toLeft = toLeft(field);
      int toBottom = field / 8;
      int toRight = 8 - toLeft - 1;
      int toTop = 8 - toBottom - 1;

      int expectedNumberOfFields = calculateRequiredStorageOfDiagonalFields(toLeft, toBottom, toRight, toTop);
      map[field] = new byte[expectedNumberOfFields];
      int moveIndex = 0;

      // left down
      moveIndex = fillBishopFields(map, field, toLeft, toBottom, moveIndex, -9);
      map[field][moveIndex] = -1;
      moveIndex++;

      // right down
      moveIndex = fillBishopFields(map, field, toRight, toBottom, moveIndex, -7);
      map[field][moveIndex] = -1;
      moveIndex++;

      // right top
      moveIndex = fillBishopFields(map, field, toRight, toTop, moveIndex, 9);
      map[field][moveIndex] = -1;
      moveIndex++;

      // left top
      moveIndex = fillBishopFields(map, field, toLeft, toTop, moveIndex, 7);
    }
    return map;
  }

  private static int fillBishopFields(byte[][] map, int field, int component0, int component1, int moveIndex, int delta) {
    byte tempField = (byte) field;
    for (int numberOfFields = min(component0, component1); numberOfFields > 0; numberOfFields--) {
      tempField += delta;
      map[field][moveIndex] = tempField;
      moveIndex++;
    }
    return moveIndex;
  }

  private static int calculateRequiredStorageOfDiagonalFields(int toLeft, int toBottom, int toRight, int toTop) {
    int minHeight = min(toTop, toBottom);
    int minWidth = min(toLeft, toRight);

    int minCommon = min(minHeight, minWidth);
    int expectedNumberOfFields = 10 + 2 * minCommon;
    return expectedNumberOfFields;
  }

  public static byte[][] createQueenMap() {
    byte[][] map = createEmptyMap();

    for (byte field = 0; field < 64; field++) {
      int toLeft = toLeft(field);
      int toBottom = field / 8;
      int toRight = 8 - toLeft - 1;
      int toTop = 8 - toBottom - 1;

      int requiredStorageOfDiagonalFields = calculateRequiredStorageOfDiagonalFields(toLeft, toBottom, toRight, toTop);
      map[field] = new byte[17 + requiredStorageOfDiagonalFields];
      int moveIndex = fillCastleFields(map, field);

      // left down
      moveIndex = fillBishopFields(map, field, toLeft, toBottom, moveIndex, -9);
      map[field][moveIndex] = -1;
      moveIndex++;

      // right down
      moveIndex = fillBishopFields(map, field, toRight, toBottom, moveIndex, -7);
      map[field][moveIndex] = -1;
      moveIndex++;

      // right top
      moveIndex = fillBishopFields(map, field, toRight, toTop, moveIndex, 9);
      map[field][moveIndex] = -1;
      moveIndex++;

      // left top
      moveIndex = fillBishopFields(map, field, toLeft, toTop, moveIndex, 7);

    }
    return map;
  }
}
