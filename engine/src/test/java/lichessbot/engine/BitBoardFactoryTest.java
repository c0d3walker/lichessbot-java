package lichessbot.engine;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import lichessbot.engine.impl.common.BitBoardFactory;

public class BitBoardFactoryTest {

  @Test
  public void testMergeBitboard() {
    boolean[] bishopMovementBitboard = BitBoardFactory.FigureMovementBitboard.createBishopMovementBitboard()[35];
    boolean[] castelMovementBitboard = BitBoardFactory.FigureMovementBitboard.createCastelMovementBitboard()[35];
    boolean[] mergedBitboard = BitBoardFactory.GeneralUsage.merge(bishopMovementBitboard, castelMovementBitboard);
    assertThat(mergedBitboard).isNotSameAs(bishopMovementBitboard);
    assertThat(mergedBitboard).isNotSameAs(castelMovementBitboard);

    boolean[] queenMovementBitboard = BitBoardFactory.FigureMovementBitboard.createQueenMovementBitboard()[35];
    assertThat(mergedBitboard).isEqualTo(queenMovementBitboard);
  }

  @Test
  public void testCloneBitboard() {
    boolean[] originalBitboard = BitBoardFactory.GeneralUsage.createEmptyBitboard();
    originalBitboard[4] = true;
    boolean[] copyBitboard = BitBoardFactory.GeneralUsage.copyBitboard(originalBitboard);
    assertThat(copyBitboard).isNotSameAs(originalBitboard);
    for (int field = 0; field < 64; field++) {
      assertThat(copyBitboard[field]).isEqualTo(originalBitboard[field]);
    }
  }

  @Test
  public void testCreatedBitboard() {
    boolean[] emptyBitboard = BitBoardFactory.GeneralUsage.createEmptyBitboard();
    for (boolean field : emptyBitboard) {
      assertThat(field).isEqualTo(false);
    }
  }

  @Test
  public void testKingMovementBitboard() {
    boolean[][] kingMovementBitboard = BitBoardFactory.FigureMovementBitboard.createKingMovementBitboard();
    boolean[] bitboardForC4 = getAndAssertBitboard(kingMovementBitboard, 26);

    List<Integer> expectedFields = Arrays.asList(17, 18, 19, 25, 27, 33, 34, 35);
    assertMoveFields(bitboardForC4, expectedFields);
  }

  @Test
  public void testCastleMovmentBitboard() {
    boolean[][] castleMovementBitboard = BitBoardFactory.FigureMovementBitboard.createCastelMovementBitboard();
    boolean[] c4Bitboard = getAndAssertBitboard(castleMovementBitboard, 26);

    List<Integer> expectedFields = Arrays.asList(24, 25, 27, 28, 29, 30, 31, 2, 10, 18, 34, 42, 50, 58);
    assertMoveFields(c4Bitboard, expectedFields);
  }

  @Test
  public void testKnightMovmentBitboard() {
    boolean[][] knigtMovementBitboard = BitBoardFactory.FigureMovementBitboard.createKnightMovementBitboard();
    boolean[] c4Bitboard = getAndAssertBitboard(knigtMovementBitboard, 26);

    List<Integer> expectedFields = Arrays.asList(9, 11, 16, 20, 32, 36, 41, 43);
    assertMoveFields(c4Bitboard, expectedFields);
  }

  @Test
  public void testBishopMovementBitboard() {
    boolean[][] bishopMovementBitboard = BitBoardFactory.FigureMovementBitboard.createBishopMovementBitboard();
    boolean[] c4Bitboard = getAndAssertBitboard(bishopMovementBitboard, 26);

    List<Integer> expectedFields = Arrays.asList(19, 12, 5, 8, 17, 40, 33, 35, 44, 53, 62);
    assertMoveFields(c4Bitboard, expectedFields);
  }

  @Test
  public void testQueenMovementBitboard() {
    boolean[][] bishopMovementBitboard = BitBoardFactory.FigureMovementBitboard.createQueenMovementBitboard();
    boolean[] c4Bitboard = getAndAssertBitboard(bishopMovementBitboard, 26);

    List<Integer> expectedFields = Arrays.asList(19, 12, 5, 8, 17, 40, 33, 35, 44, 53, 62, 24, 25, 27, 28, 29, 30, 31,
        2, 10, 18, 34, 42, 50, 58);
    assertMoveFields(c4Bitboard, expectedFields);
  }

  @Test
  public void testPawnMovementBitboard() {
    boolean[][] pawnMovementBitboard = BitBoardFactory.FigureMovementBitboard.createPawnMovementBitboard();
    boolean[] c4Bitboard = getAndAssertBitboard(pawnMovementBitboard, 26);

    List<Integer> expectedFields = Arrays.asList(34, 33, 35);
    assertMoveFields(c4Bitboard, expectedFields);
  }

  @Test
  public void testPawnMovementBitboard_initialMove() {
    boolean[][] pawnMovementBitboard = BitBoardFactory.FigureMovementBitboard.createPawnMovementBitboard();
    boolean[] c4Bitboard = getAndAssertBitboard(pawnMovementBitboard, 10);

    List<Integer> expectedFields = Arrays.asList(17, 18, 19, 26);
    assertMoveFields(c4Bitboard, expectedFields);
  }

  private boolean[] getAndAssertBitboard(boolean[][] kingMovementBitboard, int field) {
    assertThat(kingMovementBitboard.length).isEqualTo(64);

    boolean[] bitboardForC4 = kingMovementBitboard[field];
    assertThat(bitboardForC4).hasSize(64);
    return bitboardForC4;
  }

  private void assertMoveFields(boolean[] bitboardForC4, List<Integer> expectedFields) {
    for (int fieldIndex = 0; fieldIndex < 64; fieldIndex++) {
      if (expectedFields.contains(fieldIndex)) {
        assertThat(bitboardForC4[fieldIndex]).isEqualTo(true);
      } else {
        assertThat(bitboardForC4[fieldIndex]).isEqualTo(false);
      }
    }
  }

}
