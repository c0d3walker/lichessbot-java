package lichessbot.engine;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lichessbot.engine.impl.game.GameLoader;
import lichessbot.engine.impl.game.MetaDataBitboard;
import lichessbot.engine.impl.game.MoveExecutor;
import lichessbot.engine.impl.game.Position;

public class MoveExecutorTest {

  private Position _position;

  @BeforeEach
  void setup() {
    _position = GameLoader.createEmptyPosition();
  }

  @Test
  void testExecuteMove_invalidMove_missingFigures() {
    IStatus status = MoveExecutor.execute(_position, "a1a2");
    assertThat(status.isOK()).isEqualTo(false);
  }

  @Test
  void testExecuteMove_invalidMove_wrongTurn() {
    GameLoader.setPawn(_position, 8, true);
    GameLoader.setPawn(_position, 32, false);
    IStatus status = MoveExecutor.execute(_position, "a5a4");
    assertThat(status.isOK()).isEqualTo(false);
  }

  @Test
  void testExecuteMove_validMove_normalMove() {
    GameLoader.setPawn(_position, 8, true);
    String move = "a2a3";
    IStatus status = MoveExecutor.execute(_position, move);
    assertThat(status.isOK()).isEqualTo(true);
    String lastMove = MetaDataBitboard.getLastMove(_position.getMetaDataBitboard());
    assertThat(lastMove).isEqualTo(move);
  }

  @Test
  void testExecuteMove_validMove_normalDobuleJumb() {
    GameLoader.setPawn(_position, 8, true);
    String move = "a2a4";
    IStatus status = MoveExecutor.execute(_position, move);
    assertThat(status.isOK()).isEqualTo(true);
    String lastMove = MetaDataBitboard.getLastMove(_position.getMetaDataBitboard());
    assertThat(lastMove).isEqualTo(move);
  }

  @Test
  void testExecuteMove_validMove_auPassant() {
    GameLoader.setPawn(_position, 24, true);
    GameLoader.setPawn(_position, 25, false);
    MetaDataBitboard.setLastMove(_position.getMetaDataBitboard(), "a2a4");
    MetaDataBitboard.setBlackTurn(_position.getMetaDataBitboard());
    String move = "b4a3";
    IStatus status = MoveExecutor.execute(_position, move);
    assertThat(status.isOK()).isEqualTo(true);
    String lastMove = MetaDataBitboard.getLastMove(_position.getMetaDataBitboard());
    assertThat(lastMove).isEqualTo(move);
  }

  @Test
  void testExecuteMove_invalidMove_auPassant() {
    GameLoader.setPawn(_position, 24, true);
    GameLoader.setPawn(_position, 25, false);
    String originalLastMove = "a3a4";
    MetaDataBitboard.setLastMove(_position.getMetaDataBitboard(), originalLastMove);
    String move = "b4a3";
    IStatus status = MoveExecutor.execute(_position, move);
    assertThat(status.isOK()).isEqualTo(false);
    String lastMove = MetaDataBitboard.getLastMove(_position.getMetaDataBitboard());
    assertThat(lastMove).isEqualTo(originalLastMove);
  }

  @Test
  void testExecuteMove_validMove_pawnTransformation() {
    GameLoader.setPawn(_position, 50, true);
    String move = "c7c8k";
    IStatus status = MoveExecutor.execute(_position, move);
    assertThat(status.isOK()).isEqualTo(true);
    String lastMove = MetaDataBitboard.getLastMove(_position.getMetaDataBitboard());
    assertThat(lastMove).isEqualTo(move.substring(0,4));
    assertThat(_position.getPawnBitboard()[58]).isEqualTo(false);
    assertThat(_position.getPawnBitboard()[50]).isEqualTo(false);
    assertThat(_position.getKnightBitboard()[58]).isEqualTo(true);
  }
}
