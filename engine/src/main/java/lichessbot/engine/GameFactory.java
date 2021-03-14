package lichessbot.engine;

import lichessbot.engine.impl.Game;
import lichessbot.engine.impl.GameLoader;
import lichessbot.engine.impl.Position;

public class GameFactory {
  private GameFactory() {
    // do nothing
  }

  public static IGame createGame(int aheadMoves) {
    Position position = GameLoader.createDefaultStartPosition();
    return new Game(position, aheadMoves);
  }
}
