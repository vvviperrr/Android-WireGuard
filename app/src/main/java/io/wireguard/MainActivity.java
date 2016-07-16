package io.wireguard;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends WgActivityBase {

    private Button toggle_vpn_button;
    private TextView status_textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toggle_vpn_button = (Button) findViewById(R.id.toggle_vpn_button);
        status_textview = (TextView) findViewById(R.id.status_textview) ;

        toggle_vpn_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectionStatus() != ConnectionStatus.CONNECTED) {
                    connect(new ConnectionInfo());
                } else {
                    disconnect();
                }
            }
        });
    }

    @Override
    protected void onConnectionStatus(ConnectionStatus status) {
        status_textview.setText(status.toString());
    }

    @Override
    protected void onConnectionInfo(ConnectionInfo info) {

    }

    @Override
    protected void onSessionStat(SessionStat stat) {

    }
}
