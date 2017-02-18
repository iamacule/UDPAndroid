package vn.mran.udpandroid;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

import vn.mran.udpandroid.model.P2pData;

/**
 * Created by AnPham on 2/17/2017.
 * PROJECT UDPAndroid
 */

public class MainPresenter {
    private final String TAG = getClass().getSimpleName();
    public final String TYPE_TEXT = "TYPE_TEXT";
    public final String TYPE_IMAGE = "TYPE_IMAGE";
    public final String TYPE_VIDEO = "TYPE_VIDEO";

    public final String CANCEL = "CANCEL";
    public final String WAITING_FOR_IMAGE = "WAITING_FOR_IMAGE";

    private MainView view;
    private ListenerThread listenerThread;
    private int myPort;
    private String myIP;

    public MainPresenter(MainView view, int myPort) {
        this.view = view;
        this.myPort = myPort;
    }

    public void startThread() {
        Log.d(TAG, "startThread: ");
        listenerThread = new ListenerThread();
        listenerThread.start();
    }

    public void endThread() {
        Log.d(TAG, "endThread: ");
        if (listenerThread != null && listenerThread.isAlive()) {
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
                    DatagramPacket sendPacket = new DatagramPacket(message.getBytes(), message.getBytes().length,
                            P2pData.getInstance().getP2pIP(), P2pData.getInstance().getP2pPort());
                    clientSocket.send(sendPacket);
                    Log.d(TAG, "Sent : " + message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void sendFile(final File file) {
        view.loading("Sending ... ");
        new Thread(new Runnable() {
            @Override
            public void run() {
                ServerSocket ss = null;
                Socket clientSock = null;
                try {
                    ss = new ServerSocket(myPort + 1);
                    clientSock = ss.accept();
                    DataOutputStream dos = new DataOutputStream(clientSock.getOutputStream());
                    FileInputStream fis = new FileInputStream(file);
                    byte[] buffer = new byte[4096];

                    while (fis.read(buffer) > 0) {
                        dos.write(buffer);
                    }

                    fis.close();
                    dos.close();
                    clientSock.close();
                    ss.close();
                    view.onSendImageSuccess();
                } catch (Exception e) {
                    e.printStackTrace();
                    view.onSendImageError();
                } finally {
                }
            }
        }).start();
    }

    private class ListenerThread extends Thread {
        private volatile boolean run = true;
        private DatagramSocket datagramSocket;
        private DatagramPacket receivePacket;

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
            receivePacket = new DatagramPacket(receiveData, receiveData.length);
            while (run && !Thread.currentThread().isInterrupted()) {
                switch (receiveText()) {
                    case TYPE_TEXT:
                        Log.d(TAG, "run: TYPE_TEXT");
                        while (true) {
                            String data = receiveText();
                            if (!data.equals(CANCEL)) {
                                view.onReceiveText(data, receivePacket.getSocketAddress().toString());
                                break;
                            } else {
                                break;
                            }
                        }
                        break;
                    case TYPE_IMAGE:
                        Log.d(TAG, "run: TYPE_IMAGE");
                        while (true) {
                            String fileName = receiveText();
                            if (!fileName.equals(CANCEL)) {
                                view.loading("Receiving ... ");
                                Log.d(TAG, "run: Receiving file name : " + fileName);
                                String ip = receiveText();
                                Log.d(TAG, "run: Receiving partner IP : " + ip);
                                String newPort = receiveText();
                                Log.d(TAG, "run: Receiving partner Port : " + newPort);
                                String fileLength = receiveText();
                                Log.d(TAG, "run: Receiving file size : " + fileLength);
                                Bitmap responseBitmap = BitmapFactory.decodeFile(receiveFile(ip, Integer.parseInt(newPort), fileName,
                                        Integer.parseInt(fileLength)).getPath());
                                if (responseBitmap != null) {
                                    view.onReceiveImageSuccess(responseBitmap, ip);
                                } else {
                                    view.onReceiveImageError();
                                }
                                break;
                            } else {
                                break;
                            }
                        }
                        break;
                    case TYPE_VIDEO:
                        Log.d(TAG, "run: TYPE_VIDEO");
                        while (true) {
                            String fileName = receiveText();
                            if (!fileName.equals(CANCEL)) {
                                view.loading("Receiving ... ");
                                Log.d(TAG, "run: Receiving file name : " + fileName);
                                String ip = receiveText();
                                Log.d(TAG, "run: Receiving partner IP : " + ip);
                                String newPort = receiveText();
                                Log.d(TAG, "run: Receiving partner Port : " + newPort);
                                String fileLength = receiveText();
                                Log.d(TAG, "run: Receiving file size : " + fileLength);
                                File file = receiveFile(ip, Integer.parseInt(newPort), fileName, Integer.parseInt(fileLength));
                                if (file != null) {
                                    view.onReceiveVideoSuccess(file , ip);
                                } else {
                                    view.onReceiveVideoError();
                                }
                                break;
                            } else {
                                break;
                            }
                        }
                        break;
                }
            }
        }

        private File receiveFile(String ip, int port, String fileName, int fileLength) {
            Socket s;
            try {
                File file = new File(Environment.getExternalStorageDirectory(), fileName);
                if (file.exists()) {
                    file.delete();
                }
                file.createNewFile();

                s = new Socket(ip, port);
                DataInputStream dis = new DataInputStream(s.getInputStream());
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buffer = new byte[4096];

                int read = 0;
                int totalRead = 0;
                while ((read = dis.read(buffer, 0, Math.min(buffer.length, fileLength))) > 0) {
                    totalRead += read;
                    fileLength -= read;
                    System.out.println("read " + totalRead + " bytes.");
                    fos.write(buffer, 0, read);
                }

                fos.close();
                dis.close();

                Log.d(TAG, "receiveFile: file length : " + file.length());
                return file;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public void endThread() {
            run = false;
            Thread.currentThread().interrupt();
        }

        private String receiveText() {
            try {
                datagramSocket.receive(receivePacket);
                return new String(
                        receivePacket.getData(),
                        receivePacket.getOffset(),
                        receivePacket.getLength(),
                        StandardCharsets.UTF_8
                );
            } catch (Exception e) {
                return null;
            }
        }
    }

    public void setP2pData(View v, String p2pId, String p2pPort) {
        try {
            P2pData.getInstance().setP2pPort(Integer.parseInt(p2pPort));
        } catch (Exception e) {
            view.onPortError();
            return;
        }
        try {
            P2pData.getInstance().setP2pIP(InetAddress.getByName(p2pId));
        } catch (Exception e) {
            view.onIpError();
            return;
        }
        view.onCreateP2pSuccess(v.getId());
    }
}
