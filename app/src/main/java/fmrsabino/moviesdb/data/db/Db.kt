package fmrsabino.moviesdb.data.db

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

object Db {
    @Entity(tableName = ImageConfiguration.TABLE_NAME)
    class ImageConfiguration {
        @PrimaryKey @ColumnInfo(name = COL_BASE_URL) var baseUrl: String? = null
        @ColumnInfo(name = COL_SECURE_BASE_URL) var secureBaseUrl: String? = null
        @ColumnInfo(name = COL_BACKDROP_SIZES) var backdropSizes: List<String>? = null
        @ColumnInfo(name = COL_POSTER_SIZES) var posterSizes: List<String>? = null
        @ColumnInfo(name = COL_LOGO_SIZES) var logoSizes: List<String>? = null

        companion object {
            const val TABLE_NAME = "image_configuration"
            const val COL_BASE_URL = "base_url"
            const val COL_SECURE_BASE_URL = "secure_base_url"
            const val COL_BACKDROP_SIZES = "backdrop_sizes"
            const val COL_POSTER_SIZES = "poster_sizes"
            const val COL_LOGO_SIZES = "logo_sizes"
        }
    }
}