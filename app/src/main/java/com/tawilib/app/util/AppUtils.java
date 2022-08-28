package com.tawilib.app.util;

import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

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

    public static void hideKeyboard(BaseActivity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(BaseActivity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void toastMessage(BaseActivity activity, String message, int length) {
        Toast toast = Toast.makeText( activity, message, length);
        toast.show();
    }
}
