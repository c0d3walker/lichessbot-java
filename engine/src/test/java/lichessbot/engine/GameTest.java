package lichessbot.engine;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class GameTest {

  @Test
  public void testGameExecutingFirstMove() {
    IGame game = GameFactory.createAntichessGame(0);
    IStatus status = game.getMove();
    assertThat(status.isOK()).isEqualTo(true);
  }

  @Test
  public void testGameExecutingFirstBlackMove() {
    IGame game = GameFactory.createAntichessGame(0);
    IStatus status = game.executeMove("e7e6");
    assertThat(status.isOK()).isEqualTo(false);
  }

}
