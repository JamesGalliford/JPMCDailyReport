package uk.co.jpmorgan.dailyreporting.model.reporting;

import uk.co.jpmorgan.dailyreporting.model.Instruction;
import uk.co.jpmorgan.dailyreporting.util.LineFormatter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static java.util.Collections.reverseOrder;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.reducing;

public class DailyTotalOutgoingReport extends Report{

    public DailyTotalOutgoingReport() {
        this.setTitle("Daily Total Outgoing");
    }

    @Override
    public StringBuilder getReportContents(List<Instruction> instructions) {

        StringBuilder reportOutput = getFormattedTitle();

        Map<LocalDate, BigDecimal> dailyOutgoingTotal = instructions.stream()
                .filter(outgoingPredicate)
                .filter(Instruction::isValidInstruction)
                .collect(groupingBy(Instruction::getSettlementDate,
                        mapping(Instruction::getTotalAmount,
                                reducing(BigDecimal.ZERO, BigDecimal::add))));

        reportOutput
                .append(LineFormatter.twoColumns("Date", "Total Amount"))
                .append("\n------------------------------------------\n");

        dailyOutgoingTotal.entrySet().stream().sorted(reverseOrder(Map.Entry.comparingByKey()))
                .forEach(dateEntry ->
                        reportOutput
                            .append(LineFormatter.twoColumns(formatDateforOutput(dateEntry.getKey()), String.format("%s USD", dateEntry.getValue())))
                            .append("\n")
                );

        reportOutput.append(getFormattedTotal(instructions, outgoingPredicate));

        return reportOutput;
    }
}
