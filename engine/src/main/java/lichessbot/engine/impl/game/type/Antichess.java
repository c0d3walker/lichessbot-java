package lichessbot.engine.impl.game.type;

import java.util.ArrayList;
import java.util.List;

import lichessbot.engine.IMoveEvaluator;
import lichessbot.engine.IStatus;
import lichessbot.engine.impl.common.FieldConverter;
import lichessbot.engine.impl.common.Status;
import lichessbot.engine.impl.game.GameLoader;
import lichessbot.engine.impl.game.Position;

public class Antichess implements IMoveEvaluator {

  private int _aheadMoves;

  public Antichess(int aheadMoves) {
    _aheadMoves = aheadMoves;
  }

  @Override
  public IStatus evaluateAndFindBestMove(Position position, List<String> moves) {
    List<String> takeMoves = new ArrayList<>();
    List<String> normalMoves = new ArrayList<>();

    boolean[] gameField = GameLoader.getGameField(position);
    for (String move : moves) {
      String to = move.substring(2);
      int field = FieldConverter.toIndex(to);
      if (gameField[field]) {
        takeMoves.add(move);
      } else {
        normalMoves.add(move);
      }
    }
    String bestMove = findBestMove(takeMoves);
    if (bestMove == null) {
      findBestMove(normalMoves);
    }
    return new Status(true, "best move", bestMove);
  }

  private String findBestMove(List<String> takeMoves) {
    if (takeMoves.isEmpty()) {
      return null;
    }
    int moveIndex = (int) (Math.random() * (takeMoves.size() - 1));
    return takeMoves.get(moveIndex);
  }

}
