package fmrsabino.moviesdb.data.model

import fmrsabino.moviesdb.data.db.Db
import fmrsabino.moviesdb.data.remote.Network

fun Network.ImageConfiguration.toDb(): Db.ImageConfiguration {
    val dbModel = Db.ImageConfiguration()
    dbModel.baseUrl = baseUrl
    dbModel.secureBaseUrl = secureBaseUrl
    dbModel.backdropSizes = backdropSizes
    dbModel.posterSizes = posterSizes
    dbModel.logoSizes = logoSizes
    return dbModel
}

fun Db.ImageConfiguration.fromDb(): ImageConfiguration {
    return ImageConfiguration(baseUrl, secureBaseUrl, backdropSizes, posterSizes, logoSizes)
}

fun ImageConfiguration.toDb(): Db.ImageConfiguration {
    val dbModel = Db.ImageConfiguration()
    dbModel.baseUrl = baseUrl
    dbModel.secureBaseUrl = secureBaseUrl
    dbModel.backdropSizes = backdropSizes
    dbModel.posterSizes = posterSizes
    dbModel.logoSizes = logoSizes
    return dbModel
}

fun Network.Movie.fromNetwork(): Movie =
        Movie(id, backdropPath, posterPath, releaseDate, imdbId, title, tagline, overview)