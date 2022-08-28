package com.tawilib.app.di.auth;

import com.tawilib.app.ui.auth.signin.SignInFragment;
import com.tawilib.app.ui.auth.signup.SignUpFragment;
import com.tawilib.app.ui.auth.welcome.WelcomeFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class AuthFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract WelcomeFragment contributeWelcomeFragment();

    @ContributesAndroidInjector
    abstract SignInFragment contributeSignInFragment();

    @ContributesAndroidInjector
    abstract SignUpFragment contributeSignUpFragment();
}
