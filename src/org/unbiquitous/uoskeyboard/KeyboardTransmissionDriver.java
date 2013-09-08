package org.unbiquitous.uoskeyboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.unbiquitous.json.JSONException;
import org.unbiquitous.json.JSONObject;
import org.unbiquitous.uos.core.adaptabitilyEngine.Gateway;
import org.unbiquitous.uos.core.adaptabitilyEngine.ServiceCallException;
import org.unbiquitous.uos.core.applicationManager.CallContext;
import org.unbiquitous.uos.core.driverManager.UosDriver;
import org.unbiquitous.uos.core.messageEngine.dataType.UpDevice;
import org.unbiquitous.uos.core.messageEngine.dataType.UpDriver;
import org.unbiquitous.uos.core.messageEngine.dataType.UpService.ParameterType;
import org.unbiquitous.uos.core.messageEngine.messages.Call;
import org.unbiquitous.uos.core.messageEngine.messages.Response;

public class KeyboardTransmissionDriver implements UosDriver {

  public static final String TRANSMISSION_DRIVER  = "KeyboardTransmissionDriver";
  public static final String RECEPTION_DRIVER     = "KeyboardReceptionDriver";
  
  private Gateway gateway = null;
  private UpDevice receiver_device = null;
  private boolean transmitting = false;
  private String current_device = null;
  
  @Override
  public UpDriver getDriver() {
    UpDriver driver = new UpDriver(TRANSMISSION_DRIVER);

    driver.addService("receiveRequest")
    .addParameter("receiver_device", ParameterType.MANDATORY)
    .addParameter("application_name", ParameterType.MANDATORY);
    
    return driver;
  }

  @Override
  public List<UpDriver> getParent() {
    return new ArrayList<UpDriver>();
  }

  @Override
  public void init(Gateway gateway, String instanceId) {
    this.gateway = gateway;
    KeyboardTransmissionDriverManager.setDriver(this);
    current_device = gateway.getCurrentDevice().getName();
  }

  @Override
  public void destroy() {
    KeyboardTransmissionDriverManager.setDriver(null);
  }

  public void receiveRequest(Call serviceCall,
      Response serviceResponse, CallContext messageContext) {
    if (transmitting || receiver_device != null || !KeyboardTransmissionDriverManager.receiveRequest(serviceCall.getParameterString("application_name")))
      return;
    
    try {
      receiver_device = UpDevice.fromJSON(new JSONObject(serviceCall.getParameterString("receiver_device")));
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }
  
  public void acceptRequest() {
    if (receiver_device == null)
      return;
    
    transmitting = true;
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("transmitter_device", current_device);
    try {
      gateway.callService(receiver_device, "requestAccepted", RECEPTION_DRIVER, null, null, map);
    }
    catch (ServiceCallException e) {
      e.printStackTrace();
    }
  }
  
  public void cancelRequest() {
    receiver_device = null;
    transmitting = false;
  }
  
  public void closeKeyboard() {
    if (receiver_device == null)
      return;
    
    transmitting = false;
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("transmitter_device", current_device);
    try {
      gateway.callService(receiver_device, "keyboardClosed", RECEPTION_DRIVER, null, null, map);
    }
    catch (ServiceCallException e) {
      e.printStackTrace();
    }
  }

  public void broadcastKeyDown(int unicodeChar) {
    if (!transmitting)
      return;

    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("transmitter_device", current_device);
    map.put("unicode_char", Integer.valueOf(unicodeChar));
    try {
      gateway.callService(receiver_device, "keyDown", RECEPTION_DRIVER, null, null, map);
    }
    catch (ServiceCallException e) {
      e.printStackTrace();
    }
  }

  public void broadcastKeyUp(int unicodeChar) {
    if (!transmitting)
      return;

    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("transmitter_device", current_device);
    map.put("unicode_char", Integer.valueOf(unicodeChar));
    try {
      gateway.callService(receiver_device, "keyUp", RECEPTION_DRIVER, null, null, map);
    }
    catch (ServiceCallException e) {
      e.printStackTrace();
    }
  }
}
