package vn.mran.udpandroid.model;

import java.net.InetAddress;

/**
 * Created by An Pham on 18-Feb-17.
 * Last modifined on 18-Feb-17
 */

public class P2pData {
    private static P2pData instance;
    private int p2pPort;
    private InetAddress p2pIP;

    private P2pData() {
    }

    public static P2pData getInstance() {
        if (instance == null) instance = new P2pData();
        return instance;
    }

    public int getP2pPort() {
        return p2pPort;
    }

    public void setP2pPort(int p2pPort) {
        this.p2pPort = p2pPort;
    }

    public InetAddress getP2pIP() {
        return p2pIP;
    }

    public void setP2pIP(InetAddress p2pIP) {
        this.p2pIP = p2pIP;
    }
}
