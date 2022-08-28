package com.tawilib.app.ui.auth.signin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.tawilib.app.R;
import com.tawilib.app.ui.BaseActivity;
import com.tawilib.app.ui.BaseFragment;
import com.tawilib.app.ui.auth.AuthFragmentNavListener;
import com.tawilib.app.ui.auth.welcome.WelcomeFragment;
import com.tawilib.app.ui.main.MainActivity;
import com.tawilib.app.util.AppUtils;
import com.tawilib.app.viewmodels.ViewModelProviderFactory;

import javax.inject.Inject;

import butterknife.BindView;

public class SignInFragment extends BaseFragment {

    private static final String TAG = "SignInFragment";

    @Inject
    ViewModelProviderFactory providerFactory;

    @BindView(R.id.btn_back)
    TextView txtBack;

    @BindView(R.id.txt_email)
    EditText txtEmail;

    @BindView(R.id.txt_password)
    EditText txtPassword;

    @BindView(R.id.btn_sign_in)
    Button btnSignIn;

    @BindView(R.id.layout_load)
    FrameLayout layoutLoad;

    @BindView(R.id.layout_root)
    FrameLayout layoutRoot;

    private AuthFragmentNavListener listener;

    private SignInViewModel viewModel;

    public SignInFragment() {
        // Required empty public constructor
    }

    public SignInFragment(AuthFragmentNavListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this, providerFactory).get(SignInViewModel.class);
        viewModel.getFirebaseUser().observe(getActivity(), resource -> {
            layoutLoad.setVisibility(View.GONE);
            switch (resource.status) {
                case SUCCESS: {
                    loginSuccessful();
                    break;
                }

                case ERROR: {
                    Snackbar snackbar = Snackbar
                            .make(layoutRoot, resource.message, Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.retry), view1 -> login());

                    snackbar.show();
                    break;
                }

                case LOADING: {
                    layoutLoad.setVisibility(View.VISIBLE);
                    break;
                }
            }
        });

        txtBack.setOnClickListener(v -> navigate(new WelcomeFragment(listener)));

        btnSignIn.setOnClickListener(v -> login());
    }

    private void login() {

        AppUtils.hideKeyboard((BaseActivity) getActivity());

        boolean isValid = isValidString(txtEmail)
                && isValidString(txtPassword);

        if (isValid) {
            String username = txtEmail.getText().toString();
            String password = txtPassword.getText().toString();
            viewModel.login(username, password);
        }

    }

    private void loginSuccessful() {

        BaseActivity activity = (BaseActivity) getActivity();

        AppUtils.toastMessage(activity, getString(R.string.login_successful), Toast.LENGTH_LONG);

        Intent intent = new Intent(activity, MainActivity.class);
        startActivity(intent);

        activity.finish();
    }

    private boolean isValidString(EditText editText) {
        boolean isEmpty = editText.getText().toString().isEmpty();
        if (isEmpty) {
            editText.setBackground(getResources().getDrawable(R.drawable.shape_text_error));
        } else {
            editText.setBackground(getResources().getDrawable(R.drawable.shape_text));
        }

        return !isEmpty;
    }

    private void navigate(BaseFragment fragment) {
        if (listener != null) {
            listener.onNavigate(fragment);
        }
    }
}