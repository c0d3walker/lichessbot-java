package lichessbot.engine;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lichessbot.engine.impl.common.BitBoardFactory;
import lichessbot.engine.impl.game.MetaDataBitboard;

public class MetaDataBitboardTest {

  private boolean[] _metaDataBitboard;

  @BeforeEach
  void setup() {
    _metaDataBitboard = BitBoardFactory.GeneralUsage.createEmptyBitboard();
  }

  @Test
  void testSetBlackTurn() {
    MetaDataBitboard.setBlackTurn(_metaDataBitboard);
    assertThat(MetaDataBitboard.isWhiteTurn(_metaDataBitboard)).isEqualTo(false);
  }

  @Test
  void testSetWhiteTurn() {
    MetaDataBitboard.setWhiteTurn(_metaDataBitboard);
    assertThat(MetaDataBitboard.isWhiteTurn(_metaDataBitboard)).isEqualTo(true);
  }

  @Test
  void testSetLastMove() {
    MetaDataBitboard.setLastMove(_metaDataBitboard, "e4e5");
    List<Integer> expectedSetIndices = Arrays.asList(2, 5, 8, 11, 13);
    List<Integer> setPosition = new ArrayList<>();
    for (int setConfigurationIndex = 0; setConfigurationIndex < _metaDataBitboard.length; setConfigurationIndex++) {
      if (_metaDataBitboard[setConfigurationIndex]) {
        setPosition.add(setConfigurationIndex);
      }
    }
    assertThat(setPosition).containsExactlyInAnyOrderElementsOf(expectedSetIndices);
  }

  @Test
  void testGetLastMove() {
    List<Integer> expectedSetIndices = Arrays.asList(2, 5, 8, 11, 13);
    for (Integer configurationIndex : expectedSetIndices) {
      _metaDataBitboard[configurationIndex] = true;
    }
    String lastMove = MetaDataBitboard.getLastMove(_metaDataBitboard);
    assertThat(lastMove).isEqualTo("e4e5");
  }
  
  @Test
  void testLastMoveRoundtrip() {
    String move="a7h1";// isn't a valid move but should work
    MetaDataBitboard.setLastMove(_metaDataBitboard, move);
    String lastMove = MetaDataBitboard.getLastMove(_metaDataBitboard);
    assertThat(lastMove).isEqualTo(move);
  }
}
