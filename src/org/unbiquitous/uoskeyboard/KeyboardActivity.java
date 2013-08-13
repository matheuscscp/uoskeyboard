package org.unbiquitous.uoskeyboard;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyboardActivity extends Activity {
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_keyboard);
    
    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.showSoftInput(findViewById(R.id.keyboard_view), InputMethodManager.SHOW_IMPLICIT);
  }
  
  public void goBack(View view) {
    
  }
  
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    
    return true;
  }
  
  public boolean onKeyUp(int keyCode, KeyEvent event) {
    
    return true;
  }
  
}
