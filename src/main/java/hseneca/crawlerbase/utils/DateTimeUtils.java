package hseneca.crawlerbase.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class DateTimeUtils {
    public static String DATE_FORMAT_INPUT_TYPE_1 = "yyyy-MM-dd";
    public static String DATE_FORMAT_INPUT_TYPE_2 = "dd MMM yyyy";
    public static LocalDate convert(String dateStr) {
        if (dateStr == null ){
            return null;
        }
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT_INPUT_TYPE_1);
        return LocalDate.parse(dateStr, inputFormatter);
    }

    public static LocalDate convert2(String dateStr) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT_INPUT_TYPE_2);
        return LocalDate.parse(dateStr, inputFormatter);
    }

}
