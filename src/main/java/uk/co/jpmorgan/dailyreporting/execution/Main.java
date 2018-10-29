package uk.co.jpmorgan.dailyreporting.execution;

import uk.co.jpmorgan.dailyreporting.model.Instruction;
import uk.co.jpmorgan.dailyreporting.util.InstructionReader;
import uk.co.jpmorgan.dailyreporting.service.ReportService;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        InstructionReader reader = new InstructionReader();
        List<Instruction> input = reader.readInstructions("instructions.json");
        ReportService reportService = new ReportService(input);
        reportService.printAllReports();
    }
}
