package fmrsabino.moviesdb.ui.base

import android.support.v4.app.Fragment

abstract class BaseFragment : Fragment() {
    override fun onResume() {
        super.onResume()
        onViewAttached()
    }

    override fun onPause() {
        super.onPause()
        onViewDetached()
    }

    protected abstract fun onViewAttached()
    protected abstract fun onViewDetached()
}
