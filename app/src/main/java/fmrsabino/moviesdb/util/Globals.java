package fmrsabino.moviesdb.util;

import android.content.Context;

import com.annimon.stream.Stream;

import java.util.Arrays;
import java.util.List;

public class Globals {
    public static String listToString(List<String> list, String delimiter) {
        if (list == null) { return null; }
        return Stream.of(list)
                .reduce((string1, string2) -> string1 + delimiter + string2)
                .get();
    }

    public static List<String> stringToList(String string, String delimiter) {
        if (string == null) { return null; }
        String[] arr = string.split(delimiter);
        return Arrays.asList(arr);
    }

    public static int dpsToPixels(Context context,int dps) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dps * scale + 0.5);
    }
}
