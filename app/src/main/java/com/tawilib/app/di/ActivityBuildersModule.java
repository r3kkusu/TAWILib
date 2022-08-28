package com.tawilib.app.di;

import com.tawilib.app.di.auth.AuthFragmentBuildersModule;
import com.tawilib.app.di.auth.AuthModule;
import com.tawilib.app.di.auth.AuthScope;
import com.tawilib.app.di.auth.AuthViewModelsModule;
import com.tawilib.app.ui.auth.AuthActivity;
import com.tawilib.app.ui.main.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule  {
    @AuthScope
    @ContributesAndroidInjector(
            modules = { AuthViewModelsModule.class, AuthModule.class, AuthFragmentBuildersModule.class })
    abstract AuthActivity contributeAuthActivity();

    @AuthScope
    @ContributesAndroidInjector(
            modules = {  })
    abstract MainActivity contributeMainActivity();
}
