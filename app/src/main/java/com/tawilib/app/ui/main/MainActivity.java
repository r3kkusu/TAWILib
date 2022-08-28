package com.tawilib.app.ui.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import com.tawilib.app.R;
import com.tawilib.app.ui.BaseActivity;
import com.tawilib.app.ui.BaseFragment;
import com.tawilib.app.ui.auth.AuthActivity;
import com.tawilib.app.ui.common.FragmentNavListener;
import com.tawilib.app.ui.main.list.ListFragment;
import com.tawilib.app.ui.main.list.ListViewModel;
import com.tawilib.app.util.AppUtils;
import com.tawilib.app.viewmodels.ViewModelProviderFactory;

import javax.inject.Inject;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements FragmentNavListener {

    private static final String TAG = "MainActivity";

    @Inject
    ViewModelProviderFactory providerFactory;

    @BindView(R.id.btn_logout)
    ImageButton btnLogout;

    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this, providerFactory).get(MainViewModel.class);
        viewModel.getSignInStatus().observe(this, status -> {
            if (!status) {
                welcomeScreen();
            }
        });

        btnLogout.setOnClickListener(v -> logout());

        showListFragment();
    }

    @Override
    public void onNavigate(BaseFragment fragment) {
        AppUtils.replaceFragment(this, fragment, R.id.layout_fragment);
    }

    private void showListFragment() {
        AppUtils.replaceFragment(this, new ListFragment(this), R.id.layout_fragment);
    }

    private void logout() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string._continue))
                .setMessage(getString(R.string.are_you_sure_you_logout))
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    viewModel.logout();
                })
                .setNegativeButton(R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void welcomeScreen() {
        Log.d(TAG, "logout!");

        AppUtils.toastMessage(this, "Logout!", Toast.LENGTH_LONG);

        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
        finish();
    }
}