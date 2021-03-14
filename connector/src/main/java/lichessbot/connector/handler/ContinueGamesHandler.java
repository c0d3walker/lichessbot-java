package lichessbot.connector.handler;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import lichessbot.util.Communication;

public class ContinueGamesHandler implements Runnable {

  private String _bearerToken;

  public ContinueGamesHandler(String bearerToken) {
    _bearerToken = bearerToken;
  }

  @Override
  public void run() {
    String gameResult = Communication.getCall("https://lichess.org/api/account/playing", _bearerToken);
    if (gameResult != null && !gameResult.isEmpty()) {
      JsonReader gameReader = Json.createReader(new StringReader(gameResult));
      JsonObject currentGamesObject = gameReader.readObject();
      JsonArray gameArray = currentGamesObject.getJsonArray("nowPlaying");
      for (int gameIndex = 0; gameIndex < gameArray.size(); gameIndex++) {
        JsonObject game = gameArray.getJsonObject(gameIndex);
        String gameID = game.getString("gameId");
        if ("HCIXqIxk".equals(gameID)) {
          continueGame(gameID);
        }
      }
    }
  }

  private void continueGame(String gameID) {
    GameEventHandler gameEventHandler = new GameEventHandler(_bearerToken, gameID);
    HandlerEnvironment.invokeInThread(gameEventHandler, "Game " + gameID);
  }

}
