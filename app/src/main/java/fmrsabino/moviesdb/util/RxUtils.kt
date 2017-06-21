package fmrsabino.moviesdb.util

import io.reactivex.Observable
import java.util.concurrent.TimeUnit


fun <T> Observable<T>.debounceExceptFirst(timeout: Long, unit: TimeUnit): Observable<T> =
        this.publish { items -> items.take(1).concatWith(items.skip(1).debounce(timeout, unit)) }
