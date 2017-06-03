package fmrsabino.moviesdb.data.db

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

object Db {
    @Entity(tableName = "image_configuration")
    class ImageConfiguration {
        @PrimaryKey @ColumnInfo(name = "base_url") var baseUrl: String? = null
        @ColumnInfo(name = "secure_base_url") var secureBaseUrl: String? = null
        @ColumnInfo(name = "backdrop_sizes") var backdropSizes: List<String>? = null
        @ColumnInfo(name = "poster_sizes") var posterSizes: List<String>? = null
        @ColumnInfo(name = "logo_sizes") var logoSizes: List<String>? = null
    }
}