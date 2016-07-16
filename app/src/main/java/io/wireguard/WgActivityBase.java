package io.wireguard;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.net.VpnService;
import android.util.Log;

public abstract class WgActivityBase extends Activity {
    private static final String TAG = "WgActivityBase";

    private boolean mIsBound = false;
    private Messenger mService = null;
    private final Messenger mMessenger = new Messenger(new IncomingHandler(this));

    private Bundle mConnectionExtras = null;

    private ConnectionStatus mLastConnectionStatus = ConnectionStatus.DISCONNECTED;
    private ConnectionInfo mLastConnectionInfo = null;
    private SessionStat mLastSessionStat = null;

    private final static class IncomingHandler extends Handler {
        private WgActivityBase mBaseActivity;

        public IncomingHandler(WgActivityBase base) {
            mBaseActivity = base;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (ServiceMessage.values()[msg.what]) {
            case CONNECTION_STATUS:
                mBaseActivity.onConnectionStatusMsg(ConnectionStatus.values()[msg.getData().getInt("status")]);
                break;
            case CONNECTION_INFO:
                mBaseActivity.onConnectionInfoMsg(ConnectionInfo.fromBundle(msg.getData().isEmpty() ? null : msg.getData()));
                break;
            case SESSION_STAT:
                mBaseActivity.onSessionStatMsg(SessionStat.fromBundle(msg.getData()));
                break;
            default:
                Log.w(TAG, "unsupported message received: " + msg.what);
                break;
            }
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.i(TAG, "onServiceConnected");
            mService = new Messenger(((WgService.LocalBinder) service).getMessageBinder());
            try {
                Message msg = Message.obtain(null, ClientMessage.REGISTER_CLIENT.ordinal());
                msg.replyTo = mMessenger;
                mService.send(msg);
            } catch (RemoteException e) {
                Log.e(TAG, e.getMessage());
            }

            updateConnectionStatus();
            updateConnectionInfo();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.i(TAG, "onServiceDisconnected");
            mService = null;
        }
    };

    private void send(Message msg) {
        if (mIsBound) {
            if (mService != null) {
                try {
                    msg.replyTo = mMessenger;
                    mService.send(msg);
                } catch (RemoteException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        Log.i(TAG, "onStart");
        super.onStart();
        bindService(new Intent(this, WgService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop");
        super.onStop();

        if (mIsBound) {
            if (mService != null) {
                try {
                    Message msg = Message.obtain(null, ClientMessage.UNREGISTER_CLIENT.ordinal());
                    msg.replyTo = mMessenger;
                    mService.send(msg);
                } catch (RemoteException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
            unbindService(mConnection);
            mIsBound = false;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    protected void onActivityResult(int request, int result, Intent data) {
        super.onActivityResult(request, result, data);
        Log.i(TAG, "onActivityResult");
        if (request == 0 && result == RESULT_OK) {
            Intent intent = new Intent(this, WgService.class);
            intent.putExtras(mConnectionExtras);
            startService(intent);
        }
    }

    private void onConnectionStatusMsg(ConnectionStatus status) {
        setConnectionStatus(status);
        onConnectionStatus(status);
    }

    private void onConnectionInfoMsg(ConnectionInfo info) {
        setConnectionInfo(info);
        onConnectionInfo(info);
    }

    private void onSessionStatMsg(SessionStat stat) {
        setSessionStat(stat);
        onSessionStat(stat);
    }

    private void setConnectionStatus(ConnectionStatus status) {
        mLastConnectionStatus = status;

        if (mLastConnectionStatus == ConnectionStatus.DISCONNECTED) {
            setConnectionInfo(null);
            setSessionStat(null);
        }
    }

    private void setConnectionInfo(ConnectionInfo info) { mLastConnectionInfo = info; }
    private void setSessionStat(SessionStat stat) { mLastSessionStat = stat; }

    public ConnectionStatus connectionStatus() { return mLastConnectionStatus; }
    public ConnectionInfo connectionInfo() { return mLastConnectionInfo; }
    public SessionStat sessionStat() { return mLastSessionStat; }

    public void connect(ConnectionInfo info) {
        if (info == null) {
            Log.e(TAG, "Connection info is null!");
            //TODO: notify caller
            return;
        }

        /*
        if (!hasTunDriver()) {
            onConnectionErrorMsg(ConnectionError.TUN_FAILED);
            return;
        }
        */

        mConnectionExtras = ConnectionInfo.toBundle(info);
        if (mConnectionExtras == null) {
            Log.e(TAG, "Cannot create connection info extras!");
            //TODO: notify caller
            return;
        }

        Intent intent = VpnService.prepare(this);
        if (intent != null) {
            startActivityForResult(intent, 0);
        } else {
            onActivityResult(0, RESULT_OK, null);
        }
    }

    public void updateConnectionStatus() {
        Log.i(TAG, "updateConnectionStatus");
        send(Message.obtain(null, ClientMessage.UPDATE_CONNECTION_STATUS.ordinal()));
    }

    public void updateConnectionInfo() {
        Log.i(TAG, "updateConnectionInfo");
        send(Message.obtain(null, ClientMessage.UPDATE_CONNECTION_INFO.ordinal()));
    }

    public void updateSessionStat() {
        Log.i(TAG, "updateSessionStat");
        send(Message.obtain(null, ClientMessage.UPDATE_SESSION_STAT.ordinal()));
    }

    public void disconnect() {
        Log.i(TAG, "disconnect");
        send(Message.obtain(null, ClientMessage.DISCONNECT.ordinal()));
    }

    protected abstract void onConnectionStatus(ConnectionStatus status);
    protected abstract void onConnectionInfo(ConnectionInfo info);
    protected abstract void onSessionStat(SessionStat stat);
}
