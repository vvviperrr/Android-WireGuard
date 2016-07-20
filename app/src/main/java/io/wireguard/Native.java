package io.wireguard;

public final class Native {
    public static native boolean enable();
    public static native void disable();
    public static native void setTunFd(int fd);
    public static native SessionStat sessionStat();

    static {
        System.loadLibrary("wgjni");
    }
}
