package fmrsabino.moviesdb.util;


import android.database.Cursor;

public class CursorHelper {
    //https://developer.android.com/reference/android/database/CursorWindow.html#getLong(int, int)
    public static final long NULL_VALUE = 0L;

    public static String getString(Cursor c, String columnName) {
        return c.getString(c.getColumnIndexOrThrow(columnName));
    }

    public static long getLong(Cursor c, String columnName) {
        return c.getLong(c.getColumnIndexOrThrow(columnName));
    }

    public static int getInt(Cursor c, String columnName) {
        return c.getInt(c.getColumnIndexOrThrow(columnName));
    }
}
