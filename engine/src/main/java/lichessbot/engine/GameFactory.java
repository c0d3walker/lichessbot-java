package lichessbot.engine;

import lichessbot.engine.impl.game.Game;
import lichessbot.engine.impl.game.GameLoader;
import lichessbot.engine.impl.game.Position;

public class GameFactory {
  private GameFactory() {
    // do nothing
  }

  public static IGame createGame(int aheadMoves) {
    Position position = GameLoader.createDefaultStartPosition();
    return new Game(position, null);
  }
}
