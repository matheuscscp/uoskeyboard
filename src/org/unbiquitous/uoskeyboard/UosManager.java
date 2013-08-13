package org.unbiquitous.uoskeyboard;

import java.util.ListResourceBundle;

import org.unbiquitous.uos.core.UOS;
import org.unbiquitous.uos.network.socket.connectionManager.EthernetTCPConnectionManager;

public class UosManager {
  private static UOS uos = null;
  
  private static MainActivity main_activity = null;
  private static AndroidKeyboardDriver driver = null;
  
  public static MainActivity getMainActivity() {
    return main_activity;
  }

  public static void setMainActivity(MainActivity main_activity) {
    UosManager.main_activity = main_activity;
  }

  public static AndroidKeyboardDriver getDriver() {
    return driver;
  }

  public static void setDriver(AndroidKeyboardDriver driver) {
    UosManager.driver = driver;
  }

  public static void toggle() {
    if (uos == null)
      start();
    else
      stop();
  }
  
  private static void start() {
    uos = new UOS();
    uos.init(new ListResourceBundle() {
      protected Object[][] getContents() {
        return new Object[][] {
            {"ubiquitos.connectionManager", EthernetTCPConnectionManager.class.getName()},
            {"ubiquitos.eth.tcp.port", "14984"},
            {"ubiquitos.eth.tcp.passivePortRange", "14985-15000"},
            {"ubiquitos.eth.udp.port", "15001"},
            {"ubiquitos.eth.udp.passivePortRange", "15002-15017"},
            {"ubiquitos.driver.deploylist", AndroidKeyboardDriver.class.getName()}
        };
      }
    });
  }
  
  private static void stop() {
    driver = null;
    uos.tearDown();
    uos = null;
  }
}
