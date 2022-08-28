package com.tawilib.app.di.auth;

import androidx.lifecycle.ViewModel;

import com.tawilib.app.di.ViewModelKey;
import com.tawilib.app.ui.auth.AuthViewModel;
import com.tawilib.app.ui.auth.signin.SignInViewModel;
import com.tawilib.app.ui.auth.signup.SignUpViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class AuthViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel.class)
    public abstract ViewModel bindAuthViewModel(AuthViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SignUpViewModel.class)
    public abstract ViewModel bindSignUpViewModel(SignUpViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SignInViewModel.class)
    public abstract ViewModel bindSignInViewModel(SignInViewModel viewModel);
}
