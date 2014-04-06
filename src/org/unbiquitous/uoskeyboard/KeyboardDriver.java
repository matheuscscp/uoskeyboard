package org.unbiquitous.uoskeyboard;

import java.util.ArrayList;
import java.util.List;

import org.unbiquitous.uos.core.InitialProperties;
import org.unbiquitous.uos.core.adaptabitilyEngine.Gateway;
import org.unbiquitous.uos.core.adaptabitilyEngine.NotifyException;
import org.unbiquitous.uos.core.applicationManager.CallContext;
import org.unbiquitous.uos.core.driverManager.UosEventDriver;
import org.unbiquitous.uos.core.messageEngine.dataType.UpDevice;
import org.unbiquitous.uos.core.messageEngine.dataType.UpDriver;
import org.unbiquitous.uos.core.messageEngine.dataType.UpService.ParameterType;
import org.unbiquitous.uos.core.messageEngine.messages.Call;
import org.unbiquitous.uos.core.messageEngine.messages.Notify;
import org.unbiquitous.uos.core.messageEngine.messages.Response;

public class KeyboardDriver implements UosEventDriver {
  public static final String KEYBOARD_DRIVER = "KEYBOARD_DRIVER";
  public static final String REGISTER_DENIED = "REGISTER_DENIED";
  public static final String EVENT_KEY_DOWN = "EVENT_KEY_DOWN";
  public static final String EVENT_KEY_UP = "EVENT_KEY_UP";
  
  private static final int TIME_OUT = 10;
  
  private Gateway gateway = null;
  private UpDevice clientDevice = null;
  private String instanceId = null;
  
  public UpDriver getDriver() {
    UpDriver driver = new UpDriver(KEYBOARD_DRIVER);
    
    driver.addService("registerListener").addParameter("displayTitle", ParameterType.MANDATORY);
    
    driver.addService("unregisterListener");
    
    return driver;
  }
  
  public List<UpDriver> getParent() {
    return new ArrayList<UpDriver>();
  }
  
  public void init(Gateway gateway, InitialProperties properties, String instanceId) {
    this.gateway = gateway;
    this.instanceId = instanceId;
    Globals.keyboardDriver = this;
  }
  
  public void destroy() {
    
  }
  
  public synchronized void registerListener(Call serviceCall, Response serviceResponse,
      CallContext messageContext) {
    if (clientDevice != null)
      return;
    clientDevice = messageContext.getCallerDevice();
    
    Globals.mainActivity.showRequest(serviceCall.getParameterString("displayTitle"));
    
    long timeOut = System.currentTimeMillis() + TIME_OUT*1000;
    while (System.currentTimeMillis() < timeOut && Globals.mainActivity.answer == null) {
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
      }
    }
    
    Globals.mainActivity.hideRequest();
    
    if (Globals.mainActivity.answer == null || Globals.mainActivity.answer.equals(false)) {
      clientDevice = null;
      serviceResponse.setError(REGISTER_DENIED);
    }
    
    Globals.mainActivity.answer = null;
  }
  
  public synchronized void unregisterListener(Call serviceCall, Response serviceResponse,
      CallContext messageContext) {
    if (!messageContext.getCallerDevice().equals(clientDevice))
      return;
    stopEvents();
  }
  
  public void notifyKeyDown(final int unicodeChar) {
    if (clientDevice == null)
      return;
    try {
      gateway.notify(new Notify(EVENT_KEY_DOWN, KEYBOARD_DRIVER, instanceId) {{
        addParameter("unicodeChar", unicodeChar);
      }}, clientDevice);
    } catch (NotifyException e) {
      stopEvents();
    }
  }
  
  public void notifyKeyUp(final int unicodeChar) {
    if (clientDevice == null)
      return;
    try {
      gateway.notify(new Notify(EVENT_KEY_UP, KEYBOARD_DRIVER, instanceId) {{
        addParameter("unicodeChar", unicodeChar);
      }}, clientDevice);
    } catch (NotifyException e) {
      stopEvents();
    }
  }
  
  private void stopEvents() {
    clientDevice = null;
    if (Globals.keyboardActivity != null)
      Globals.keyboardActivity.exit(null);
  }
}
