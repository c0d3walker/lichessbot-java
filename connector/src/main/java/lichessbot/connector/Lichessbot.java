package lichessbot.connector;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import lichessbot.connector.handler.CommonEventHandler;
import lichessbot.connector.handler.ContinueGamesHandler;
import lichessbot.connector.handler.HandlerEnvironment;

public class Lichessbot {
// bot acc: jBgQDcbQ1WlxMGOw
  public Lichessbot() {
    // curl -X POST -d 'room=player&text=Hugo'
    // https://lichess.org/api/board/game/imj7iFUs/chat -H "Authorization: Bearer
    // jBgQDcbQ1WlxMGOw"
    final String bearerToken = "jBgQDcbQ1WlxMGOw";
    startEventHandler(bearerToken);
  }

  private void startEventHandler(String bearerToken) {
    ContinueGamesHandler continueGamesHandler = new ContinueGamesHandler(bearerToken);
    HandlerEnvironment.invokeInThread(continueGamesHandler, "Condinue Game Handler");

    CommonEventHandler commonEventHandler = new CommonEventHandler(bearerToken);
//		HandlerEnvironment.invokeInThread(commonEventHandler, "Common Event Handler");

  }

  private void sendIGM() {
    BufferedReader reader = null;
    try {
      URL url = new URL("https://lichess.org/inbox/pattfalle");
      HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
      String bearerToken = "jBgQDcbQ1WlxMGOw";
      connection.setRequestProperty("Authorization", "Bearer " + bearerToken);
      connection.setDoOutput(true);

      connection.setRequestMethod("POST");
      OutputStream os = connection.getOutputStream();
      os.write(
          "text=Update: Dieser Account kann noch niemanden herausfordern. Ich melde mich heute Abend einfach mal im Discord."
              .getBytes());
      os.flush();
      os.close();

      int responseCode = connection.getResponseCode();
//			if (responseCode == HttpsURLConnection.HTTP_OK) {

      reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String line = null;
      StringWriter out = new StringWriter(connection.getContentLength() > 0 ? connection.getContentLength() : 2048);
      while ((line = reader.readLine()) != null) {
        out.append(line);
      }
      String response = out.toString();
      System.out.println(response);
//			}
    } catch (Exception e) {

    }
  }

  public static void main(String[] args) {
    Lichessbot lichessbot = new Lichessbot();
  }

}
