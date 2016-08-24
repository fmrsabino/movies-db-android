package fmrsabino.moviesdb.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import fmrsabino.moviesdb.MoviesDbApplication;
import fmrsabino.moviesdb.injection.component.ActivityComponent;
import fmrsabino.moviesdb.injection.component.DaggerActivityComponent;
import fmrsabino.moviesdb.injection.module.ActivityModule;

public abstract class BaseActivity extends AppCompatActivity {
    private ActivityComponent activityComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (activityComponent == null) {
            activityComponent = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .applicationComponent(MoviesDbApplication.get(this).getComponent())
                    .build();
        }
        inject();
    }

    @Override
    protected void onStart() {
        super.onStart();
        onViewAttached();
    }

    @Override
    protected void onStop() {
        super.onStop();
        onViewDetached();
    }

    public ActivityComponent getActivityComponent() {
        return activityComponent;
    }

    public void setActivityComponent(ActivityComponent activityComponent) {
        this.activityComponent = activityComponent;
    }

    protected abstract void onViewAttached();
    protected abstract void onViewDetached();
    protected abstract void inject();
}
