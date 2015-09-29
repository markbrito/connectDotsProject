package com.mdb.connectdots;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class SocketServer extends Service {
    private final ISocketServer.Stub mBinder = new ISocketServer.Stub() {
        public int getPort() {
            return MainActivity.webServer.mPort;
        }
    };
    @Override
    public void onCreate() {
        super.onCreate();
        try
        {
            MainActivity.connectDotsGame=new ConnectDots();
            MainActivity.webServer=new WebServer();
            MainActivity.webServer.setDaemon(true);
            MainActivity.webServer.start();
            String mMainURL ="http://localhost:" +
                    WebServer.mPort + "/";
            MainActivity.webView.loadUrl(mMainURL);
        }
        catch(Exception e){
            Log.e(MainActivity.TAG, e.getMessage() + e.getStackTrace());
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        if (SocketServer.class.getName().equals(intent.getAction())) {
            return mBinder;
        }
        return null;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (MainActivity.webServer != null) {
            MainActivity.webServer.interrupt();
            try {
                MainActivity.webServer.join();
            } catch (InterruptedException e) {

            }
        }
    }
}
