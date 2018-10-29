package uk.co.jpmorgan.dailyreporting.model.reporting;

import uk.co.jpmorgan.dailyreporting.model.Instruction;
import uk.co.jpmorgan.dailyreporting.util.LineFormatter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static java.util.Collections.reverseOrder;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.reducing;

public class TotalRankedEntityIncomingReport extends Report{

    public TotalRankedEntityIncomingReport() {
        this.setTitle("All-Time Ranked Entities by Incoming");
    }

    @Override
    public StringBuilder getReportContents(List<Instruction> instructions) {

        StringBuilder reportOutput = getFormattedTitle();

        Map<String, BigDecimal> dailyIncomingTotal = instructions.stream()
                .filter(incomingPredicate)
                .filter(Instruction::isValidInstruction)
                .collect(groupingBy(Instruction::getEntity,
                        mapping(Instruction::getTotalAmount,
                                reducing(BigDecimal.ZERO, BigDecimal::add))));

        reportOutput
                .append(LineFormatter.twoColumns("Entity", "Total Amount"))
                .append("\n------------------------------------------\n");

        dailyIncomingTotal.entrySet().stream().sorted(reverseOrder(Map.Entry.comparingByValue()))
                .forEach(dateEntry ->
                        reportOutput
                            .append(LineFormatter.twoColumns(dateEntry.getKey(), String.format("%s USD", dateEntry.getValue())))
                            .append("\n")
                );

        reportOutput.append(getFormattedTotal(instructions, incomingPredicate));

        return reportOutput;
    }
}
