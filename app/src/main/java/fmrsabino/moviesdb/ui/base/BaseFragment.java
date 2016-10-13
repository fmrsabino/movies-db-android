package fmrsabino.moviesdb.ui.base;

import android.support.v4.app.Fragment;

public abstract class BaseFragment extends Fragment {
    @Override
    public void onResume() {
        super.onResume();
        onViewAttached();
    }

    @Override
    public void onPause() {
        super.onPause();
        onViewDetached();
    }

    protected abstract void onViewAttached();
    protected abstract void onViewDetached();
}
