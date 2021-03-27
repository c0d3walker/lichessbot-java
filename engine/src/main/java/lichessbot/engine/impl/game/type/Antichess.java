package lichessbot.engine.impl.game.type;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import lichessbot.engine.IMoveEvaluator;
import lichessbot.engine.IStatus;
import lichessbot.engine.impl.common.FieldConverter;
import lichessbot.engine.impl.common.SpecialMoveUtility;
import lichessbot.engine.impl.common.Status;
import lichessbot.engine.impl.game.GameLoader;
import lichessbot.engine.impl.game.MetaDataBitboard;
import lichessbot.engine.impl.game.MoveCollector;
import lichessbot.engine.impl.game.Position;

public class Antichess implements IMoveEvaluator {

  private int _aheadMoves;

  public Antichess(int aheadMoves) {
    _aheadMoves = aheadMoves;
  }

  @Override
  public IStatus evaluateAndFindBestMove(Position position) {
    return calculateOwnMove(position, _aheadMoves);
  }

  private IStatus calculateOwnMove(Position position, int aheadMoves) {
//    aheadMoves--;
//    if (aheadMoves < 0) {
//      return null;
//    }

    boolean isWhite = MetaDataBitboard.isWhiteTurn(position.getMetaDataBitboard());
    List<String> moves = MoveCollector.collectAllPossibleMoves(position, isWhite);

    Map<String, Double> evaluationMap = evaluateMoves(position, moves);
    Set<Entry<String, Double>> movesWithEvaluation = evaluationMap.entrySet();
    Entry<String, Double> bestMove = null;
    if (isWhite) {
      bestMove = movesWithEvaluation.stream().max((e0, e1) -> (int) ((e0.getValue() - e1.getValue()) * 100)).get();
    } else {
      bestMove = movesWithEvaluation.stream().min((e0, e1) -> (int) ((e0.getValue() - e1.getValue()) * 100)).get();
    }
    return new Status(true, "move calculated", bestMove.getKey());

  }

  private Map<String, Double> evaluateMoves(Position position, List<String> moves) {
    Map<String, Double> map = new HashMap<>();

    boolean[] gameField = GameLoader.getGameField(position);
    for (String move : moves) {
      String from = move.substring(0,2);
      String to = move.substring(2);
      int toField = FieldConverter.toIndex(to);
      int fromField=FieldConverter.toIndex(from);
      if (isTakeMove(position,gameField, fromField,toField)) {
        map.put(move, 1.);
      } else {
        map.put(move, 0.);
      }
    }
    return map;
  }

  private boolean isTakeMove(Position position, boolean[] gameField, int fromField, int toField) {
    return gameField[toField]||SpecialMoveUtility.isAuPassant(position,fromField,toField);
  }

}
