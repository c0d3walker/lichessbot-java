package lichessbot.engine;

import lichessbot.engine.impl.game.Game;
import lichessbot.engine.impl.game.GameLoader;
import lichessbot.engine.impl.game.Position;
import lichessbot.engine.impl.game.type.Antichess;

public class GameFactory {
  private GameFactory() {
    // do nothing
  }

  public static IGame createAntichessGame(int aheadMoves) {
    return createGame(new Antichess(aheadMoves));
  }

  private static IGame createGame(IMoveEvaluator evaluator) {
    Position position = GameLoader.createDefaultStartPosition();
    return new Game(position, evaluator);
  }
}
