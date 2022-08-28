package com.tawilib.app.di.main;

import androidx.lifecycle.ViewModel;

import com.tawilib.app.di.ViewModelKey;
import com.tawilib.app.ui.main.add.AddViewModel;
import com.tawilib.app.ui.main.list.ListViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class MainViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(AddViewModel.class)
    public abstract ViewModel bindAddViewModel(AddViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ListViewModel.class)
    public abstract ViewModel bindListViewModel(ListViewModel viewModel);

}
