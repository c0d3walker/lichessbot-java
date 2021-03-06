package lichessbot.engine;

import java.util.List;

import lichessbot.engine.impl.game.Position;

public interface IMoveEvaluator {

  /**
   * evaluates the moves and returns the best one
   * @param position which shall be evaluated
   * @return an status containing the best move
   */
  IStatus evaluateAndFindBestMove(Position position);

}
