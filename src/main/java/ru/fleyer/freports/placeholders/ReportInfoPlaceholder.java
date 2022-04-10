package ru.fleyer.freports.placeholders;

import com.rainchat.raingui.utils.placeholder.BaseReplacements;
import ru.fleyer.freports.report.Report;
import ru.fleyer.freports.report.ReportInfo;

public class ReportInfoPlaceholder extends BaseReplacements<ReportInfo> {

    private ReportInfo report;

    public ReportInfoPlaceholder(ReportInfo report) {
        super("reportinfo_");
        this.report = report;
    }


    protected String getReplacement(String base, String fullKey) {

        return switch (base) {
            case "reason" -> report.getReason();
            case "name" -> report.getSender();
            case "time" -> String.valueOf(report.getTime());
            default -> null;
        };

    }

    @Override
    public Class<ReportInfo> forClass() {
        return ReportInfo.class;
    }

}
