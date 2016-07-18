package io.wireguard;

import android.os.Bundle;

public class ConnectionInfo {
    private static final String prefix = ConnectionInfo.class.getPackage().getName();

    private static final String STORED_IP = prefix + ".IP";
    private static final String STORED_PORT = prefix + ".PORT";
    private static final String STORED_TIME = prefix + ".TIME";

    private String mIfaceIp = null;
    private int mListenPort = 0;

    private long mConnectionTime = 0;


    public void setInterfaceIp(String ip) { mIfaceIp = ip; }
    public void setListenPort(int port) { mListenPort = port; }

    public void setConnectionTime(long time) { mConnectionTime = time; }

    public String interfaceIp() { return mIfaceIp; }
    public int listenPort() { return mListenPort; }
    public long connectionTime() { return mConnectionTime; }

    public static Bundle toBundle(ConnectionInfo info) {
        if (info == null) {
            return null;
        }

        Bundle ret = new Bundle();
        ret.putString(STORED_IP, info.interfaceIp());
        ret.putInt(STORED_PORT, info.listenPort());

        ret.putLong(STORED_TIME, info.connectionTime());

        return ret;
    }

    public static ConnectionInfo fromBundle(Bundle extras) {
        if (extras == null) {
            return null;
        }

        ConnectionInfo ret = new ConnectionInfo();
        ret.setInterfaceIp(extras.getString(STORED_IP));
        ret.setListenPort(extras.getInt(STORED_PORT));
        ret.setConnectionTime(extras.getLong(STORED_TIME));

        return ret;
    }
}

