package com.tawilib.app.ui.main.list;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.tawilib.app.R;
import com.tawilib.app.ui.BaseFragment;
import com.tawilib.app.ui.common.FragmentNavListener;
import com.tawilib.app.ui.common.adapter.BooksAdapter;
import com.tawilib.app.viewmodels.ViewModelProviderFactory;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;

public class ListFragment extends BaseFragment {

    private static final String TAG = "ListFragment";

    @Inject
    ViewModelProviderFactory providerFactory;

    @BindView(R.id.list_view)
    RecyclerView listView;

    @BindView(R.id.layout_load)
    FrameLayout layoutLoad;

    private ListViewModel viewModel;

    public ListFragment() {
        // Required empty public constructor
    }

    public ListFragment(FragmentNavListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BooksAdapter booksAdapter = new BooksAdapter(getContext(), new ArrayList<>());

        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        listView.setAdapter(booksAdapter);

        viewModel = new ViewModelProvider(this, providerFactory).get(ListViewModel.class);
        viewModel.getBooks().observe(getActivity(), resource -> {
            layoutLoad.setVisibility(View.GONE);
            switch (resource.status) {
                case SUCCESS: {
                    booksAdapter.setBooks(resource.data);
                    booksAdapter.notifyDataSetChanged();
                    break;
                }

                case ERROR:
                    break;

                case LOADING: {
                    layoutLoad.setVisibility(View.VISIBLE);
                    break;
                }
            }
        });
        viewModel.loadBooks();
    }
}