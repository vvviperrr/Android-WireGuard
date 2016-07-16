package io.wireguard;

public final class Native {
    public static native boolean connect();
    public static native void disconnect();
    public static native void setTunFd(int fd);
    public static native SessionStat sessionStat();

    static {
        System.loadLibrary("wgjni");
    }
}
