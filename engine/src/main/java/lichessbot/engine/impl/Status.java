package lichessbot.engine.impl;

import lichessbot.engine.IStatus;

public class Status implements IStatus {

  private String _additionalInformation;
  private String _description;
  private boolean _isOK;

  public Status(boolean isOK, String description, String additionalInformation) {
    _isOK = isOK;
    _description = description;
    _additionalInformation = additionalInformation;
  }

  @Override
  public boolean isOK() {
    return _isOK;
  }

  @Override
  public String getDescription() {
    return _description;
  }

  @Override
  public String additionalInformation() {
    return _additionalInformation;
  }

}
