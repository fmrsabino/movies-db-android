package fmrsabino.moviesdb.util

import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId

fun LocalDateTime.toDefaultEpochMilli() = atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
fun Long.toDefaultLocalDateTime(): LocalDateTime = Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDateTime()