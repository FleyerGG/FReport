/*
 * Decompiled with CFR 0.150.
 */
package ru.fleyer.freports.report;

import java.util.ArrayList;
import ru.fleyer.freports.report.ReportInfo;

public class Report {
    private ArrayList<ReportInfo> reports;
    private String playerName;

    public Report(String playerName, ArrayList<ReportInfo> reports) {
        this.playerName = playerName;
        this.reports = reports;
    }

    public ArrayList<ReportInfo> getReports() {
        return this.reports;
    }

    public String getName() {
        return this.playerName;
    }

    public void add(String sender, String reason, long time) {
        this.reports.add(new ReportInfo(sender, reason, time));
    }
}

