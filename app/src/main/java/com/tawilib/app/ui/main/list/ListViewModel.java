package com.tawilib.app.ui.main.list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tawilib.app.data.model.Book;
import com.tawilib.app.ui.common.Resource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ListViewModel extends ViewModel {

    private MutableLiveData<Resource<List<Book>>> books;

    @Inject
    public ListViewModel() {
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
}
