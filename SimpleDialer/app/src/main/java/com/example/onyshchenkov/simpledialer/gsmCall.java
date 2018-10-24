package com.example.onyshchenkov.simpledialer;


public class gsmCall  {
    private gsmCall.Status status;
    private String displayName;

    public gsmCall(gsmCall.Status status, String displayName) {
        super();
        this.status = status;
        this.displayName = displayName;
    }

    public gsmCall.Status getStatus() {
        return this.status;
    }

    public enum Status {
        CONNECTING,
        DIALING,
        RINGING,
        ACTIVE,
        DISCONNECTED,
        UNKNOWN
    }
}
