package org.unbiquitous.uoskeyboard.uos;

import java.util.ListResourceBundle;

import org.unbiquitous.uos.core.UOS;
import org.unbiquitous.uos.network.socket.connectionManager.EthernetTCPConnectionManager;
import org.unbiquitous.uoskeyboard.KeyboardActivity;
import org.unbiquitous.uoskeyboard.MainActivity;

public class UosManager {
  private static UOS uos = null;

  private static MainActivity main_activity = null;
  private static KeyboardActivity keyboard_activity = null;
  private static KeyboardTransmissionDriver driver = null;

  public static void setMainActivity(MainActivity main_activity) {
    UosManager.main_activity = main_activity;
  }
  
  public static void setKeyboardActivity(KeyboardActivity keyboard_activity) {
    UosManager.keyboard_activity = keyboard_activity;
  }
  
  public static void setDriver(KeyboardTransmissionDriver driver) {
    UosManager.driver = driver;
  }

  public static void startUos() {
    if (uos != null)
      return;
    
    uos = new UOS();
    uos.init(new ListResourceBundle() {
      protected Object[][] getContents() {
        return new Object[][] {
            {"ubiquitos.connectionManager", EthernetTCPConnectionManager.class.getName()},
            {"ubiquitos.eth.tcp.port", "14984"},
            {"ubiquitos.eth.tcp.passivePortRange", "14985-15000"},
            {"ubiquitos.eth.udp.port", "15001"},
            {"ubiquitos.eth.udp.passivePortRange", "15002-15017"},
            {"ubiquitos.driver.deploylist", KeyboardTransmissionDriver.class.getName()}
        };
      }
    });
  }
  
  public static void stopUos() {
    if (uos == null)
      return;
    
    if (driver != null)
      driver.closeKeyboard();
    driver = null;
    uos.tearDown();
    uos = null;
  }
  
  public static boolean receiveRequest(String application_name) {
    if (main_activity == null)
      return false;
    main_activity.receiveRequest(application_name);
    return true;
  }
  
  public static void stopTransmission() {
    if (main_activity != null)
      main_activity.stopTransmission();
    if (keyboard_activity != null)
      keyboard_activity.stopTransmission();
  }
  
  public static void acceptRequest() {
    if (driver != null)
      driver.acceptRequest();
  }
  
  public static void cancelRequest() {
    if (driver != null)
      driver.cancelRequest();
  }
  
  public static void broadcastKeyDown(int unicodeChar) {
    if (driver != null)
      driver.broadcastKeyDown(unicodeChar);
  }

  public static void broadcastKeyUp(int unicodeChar) {
    if (driver != null)
      driver.broadcastKeyUp(unicodeChar);
  }
}