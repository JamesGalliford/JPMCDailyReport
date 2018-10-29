package uk.co.jpmorgan.dailyreporting.service;

import uk.co.jpmorgan.dailyreporting.model.Instruction;
import uk.co.jpmorgan.dailyreporting.model.reporting.*;

import java.util.List;

public class ReportService {

    private List<Instruction> instructions;

    public ReportService (List<Instruction> input){
        this.instructions = input;
    }

    public void printAllReports(){
        printDailyTotalIncomingReport();
        printDailyTotalOutgoingReport();
        printDailyRankedEntityIncomingReport();
        printDailyRankedEntityOutgoingReport();
        printTotalRankedEntityIncomingReport();
        printTotalRankedEntityOutgoingReport();
    }

    public void printDailyRankedEntityIncomingReport(){
        Report dailyRankedEntityIncomingReport = new DailyRankedEntityIncomingReport();
        System.out.println(dailyRankedEntityIncomingReport.getReportContents(instructions).toString());
    }

    public void printDailyRankedEntityOutgoingReport(){
        Report dailyRankedEntityOutgoingReport = new DailyRankedEntityOutgoingReport();
        System.out.println(dailyRankedEntityOutgoingReport.getReportContents(instructions).toString());
    }

    public void printDailyTotalIncomingReport(){
        Report dailyTotalIncomingReport = new DailyTotalIncomingReport();
        System.out.println(dailyTotalIncomingReport.getReportContents(instructions).toString());
    }

    public void printDailyTotalOutgoingReport(){
        Report dailyTotalOutgoingReport = new DailyTotalOutgoingReport();
        System.out.println(dailyTotalOutgoingReport.getReportContents(instructions).toString());
    }

    public void printTotalRankedEntityIncomingReport(){
        Report totalRankedEntityIncomingReport = new TotalRankedEntityIncomingReport();
        System.out.println(totalRankedEntityIncomingReport.getReportContents(instructions).toString());
    }

    public void printTotalRankedEntityOutgoingReport(){
        Report totalRankedEntityOutgoingReport = new TotalRankedEntityOutgoingReport();
        System.out.println(totalRankedEntityOutgoingReport.getReportContents(instructions).toString());
    }
}
