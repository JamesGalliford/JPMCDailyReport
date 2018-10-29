package uk.co.jpmorgan.dailyreporting.model;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.List;

import static java.time.DayOfWeek.*;

public class Instruction {

    private static final List<DayOfWeek> GLOBAL_TRADING_WEEK = Arrays.asList(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY);
    private static final List<DayOfWeek> ALTERNATE_TRADING_WEEK = Arrays.asList(SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY);
    private static final List<String> ALTERNATE_TRADING_CURRENCIES = Arrays.asList("AED", "SAR");
    private static final String EXPECTED_DATE_STRING = "dd MMM yyyy";

    private String entity;
    private Character operation;
    private BigDecimal exchangeRate;
    private String currency;
    private LocalDate instructionDate;
    private LocalDate settlementDate;
    private BigDecimal units;
    private BigDecimal unitPrice;
    private BigDecimal totalAmount;

    private Boolean validInstruction;


    public Instruction(String entity, Character operation, BigDecimal exchangeRate, String currency, String instructionDate,
                       String settlementDate, BigDecimal units, BigDecimal unitPrice) {

        this.validInstruction = true;
        this.entity = entity;
        this.operation = operation;
        if (exchangeRate.compareTo(BigDecimal.ZERO) > 0) {
            this.exchangeRate = exchangeRate;
        }
        else{
            this.exchangeRate = BigDecimal.valueOf(0.0);
            this.validInstruction = false;;
        }
        this.currency = currency;
        this.instructionDate = formatDate(instructionDate);
        this.settlementDate = findNextWorkingDay(settlementDate);
        if (units.compareTo(BigDecimal.ZERO) > 0) {
            this.units = units;
        }
        else{
            this.exchangeRate = BigDecimal.valueOf(0.0);
            this.validInstruction = false;
        }

        if (unitPrice.compareTo(BigDecimal.ZERO) > 0) {
            this.unitPrice = unitPrice;
        }
        else{
            this.exchangeRate = BigDecimal.valueOf(0.0);
            this.validInstruction = false;
        }

        setTotalAmount();

        if (this.instructionDate.isAfter(this.settlementDate)){
            this.validInstruction = false;
        }
    }

    public String getEntity(){
        return entity;
    }

    public Character getOperation(){
        return operation;
    }

    public LocalDate getInstructionDate(){
        return instructionDate;
    }

    public LocalDate getSettlementDate(){
        return settlementDate;
    }

    public BigDecimal getTotalAmount(){
        return totalAmount;
    }

    public boolean isValidInstruction() {
        return validInstruction;
    }

    private void setTotalAmount() {
        this.totalAmount =  unitPrice
                            .multiply(units)
                            .multiply(exchangeRate)
                            .setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }

    private LocalDate formatDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(EXPECTED_DATE_STRING);
        LocalDate date;
        try {
             date = LocalDate.parse(dateString, formatter);
        }
        catch (DateTimeParseException ex) {
            System.out.printf("%s is not a valid date in format %s.%n", dateString, EXPECTED_DATE_STRING);
            date = LocalDate.parse("01 Jan 1900", formatter);
            this.validInstruction = false;
        }
        return date;
    }

    private LocalDate findNextWorkingDay(String dateString) {
        LocalDate settlementDate = formatDate(dateString);

        if (validInstruction) {
            if(ALTERNATE_TRADING_CURRENCIES.contains(currency.toUpperCase())) {
                if(!ALTERNATE_TRADING_WEEK.contains(settlementDate.getDayOfWeek())) {
                    settlementDate = settlementDate.with(TemporalAdjusters.next(ALTERNATE_TRADING_WEEK.get(0)));
                }
            } else {
                if(!GLOBAL_TRADING_WEEK.contains(settlementDate.getDayOfWeek())) {
                    settlementDate = settlementDate.with(TemporalAdjusters.next(GLOBAL_TRADING_WEEK.get(0)));
                }
            }
        }

        return settlementDate;
    }
}
