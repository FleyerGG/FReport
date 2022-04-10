package ru.fleyer.freports.placeholders;

import com.rainchat.raingui.utils.placeholder.BaseReplacements;
import ru.fleyer.freports.report.Report;

public class ReportPlaceholder extends BaseReplacements<Report> {

    private Report report;

    public ReportPlaceholder(Report report) {
        super("report_");
        this.report = report;
    }


    protected String getReplacement(String base, String fullKey) {

        return switch (base) {
            case "name" -> report.getName();
            case "size" -> String.valueOf(report.getReports().size());
            default -> null;
        };

    }

    @Override
    public Class<Report> forClass() {
        return Report.class;
    }
}
