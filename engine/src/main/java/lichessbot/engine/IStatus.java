package lichessbot.engine;

public interface IStatus {
  
  /**
   * @return true if it's a ok status
   */
  boolean isOK();

  /**
   * @return the desscription of the status
   */
  String getDescription();
  
  /**
   * @return additional information to the status
   */
  String additionalInformation();

}
