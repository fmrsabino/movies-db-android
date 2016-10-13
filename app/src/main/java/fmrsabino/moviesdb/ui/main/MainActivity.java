package fmrsabino.moviesdb.ui.main;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.FrameLayout;

import com.roughike.bottombar.BottomBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import fmrsabino.moviesdb.R;
import fmrsabino.moviesdb.ui.base.BaseActivity;
import fmrsabino.moviesdb.ui.search.SearchFragment;

public class MainActivity extends BaseActivity {
    @BindView(R.id.activity_main_container) FrameLayout container;
    @BindView(R.id.activity_main_bottomBar) BottomBar bottomBar;

    private int selectedTabId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomBar.setOnTabSelectListener(tabId -> {
            if (selectedTabId == tabId) return;
            selectedTabId = tabId;
            Fragment fragment = null;
            switch (tabId) {
                case R.id.tab_search:
                    fragment = new SearchFragment();
                    break;
                case R.id.tab_favorites:
                    break;
            }
            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.activity_main_container, fragment)
                        .commit();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("tabId", selectedTabId);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        selectedTabId = savedInstanceState.getInt("tabId");
    }

    @Override
    protected void onViewAttached() {}

    @Override
    protected void onViewDetached() {}
}
