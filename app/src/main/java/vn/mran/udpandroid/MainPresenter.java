package vn.mran.udpandroid;

import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

/**
 * Created by thong on 2/17/2017.
 * PROJECT UDPAndroid
 */

public class MainPresenter {
    private final String TAG = getClass().getSimpleName();
    public final String TYPE_TEXT = "TYPE_TEXT";
    public final String TYPE_IMAGE = "TYPE_IMAGE";
    public final String TYPE_VIDEO = "TYPE_VIDEO";

    private MainView view;
    private ListenerThread listenerThread;
    private int myPort;
    private int p2pPort;
    private String p2pIP;
    private InetAddress inetAddress;

    public MainPresenter(MainView view, int myPort) {
        this.view = view;
        this.myPort = myPort;
    }

    public void startThread(){
        Log.d(TAG, "startThread: ");
        listenerThread = new ListenerThread();
        listenerThread.start();
    }

    public void endThread() {
        Log.d(TAG, "endThread: ");
        if (listenerThread!=null && listenerThread.isAlive()){
            listenerThread.endThread();
            listenerThread = null;
        }
    }

    public void sendMessage(final String message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DatagramSocket clientSocket = new DatagramSocket();
                    DatagramPacket sendPacket = new DatagramPacket(message.getBytes(), message.getBytes().length, inetAddress, p2pPort);
                    clientSocket.send(sendPacket);
                    Log.d(TAG, "Sent : " + message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private class ListenerThread extends Thread {
        private volatile boolean run = true;
        private DatagramSocket datagramSocket;

        private ListenerThread() {
            try {
                datagramSocket = new DatagramSocket(myPort);
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            byte[] receiveData = new byte[1024];
            while (run && !Thread.currentThread().isInterrupted()) {
                try {
                    Log.d(TAG, "run: waiting");
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    datagramSocket.receive(receivePacket);
                    String type = new String(
                            receivePacket.getData(),
                            receivePacket.getOffset(),
                            receivePacket.getLength(),
                            StandardCharsets.UTF_8 // or some other charset
                    );
                    Log.d(TAG, "Data : "+type);
                    switch (type) {
                        case TYPE_TEXT:
                            Log.d(TAG, "run: TYPE_TEXT");
                            view.onReceiveText(type);
                            break;
                        case TYPE_IMAGE:
                            Log.d(TAG, "run: TYPE_IMAGE");
                            break;
                        case TYPE_VIDEO:
                            Log.d(TAG, "run: TYPE_VIDEO");
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public void endThread() {
            run = false;
            Thread.currentThread().interrupt();
        }
    }

    public int getP2pPort() {
        return p2pPort;
    }

    public void setP2pPort(int p2pPort) {
        this.p2pPort = p2pPort;
    }

    public String getP2pIP() {
        return p2pIP;
    }

    public void setP2pIP(String p2pIP) {
        this.p2pIP = p2pIP;
        try {
            inetAddress = InetAddress.getByName(p2pIP);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
