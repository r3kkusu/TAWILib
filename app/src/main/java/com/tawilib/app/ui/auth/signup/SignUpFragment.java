package com.tawilib.app.ui.auth.signup;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.tawilib.app.R;
import com.tawilib.app.ui.BaseActivity;
import com.tawilib.app.ui.BaseFragment;
import com.tawilib.app.ui.auth.AuthFragmentNavListener;
import com.tawilib.app.ui.auth.welcome.WelcomeFragment;
import com.tawilib.app.util.AppUtils;
import com.tawilib.app.viewmodels.ViewModelProviderFactory;

import javax.inject.Inject;

import butterknife.BindView;

public class SignUpFragment extends BaseFragment {

    private static final String TAG = "SignUpFragment";

    @Inject
    ViewModelProviderFactory providerFactory;

    @BindView(R.id.btn_back)
    TextView txtBack;

    @BindView(R.id.txt_email)
    EditText txtEmail;

    @BindView(R.id.txt_password)
    EditText txtPassword;

    @BindView(R.id.txt_conf_password)
    EditText txtConfPassword;

    @BindView(R.id.btn_sign_up)
    Button btnSignUp;

    @BindView(R.id.layout_load)
    FrameLayout layoutLoad;

    @BindView(R.id.layout_root)
    FrameLayout layoutRoot;

    private AuthFragmentNavListener listener;

    private SignUpViewModel viewModel;

    public SignUpFragment() {
        // Required empty public constructor
    }

    public SignUpFragment(AuthFragmentNavListener listener) {
        this.listener = listener;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this, providerFactory).get(SignUpViewModel.class);
        viewModel.getFirebaseUser().observe(getActivity(), resource -> {
            layoutLoad.setVisibility(View.GONE);
            switch (resource.status) {
                case SUCCESS: {
                    registrationSuccessful();
                    break;
                }

                case ERROR: {
                    Snackbar snackbar = Snackbar
                            .make(layoutRoot, resource.message, Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.retry), view1 -> register());

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

        btnSignUp.setOnClickListener(v -> register());
    }

    private void register() {

        AppUtils.hideKeyboard((BaseActivity) getActivity());

        boolean isValid = isValidString(txtEmail)
                && isValidString(txtPassword)
                && isValidString(txtConfPassword)
                && isPasswordsValid();

        if (isValid) {
            String username = txtEmail.getText().toString();
            String password = txtPassword.getText().toString();
            viewModel.register(username, password);
        }

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

    public boolean isPasswordsValid() {
        String password = txtPassword.getText().toString();
        String confPassword = txtConfPassword.getText().toString();
        boolean isValid = password.equalsIgnoreCase(confPassword);
        if (isValid) {
            txtConfPassword.setBackground(getResources().getDrawable(R.drawable.shape_text));
        } else {
            txtConfPassword.setBackground(getResources().getDrawable(R.drawable.shape_text_error));
        }
        return isValid;
    }

    private void registrationSuccessful() {

        AppUtils.toastMessage((BaseActivity) getActivity(), getString(R.string.registration_successful), Toast.LENGTH_LONG);
        viewModel.logout();

        navigate(new WelcomeFragment(listener));
    }

    private void navigate(BaseFragment fragment) {
        if (listener != null) {
            listener.onNavigate(fragment);
        }
    }
}