package com.tawilib.app.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tawilib.app.ui.common.FragmentNavListener;

import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;

public class BaseFragment extends DaggerFragment {

    protected FragmentNavListener listener;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
    }

    protected void navigate(BaseFragment fragment) {
        if (listener != null) {
            listener.onNavigate(fragment);
        }
    }
}
