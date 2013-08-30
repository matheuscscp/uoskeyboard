package org.unbiquitous.uoskeyboard;

import android.os.AsyncTask;

public class UosStarter extends AsyncTask<Void, Void, Void> {

  @Override
  protected Void doInBackground(Void... params) {
    UosManager.startUos();
    return null;
  }
  
}
