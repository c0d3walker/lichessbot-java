package lichessbot.connector.handler;

public class HandlerEnvironment {

  private HandlerEnvironment() {
// do nothing
  }

  public static void invokeInThread(Runnable handler, String name) {
    Thread thread = new Thread(handler);
    thread.setName(name);
    thread.start();
  }

}
