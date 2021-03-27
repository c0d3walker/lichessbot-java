package lichessbot.connector.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.net.ssl.HttpsURLConnection;

import lichessbot.connector.ILichessbotConstants;
import lichessbot.engine.GameFactory;
import lichessbot.engine.IGame;
import lichessbot.engine.IStatus;
import lichessbot.util.Communication;

public class GameEventHandler implements Runnable {

  private static final long WAIT_FOR_AHEAD_MOVE_NUMBER = 30;
  private String _bearerToken;
  private String _gameID;
  private boolean _isMyTurn;
  private boolean _isWhite;

  public GameEventHandler(String bearerToken, String gameID) {
    _bearerToken = bearerToken;
    _gameID = gameID;
  }

  @Override
  public void run() {
//    Communication.sendChat(_bearerToken, _gameID, "Number of calculate-ahead moves. Taking 1 moves ahead in " + WAIT_FOR_AHEAD_MOVE_NUMBER + " seconds.");
//    int aheadMoves = getNumberOfLookAheadMoves();
    int aheadMoves = 0;
//    Communication.sendChat(_bearerToken, _gameID, "I'll calculate " + aheadMoves + " moves ahead");

    HttpsURLConnection connection = null;
    try {
      URL url = new URL("https://lichess.org/api/bot/game/stream/" + _gameID);
      connection = (HttpsURLConnection) url.openConnection();
      connection.setRequestProperty("Authorization", "Bearer " + _bearerToken);
      connection.setDoOutput(true);
      IGame game = null;

      try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
        String gameStatusString = null;
        while ((gameStatusString = reader.readLine()) != null) {
          if (!gameStatusString.isEmpty()) {
            JsonObject statusObject = createStatusObject(gameStatusString);
            if (game == null) {
              game = setupGame(aheadMoves, statusObject);
            } else {
              applyChange(game, statusObject);
            }
            if (!isFinished(statusObject)) {
              executeMoveIfNeccesary(game);
            }
          }
          checkConnection(connection);
        }
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    } finally {
      connection.disconnect();
    }

  }

  private JsonObject createStatusObject(String gameStatusString) {
    JsonReader statusReader = Json.createReader(new StringReader(gameStatusString));
    return statusReader.readObject();
  }

  private boolean isFinished(JsonObject statusObject) {
    JsonObject stateObject = statusObject.getJsonObject("state");
    if (stateObject != null) {
      return !"started".equals(stateObject.getString("status"));
    }
    return !"started".equals(statusObject.getString("status"));

  }

  private void executeMoveIfNeccesary(IGame game) {
    if (_isMyTurn) {
      IStatus moveStatus = game.getMove();
      String move = moveStatus.getAdditionalInformation();
      boolean isMoveSet = Communication.setMove(_bearerToken, _gameID, move);
      if (!isMoveSet) {
        System.err.println("Error occurred during executing move:" + move + (_isWhite ? " white" : "black"));
      }
    }
  }

  private void checkConnection(HttpsURLConnection connection) throws IOException {
    int responseCode = connection.getResponseCode();
    if (responseCode == 429) {
      System.out.println("Waiting 60s....");
      try {
        Thread.sleep(60000);
      } catch (InterruptedException ex) {
        ex.printStackTrace();
      }
    }
  }

  private void applyChange(IGame game, JsonObject statusObject) {
    String statusType = statusObject.getString("type");
    if (statusType.equals("gameState")) {
      String moves = statusObject.getString("moves");
      String lastMove = moves.substring(moves.length() - 4);
      game.executeMove(lastMove);
      _isMyTurn = !_isMyTurn;
    }
  }

  private IGame setupGame(int aheadMoves, JsonObject statusObject) {
    JsonObject whitePlayer = statusObject.getJsonObject("white");
    String whitePlayerName = whitePlayer.getString("name");
    _isWhite = whitePlayerName.equals(System.getProperty(ILichessbotConstants.BOTNAME));
    IGame game = createGame(statusObject, aheadMoves);
    loadExecutedMoves(game, statusObject);

    return game;

  }

  private void loadExecutedMoves(IGame game, JsonObject statusObject) {
    JsonObject stateObject = statusObject.getJsonObject("state");
    String[] moves = stateObject.getString("moves").split(" ");
    List<String> cleanedMoves = Arrays.stream(moves).filter(m -> !m.isEmpty()).collect(Collectors.toList());
    cleanedMoves.stream().forEach(game::executeMove);
    boolean isWhiteTurn = cleanedMoves.size() % 2 == 0;
    _isMyTurn = Objects.equals(isWhiteTurn, _isWhite);
  }

  private IGame createGame(JsonObject statusObject, int aheadMoves) {
    JsonObject variantObject = statusObject.getJsonObject("variant");
    String variantKey = variantObject.getString("key");
    IGame game = null;
    switch (variantKey) {
    case "antichess":
      game = GameFactory.createAntichessGame(aheadMoves);
      break;
    default:
      game = null;
    }
    return game;
  }

  private int getNumberOfLookAheadMoves() {
//    String result = Communication.getCall("https://lichess.org/api/bot/game/stream/"+_gameID,_bearerToken);
    int number = 1;
    long now = System.currentTimeMillis();
    try {
      URL url = new URL("https://lichess.org/api/bot/game/stream/" + _gameID);
      HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
      connection.setRequestProperty("Authorization", "Bearer " + _bearerToken);
      connection.setDoOutput(true);
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
        String value = null;
        while ((value = reader.readLine()) != null && isStillTimeAvailable(now)) {
          if (value != null && !value.isEmpty()) {
            JsonReader event = Json.createReader(new StringReader(value));
            JsonObject receivedMessage = event.readObject();
            String type = receivedMessage.getString("type");

            if ("chatLine".equals(type)) {
              String room = receivedMessage.getString("room");
              if ("player".equals(room)) {
                String message = receivedMessage.getString("text");
                if (message.matches("\\d")) {
                  number = Integer.parseInt(message);
                  break;
                }
              }
            }
          }
          System.out.println("-->\n" + value + "\n<--");

        }
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return number;

  }

  private boolean isStillTimeAvailable(long start) {
    long now = System.currentTimeMillis();
    return now - start < WAIT_FOR_AHEAD_MOVE_NUMBER * 1000;
  }

}
