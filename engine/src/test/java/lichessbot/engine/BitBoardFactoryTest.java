package lichessbot.engine;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import lichessbot.engine.impl.BitBoardFactory;

public class BitBoardFactoryTest {

  @Test
  public void testKingMovementBitboard() {
    boolean[][] kingMovementBitboard = BitBoardFactory.FigureMovementBitboard.createKingMovementBitboard();
    boolean[] bitboardForC4 = getAndAssertBitboard(kingMovementBitboard, 26);

    BitBoardFactory.GeneralUsage.printBitboard(bitboardForC4);

    List<Integer> expectedFields = new ArrayList<>();
    expectedFields.add(17);
    expectedFields.add(18);
    expectedFields.add(19);
    expectedFields.add(25);
    expectedFields.add(27);
    expectedFields.add(33);
    expectedFields.add(34);
    expectedFields.add(35);

    assertMoveFields(bitboardForC4, expectedFields);
  }

  @Test
  public void testCastleMovmentBitboard() {
    boolean[][] castleMovementBitboard = BitBoardFactory.FigureMovementBitboard.createCastelMovementBitboard();
    boolean[] c4Bitboard = getAndAssertBitboard(castleMovementBitboard, 26);

    BitBoardFactory.GeneralUsage.printBitboard(c4Bitboard);
    List<Integer> expectedFields = Arrays.asList(24, 25, 27, 28, 29, 30, 31, 2, 10, 18, 34, 42, 50, 58);

    assertMoveFields(c4Bitboard, expectedFields);
  }

  @Test
  public void testKnightMovmentBitboard() {
    boolean[][] knigtMovementBitboard = BitBoardFactory.FigureMovementBitboard.createKnigtMovementBitboard();
    boolean[] c4Bitboard = getAndAssertBitboard(knigtMovementBitboard, 26);

    BitBoardFactory.GeneralUsage.printBitboard(c4Bitboard);
    List<Integer> expectedFields = Arrays.asList(9, 11, 16, 20, 32, 36, 41, 43);

    assertMoveFields(c4Bitboard, expectedFields);
  }

  @Test
  public void testBishopMovementBitboard() {
    boolean[][] bishopMovementBitboard = BitBoardFactory.FigureMovementBitboard.createBishopMovementBitboard();
    boolean[] c4Bitboard = getAndAssertBitboard(bishopMovementBitboard, 26);

    BitBoardFactory.GeneralUsage.printBitboard(c4Bitboard);
    List<Integer> expectedFields = Arrays.asList(19, 12, 5, 8, 17, 40, 33, 35, 44, 53, 62);

    assertMoveFields(c4Bitboard, expectedFields);
  }

  @Test
  public void testQueenMovementBitboard() {
    boolean[][] bishopMovementBitboard = BitBoardFactory.FigureMovementBitboard.createQueenMovementBitboard();
    boolean[] c4Bitboard = getAndAssertBitboard(bishopMovementBitboard, 26);

    BitBoardFactory.GeneralUsage.printBitboard(c4Bitboard);
    List<Integer> expectedFields = Arrays.asList(19, 12, 5, 8, 17, 40, 33, 35, 44, 53, 62, 24, 25, 27, 28, 29, 30, 31,
        2, 10, 18, 34, 42, 50, 58);

    assertMoveFields(c4Bitboard, expectedFields);
  }

  @Test
  public void testPawnMovementBitboard() {
    boolean[][] pawnMovementBitboard = BitBoardFactory.FigureMovementBitboard.createPawnMovementBitboard();
    boolean[] c4Bitboard = getAndAssertBitboard(pawnMovementBitboard, 26);

    BitBoardFactory.GeneralUsage.printBitboard(c4Bitboard);
    List<Integer> expectedFields = Arrays.asList(34, 33, 35);

    assertMoveFields(c4Bitboard, expectedFields);
  }

  @Test
  public void testPawnMovementBitboard_initialMove() {
    boolean[][] pawnMovementBitboard = BitBoardFactory.FigureMovementBitboard.createPawnMovementBitboard();
    boolean[] c4Bitboard = getAndAssertBitboard(pawnMovementBitboard, 10);

    BitBoardFactory.GeneralUsage.printBitboard(c4Bitboard);
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
