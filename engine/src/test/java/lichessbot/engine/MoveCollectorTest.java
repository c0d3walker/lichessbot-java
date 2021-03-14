package lichessbot.engine;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import lichessbot.engine.impl.GameLoader;
import lichessbot.engine.impl.MoveCollector;
import lichessbot.engine.impl.Position;

public class MoveCollectorTest {

  @Test
  void testMoveCollector_pawn_doubleJump() {
    Position position = GameLoader.createEmptyPosition();
    GameLoader.setPawn(position, 10, true);
    List<String> moves = MoveCollector.collectAllPossibleMoves(position, true);
    assertThat(moves).containsExactlyInAnyOrder("c2c3","c2c4");
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
  void testMoveCollector_pawn_singleMove() {
    Position position = GameLoader.createEmptyPosition();
    GameLoader.setPawn(position, 18, true);
    List<String> moves = MoveCollector.collectAllPossibleMoves(position, true);
    assertThat(moves).containsExactly("c3c4");
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
  void testMoveCollector_knight() {
    Position position = GameLoader.createEmptyPosition();
    GameLoader.setKnight(position, 27, true);
    GameLoader.setBishop(position, 42, false);
    GameLoader.setQueen(position, 33, true);
    List<String> moves = MoveCollector.collectAllPossibleMoves(position, true);
    assertThat(moves).containsExactlyInAnyOrder("d4b3","d4c2","d4e2","d4f3","d4f5","d4e6","d4c6");
  }
  
  @Test
  void testMoveCollector_king() {
    Position position = GameLoader.createEmptyPosition();
    GameLoader.setKing(position, 63, true);
    GameLoader.setBishop(position,54, false);
    GameLoader.setPawn(position, 55, true);
    List<String> moves = MoveCollector.collectAllPossibleMoves(position, true);
    assertThat(moves).containsExactlyInAnyOrder("h8g8","h8g7");
    
  }
  
  @Test
  void testMoveCollector_castle() {
    Position position = GameLoader.createEmptyPosition();
    GameLoader.setCastel(position, 10, true);
    GameLoader.setPawn(position, 34, false);
    GameLoader.setCastel(position, 11, false);

    List<String> moves = MoveCollector.collectAllPossibleMoves(position, true);
    assertThat(moves).containsExactlyInAnyOrder("c2b2", "c2a2", "c2c1", "c2c3", "c2,c4", "c2c5");
  }

}
