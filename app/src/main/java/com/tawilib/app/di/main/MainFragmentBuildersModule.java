package com.tawilib.app.di.main;


import com.tawilib.app.ui.main.add.AddFragment;
import com.tawilib.app.ui.main.list.ListFragment;
import com.tawilib.app.ui.main.update.UpdateFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract ListFragment contributeListFragment();

    @ContributesAndroidInjector
    abstract AddFragment contributeAddFragment();

    @ContributesAndroidInjector
    abstract UpdateFragment contributeUpdateFragment();
}