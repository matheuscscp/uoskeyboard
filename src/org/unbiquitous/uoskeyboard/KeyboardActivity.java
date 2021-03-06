package org.unbiquitous.uoskeyboard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ToggleButton;

public class KeyboardActivity extends Activity {
  private InputMethodManager imm = null;
  private View keyboard_view = null;
  
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_keyboard);
    
    imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
    keyboard_view = findViewById(R.id.keyboard_view);
  }
  
  protected void onResume() {
    super.onResume();
    Globals.keyboardActivity = this;
  }
  
  protected void onPause() {
    super.onResume();
    Globals.keyboardActivity = null;
  }
  
  public void exit(View view) {
    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
    startActivity(intent);
  }
  
  public void toggleKeyboard(View view) {
    if (((ToggleButton)view).isChecked())
      imm.showSoftInput(keyboard_view, InputMethodManager.SHOW_IMPLICIT);
    else
      imm.hideSoftInputFromWindow(keyboard_view.getWindowToken(), 0);
  }
  
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    Globals.keyboardDriver.notifyKeyDown(event.getUnicodeChar());
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
    }
    Globals.keyboardDriver.notifyKeyUp(event.getUnicodeChar());
    return true;
  }
}
