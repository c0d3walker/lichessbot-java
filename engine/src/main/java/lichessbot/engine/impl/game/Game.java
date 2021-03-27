package lichessbot.engine.impl.game;

import lichessbot.engine.IGame;
import lichessbot.engine.IMoveEvaluator;
import lichessbot.engine.IStatus;

public class Game implements IGame {

  private Position _position;
  IMoveEvaluator _moveEvaluator;

  public Game(Position position, IMoveEvaluator moveEvaluator) {
    _position = position;
    _moveEvaluator = moveEvaluator;
  }

  @Override
  public IStatus getMove() {
    return _moveEvaluator.evaluateAndFindBestMove(_position);
  }

  @Override
  public IStatus executeMove(String move) {
    return MoveExecutor.execute(_position, move);
  }

}
