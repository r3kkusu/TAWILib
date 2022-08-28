package com.tawilib.app.di;

import com.tawilib.app.di.auth.AuthFragmentBuildersModule;
import com.tawilib.app.di.auth.AuthScope;
import com.tawilib.app.di.auth.AuthViewModelsModule;
import com.tawilib.app.di.main.MainFragmentBuildersModule;
import com.tawilib.app.di.main.MainScope;
import com.tawilib.app.di.main.MainViewModelsModule;
import com.tawilib.app.ui.auth.AuthActivity;
import com.tawilib.app.ui.main.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule  {
    @AuthScope
    @ContributesAndroidInjector(
            modules = { AuthViewModelsModule.class, AuthFragmentBuildersModule.class }
    )
    abstract AuthActivity contributeAuthActivity();

    @MainScope
    @ContributesAndroidInjector(
            modules = { MainViewModelsModule.class, MainFragmentBuildersModule.class }
    )
    abstract MainActivity contributeMainActivity();
}
