package fmrsabino.moviesdb.util;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class DateTypeAdapter {

    private static final String[] formats = {
            "yyyy-MM-dd"
    };

    @ToJson public String toJson(DateTime dateTime) {
        return dateTime.toString();
    }

    @FromJson DateTime fromJson(String date) {
        for (String format : formats) {
            try {
                return DateTimeFormat.forPattern(format).parseDateTime(date);
            } catch (IllegalArgumentException e) {}
        }
        return null;
    }
}
