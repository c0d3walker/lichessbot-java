package lichessbot.engine;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import lichessbot.engine.impl.BitBoardFactory;
import lichessbot.engine.impl.MoveByteMapFactory;

public class MoveByteMapFactoryTest {

  @Test
  void testCastleMovementMap_existingVariants() {
    byte[][] castleMovementMap = MoveByteMapFactory.createCastleMap();
    boolean[][] castelMovementBitboard = BitBoardFactory.FigureMovementBitboard.createCastelMovementBitboard();
    for (int variantIndex = 0; variantIndex < castleMovementMap.length; variantIndex++) {
      assertCompareMovmentToProfile(castleMovementMap, castelMovementBitboard, variantIndex);
    }
  }
  
  @Test
  void testBishopMovementMap_existingVariants() {
    byte[][] bishopMovementMap = MoveByteMapFactory.createBishopMap();
    boolean[][] bishopMovementBitboard = BitBoardFactory.FigureMovementBitboard.createBishopMovementBitboard();
    for (int variantIndex = 0; variantIndex < bishopMovementMap.length; variantIndex++) {
      assertCompareMovmentToProfile(bishopMovementMap, bishopMovementBitboard, variantIndex);
    }
  }
  
  @Test
  void testQueenMovementMap_existingVariants() {
    byte[][] queenMovementMap = MoveByteMapFactory.createQueenMap();
    boolean[][] queenMovementBitboard = BitBoardFactory.FigureMovementBitboard.createQueenMovementBitboard();
    for (int variantIndex = 0; variantIndex < queenMovementMap.length; variantIndex++) {
      assertCompareMovmentToProfile(queenMovementMap, queenMovementBitboard, variantIndex);
    }
  }

  private static void assertCompareMovmentToProfile(byte[][] queenMovementMap, boolean[][] queenMovementBitboard, int variantIndex) {
    byte[] variant = queenMovementMap[variantIndex];
    assertThat(variant).contains(-1, -1, -1);
    boolean[] bitboard = BitBoardFactory.GeneralUsage.createEmptyBitboard();
    for (byte field : variant) {
      if (field >= 0) {
        bitboard[field] = true;
      }
    }
    assertThat(bitboard).isEqualTo(queenMovementBitboard[variantIndex]);
  }
}
