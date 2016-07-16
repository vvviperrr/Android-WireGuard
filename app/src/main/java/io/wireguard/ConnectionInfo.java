package io.wireguard;

import android.os.Bundle;
import android.util.Log;

public class ConnectionInfo {
    private static final String TAG = "ConnectionInfo";
    private static final String prefix = ConnectionInfo.class.getPackage().getName();

    public static final int TRANSPORT_UDP = 0;
    public static final int TRANSPORT_TCP = 1;

    public static final int PROXY_NO_AUTH = 0;
    public static final int PROXY_BASIC_AUTH = 1;
    public static final int PROXY_NTLM_AUTH = 2;
    public static final int PROXY_NEGOTIATE_AUTH = 3;
    public static final int PROXY_KERBEROS_AUTH = 4;

    private static final String STORED_SERVER_IP = prefix + ".SERVER_IP";
    private static final String STORED_SERVER_PORT = prefix + ".SERVER_PORT";
    private static final String STORED_CLIENT_PORT = prefix + ".CLIENT_PORT";
    private static final String STORED_TRANSPORT = prefix + ".TRANSPORT";
    private static final String STORED_CLIENT_ID = prefix + ".CLIENT_ID";
    private static final String STORED_PRIMARY_DNS = prefix + ".PRIMARY_DNS";
    private static final String STORED_SECONDARY_DNS = prefix + ".SECONDARY_DNS";
    private static final String STORED_DOMAIN = prefix + ".DOMAIN";
    private static final String STORED_MTU = prefix + ".MTU";
    private static final String STORED_CREDS_DIR = prefix + ".CREDS_DIR";
    private static final String STORED_CREDS_NAME = prefix + ".CREDS_NAME";
    private static final String STORED_INACTIVE_TIMEOUT = prefix + ".INACTIVE_TIMEOUT";
    private static final String STORED_RECONNECT_COUNT = prefix + ".RECONNECT_COUNT";
    private static final String STORED_RECONNECT_DELAY = prefix + ".RECONNECT_DELAY";
    private static final String STORED_USER_CERT = prefix + ".USER_CERT";
    private static final String STORED_ROOT_CERT = prefix + ".ROOT_CERT";
    private static final String STORED_USER_KEY = prefix + ".USER_KEY";
    private static final String STORED_PASSWORD = prefix + ".PASSWORD";
    private static final String STORED_PASSWORD_MASK = prefix + ".PASSWORD_MASK";

    private static final String STORED_PROXY_IP = prefix + ".PROXY_IP";
    private static final String STORED_PROXY_PORT = prefix + ".PROXY_PORT";
    private static final String STORED_PROXY_AUTH = prefix + ".PROXY_AUTH";
    private static final String STORED_PROXY_LOGIN = prefix + ".PROXY_LOGIN";
    private static final String STORED_PROXY_PASSWORD = prefix + ".PROXY_PASSWORD";


    private static final String VIRTUAL_ADDR = prefix + ".VIRTUAL_ADDR";
    private static final String ROUTES = prefix + ".ROUTES";
    private static final String TIME = prefix + ".TIME";
    private static final String STORED_JACARTA_PIN = ".JACARTA_PIN";
    private static final String STORED_JACARTA_PIN_MASK = ".JACARTA_PIN_MASK";

    private String mServerIp = null;
    private int mServerPort = 0;
    private int mClientPort = 0;
    private int mTransport = TRANSPORT_UDP;
    private String mClientId = null;
    private String mPrimaryDns = null;
    private String mSecondaryDns = null;
    private String mDomain = null;
    private int mMtu = 0;
    private int mInactiveTimeout = 0;
    private int mReconnectCount = 0;
    private int mReconnectDelay = 0;
    private String mCredsDir = null;
    private String mCredsName = null;

    private byte[] mUserCert = null;
    private byte[] mRootCert = null;
    private byte[] mUserKey = null;

    private String mProxyIp = null;
    private int mProxyPort = 0;
    private int mProxyAuth = PROXY_NO_AUTH;
    private String mProxyLogin = null;
    private String mProxyPassword = null;

    private String mVirtualAddr = null;
    private String[] mRoutes = null;

    private long mTime = 0;

    private byte[] mPassword = null;

    public ConnectionInfo() {
        Log.e(TAG, "new ConnectionInfo ");
    }

    public void toLog() {
        Log.i(TAG, "server ip = " + (mServerIp == null ? "null" : mServerIp));
        Log.i(TAG, "server port = " + Integer.toString(mServerPort));
        Log.i(TAG, "client port = " + Integer.toString(mClientPort));
        Log.i(TAG, "transport = " + (mTransport == TRANSPORT_UDP ? "udp" : "tcp"));
        Log.i(TAG, "client id = " + (mClientId == null ? "null" : mClientId));
        Log.i(TAG, "primary dns = " + (mPrimaryDns == null ? "null" : mPrimaryDns));
        Log.i(TAG, "secondary dns = " + (mSecondaryDns == null ? "null" : mSecondaryDns));
        Log.i(TAG, "domain = " + (mDomain == null ? "null" : mDomain));
        Log.i(TAG, "creds dir = " + (mCredsDir == null ? "null" : mCredsDir));
        Log.i(TAG, "creds name = " + (mCredsName == null ? "null" : mCredsName));
        Log.i(TAG, "mtu = " + Integer.toString(mMtu));
        Log.i(TAG, "inactive timeout = " + Integer.toString(mInactiveTimeout));
        Log.i(TAG, "reconnect count = " + Integer.toString(mReconnectCount));
        Log.i(TAG, "reconnect delay = " + Integer.toString(mReconnectDelay));
        Log.i(TAG, "virtual addr" + (mVirtualAddr == null ? "null" : mVirtualAddr));
        Log.i(TAG, "time = " + Long.toString(mTime));

        Log.i(TAG, "proxy ip = " + (mProxyIp == null ? "null" : mProxyIp));
        Log.i(TAG, "proxy port = " + Integer.toString(mProxyPort));
        Log.i(TAG, "proxy auth = " + Integer.toString(mProxyAuth));
        Log.i(TAG, "proxy login = " + (mProxyLogin == null ? "null" : mProxyLogin));

        if (mRoutes != null) {
            for (String i : mRoutes) {
                Log.i(TAG, "route = " + i);
            }
        }
    }

    public void setServerIp(String ip) { mServerIp = ip; }
    public void setServerPort(int port) { mServerPort = port; }
    public void setClientPort(int port) { mClientPort = port; }
    public void setTransport(int i) { mTransport = i; }
    public void setClientId(String id) { mClientId = id; }
    public void setPrimaryDns(String dns) { mPrimaryDns = dns; }
    public void setSecondaryDns(String dns) { mSecondaryDns = dns; }
    public void setDomain(String domain) { mDomain = domain; }
    public void setMtu(int mtu) { mMtu = mtu; }
    public void setInactiveTimeout(int val) { mInactiveTimeout = val; }
    public void setReconnectCount(int val) { mReconnectCount = val; }
    public void setReconnectDelay(int val) { mReconnectDelay = val; }
    public void setCredsDir(String dir) { mCredsDir = dir; }
    public void setCredsName(String name) { mCredsName = name; }
    public void setUserCert(byte[] cert) { mUserCert = cert; }
    public void setRootCert(byte[] cert) { mRootCert = cert; }
    public void setUserKey(byte[] key) { mUserKey = key; }

    public void setPassword(byte[] pass) {
        mPassword = pass;
    }

    public void setVirtualAddr(String addr) { mVirtualAddr = addr; }
    public void setRoutes(String[] routes) { mRoutes = routes; }
    public void setConnectionTime(long time) { mTime = time; }

    public void setProxyIp(String ip) { mProxyIp = ip; }
    public void setProxyPort(int port) { mProxyPort = port; }
    public void setProxyAuth(int auth) { mProxyAuth = auth; }
    public void setProxyLogin(String login) { mProxyLogin = login; }
    public void setProxyPassword(String password) { mProxyPassword = password; }

    public String serverIp() { return mServerIp; }
    public int serverPort() { return mServerPort; }
    public int clientPort() { return mClientPort; }
    public int transport() { return mTransport; }
    public String clientId() { return mClientId; }
    public String primaryDns() { return mPrimaryDns; }
    public String secondaryDns() { return mSecondaryDns; }
    public String domain() { return mDomain; }
    public int mtu() { return mMtu; }
    public int inactiveTimeout() { return mInactiveTimeout; }
    public int reconnectCount() { return mReconnectCount; }
    public int reconnectDelay() { return mReconnectDelay; }
    public String credsDir() { return mCredsDir; }
    public String credsName() { return mCredsName; }
    public byte[] userCert() { return mUserCert; }
    public byte[] rootCert() { return mRootCert; }
    public byte[] userKey() { return mUserKey; }

    public byte[] password() { return mPassword; }

    public String virtualAddr() { return mVirtualAddr; }
    public String[] routes() { return mRoutes; }
    public long connectionTime() { return mTime; }

    public String proxyIp() { return mProxyIp; }
    public int proxyPort() { return mProxyPort; }
    public int proxyAuth() { return mProxyAuth; }
    public String proxyLogin() { return mProxyLogin; }
    public String proxyPassword() { return mProxyPassword; }

    public static Bundle toBundle(ConnectionInfo info) {
        if (info == null) {
            return null;
        }

        Bundle ret = new Bundle();
        ret.putString(STORED_SERVER_IP, info.mServerIp);
        ret.putInt(STORED_SERVER_PORT, info.mServerPort);
        ret.putInt(STORED_CLIENT_PORT, info.mClientPort);
        ret.putInt(STORED_TRANSPORT, info.mTransport);
        ret.putString(STORED_CLIENT_ID, info.mClientId);
        ret.putString(STORED_PRIMARY_DNS, info.mPrimaryDns);
        ret.putString(STORED_SECONDARY_DNS, info.mSecondaryDns);
        ret.putString(STORED_DOMAIN, info.mDomain);
        ret.putInt(STORED_MTU, info.mMtu);
        ret.putInt(STORED_INACTIVE_TIMEOUT, info.mInactiveTimeout);
        ret.putInt(STORED_RECONNECT_COUNT, info.mReconnectCount);
        ret.putInt(STORED_RECONNECT_DELAY, info.mReconnectDelay);
        ret.putString(STORED_CREDS_DIR, info.mCredsDir);
        ret.putString(STORED_CREDS_NAME, info.mCredsName);

        ret.putString(STORED_PROXY_IP, info.proxyIp());
        ret.putInt(STORED_PROXY_PORT, info.proxyPort());
        ret.putInt(STORED_PROXY_AUTH, info.proxyAuth());
        ret.putString(STORED_PROXY_LOGIN, info.proxyLogin());
        ret.putString(STORED_PROXY_PASSWORD, info.proxyPassword());


        if (info.mUserCert != null) {
            ret.putByteArray(STORED_USER_CERT, info.mUserCert);
        }
        if (info.mRootCert != null) {
            ret.putByteArray(STORED_ROOT_CERT, info.mRootCert);
        }
        if (info.mUserKey != null) {
            ret.putByteArray(STORED_USER_KEY, info.mUserKey);
        }

        if (info.mVirtualAddr != null) {
            ret.putString(VIRTUAL_ADDR, info.mVirtualAddr);
        }
        if (info.mRoutes != null) {
            ret.putStringArray(ROUTES, info.mRoutes);
        }

        ret.putLong(TIME, info.mTime);

        return ret;
    }

    public static ConnectionInfo fromBundle(Bundle extras) {
        if (extras == null) {
            return null;
        }

        ConnectionInfo ret = new ConnectionInfo();
        ret.setServerIp(extras.getString(STORED_SERVER_IP));
        ret.setServerPort(extras.getInt(STORED_SERVER_PORT));
        ret.setClientPort(extras.getInt(STORED_CLIENT_PORT));
        ret.setTransport(extras.getInt(STORED_TRANSPORT));
        ret.setClientId(extras.getString(STORED_CLIENT_ID));
        ret.setPrimaryDns(extras.getString(STORED_PRIMARY_DNS));
        ret.setSecondaryDns(extras.getString(STORED_SECONDARY_DNS));
        ret.setDomain(extras.getString(STORED_DOMAIN));
        ret.setCredsDir(extras.getString(STORED_CREDS_DIR));
        ret.setCredsName(extras.getString(STORED_CREDS_NAME));
        ret.setMtu(extras.getInt(STORED_MTU));
        ret.setInactiveTimeout(extras.getInt(STORED_INACTIVE_TIMEOUT));
        ret.setReconnectCount(extras.getInt(STORED_RECONNECT_COUNT));
        ret.setReconnectDelay(extras.getInt(STORED_RECONNECT_DELAY));
        ret.setUserCert(extras.getByteArray(STORED_USER_CERT));
        ret.setRootCert(extras.getByteArray(STORED_ROOT_CERT));
        ret.setUserKey(extras.getByteArray(STORED_USER_KEY));

        ret.setVirtualAddr(extras.getString(VIRTUAL_ADDR));
        ret.setRoutes(extras.getStringArray(ROUTES));
        ret.setConnectionTime(extras.getLong(TIME));

        ret.setProxyIp(extras.getString(STORED_PROXY_IP));
        ret.setProxyPort(extras.getInt(STORED_PROXY_PORT));
        ret.setProxyAuth(extras.getInt(STORED_PROXY_AUTH));
        ret.setProxyLogin(extras.getString(STORED_PROXY_LOGIN));
        ret.setProxyPassword(extras.getString(STORED_PROXY_PASSWORD));

        return ret;
    }
}

