package org.unbiquitous.uoskeyboard;

import java.util.ArrayList;
import java.util.List;

import org.unbiquitous.uos.core.adaptabitilyEngine.Gateway;
import org.unbiquitous.uos.core.applicationManager.UOSMessageContext;
import org.unbiquitous.uos.core.driverManager.UosEventDriver;
import org.unbiquitous.uos.core.messageEngine.dataType.UpDriver;
import org.unbiquitous.uos.core.messageEngine.dataType.UpService.ParameterType;
import org.unbiquitous.uos.core.messageEngine.messages.ServiceCall;
import org.unbiquitous.uos.core.messageEngine.messages.ServiceResponse;

import android.view.View;

public class AndroidKeyboardDriver implements UosEventDriver {
  
  private String device_name = null;
  
  @Override
  public UpDriver getDriver() {
    UpDriver driver = new UpDriver("AndroidKeyboardDriver");
    
    driver.addService("receiveRequest").addParameter("deviceName", ParameterType.MANDATORY);
    
    return driver;
  }

  @Override
  public List<UpDriver> getParent() {
    return new ArrayList<UpDriver>();
  }

  @Override
  public void init(Gateway gateway, String instanceId) {
    UosManager.setDriver(this);
  }

  @Override
  public void destroy() {}

  @Override
  public void registerListener(ServiceCall serviceCall,
      ServiceResponse serviceResponse, UOSMessageContext messageContext) {
    
  }

  @Override
  public void unregisterListener(ServiceCall serviceCall,
      ServiceResponse serviceResponse, UOSMessageContext messageContext) {
    
  }
  
  public void receiveRequest(ServiceCall serviceCall,
      ServiceResponse serviceResponse, UOSMessageContext messageContext) {
    if (device_name != null)
      return;
    device_name = serviceCall.getParameterString("deviceName");
    UosManager.getMainActivity().keyboardRequestView(View.VISIBLE);
  }
  
  public void cancelRequest() {
    device_name = null;
  }
}
