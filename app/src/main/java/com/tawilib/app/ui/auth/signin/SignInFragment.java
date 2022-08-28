package com.tawilib.app.ui.auth.signin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tawilib.app.R;
import com.tawilib.app.ui.BaseFragment;
import com.tawilib.app.ui.auth.AuthFragmentNavListener;

public class SignInFragment extends BaseFragment {

    private AuthFragmentNavListener listener;

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
    }

    private void navigate(BaseFragment fragment) {
        if (listener != null) {
            listener.onNavigate(fragment);
        }
    }
}