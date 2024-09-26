package DateUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatterFuncs {

    public static void main(String[] args) {
        String input = "2025-04-06 00:00:00";
        String inputFormat = "yyyy-MM-dd HH:mm:ss";
        String outputFormat = "MM/dd/yyyy";
        String output = formatDate(input, inputFormat, outputFormat);
        System.out.println("Input: " + input);
        System.out.println("Output: " + output);
    }

    public static String formatDate(String dateString, String inputFormat, String outputFormat) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(inputFormat);
        LocalDateTime dateTime = LocalDateTime.parse(dateString, inputFormatter);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(outputFormat);
        return dateTime.format(outputFormatter);
    }
}
