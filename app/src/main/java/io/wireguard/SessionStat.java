package io.wireguard;

import android.os.Bundle;

public class SessionStat {
    public long packetsTransmitted;
    public long packetsReceived;
    public long packetsDropped;

    public long bytesTransmitted;
    public long bytesReceived;
    public long bytesDropped;

    public SessionStat() {
        this(0, 0, 0, 0, 0, 0);
    }

    public SessionStat(long ptx, long prx, long pdx, long btx, long brx, long bdx) {
        packetsTransmitted = ptx;
        packetsReceived = prx;
        packetsDropped = pdx;

        bytesTransmitted = btx;
        bytesReceived = brx;
        bytesDropped = bdx;
    }

    public static Bundle toBundle(SessionStat stat) {
        if (stat == null) {
            return null;
        }

        Bundle b = new Bundle();
        b.putLong("ptx", stat.packetsTransmitted);
        b.putLong("prx", stat.packetsReceived);
        b.putLong("pdx", stat.packetsDropped);
        b.putLong("btx", stat.bytesTransmitted);
        b.putLong("brx", stat.bytesReceived);
        b.putLong("bdx", stat.bytesDropped);
        return b;
    }

    public static SessionStat fromBundle(Bundle extras) {
        SessionStat s = new SessionStat();
        s.packetsTransmitted = extras.getLong("ptx");
        s.packetsReceived = extras.getLong("prx");
        s.packetsDropped = extras.getLong("pdx");
        s.bytesTransmitted = extras.getLong("btx");
        s.bytesReceived = extras.getLong("brx");
        s.bytesDropped = extras.getLong("bdx");
        return s;
    }
}