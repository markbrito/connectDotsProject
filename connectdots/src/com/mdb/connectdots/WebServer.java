package com.mdb.connectdots;

import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class WebServer extends Thread {
    public static final int HTTPPORT = 8998;
    public static int mPort = HTTPPORT;

    protected Object mMutex = new Object();
    protected Socket mSocketMain;
    public ServerSocketChannel mServerSocketChannel;

    public WebServer() {
        initServerSocket();
    }

    public void initServerSocket() {
        try {
            mServerSocketChannel =
                    ServerSocketChannel.open();
            mServerSocketChannel.socket().setReuseAddress(true);
            mServerSocketChannel.socket().bind(
                    new InetSocketAddress(mPort));
        } catch (Exception e) {
            Log.e(MainActivity.TAG, e.getMessage());
        }
    }

    public void run() {
        while (true) {
            try {

                SocketChannel socketChannel = mServerSocketChannel.accept();
                Socket socketAccept = socketChannel.socket();
                synchronized (mMutex) {
                    mSocketMain = socketAccept;
                    mSocketMain.setSoTimeout(1000 * 30);
                    handleRequest(mSocketMain);
                }
            } catch (InterruptedException ie) {
                try {
                    mSocketMain.close();
                } catch (Exception e) {
                    ;
                }
                try {
                    mServerSocketChannel.close();
                } catch (Exception e) {
                    ;
                }
            } catch (IOException ioe) {
            } catch (Exception e) {
                Log.e(MainActivity.TAG, e.getMessage());
                initServerSocket();
            }
        }
    }

    public void handleRequest(java.net.Socket s) throws Exception {
        try {
            InputStream socketInputStream = s.getInputStream();
            byte[] bytesInitGET = new byte[1024 * 64];
            byte[] bytesReadGET = new byte[socketInputStream.read(bytesInitGET)];
            for (int i = 0; i < bytesReadGET.length; ++i)
                bytesReadGET[i] = bytesInitGET[i];
            String stringGET = new String(bytesReadGET);
            String stringGETQS = stringGET.replaceAll("^GET ", "").
                    replaceAll(" HTTP/1.1.*", "");
            stringGET = stringGETQS.
                    replaceAll("\\?.*", "");
            stringGET = stringGET.substring(0, stringGET.indexOf('\r'));
            byte[] bytesResponse;
            URLCommand urlCommand = new URLCommand(stringGET).invoke();
            String contentResponse = urlCommand.getContentResponse();
            String contentTypeSuffix = urlCommand.getContentTypeSuffix();
            bytesResponse = contentResponse.getBytes();
            String header = "HTTP/1.1 200 OK\r\n";
            String type = "Content-Type: " +
                    contentTypeSuffix + "\r\n";
            String length = "Content-Length: " +
                    bytesResponse.length + "\r\n\r\n";
            String response = header + type + length;
            s.getOutputStream().write(response.getBytes());
            s.getOutputStream().write(bytesResponse);
        } catch (IOException ioe) {
            Log.e(MainActivity.TAG, ioe.getMessage());
        } catch (Exception ie) {
            Log.e(MainActivity.TAG, ie.getMessage());
        } finally {
            s.close();
        }
    }


}
