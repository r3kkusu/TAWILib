package com.tawilib.app.di.main;


import com.tawilib.app.ui.main.add.EditBookFragment;
import com.tawilib.app.ui.main.list.ListFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract ListFragment contributeListFragment();

    @ContributesAndroidInjector
    abstract EditBookFragment contributeEditBookFragment();
}
