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
    boolean returnStatus = false;
    try {
      URL reactionUrl = new URL(chatID);
      HttpsURLConnection connection = (HttpsURLConnection) reactionUrl.openConnection();
      connection.setRequestProperty("Authorization", "Bearer " + bearerToken);
      connection.setRequestMethod("POST");
      connection.setDoOutput(true);
      connection.setDoInput(true);

      Map<String, String> keyValue = new HashMap<>();
      keyValue.put("room", "player");
      keyValue.put("text", message);

      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));

      writer.write(getEncodedString(keyValue));
      writer.flush();
      writer.close();

      returnStatus = connection.getResponseCode() == HttpsURLConnection.HTTP_OK;
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
      builder.append(URLEncoder.encode(entry.getKey(), "UTF-8")).append('=')
          .append(URLEncoder.encode(entry.getValue(), "UTF-8"));
    }
    return builder.toString();
  }

  public static String getCall(String endpoint, String bearerToken) {
    String value = null;
    try {
      URL url = new URL(endpoint);
      HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
      connection.setRequestProperty("Authorization", "Bearer " + bearerToken);
      connection.setDoOutput(true);
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
        value = reader.readLine();
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return value;
  }

}
