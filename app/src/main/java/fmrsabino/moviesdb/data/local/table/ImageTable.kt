package fmrsabino.moviesdb.data.local.table


import android.content.ContentValues
import android.database.Cursor
import fmrsabino.moviesdb.data.model.configuration.Image
import fmrsabino.moviesdb.util.getString

object ImageTable {
    val TABLE_NAME = "config_image"

    val COLUMN_BASE_URL = "base_url"
    val COLUMN_SECURE_BASE_URL = "secure_base_url"
    val COLUMN_BACKDROP_SIZES = "backdrop_sizes"
    val COLUMN_POSTER_SIZES = "poster_sizes"
    val COLUMN_LOGO_SIZES = "logo_sizes"


    val CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_BASE_URL + " TEXT, " +
            COLUMN_SECURE_BASE_URL + " TEXT, " +
            COLUMN_BACKDROP_SIZES + " TEXT, " +
            COLUMN_POSTER_SIZES + " TEXT, " +
            COLUMN_LOGO_SIZES + " TEXT" +
            " ); "

    fun toContentValues(image: Image): ContentValues {
        val values = ContentValues()
        image.baseUrl?.let          { values.put(COLUMN_BASE_URL, image.baseUrl) }
        image.secureBaseUrl?.let    { values.put(COLUMN_SECURE_BASE_URL, image.secureBaseUrl) }
        image.backdropSizes?.reduce { s1, s2 -> s1 + "," + s2 }.let { backdrops ->  values.put(COLUMN_BACKDROP_SIZES, backdrops) }
        image.posterSizes?.reduce   { s1, s2 -> s1 + "," + s2 }.let { posters ->    values.put(COLUMN_POSTER_SIZES, posters) }
        image.logoSizes?.reduce     { s1, s2 -> s1 + "," + s2 }.let { logos ->      values.put(COLUMN_LOGO_SIZES, logos)}
        return values
    }

    fun parseCursor(c: Cursor): Image {
        return Image(
                c.getString(COLUMN_BASE_URL),
                c.getString(COLUMN_SECURE_BASE_URL),
                c.getString(COLUMN_BACKDROP_SIZES)?.split(','),
                c.getString(COLUMN_POSTER_SIZES)?.split(','),
                c.getString(COLUMN_LOGO_SIZES)?.split(','))
    }
}
