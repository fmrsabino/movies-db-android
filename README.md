# movies-db-android

This is a sample kotlin project to showcase a demo MVP Android project. It shows how different frameworks interact with each other on the MVP architecture.

## Libraries
* RxJava 2 (with Kotlin extensions)
* Retrofit 2
* Dagger 2 (for Dependency Injection)
* SQLBrite (Reactive SQL queries)
* Timber (logging)
* Picasso (for image loading)
* ThreeTenABP (JSR-310)
* JUnit + Mockito (Unit tests)
* Robolectric (Android SDK unit tests)
* Espresso (Android UI tests)

## Setup
This application uses [The Movie DB API](https://www.themoviedb.org/documentation/api?language=en). You need to obtain an API key for the application before using it. Put your API key in [Constants.java](https://github.com/fmrsabino/movies-db-android/blob/master/app/src/main/java/fmrsabino/moviesdb/util/Constants.java)

## MVP
This project follows the Model-View-Presenter architecture. You can read more about it [here](https://github.com/ribot/android-guidelines/blob/master/architecture_guidelines/android_architecture.md).

I've used the Loaders provided by the Android SDK for the presenters because they survive configuration changes. You can read more about it [here](https://medium.com/google-developers/making-loading-data-on-android-lifecycle-aware-897e12760832#.c81zmpcjd).

## Notes
The `RxSchedulersOverrideRule` is from this [project](https://github.com/ribot/android-boilerplate/blob/master/app/src/test/java/uk/co/ribot/androidboilerplate/util/RxSchedulersOverrideRule.java).
