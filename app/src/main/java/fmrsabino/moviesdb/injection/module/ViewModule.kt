package fmrsabino.moviesdb.injection.module

import android.content.Context
import dagger.Module
import dagger.Provides
import fmrsabino.moviesdb.injection.scope.ActivityContext
import fmrsabino.moviesdb.injection.scope.ForView

@Module
class ViewModule(val context: Context) {
    @Provides
    @ForView
    @ActivityContext
    fun providesContext() = context
}