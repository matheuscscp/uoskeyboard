package org.unbiquitous.uoskeyboard.uos;

public class KeyboardTransmissionDriverManager {
  public static void setDriver(KeyboardTransmissionDriver driver) {
    UosManager.setDriver(driver);
  }
  
  public static boolean receiveRequest(String application_name) {
    return UosManager.receiveRequest(application_name);
  }
  
  public static void stopTransmission() {
    UosManager.stopTransmission();
  }
}
