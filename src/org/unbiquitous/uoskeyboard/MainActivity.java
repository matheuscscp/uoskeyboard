package org.unbiquitous.uoskeyboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
  
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
    findViewById(R.id.textView1).setVisibility(visibility);
    findViewById(R.id.applicationName).setVisibility(visibility);
    findViewById(R.id.textView5).setVisibility(visibility);
    findViewById(R.id.button1).setVisibility(visibility);
    findViewById(R.id.Button01).setVisibility(visibility);
  }
  
  public void toggleUos(View view) {
    if (((ToggleButton) view).isChecked())
      UosManager.startUos();
    else
      UosManager.stopUos();
  }

  public void receiveRequest() {
    setRequestVisibility(View.VISIBLE);
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
