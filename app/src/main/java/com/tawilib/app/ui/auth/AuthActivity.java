package com.tawilib.app.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.ViewModelProvider;

import com.tawilib.app.R;
import com.tawilib.app.ui.BaseActivity;
import com.tawilib.app.ui.BaseFragment;
import com.tawilib.app.ui.auth.welcome.WelcomeFragment;
import com.tawilib.app.ui.main.MainActivity;
import com.tawilib.app.util.AppUtils;
import com.tawilib.app.viewmodels.ViewModelProviderFactory;

import javax.inject.Inject;

public class AuthActivity extends BaseActivity implements AuthFragmentNavListener {

    private static final String TAG = "AuthActivity";

    @Inject
    ViewModelProviderFactory providerFactory;

    private AuthViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        viewModel = new ViewModelProvider(this, providerFactory).get(AuthViewModel.class);
        viewModel.getFirebaseUser().observe(this, resource -> {
            switch (resource.status) {
                case SUCCESS: {
                    isAuthenticated();
                    break;
                }
            }
        });
        viewModel.authenticate();

        showWelcomeFragment();
    }

    private void showWelcomeFragment() {
        AppUtils.replaceFragment(this, new WelcomeFragment(this), R.id.fragment_view);
    }

    private void isAuthenticated(){
        Log.d(TAG, "isAuthenticated: login successful!");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onNavigate(BaseFragment fragment) {
        AppUtils.replaceFragment(this, fragment, R.id.fragment_view);
    }
}