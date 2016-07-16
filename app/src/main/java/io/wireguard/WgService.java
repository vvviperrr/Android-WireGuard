package io.wireguard;

import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Messenger;

public class WgService extends VpnService {
    private static final String TAG = "WgService";

    private ParcelFileDescriptor mInterface = null;

    private ConnectionInfo mConnectionInfo = null;
    private ConnectionStatus mConnectionStatus = ConnectionStatus.DISCONNECTED;

    private Messenger mClient = null;
    private final Messenger mMessenger = new Messenger(new IncomingHandler(this));
    private final IBinder mBinder = new LocalBinder();

    private final SessionStatThread mSessionStatThread = new SessionStatThread();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy called");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand called");

        if (intent == null) {
            Log.i(TAG, "Intent is null");
            return START_NOT_STICKY;
        }

        connect(intent.getExtras());
        return START_NOT_STICKY;
    }

    @Override
    public void onRevoke() {
        Log.i(TAG, "onRevoke called");
        disconnect();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind called");
        return mBinder;
    }

    public void connect(Bundle extras) {
        Log.i(TAG, "connect");

        if (mConnectionStatus != ConnectionStatus.DISCONNECTED) {
            Log.e(TAG, "still not disconnected!");
            //TODO; notify caller
            return;
        }

        // extract connection info
        mConnectionInfo = ConnectionInfo.fromBundle(extras);

        // print info
        mConnectionInfo.toLog();

        if (!Native.connect()) {
            //TODO notify client
            Log.e(TAG, "connection started error");
            return;
        }

        Builder builder = new Builder();
        builder.setConfigureIntent(null);

        // REMOVE!!!
        String ipAddr = "192.168.177.10";
        String[] dnsServers = new String[]{"8.8.8.8"};

        try {
            builder.setMtu(mConnectionInfo.mtu());
            builder.addSearchDomain(mConnectionInfo.domain());
            builder.setSession(mConnectionInfo.serverIp());
        } catch (Exception e) {
            // bad config
            //TODO: notify client
            Log.e(TAG, e.getMessage());
        }

        try {
            Log.i(TAG, "ipAddr = " + ipAddr);
            builder.addAddress(ipAddr, 32);
        } catch (Exception e) {
            // bad client ip address
            //TODO: notify client
            Log.e(TAG, e.getMessage());
        }

        try {
            if (dnsServers.length > 0) {
                // use server dns
                for (String dns : dnsServers) {
                    Log.i(TAG, "dns = " + dns);
                    builder.addDnsServer(dns);
                }
            } else {
                // use client dns
                builder.addDnsServer(mConnectionInfo.primaryDns());
                builder.addDnsServer(mConnectionInfo.secondaryDns());
            }
        } catch (Exception e) {
            // bad dns
            //TODO: notify client
            Log.e(TAG, e.getMessage());
        }

        /*
        try {
            for (String route : routes) {
                Log.i(TAG, "route = " + route.destIp + "/" + route.prefixLen);
                builder.addRoute(route.destIp, route.prefixLen);
            }
        } catch (Exception e) {
            // bad route
            //TODO: notify client
            Log.e(TAG, e.getMessage());
        }
        */

        try {
            mInterface = builder.establish();
            Native.setTunFd(mInterface.getFd());
            setConnectionStatus(ConnectionStatus.CONNECTED);
        } catch (Exception e) {
            // cannot establish connection
            //TODO: notify client
            Log.e(TAG, e.getMessage());
        }

        // store connection info
        if (mConnectionInfo != null) {
            /*
            String[] routesList = new String[routes.length];
            for (int i = 0; i < routes.length; i++) {
                routesList[i] = routes[i].destIp + "/" + routes[i].prefixLen;
            }
            mConnectionInfo.setRoutes(routesList);
            */
            mConnectionInfo.setVirtualAddr(ipAddr);

            if (dnsServers.length > 0) {
                mConnectionInfo.setPrimaryDns(dnsServers[0]);
                if (dnsServers.length > 1) {
                    mConnectionInfo.setSecondaryDns(dnsServers[1]);
                }
            }
        }

        // set connection time
        mConnectionInfo.setConnectionTime(System.currentTimeMillis());

        // start session stat thread
        mSessionStatThread.start();

        // update connection info
        sendConnectionInfo();
    }

    public void disconnect() {
        Log.i(TAG, "disconnect");

        if (mConnectionStatus == ConnectionStatus.DISCONNECTED) {
            Log.w(TAG, "Already disconnected!");
            return;
        }

        Native.disconnect();

        if (mInterface != null) {
            try {
                mInterface.close();
                mInterface = null;
            } catch (java.io.IOException e) {
                Log.i(TAG, e.getMessage());
            }
        }

        setConnectionStatus(ConnectionStatus.DISCONNECTED);

        mConnectionInfo = null;
    }

    private void registerClient(Messenger client) {
        mClient = client;

        if (mConnectionStatus == ConnectionStatus.CONNECTED) {
            mSessionStatThread.start();
        }
    }

    private void unregisterClient() {
        mClient = null;
    }

    private void sendConnectionStatus(ConnectionStatus status) {
        Message msg = Message.obtain(null, ServiceMessage.CONNECTION_STATUS.ordinal());
        Bundle b = new Bundle();
        b.putInt("status", status.ordinal());
        msg.setData(b);
        send(msg);
    }

    private void sendConnectionInfo() {
        Message msg = Message.obtain(null, ServiceMessage.CONNECTION_INFO.ordinal());
        if (mConnectionInfo != null) {
            msg.setData(ConnectionInfo.toBundle(mConnectionInfo));
        }
        send(msg);
    }

    private void sendSessionStat() {
        SessionStat stat = Native.sessionStat();
        if (stat == null) {
            Log.e(TAG, "sendSessionStat: stat is null");
            return;
        }

        Message msg = Message.obtain(null, ServiceMessage.SESSION_STAT.ordinal());
        msg.setData(SessionStat.toBundle(stat));
        send(msg);
    }

    private void send(Message msg) {
        if (mClient != null) {
            try {
                mClient.send(msg);
            } catch (RemoteException e) {
                Log.e(TAG, e.getMessage());
            }
        } else {
            Log.w(TAG, "cannot send msg. client is null");
        }
    }

    private void setConnectionStatus(ConnectionStatus status) {
        mConnectionStatus = status;
        sendConnectionStatus(mConnectionStatus);
    }

    public class LocalBinder extends Binder {
        public IBinder getMessageBinder() {
            return mMessenger.getBinder();
        }

        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) {
            if (code == IBinder.LAST_CALL_TRANSACTION) {
                onRevoke();
                return true;
            }
            return false;
        }
    }

    private final static class IncomingHandler extends Handler {
        private WgService mService = null;

        public IncomingHandler(WgService service) {
            mService = service;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (ClientMessage.values()[msg.what]) {
            case REGISTER_CLIENT:
                mService.registerClient(msg.replyTo);
                break;
            case UNREGISTER_CLIENT:
                mService.unregisterClient();
                break;
            case UPDATE_CONNECTION_STATUS:
                mService.sendConnectionStatus(mService.mConnectionStatus);
                break;
            case UPDATE_CONNECTION_INFO:
                mService.sendConnectionInfo();
                break;
            case UPDATE_SESSION_STAT:
                mService.sendSessionStat();
                break;
            case DISCONNECT:
                mService.disconnect();
                break;
            default:
                Log.w(TAG, "unsupported message received: " + msg.what);
            }
        }
    }

    private final class SessionStatThread {
        private volatile boolean mStarted = false;
        private static final int SESSION_STAT_UPDATE_TIMEOUT = 1000;

        private boolean isActive() {
            return mConnectionStatus == ConnectionStatus.CONNECTED && mClient != null;
        }

        public void start() {
            if (mStarted) {
                return;
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    mStarted = true;
                    Log.w(TAG, "SessionStat thread started");
                    while (isActive()) {
                        sendSessionStat();
                        try {
                            Thread.sleep(SESSION_STAT_UPDATE_TIMEOUT);
                        } catch (InterruptedException e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }

                    mStarted = false;
                    Log.w(TAG, "SessionStat thread terminated");
                }
            }).start();
        }
    }
}
