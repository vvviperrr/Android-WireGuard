package io.wireguard;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends WgActivityBase {

    private Button toggle_vpn_button;
    private TextView status_textview;

    private TextView ip_textview;
    private TextView listen_port_view;
    private TextView bytes_sent_view;
    private TextView bytes_received_view;
    private TextView bytes_dropped_view;
    private TextView packets_sent_view;
    private TextView packets_received_view;
    private TextView packets_dropped_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toggle_vpn_button = (Button) findViewById(R.id.toggle_vpn_button);
        status_textview = (TextView) findViewById(R.id.status_textview);

        ip_textview = (TextView) findViewById(R.id.ip_address);
        listen_port_view = (TextView) findViewById(R.id.listen_port);

        bytes_sent_view = (TextView) findViewById(R.id.bytes_sent);
        bytes_received_view = (TextView) findViewById(R.id.bytes_received);
        bytes_dropped_view = (TextView) findViewById(R.id.bytes_dropped);
        packets_sent_view = (TextView) findViewById(R.id.packets_sent);
        packets_received_view = (TextView) findViewById(R.id.packets_received);
        packets_dropped_view = (TextView) findViewById(R.id.packets_dropped);

        toggle_vpn_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectionStatus() != ConnectionStatus.CONNECTED) {

                    ConnectionInfo.Interface iface = new ConnectionInfo.Interface();
                    iface.setInterfaceIp("192.168.177.15");
                    iface.setListenPort(55555);
                    iface.setPrivateKey("aabbccddee...");

                    ConnectionInfo.Peer peer1 = new ConnectionInfo.Peer();
                    peer1.setPublicKey("peer1_public_key");

                    ConnectionInfo.Peer peer2 = new ConnectionInfo.Peer();
                    peer2.setEndpointIp("89.169.163.10");
                    peer2.setEndpointPort(44444);
                    peer2.setPublicKey("peer2_public_key");

                    ConnectionInfo conn = new ConnectionInfo();
                    conn.setInterface(iface);
                    conn.addPeer(peer1);
                    conn.addPeer(peer2);

                    connect(conn);
                } else {
                    disconnect();
                }
                onConnectionInfo(null);
                onSessionStat(null);
            }
        });

        onConnectionStatus(ConnectionStatus.DISCONNECTED);
    }

    @Override
    protected void onConnectionStatus(ConnectionStatus status) {
        status_textview.setText(status.toString());
        toggle_vpn_button.setText(status == ConnectionStatus.CONNECTED ? "STOP" : "START");
    }

    @Override
    protected void onConnectionInfo(ConnectionInfo info) {
        if (info != null) {
            ip_textview.setText(info.iface().interfaceIp());
            listen_port_view.setText(Integer.toString(info.iface().listenPort()));

            for (ConnectionInfo.Peer p : info.peers()) {
                Log.w("MainActivity", p.publicKey());
                if (p.endpointIp() != null) {
                    Log.w("MainActivity", p.endpointIp());
                    Log.w("MainActivity", Integer.toString(p.endpointPort()));
                }
                Log.w("MainActivity", "--------------------------------------");
            }

        } else {
            ip_textview.setText("");
            listen_port_view.setText("");
        }
    }

    @Override
    protected void onSessionStat(SessionStat stat) {
        if (stat != null) {
            bytes_sent_view.setText(Long.toString(stat.bytesTransmitted));
            bytes_received_view.setText(Long.toString(stat.bytesReceived));
            bytes_dropped_view.setText(Long.toString(stat.bytesDropped));
            packets_sent_view.setText(Long.toString(stat.packetsTransmitted));
            packets_received_view.setText(Long.toString(stat.packetsReceived));
            packets_dropped_view.setText(Long.toString(stat.packetsDropped));
        } else {
            bytes_sent_view.setText("");
            bytes_received_view.setText("");
            bytes_dropped_view.setText("");
            packets_sent_view.setText("");
            packets_received_view.setText("");
            packets_dropped_view.setText("");
        }
    }
}
