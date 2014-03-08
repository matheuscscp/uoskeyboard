package org.unbiquitous.uoskeyboard;

import java.util.ListResourceBundle;

import org.unbiquitous.uos.core.ClassLoaderUtils;
import org.unbiquitous.uos.core.UOS;
import org.unbiquitous.uos.network.socket.connectionManager.TCPConnectionManager;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
  
  private String applicationName = null;
  private View[] requestViews = new View[5];
  private UOS uos = null;
  
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    ClassLoaderUtils.builder = new ClassLoaderUtils.DefaultClassLoaderBuilder(){
      public ClassLoader getParentClassLoader() {
        return getClassLoader();
      };
    };
    
    int i = 0;
    requestViews[i++] = findViewById(R.id.application_name);
    requestViews[i++] = findViewById(R.id.reqkey);
    requestViews[i++] = findViewById(R.id.theapp);
    requestViews[i++] = findViewById(R.id.cancelbutton);
    requestViews[i++] = findViewById(R.id.acceptbutton);
    
    for (View v : requestViews)
      v.setVisibility(View.INVISIBLE);
    
    Globals.mainActivity = this;
  }
  
  public void toggleUos(View view) {
    if (((ToggleButton) view).isChecked())
      startUos();
    else
      stopUos();
  }
  
  private void startUos() {
    if (uos != null)
      return;
    uos = new UOS();
    new AsyncTask<Void, Void, Void> () {
      protected Void doInBackground(Void... params) {
        uos.init(new ListResourceBundle() {
          protected Object[][] getContents() {
            return new Object[][] {
              {"ubiquitos.connectionManager", TCPConnectionManager.class.getName()},
              //{"ubiquitos.radar", MulticastRadar.class.getName()},
              {"ubiquitos.eth.tcp.port", "14984"},
              {"ubiquitos.eth.tcp.passivePortRange", "14985-15000"},
              {"ubiquitos.eth.udp.port", "15001"},
              {"ubiquitos.eth.udp.passivePortRange", "15002-15017"},
              {"ubiquitos.driver.deploylist", KeyboardDriver.class.getName()}
            };
          }
        });
        return null;
      }
    }.execute();
  }
  
  private void stopUos() {
    if (uos == null)
      return;
    uos.tearDown();
    uos = null;
  }
  
  public void receiveRequest(String application_name) {
    this.applicationName = application_name;
  }

  public void receiveRequest(View view) {
    if (applicationName != null) {
      ((TextView) findViewById(R.id.application_name)).setText(applicationName);
      for (View v : requestViews)
        v.setVisibility(View.VISIBLE);
      applicationName = null;
    }
  }
  
  public void acceptRequest(View view) {
    Intent intent = new Intent(getApplicationContext(), KeyboardActivity.class);
    startActivity(intent);
  }

  public void cancelRequest(View view) {
    for (View v : requestViews)
      v.setVisibility(View.INVISIBLE);
  }
}
