package lichessbot.engine;

public interface IGame {

  /**
   * calculates the best move from the current position
   */
  IStatus getMove();

  /**
   * executes the given move
   * 
   * @param move
   */
  IStatus executeMove(String move);

}
