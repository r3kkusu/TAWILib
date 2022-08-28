package com.tawilib.app.util;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;
import com.tawilib.app.R;
import com.tawilib.app.ui.BaseActivity;
import com.tawilib.app.ui.auth.signup.SignUpFragment;

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

    public static boolean isValidString(Context context, TextView editText) {
        boolean isEmpty = editText.getText().toString().isEmpty();
        isValidField(context, editText, isEmpty);
        return !isEmpty;
    }

    public static void isValidField(Context context, TextView editText, boolean isValid) {
        if (isValid) {
            editText.setBackground(context.getDrawable(R.drawable.shape_text_error));
        } else {
            editText.setBackground(context.getDrawable(R.drawable.shape_text));
        }
    }

    public static boolean isNotEmpty(TextView ...textViews) {

        for (TextView textView: textViews) {
            if (textView.getText().toString().length() > 0)
                return true;
        }

        return false;
    }

    public static void SnackbarMessage(View layout, String message, int length, String retryMsg, View.OnClickListener onClickListener) {
        Snackbar snackbar = Snackbar.make(layout, message, length)
                .setAction(retryMsg, onClickListener);
        snackbar.show();
    }

}
