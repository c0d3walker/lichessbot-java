package lichessbot.engine;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import lichessbot.engine.impl.game.GameLoader;
import lichessbot.engine.impl.game.MetaDataBitboard;
import lichessbot.engine.impl.game.MoveCollector;
import lichessbot.engine.impl.game.Position;

public class MoveCollectorTest {

  @Test
  void testMoveCollector_pawn_doubleJump() {
    Position position = GameLoader.createEmptyPosition();
    GameLoader.setPawn(position, 10, true);
    List<String> moves = MoveCollector.collectAllPossibleMoves(position, true);
    assertThat(moves).containsExactlyInAnyOrder("c2c3", "c2c4");
  }

  @Test
  void testMoveCollector_pawn_take() {
    Position position = GameLoader.createEmptyPosition();
    GameLoader.setPawn(position, 11, true);
    GameLoader.setCastel(position, 18, false);
    GameLoader.setBishop(position, 19, false);
    List<String> moves = MoveCollector.collectAllPossibleMoves(position, true);
    assertThat(moves).containsExactlyInAnyOrder("d2c3");
  }

  @Test
  void testMoveCollecor_pawn_auPassant() {
    Position position = GameLoader.createEmptyPosition();
    GameLoader.setPawn(position, 25, true);
    GameLoader.setPawn(position, 26, false);
    MetaDataBitboard.setLastMove(position.getMetaDataBitboard(), "b2b4");
    List<String> moves = MoveCollector.collectAllPossibleMoves(position, false);
    assertThat(moves).containsExactlyInAnyOrder("c4b3","c4c3");
  }
  
  @Test
  void testMoveCollecor_pawn_auPassantNotPossible() {
    Position position = GameLoader.createEmptyPosition();
    GameLoader.setPawn(position, 25, true);
    GameLoader.setPawn(position, 26, false);
    MetaDataBitboard.setLastMove(position.getMetaDataBitboard(), "b4b5");
    List<String> moves = MoveCollector.collectAllPossibleMoves(position, false);
    assertThat(moves).containsExactlyInAnyOrder("c4c3");
  }

  @Test
  void testMoveCollector_pawn_singleMove() {
    Position position = GameLoader.createEmptyPosition();
    GameLoader.setPawn(position, 18, true);
    List<String> moves = MoveCollector.collectAllPossibleMoves(position, true);
    assertThat(moves).containsExactly("c3c4");
  }

  @Test
  void testMoveCollector_blackPawn_doubleJump() {
    Position position = GameLoader.createEmptyPosition();
    GameLoader.setPawn(position, 54, false);
    List<String> moves = MoveCollector.collectAllPossibleMoves(position, false);
    assertThat(moves).containsExactlyInAnyOrder("g7g6", "g7g5");
  }

  @Test
  void testMoveCollector_blackPawn_takeInDifferentLines() {
    Position position = GameLoader.createEmptyPosition();
    GameLoader.setPawn(position, 31, true);
    GameLoader.setPawn(position, 40, false);
    List<String> moves = MoveCollector.collectAllPossibleMoves(position, false);
    assertThat(moves).containsExactly("a6a5");
  }

  @Test
  void testMoveCollector_pawn_blockedDoubleJump() {
    Position position = GameLoader.createEmptyPosition();
    GameLoader.setPawn(position, 11, true);
    GameLoader.setPawn(position, 19, false);
    List<String> moves = MoveCollector.collectAllPossibleMoves(position, true);
    assertThat(moves).isEmpty();
  }

  @Test
  void testMoveCollector__blackPawn_take() {
    Position position = GameLoader.createEmptyPosition();
    GameLoader.setPawn(position, 30, true);
    GameLoader.setPawn(position, 37, false);
    List<String> moves = MoveCollector.collectAllPossibleMoves(position, false);
    assertThat(moves).containsExactlyInAnyOrder("f5g4", "f5f4");
  }

  @Test
  void testMoveCollector_knight() {
    Position position = GameLoader.createEmptyPosition();
    GameLoader.setKnight(position, 27, true);
    GameLoader.setBishop(position, 42, false);
    GameLoader.setQueen(position, 33, false);
    List<String> moves = MoveCollector.collectAllPossibleMoves(position, true);
    assertThat(moves).containsExactlyInAnyOrder("d4b3", "d4c2", "d4e2", "d4f3", "d4f5", "d4e6", "d4c6", "d4b5");
  }

  @Test
  void testMoveCollector_king() {
    Position position = GameLoader.createEmptyPosition();
    GameLoader.setKing(position, 63, true);
    GameLoader.setBishop(position, 54, false);
    GameLoader.setPawn(position, 55, true);
    List<String> moves = MoveCollector.collectAllPossibleMoves(position, true);
    assertThat(moves).containsExactlyInAnyOrder("h8g8", "h8g7");

  }

  @Test
  void testMoveCollector_castle() {
    Position position = GameLoader.createEmptyPosition();
    GameLoader.setCastel(position, 10, true);
    GameLoader.setPawn(position, 34, false);
    GameLoader.setCastel(position, 11, false);
    List<String> moves = MoveCollector.collectAllPossibleMoves(position, true);
    assertThat(moves).containsExactlyInAnyOrder("c2b2", "c2a2", "c2d2", "c2c1", "c2c3", "c2c4", "c2c5");
  }

  @Test
  void testMoveCollector_bishop() {
    Position position = GameLoader.createEmptyPosition();
    GameLoader.setBishop(position, 8, true);
    GameLoader.setPawn(position, 1, false);
    GameLoader.setCastel(position, 26, false);
    List<String> moves = MoveCollector.collectAllPossibleMoves(position, true);
    assertThat(moves).containsExactlyInAnyOrder("a2b1", "a2b3", "a2c4");
  }

  @Test
  void testMoveCollector_queen() {
    Position position = GameLoader.createEmptyPosition();
    GameLoader.setQueen(position, 26, true);
    GameLoader.setPawn(position, 25, false);
    GameLoader.setCastel(position, 28, false);
    List<String> moves = MoveCollector.collectAllPossibleMoves(position, true);
    assertThat(moves).containsExactlyInAnyOrder("c4c5", "c4c6", "c4c7", "c4c8", "c4c3", "c4c2", "c4c1", "c4b4", "c4e4", "c4d4", "c4a2", "c4b3", "c4d5", "c4e6", "c4f7", "c4g8",
        "c4a6", "c4b5", "c4d3", "c4e2", "c4f1");
  }

}
