package JSONWerk;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UnixTimeConversion {

    public static void main(String[] args) {
        // Unix timestamp in milliseconds
        long unixTimestamp = 1725595200000L;

        // Convert Unix timestamp to Date
        Date date = new Date(unixTimestamp);

        // Format the date to "yyyy-MM-dd HH:mm:ss"
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateFormat.format(date);

        // Print the formatted date
        System.out.println("Human-readable date: " + formattedDate);
    }
}