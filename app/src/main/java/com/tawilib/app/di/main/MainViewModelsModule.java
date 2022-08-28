package com.tawilib.app.di.main;

import androidx.lifecycle.ViewModel;

import com.tawilib.app.di.ViewModelKey;
import com.tawilib.app.ui.main.MainViewModel;
import com.tawilib.app.ui.main.list.ListViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class MainViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    public abstract ViewModel bindMainViewModel(MainViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ListViewModel.class)
    public abstract ViewModel bindListViewModel(ListViewModel viewModel);

}
