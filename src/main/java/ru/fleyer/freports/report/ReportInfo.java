/*
 * Decompiled with CFR 0.150.
 */
package ru.fleyer.freports.report;

public class ReportInfo {
    private long startTime;
    private String reason;
    private String sender;

    public ReportInfo(String sender, String reason, long startTime) {
        this.sender = sender;
        this.reason = reason;
        this.startTime = startTime;
    }

    public long getTime() {
        return (System.currentTimeMillis() - this.startTime) / 1000L;
    }

    public String getSender() {
        return this.sender;
    }

    public String getReason() {
        return this.reason;
    }
}

