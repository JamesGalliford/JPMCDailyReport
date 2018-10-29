package uk.co.jpmorgan.dailyreporting.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static java.time.DayOfWeek.*;

class InstructionTest {

    @Test
    void getTotalAmount() {
        Instruction Instruction1 = new Instruction("FOO", 'B', BigDecimal.valueOf(100), "GBP", "26 Oct 2018", "28 Oct 2018", BigDecimal.valueOf(500), BigDecimal.valueOf(150));
        Instruction Instruction2 = new Instruction("FOO", 'B', BigDecimal.valueOf(0.001), "GBP", "26 Oct 2018", "28 Oct 2018", BigDecimal.valueOf(5500), BigDecimal.valueOf(1));
        Instruction Instruction3 = new Instruction("FOO", 'B', BigDecimal.valueOf(1.2345), "GBP", "26 Oct 2018", "28 Oct 2018", BigDecimal.valueOf(450), BigDecimal.valueOf(365));

        assertEquals(Instruction1.getTotalAmount(), BigDecimal.valueOf(7500000.00).setScale(2, BigDecimal.ROUND_HALF_EVEN));
        assertEquals(Instruction2.getTotalAmount(), BigDecimal.valueOf(5.50).setScale(2, BigDecimal.ROUND_HALF_EVEN));
        assertEquals(Instruction3.getTotalAmount(), BigDecimal.valueOf(202766.625).setScale(2, BigDecimal.ROUND_HALF_EVEN));
    }

    @Test
    void isValidInstruction() {
        Instruction validInstruction = new Instruction("FOO", 'B', BigDecimal.valueOf(1.1), "GBP", "26 Oct 2018", "28 Oct 2018", BigDecimal.valueOf(500), BigDecimal.valueOf(150));
        Instruction invalidDate = new Instruction("FOO", 'B', BigDecimal.valueOf(1.1), "GBP", "32 Oct 2018", "26 Oct 2018", BigDecimal.valueOf(500), BigDecimal.valueOf(150));
        Instruction outOfOrderDates = new Instruction("FOO", 'B', BigDecimal.valueOf(1.1), "GBP", "28 Oct 2018", "26 Oct 2018", BigDecimal.valueOf(500), BigDecimal.valueOf(150));
        Instruction negativeExchangeRate = new Instruction("FOO", 'B', BigDecimal.valueOf(-1.1), "GBP", "23 Oct 2018", "26 Oct 2018", BigDecimal.valueOf(500), BigDecimal.valueOf(150));

        assertTrue(validInstruction.isValidInstruction());
        assertFalse(invalidDate.isValidInstruction());
        assertFalse(outOfOrderDates.isValidInstruction());
        assertFalse(negativeExchangeRate.isValidInstruction());
    }

    @Test
    void formatDate() {
        Instruction validDateValidFormat = new Instruction("FOO", 'B', BigDecimal.valueOf(1.1), "GBP", "28 Oct 2018", "26 Oct 2018", BigDecimal.valueOf(500), BigDecimal.valueOf(150));
        Instruction invalidDateValidFormat = new Instruction("FOO", 'B', BigDecimal.valueOf(1.1), "GBP", "32 Oct 2018", "26 Oct 2018", BigDecimal.valueOf(500), BigDecimal.valueOf(150));
        Instruction validDateInvalidFormat = new Instruction("FOO", 'B', BigDecimal.valueOf(1.1), "GBP", "2018-10-28", "26 Oct 2018", BigDecimal.valueOf(500), BigDecimal.valueOf(150));
        Instruction invalidDateInvalidFormat = new Instruction("FOO", 'B', BigDecimal.valueOf(1.1), "GBP", "2018-10-32", "26 Oct 2018", BigDecimal.valueOf(500), BigDecimal.valueOf(150));

        final String EXPECTED_DATE_STRING = "dd MMM yyyy";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(EXPECTED_DATE_STRING);

        assertEquals(validDateValidFormat.getInstructionDate(), LocalDate.parse("28 Oct 2018", formatter));
        assertEquals(invalidDateValidFormat.getInstructionDate(), LocalDate.parse("01 Jan 1900", formatter));
        assertEquals(validDateInvalidFormat.getInstructionDate(), LocalDate.parse("01 Jan 1900", formatter));
        assertEquals(invalidDateInvalidFormat.getInstructionDate(), LocalDate.parse("01 Jan 1900", formatter));
    }


    @Test
    void findNextWorkingDay() {
        final Instruction GlobalWeekDayInstruction = new Instruction("FOO", 'B', BigDecimal.valueOf(1.1), "GBP", "23 Oct 2018", "26 Oct 2018", BigDecimal.valueOf(500), BigDecimal.valueOf(150));
        final Instruction GlobalWeekEndInstruction = new Instruction("FOO", 'B', BigDecimal.valueOf(1.1), "GBP", "23 Oct 2018", "28 Oct 2018", BigDecimal.valueOf(500), BigDecimal.valueOf(150));
        final Instruction AlternateWeekDayInstruction = new Instruction("FOO", 'B', BigDecimal.valueOf(1.1), "AED", "23 Oct 2018", "28 Oct 2018", BigDecimal.valueOf(500), BigDecimal.valueOf(150));
        final Instruction AlternateWeekEndInstruction = new Instruction("FOO", 'B', BigDecimal.valueOf(1.1), "AED", "23 Oct 2018", "26 Oct 2018", BigDecimal.valueOf(500), BigDecimal.valueOf(150));

        assertEquals(GlobalWeekDayInstruction.getSettlementDate().getDayOfWeek(), FRIDAY);
        assertEquals(GlobalWeekEndInstruction.getSettlementDate().getDayOfWeek(), MONDAY);
        assertEquals(AlternateWeekDayInstruction.getSettlementDate().getDayOfWeek(), SUNDAY);
        assertEquals(AlternateWeekEndInstruction.getSettlementDate().getDayOfWeek(), SUNDAY);
    }
}