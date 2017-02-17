package vn.mran.udpandroid;

/**
 * Created by thong on 2/17/2017.
 * PROJECT UDPAndroid
 */

public class MainPresenter {
    private MainView view;

    public MainPresenter(MainView view) {
        this.view = view;
    }

    private class ListenerThread extends Thread {
        private String p2pIp;



        @Override
        public void run() {

        }
    }
}
