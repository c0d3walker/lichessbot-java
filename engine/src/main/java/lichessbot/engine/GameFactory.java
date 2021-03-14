package lichessbot.engine;

import lichessbot.engine.impl.Game;

public class GameFactory {
  private GameFactory() {
    // do nothing
  }

  public static IGame createGame(int aheadMoves) {
    return new Game();
  }
}
