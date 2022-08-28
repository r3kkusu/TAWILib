package com.tawilib.app.ui.main.add;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tawilib.app.R;
import com.tawilib.app.ui.BaseFragment;
import com.tawilib.app.viewmodels.ViewModelProviderFactory;

import javax.inject.Inject;

public class AddFragment extends BaseFragment {

    @Inject
    ViewModelProviderFactory providerFactory;

    public AddFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false);
    }
}