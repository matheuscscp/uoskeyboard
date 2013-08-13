package org.unbiquitous.uoskeyboard;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    keyboardRequestView(View.INVISIBLE);
    
    UosManager.setMainActivity(this);
  }
  
  protected void onDestroy() {
    UosManager.setMainActivity(null);
  }
  
  public void onToggleClicked(View view) {
    UosManager.toggle();
  }

  public void acceptRequest(View view) {
    
  }

  public void cancelRequest(View view) {
    keyboardRequestView(View.INVISIBLE);
    
    AndroidKeyboardDriver driver = UosManager.getDriver();
    if (driver != null)
      driver.cancelRequest();
  }
  
  public void keyboardRequestView(int visibility) {
    findViewById(R.id.textView1).setVisibility(visibility);
    findViewById(R.id.applicationName).setVisibility(visibility);
    findViewById(R.id.textView5).setVisibility(visibility);
    findViewById(R.id.button1).setVisibility(visibility);
    findViewById(R.id.Button01).setVisibility(visibility);
  }
}
