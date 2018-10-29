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

public class DailyRankedEntityIncomingReport extends Report{

    public DailyRankedEntityIncomingReport() {
        this.setTitle("Daily Ranked Entities by Incoming");
    }

    @Override
    public StringBuilder getReportContents(List<Instruction> instructions) {

        StringBuilder reportOutput = getFormattedTitle();

        Map<LocalDate, Map<String, BigDecimal>> dailyOutgoingTotal = instructions.stream()
                .filter(incomingPredicate)
                .filter(Instruction::isValidInstruction)
                .collect(groupingBy(Instruction::getSettlementDate,
                        groupingBy(Instruction::getEntity,
                                mapping(Instruction::getTotalAmount,
                                        reducing(BigDecimal.ZERO, BigDecimal::add)))));

        reportOutput
                .append(LineFormatter.threeColumns("Date", "Entity", "Total Amount"))
                .append("\n------------------------------------------\n");

        dailyOutgoingTotal.entrySet().stream().sorted(reverseOrder(Map.Entry.comparingByKey()))
                .forEach(dateEntry -> dateEntry.getValue().entrySet().stream().sorted(reverseOrder(Map.Entry.comparingByValue()))
                        .forEach(outgoingEntry ->
                                reportOutput
                                .append(LineFormatter.threeColumns(formatDateforOutput(dateEntry.getKey()), outgoingEntry.getKey(), String.format("%s USD", outgoingEntry.getValue())))
                                .append("\n")
                        )
                );

        reportOutput.append(getFormattedTotal(instructions, incomingPredicate));

        return reportOutput;
    }
}
