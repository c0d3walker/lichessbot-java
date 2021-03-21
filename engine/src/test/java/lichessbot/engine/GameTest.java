package lichessbot.engine;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import lichessbot.engine.impl.game.Game;
import lichessbot.engine.impl.game.GameLoader;
import lichessbot.engine.impl.game.MetaDataBitboard;
import lichessbot.engine.impl.game.Position;
import lichessbot.engine.impl.game.type.Antichess;

public class GameTest {

  @Test
  void testGameExecutingFirstMove() {
    IGame game = GameFactory.createAntichessGame(0);
    IStatus status = game.getMove();
    assertThat(status.isOK()).isEqualTo(true);
  }

  @Test
  void testGameExecutingFirstBlackMove() {
    IGame game = GameFactory.createAntichessGame(0);
    IStatus status = game.executeMove("e7e6");
    assertThat(status.isOK()).isEqualTo(false);
  }

  @Test
  void testMoveCalculation() {
    Position position = GameLoader.createEmptyPosition();
    GameLoader.setPawn(position, 40, false);
    GameLoader.setPawn(position, 31, true);
    MetaDataBitboard.setBlackTurn(position.getMetaDataBitboard());
    Game game = new Game(position, new Antichess(0));
    IStatus moveStatus = game.getMove();
    assertThat(moveStatus.isOK()).isEqualTo(true);
    assertThat(moveStatus.getAdditionalInformation()).isEqualTo("a6a5");
  }

}
