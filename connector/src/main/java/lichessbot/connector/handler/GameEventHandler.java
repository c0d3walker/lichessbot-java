package lichessbot.connector.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.net.ssl.HttpsURLConnection;

import lichessbot.engine.GameFactory;
import lichessbot.engine.IGame;
import lichessbot.util.Communication;

public class GameEventHandler implements Runnable {

  private static final long WAIT_FOR_AHEAD_MOVE_NUMBER = 30;
  private String _bearerToken;
  private String _gameID;

  public GameEventHandler(String bearerToken, String gameID) {
    _bearerToken = bearerToken;
    _gameID = gameID;
  }

  @Override
  public void run() {
    Communication.sendChat(_bearerToken, _gameID,
        "Number of calculate-ahead moves. Taking 1 moves ahead in " + WAIT_FOR_AHEAD_MOVE_NUMBER + " seconds.");
    int aheadMoves = getNumberOfLookAheadMoves();
    Communication.sendChat(_bearerToken, _gameID, "I'll calculate " + aheadMoves + " moves ahead");
    IGame createGame = GameFactory.createAntichessGame(aheadMoves);
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
