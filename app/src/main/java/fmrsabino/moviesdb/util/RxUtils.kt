package fmrsabino.moviesdb.util

import hu.akarnokd.rxjava.interop.RxJavaInterop
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

fun <T> rx.Observable<T>.toRx2(): Observable<T> = RxJavaInterop.toV2Observable(this)

fun <T> Observable<T>.debounceExceptFirst(timeout: Long, unit: TimeUnit) =
    this.publish { items -> items.take(1).concatWith(items.skip(1).debounce(timeout, unit)) }
