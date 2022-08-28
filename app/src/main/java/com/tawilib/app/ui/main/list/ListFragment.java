package com.tawilib.app.ui.main.list;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.RequestManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.tawilib.app.R;
import com.tawilib.app.data.model.Book;
import com.tawilib.app.ui.BaseActivity;
import com.tawilib.app.ui.BaseFragment;
import com.tawilib.app.ui.auth.AuthActivity;
import com.tawilib.app.ui.common.FragmentNavListener;
import com.tawilib.app.ui.common.adapter.BooksAdapter;
import com.tawilib.app.ui.main.add.EditBookFragment;
import com.tawilib.app.util.AppUtils;
import com.tawilib.app.viewmodels.ViewModelProviderFactory;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;

public class ListFragment extends BaseFragment implements BooksAdapter.OnClickListener {

    private static final String TAG = "ListFragment";

    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    StorageReference storageReference;

    @Inject
    RequestManager requestManager;

    @BindView(R.id.txt_username)
    TextView txtUsername;

    @BindView(R.id.btn_logout)
    ImageButton btnLogout;

    @BindView(R.id.btn_add_book)
    FloatingActionButton btnAddBook;

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
        booksAdapter.setRequestManager(requestManager);
        booksAdapter.setFirebaseStorage(storageReference);
        booksAdapter.setOnClickListener(this);

        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        listView.setAdapter(booksAdapter);

        viewModel = new ViewModelProvider(this, providerFactory).get(ListViewModel.class);

        viewModel.getFirebaseUser().observe(getActivity(), result -> {

            switch (result.status) {
                case SUCCESS: {
                    prepUserAccount(result.data);
                    break;
                }
            }
        });
        viewModel.loadUser();

        viewModel.getBookCollection().observe(getActivity(), result -> {
            layoutLoad.setVisibility(View.GONE);
            switch (result.status) {
                case SUCCESS: {
                    booksAdapter.setBooks(result.data);
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

        btnLogout.setOnClickListener(v -> logout());


        btnAddBook.setOnClickListener(v -> navigate(new EditBookFragment(listener)));
    }

    @Override
    public void onClick(Book book) {
        navigate(new EditBookFragment(listener, book));
    }

    private void logout() {
        new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string._continue))
                .setMessage(getString(R.string.are_you_sure_you_logout))
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    viewModel.logout();
                })
                .setNegativeButton(R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void prepUserAccount(FirebaseUser firebaseUser) {
        if (firebaseUser == null) {
            welcomeScreen();
        } else {
            txtUsername.setText(firebaseUser.getEmail());
            viewModel.loadBookCollection();
        }
    }

    private void welcomeScreen() {
        Log.d(TAG, "logout!");

        BaseActivity activity = (BaseActivity) getActivity();

        AppUtils.toastMessage(activity, "Logout!", Toast.LENGTH_LONG);

        Intent intent = new Intent(getContext(), AuthActivity.class);
        startActivity(intent);
        activity.finish();
    }
}