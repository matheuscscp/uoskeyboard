package org.unbiquitous.uoskeyboard;

import org.unbiquitous.uos.core.ClassLoaderUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
  
  private String application_name = null;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    UosManager.setMainActivity(this);
    setRequestVisibility(View.INVISIBLE);
  }
  
  protected void onDestroy() {
    UosManager.setMainActivity(null);
  }

  private void setRequestVisibility(int visibility) {
    findViewById(R.id.application_name).setVisibility(visibility);
    findViewById(R.id.reqkey).setVisibility(visibility);
    findViewById(R.id.theapp).setVisibility(visibility);
    findViewById(R.id.cancelbutton).setVisibility(visibility);
    findViewById(R.id.acceptbutton).setVisibility(visibility);
  }
  
  public void toggleUos(View view) {
    if (((ToggleButton) view).isChecked()) {
      ClassLoaderUtils.builder = new ClassLoaderUtils.DefaultClassLoaderBuilder(){
        public ClassLoader getParentClassLoader() {
          return getClassLoader();
        };
      };
      UosManager.startUos();
    }
    else
      UosManager.stopUos();
  }

  public void receiveRequest(String application_name) {
    this.application_name = application_name;
  }

  public void receiveRequest(View view) {
    if (application_name != null) {
      ((TextView) findViewById(R.id.application_name)).setText(application_name);
      setRequestVisibility(View.VISIBLE);
      application_name = null;
    }
  }
  
  public void acceptRequest(View view) {
    UosManager.acceptRequest();
    Intent intent = new Intent(getApplicationContext(), KeyboardActivity.class);
    startActivity(intent);
  }

  public void cancelRequest(View view) {
    UosManager.cancelRequest();
    setRequestVisibility(View.INVISIBLE);
  }
}
