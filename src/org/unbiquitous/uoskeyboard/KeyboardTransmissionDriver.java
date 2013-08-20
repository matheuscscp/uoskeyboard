package org.unbiquitous.uoskeyboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.unbiquitous.uos.core.adaptabitilyEngine.Gateway;
import org.unbiquitous.uos.core.adaptabitilyEngine.ServiceCallException;
import org.unbiquitous.uos.core.applicationManager.UOSMessageContext;
import org.unbiquitous.uos.core.driverManager.DriverData;
import org.unbiquitous.uos.core.driverManager.UosDriver;
import org.unbiquitous.uos.core.messageEngine.dataType.UpDevice;
import org.unbiquitous.uos.core.messageEngine.dataType.UpDriver;
import org.unbiquitous.uos.core.messageEngine.dataType.UpService.ParameterType;
import org.unbiquitous.uos.core.messageEngine.messages.ServiceCall;
import org.unbiquitous.uos.core.messageEngine.messages.ServiceResponse;

public class KeyboardTransmissionDriver implements UosDriver {

  public static final String TRANSMISSION_DRIVER  = "KeyboardTransmissionDriver";
  public static final String RECEPTION_DRIVER     = "KeyboardReceptionDriver";
  
  private Gateway gateway = null;
  private UpDevice receiver_device = null;
  private boolean transmitting = false;
  
  @Override
  public UpDriver getDriver() {
    UpDriver driver = new UpDriver(TRANSMISSION_DRIVER);

    driver.addService("receiveRequest")
      .addParameter("device_name", ParameterType.MANDATORY)
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
  }

  @Override
  public void destroy() {
    KeyboardTransmissionDriverManager.setDriver(null);
  }

  public void receiveRequest(ServiceCall serviceCall,
      ServiceResponse serviceResponse, UOSMessageContext messageContext) {
    if (receiver_device != null || !KeyboardTransmissionDriverManager.receiveRequest(serviceCall.getParameterString("application_name")))
      return;
    
    List<DriverData> drivers = gateway.listDrivers(RECEPTION_DRIVER);
    String device_name = serviceCall.getParameterString("device_name");
    
    for (int i = 0; i < drivers.size(); ++i) {
      UpDevice tmp = drivers.get(i).getDevice();
      if (tmp.getName().equals(device_name)) {
        receiver_device = tmp;
        break;
      }
    }
    transmitting = false;
  }
  
  public void acceptRequest() {
    if (receiver_device == null)
      return;
    
    transmitting = true;
    try {
      gateway.callService(receiver_device, "requestAccepted", RECEPTION_DRIVER, null, null, new HashMap<String, Object>());
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
    try {
      gateway.callService(receiver_device, "keyboardClosed", RECEPTION_DRIVER, null, null, new HashMap<String, Object>());
    }
    catch (ServiceCallException e) {
      e.printStackTrace();
    }
  }

  public void broadcastKeyDown(int unicodeChar) {
    if (!transmitting)
      return;

    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("unicodeChar", Integer.valueOf(unicodeChar));
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
    map.put("unicodeChar", Integer.valueOf(unicodeChar));
    try {
      gateway.callService(receiver_device, "keyUp", RECEPTION_DRIVER, null, null, map);
    }
    catch (ServiceCallException e) {
      e.printStackTrace();
    }
  }
}
