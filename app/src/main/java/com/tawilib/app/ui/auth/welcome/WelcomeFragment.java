package com.tawilib.app.ui.auth.welcome;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tawilib.app.R;
import com.tawilib.app.ui.BaseFragment;
import com.tawilib.app.ui.auth.AuthFragmentNavListener;
import com.tawilib.app.ui.auth.signin.SignInFragment;
import com.tawilib.app.ui.auth.signup.SignUpFragment;

import butterknife.BindView;

public class WelcomeFragment extends BaseFragment {

    private static final String TAG = "WelcomeFragment";

    @BindView(R.id.btn_sign_in)
    Button btnSignIn;

    @BindView(R.id.btn_sign_up)
    Button btnSignUp;

    private AuthFragmentNavListener listener;

    public WelcomeFragment() {}

    public WelcomeFragment(AuthFragmentNavListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        btnSignIn.setOnClickListener(v -> {
            navigate(new SignInFragment());
        });
        btnSignUp.setOnClickListener(v -> {
            navigate(new SignUpFragment());
        });
    }

    private void navigate(BaseFragment fragment) {
        if (listener != null) {
            listener.onNavigate(fragment);
        }
    }

}