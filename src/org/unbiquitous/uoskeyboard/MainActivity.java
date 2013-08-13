package org.unbiquitous.uoskeyboard;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class MainActivity extends Activity {
  
  boolean keyboard_shown = false;
  
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
    
    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    View v = findViewById(R.id.view1);
    if (keyboard_shown) {
      keyboard_shown = false;
      imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
    else {
      keyboard_shown = true;
      imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
    }
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
