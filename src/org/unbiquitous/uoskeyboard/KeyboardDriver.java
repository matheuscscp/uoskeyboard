package org.unbiquitous.uoskeyboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.unbiquitous.json.JSONException;
import org.unbiquitous.json.JSONObject;
import org.unbiquitous.uos.core.adaptabitilyEngine.Gateway;
import org.unbiquitous.uos.core.applicationManager.CallContext;
import org.unbiquitous.uos.core.driverManager.UosEventDriver;
import org.unbiquitous.uos.core.messageEngine.dataType.UpDevice;
import org.unbiquitous.uos.core.messageEngine.dataType.UpDriver;
import org.unbiquitous.uos.core.messageEngine.dataType.UpService.ParameterType;
import org.unbiquitous.uos.core.messageEngine.messages.Call;
import org.unbiquitous.uos.core.messageEngine.messages.Response;

public final class KeyboardDriver implements UosEventDriver {
  public static final String KEYBOARD_DRIVER = "KeyboardDriver";
  
  private HashMap<String, UpDevice> listeners = new HashMap<String, UpDevice>();
  private Gateway gateway = null;
  
  public UpDriver getDriver() {
    UpDriver driver = new UpDriver(KEYBOARD_DRIVER);
    
    driver.addService("registerListener")
    .addParameter("upDevice", ParameterType.MANDATORY)
    .addParameter("isApplication", ParameterType.MANDATORY)
    .addParameter("instanceID", ParameterType.MANDATORY)
    .addParameter("displayTitle", ParameterType.MANDATORY);
    
    driver.addService("unregisterListener")
    .addParameter("upDevice", ParameterType.MANDATORY)
    .addParameter("isApplication", ParameterType.MANDATORY)
    .addParameter("instanceID", ParameterType.MANDATORY);
    
    return driver;
  }
  
  public List<UpDriver> getParent() {
    return new ArrayList<UpDriver>();
  }
  
  public void init(Gateway gateway, String instanceId) {
    this.gateway = gateway;
  }
  
  public void destroy() {
    // TODO
  }
  
  public void registerListener(Call serviceCall, Response serviceResponse,
      CallContext messageContext) {
    UpDevice upDevice;
    try {
      upDevice = UpDevice.fromJSON(new JSONObject(serviceCall.getParameterString("upDevice")));
    } catch (JSONException e) {
      serviceResponse.setError("Invalid UpDevice");
      return;
    }
    Boolean isApplication = (Boolean)serviceCall.getParameter("isApplication");
    Long instanceID = (Long)serviceCall.getParameter("instanceID");
    listeners.put(upDevice.getName()+isApplication.toString()+instanceID.toString(), upDevice);
  }
  
  public void unregisterListener(Call serviceCall, Response serviceResponse,
      CallContext messageContext) {
    UpDevice upDevice;
    try {
      upDevice = UpDevice.fromJSON(new JSONObject(serviceCall.getParameterString("upDevice")));
    } catch (JSONException e) {
      serviceResponse.setError("Invalid UpDevice");
      return;
    }
    Boolean isApplication = (Boolean)serviceCall.getParameter("isApplication");
    Long instanceID = (Long)serviceCall.getParameter("instanceID");
    listeners.remove(upDevice.getName()+isApplication.toString()+instanceID.toString());
  }
}
