package lichessbot.engine;

import java.util.List;

public interface IMoveEvaluator {

  /**
   * evaluates the moves and returns the best one
   * @param moves to evaluate
   * @return an status containing the best move
   */
  IStatus evaluateAndFindBestMove(List<String> moves);

}
