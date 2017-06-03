package fmrsabino.moviesdb.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters

@Database(version = 1, entities = arrayOf(Db.ImageConfiguration::class))
@TypeConverters(DbConverters::class)
abstract class MoviesDb : RoomDatabase() {
    companion object {
        const val DB_NAME = "movies-db"
    }

    abstract fun imageConfigurationDao(): ImageConfigurationDao
}
