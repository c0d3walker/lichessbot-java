package lichessbot.connector.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.net.ssl.HttpsURLConnection;

public class CommonEventHandler implements Runnable {

  private String _bearerToken;

  public CommonEventHandler(String bearerToken) {
    _bearerToken = bearerToken;
  }

  @Override
  public void run() {
    try {
      URL url = new URL("https://lichess.org/api/stream/event");
      HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
      connection.setRequestProperty("Authorization", "Bearer " + _bearerToken);
      connection.setDoOutput(true);
      BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String value =null;
      while ((value = reader.readLine()) != null) {
        if ( !value.isEmpty()) {
          try (JsonReader jsonReader = Json.createReader(new StringReader(value))) {
            JsonObject currentEvent = jsonReader.readObject();
            String eventType = currentEvent.getString("type");
            switch (eventType) {
            case "challenge":
              handleChallenge(currentEvent);
              break;
            case "gameStart":
              handleGameStart(currentEvent);
              break;
            }
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  private void handleGameStart(JsonObject currentEvent) {
    JsonObject typeObject = currentEvent.getJsonObject("game");
    String gameID = typeObject.getString("id");
    Runnable gameEventHandler = new GameEventHandler(_bearerToken, gameID);
    HandlerEnvironment.invokeInThread(gameEventHandler, "Game " + gameID);

  }

  private void handleChallenge(JsonObject currentEvent) {
    JsonObject challengeObject = currentEvent.getJsonObject("challenge");
    String challengeStatus = challengeObject.getString("status");
    if (challengeStatus.equals("created")) {
      JsonObject variantObject = challengeObject.getJsonObject("variant");
      String variant = variantObject.getString("key");
      boolean isAntichess = "antichess".equals(variant);
      boolean isRated = challengeObject.getBoolean("rated");
      String id = challengeObject.getString("id");
      String urlString = " https://lichess.org/api/challenge/" + id;
      StringBuilder reasonBuilder = new StringBuilder();
      if (isAntichess && !isRated) {
        urlString += "/accept";
      } else {
        // deny
        urlString += "/decline";
        if (!isAntichess) {
          reasonBuilder.append("reason=variant");
        }
        if (isRated) {
          if (reasonBuilder.length() > 0) {
            reasonBuilder.append('&');
          }
          reasonBuilder.append("reason=rated");
        }
      }
      int responseCode = -1;
      String reason = reasonBuilder.toString();
      while (responseCode != HttpsURLConnection.HTTP_OK) {
        try {
          URL reactionUrl = new URL(urlString);
          HttpsURLConnection connection = (HttpsURLConnection) reactionUrl.openConnection();
          connection.setRequestProperty("Authorization", "Bearer " + _bearerToken);
          connection.setDoOutput(true);

          connection.setRequestMethod("POST");
          OutputStream os = connection.getOutputStream();
          os.write(reason.getBytes());
          os.flush();
          os.close();

          responseCode = connection.getResponseCode();
          try {
            Thread.sleep(2000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private void checkResponseCode(HttpsURLConnection connection) throws IOException {
    int responseCode = connection.getResponseCode();
    if (responseCode != HttpsURLConnection.HTTP_OK) {

      BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String line = null;
      StringWriter out = new StringWriter(connection.getContentLength() > 0 ? connection.getContentLength() : 2048);
      while ((line = reader.readLine()) != null) {
        out.append(line);
      }
      String response = out.toString();
      System.out.println(response);
    }
  }
}
