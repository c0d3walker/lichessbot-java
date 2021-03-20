package lichessbot.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;

public class Communication {

  public static boolean sendChat(String bearerToken, String gameID, String message) {
    String chatID = "https://lichess.org/api/bot/game/" + gameID + "/chat";
    Map<String, String> keyValue = new HashMap<>();
    keyValue.put("room", "player");
    keyValue.put("text", message);

    return post(chatID, bearerToken, keyValue);
  }
  

  public static boolean setMove(String baererToken, String gameID, String move) {
    Map<String, String> draw = Map.of("offeringDraw", "false");
    String url = "https://lichess.org/api/bot/game/" + gameID + "/move/" + move;
    return post(url, baererToken, draw);
  }

  private static boolean post(String url, String bearerToken, Map<String, String> param) {
    boolean returnStatus = false;
    try {
      URL reactionUrl = new URL(url);
      HttpsURLConnection connection = (HttpsURLConnection) reactionUrl.openConnection();
      connection.setRequestProperty("Authorization", "Bearer " + bearerToken);
      connection.setRequestMethod("POST");
      connection.setDoOutput(true);
      connection.setDoInput(true);

      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));

      writer.write(getEncodedString(param));
      writer.flush();
      writer.close();
      
      int responseCode=connection.getResponseCode();
      while(responseCode==429) {
        try {
          System.err.println("I must wait");
          Thread.sleep(60000);
          responseCode=connection.getResponseCode();
        }catch(InterruptedException e) {
          e.printStackTrace();
        }
      }
      returnStatus =  responseCode== HttpsURLConnection.HTTP_OK;
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return returnStatus;
  }

  private static String getEncodedString(Map<String, String> keyValue) throws UnsupportedEncodingException {
    StringBuilder builder = new StringBuilder();
    Set<Entry<String, String>> entrySet = keyValue.entrySet();
    for (Entry<String, String> entry : entrySet) {
      if (builder.length() > 0) {
        builder.append('&');
      }
      builder.append(URLEncoder.encode(entry.getKey(), "UTF-8")).append('=').append(URLEncoder.encode(entry.getValue(), "UTF-8"));
    }
    return builder.toString();
  }

  public static String getCall(String endpoint, String bearerToken) {
    String value = null;
    HttpsURLConnection connection = null;
    try {
      URL url = new URL(endpoint);
      connection = (HttpsURLConnection) url.openConnection();
      connection.setRequestProperty("Authorization", "Bearer " + bearerToken);
      connection.setDoOutput(true);
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
        value = reader.readLine();
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    } finally {
      connection.disconnect();
    }
    return value;
  }

}
