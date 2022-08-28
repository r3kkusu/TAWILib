package com.tawilib.app.util;

import android.util.Log;

import androidx.fragment.app.FragmentTransaction;

import com.tawilib.app.ui.BaseActivity;

import dagger.android.support.DaggerFragment;

public class AppUtils {

    private static final String TAG = "AppUtils";

    public static void replaceFragment(BaseActivity activity, DaggerFragment fragment, int fragmentView) {
        Log.d(TAG, "replaceFragment: " + fragment.getClass().getName());
        FragmentTransaction transaction = activity.getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(fragmentView, fragment);
        transaction.commit();
    }
}
