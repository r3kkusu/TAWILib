package com.tawilib.app.ui.main;

import android.os.Bundle;

import com.tawilib.app.R;
import com.tawilib.app.ui.BaseActivity;
import com.tawilib.app.ui.BaseFragment;
import com.tawilib.app.ui.common.FragmentNavListener;
import com.tawilib.app.ui.main.list.ListFragment;
import com.tawilib.app.util.AppUtils;

public class MainActivity extends BaseActivity implements FragmentNavListener {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showListFragment();
    }

    @Override
    public void onNavigate(BaseFragment fragment) {
        AppUtils.replaceFragment(this, fragment, R.id.layout_fragment);
    }

    private void showListFragment() {
        AppUtils.replaceFragment(this, new ListFragment(this), R.id.layout_fragment);
    }
}