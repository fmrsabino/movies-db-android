package fmrsabino.moviesdb.ui.base

import android.support.v7.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    override fun onStart() {
        super.onStart()
        onViewAttached()
    }

    override fun onStop() {
        super.onStop()
        onViewDetached()
    }

    protected abstract fun onViewAttached()
    protected abstract fun onViewDetached()
}
