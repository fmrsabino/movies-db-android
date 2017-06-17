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
This application uses [The Movie DB API](https://www.themoviedb.org/documentation/api?language=en). You need to obtain an API key for the application before using it.
Create a file named `project-keys` with the following line `MOVIES_DB_API_KEY = "<YOUR_API_KEY>"`

## MVP/MVI
This project follows the Model-View-Presenter/Model-View-Intent architecture. You can read more about it [here](https://medium.com/@fmrsabino/an-approach-to-mvp-mvi-with-dagger-2-part-1-viewmodel-is-here-bdfc09c8732).
