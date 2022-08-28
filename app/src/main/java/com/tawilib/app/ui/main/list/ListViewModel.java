package com.tawilib.app.ui.main.list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tawilib.app.data.model.Book;
import com.tawilib.app.ui.common.Resource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ListViewModel extends ViewModel {

    private FirebaseAuth firebaseAuth;

    private MutableLiveData<Resource<List<Book>>> books;
    private MutableLiveData<Resource<FirebaseUser>> firebaseUser;

    @Inject
    public ListViewModel(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
        this.firebaseUser = new MutableLiveData<>();
        this.books = new MutableLiveData<>();
    }

    public LiveData<Resource<List<Book>>> getBooks() {
        return books;
    }

    public void loadBooks() {
        books.postValue(Resource.loading(null));
        List<Book> stub = new ArrayList<>();

        stub.add(new Book());
        stub.add(new Book());
        stub.add(new Book());
        stub.add(new Book());
        stub.add(new Book());

        books.postValue(Resource.success(stub));
    }

    public LiveData<Resource<FirebaseUser>> getFirebaseUser() {
        return firebaseUser;
    }

    public void loadUser() {
        FirebaseUser fbUser = firebaseAuth.getCurrentUser();
        firebaseUser.postValue(Resource.success(fbUser));
    }

    public void logout() {
        this.firebaseAuth.signOut();
        this.firebaseUser.postValue(Resource.success(null));
    }
}
