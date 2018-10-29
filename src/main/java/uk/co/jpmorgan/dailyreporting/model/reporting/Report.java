package uk.co.jpmorgan.dailyreporting.model.reporting;

import uk.co.jpmorgan.dailyreporting.model.Instruction;
import uk.co.jpmorgan.dailyreporting.util.LineFormatter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.function.Predicate;

public abstract class Report {

    private String title = "";

    private static final String EXPECTED_DATE_STRING = "EEE dd MMM yyyy";

    final static Predicate<Instruction> outgoingPredicate =
            instruction -> instruction.getOperation().equals('B');

    final static Predicate<Instruction> incomingPredicate =
            instruction -> instruction.getOperation().equals('S');

    public abstract StringBuilder getReportContents(List<Instruction> instructions);

    void setTitle(String title) {
        this.title = title;
    }

    StringBuilder getFormattedTitle() {
        StringBuilder formattedTitle = new StringBuilder();
        formattedTitle.append("\n==========================================\n");
        formattedTitle.append(title);
        formattedTitle.append("\n==========================================\n");

        return formattedTitle;
    }

    StringBuilder getFormattedTotal(List<Instruction> instructions, Predicate<Instruction> operationPredicate) {
        BigDecimal total = instructions.stream()
                .filter(operationPredicate)
                .map(Instruction::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        StringBuilder formattedTotal = new StringBuilder();
        formattedTotal.append("\n==========================================\n");
        formattedTotal.append(LineFormatter.twoColumns("Total", String.format("%s USD", total)));
        formattedTotal.append("\n==========================================\n");

        return formattedTotal;
    }

    String formatDateforOutput(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(EXPECTED_DATE_STRING);
        String dateString = "";
        try {
            dateString = formatter.format(date);
        }
        catch (DateTimeParseException ex) {
            System.out.printf("Could not parse date to format %s.%n", EXPECTED_DATE_STRING);
            ex.printStackTrace();
        }
        return dateString;
    }
}
