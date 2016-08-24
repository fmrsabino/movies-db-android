package fmrsabino.moviesdb.util;

import android.support.annotation.Nullable;

import rx.Subscription;

public class RxUtil {
    public static void unsubscribe(@Nullable Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
