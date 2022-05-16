package ru.fleyer.freports.spigot.data.report;

import java.util.ArrayList;

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

