package lichessbot.engine;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lichessbot.engine.impl.common.SpecialMoveUtility;
import lichessbot.engine.impl.game.GameLoader;
import lichessbot.engine.impl.game.MetaDataBitboard;
import lichessbot.engine.impl.game.Position;

public class SpecialMoveUtilityTest {

  private Position _position;

  @BeforeEach
  void setup() {
    _position = GameLoader.createEmptyPosition();
  }

  @Test
  void testIsAuPassant_invalid() {
    GameLoader.setPawn(_position, 13, true);
    GameLoader.setPawn(_position, 38, false);
    MetaDataBitboard.setLastMove(_position.getMetaDataBitboard(), "g7g5");
    MetaDataBitboard.setWhiteTurn(_position.getMetaDataBitboard());
    boolean isAuPassant = SpecialMoveUtility.isAuPassant(_position, 13, 22);
    assertThat(isAuPassant).isEqualTo(false);
  }
  
  @Test
  void testIsAuPassant_valid() {
    GameLoader.setPawn(_position, 37, true);
    GameLoader.setPawn(_position, 38, false);
    MetaDataBitboard.setLastMove(_position.getMetaDataBitboard(), "g7g5");
    MetaDataBitboard.setWhiteTurn(_position.getMetaDataBitboard());
    boolean isAuPassant = SpecialMoveUtility.isAuPassant(_position, 37, 46);
    assertThat(isAuPassant).isEqualTo(true);
  }

}
