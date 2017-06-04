package fmrsabino.moviesdb.data.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Flowable

@Dao
interface ImageConfigurationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertImageConfiguration(imageConfiguration: Db.ImageConfiguration)

    @Query("SELECT * FROM ${Db.ImageConfiguration.TABLE_NAME} LIMIT 1")
    fun getImageConfiguration(): Flowable<Db.ImageConfiguration>
}