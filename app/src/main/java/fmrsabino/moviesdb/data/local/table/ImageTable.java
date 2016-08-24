package fmrsabino.moviesdb.data.local.table;


import android.content.ContentValues;
import android.database.Cursor;

import fmrsabino.moviesdb.data.model.configuration.Image;
import fmrsabino.moviesdb.util.CursorHelper;
import fmrsabino.moviesdb.util.Globals;

public abstract class ImageTable {

    public static final String TABLE_NAME = "config_image";

    public static final String COLUMN_BASE_URL = "base_url";
    public static final String COLUMN_SECURE_BASE_URL = "secure_base_url";
    public static final String COLUMN_BACKDROP_SIZES = "backdrop_sizes";
    public static final String COLUMN_POSTER_SIZES = "poster_sizes";
    public static final String COLUMN_LOGO_SIZES = "logo_sizes";


    public static final String CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_BASE_URL + " TEXT, " +
            COLUMN_SECURE_BASE_URL + " TEXT, " +
            COLUMN_BACKDROP_SIZES + " TEXT, " +
            COLUMN_POSTER_SIZES + " TEXT, " +
            COLUMN_LOGO_SIZES + " TEXT" +
            " ); ";

    public static ContentValues toContentValues(Image image) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_BASE_URL, image.baseUrl());
        values.put(COLUMN_SECURE_BASE_URL, image.secureBaseUrl());
        String backdrops = Globals.listToString(image.backdropSizes(), ",");
        if (backdrops != null) { values.put(COLUMN_BACKDROP_SIZES, backdrops); }
        String posters = Globals.listToString(image.backdropSizes(), ",");
        if (posters != null) { values.put(COLUMN_POSTER_SIZES, posters); }
        String logoSizes = Globals.listToString(image.logoSizes(), ",");
        if (logoSizes != null) { values.put(COLUMN_LOGO_SIZES, logoSizes); }
        return values;
    }

    public static Image parseCursor(Cursor c) {
        try {
            return Image.builder()
                    .baseUrl(CursorHelper.getString(c, COLUMN_BASE_URL))
                    .secureBaseUrl(CursorHelper.getString(c, COLUMN_SECURE_BASE_URL))
                    .backdropSizes(Globals.stringToList(CursorHelper.getString(c, COLUMN_BACKDROP_SIZES), ","))
                    .posterSizes(Globals.stringToList(CursorHelper.getString(c, COLUMN_POSTER_SIZES), ","))
                    .logoSizes(Globals.stringToList(CursorHelper.getString(c, COLUMN_LOGO_SIZES), ","))
                    .build();
        } catch (Exception e) { return null; }
    }
}
