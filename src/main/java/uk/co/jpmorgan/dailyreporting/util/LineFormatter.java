package uk.co.jpmorgan.dailyreporting.util;

public class LineFormatter {

    public static String twoColumns(String left, String right){
        return String.format("%-20s| %20s", left, right);
    }

    public static String threeColumns(String left, String middle, String right) {
        return String.format("%-15s| %-8s| %15s ", left, middle, right);
    }

}
