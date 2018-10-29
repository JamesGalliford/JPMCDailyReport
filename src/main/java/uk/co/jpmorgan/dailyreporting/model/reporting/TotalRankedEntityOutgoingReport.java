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

public class TotalRankedEntityOutgoingReport extends Report{

    public TotalRankedEntityOutgoingReport() {
        this.setTitle("All-Time Ranked Entities by Outgoing");
    }

    @Override
    public StringBuilder getReportContents(List<Instruction> instructions) {

        StringBuilder reportOutput = getFormattedTitle();

        Map<String, BigDecimal> dailyOutgoingTotal = instructions.stream()
                .filter(outgoingPredicate)
                .filter(Instruction::isValidInstruction)
                .collect(groupingBy(Instruction::getEntity,
                        mapping(Instruction::getTotalAmount,
                                reducing(BigDecimal.ZERO, BigDecimal::add))));

        reportOutput
                .append(LineFormatter.twoColumns("Entity", "Total Amount"))
                .append("\n------------------------------------------\n");

        dailyOutgoingTotal.entrySet().stream().sorted(reverseOrder(Map.Entry.comparingByValue()))
                .forEach(dateEntry ->
                        reportOutput
                            .append(LineFormatter.twoColumns(dateEntry.getKey(), String.format("%s USD", dateEntry.getValue())))
                            .append("\n")
                );

        reportOutput.append(getFormattedTotal(instructions, outgoingPredicate));

        return reportOutput;
    }

}
