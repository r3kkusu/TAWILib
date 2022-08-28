package com.tawilib.app.ui.main.add;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tawilib.app.data.model.Book;
import com.tawilib.app.ui.common.Resource;

import javax.inject.Inject;

public class AddViewModel extends ViewModel {

    private MutableLiveData<Resource<Book>> book;

    @Inject
    public AddViewModel() {
        this.book = new MutableLiveData<>();
    }

    public LiveData<Resource<Book>> getBook() {
        return book;
    }

    public void addBook() {

    }
}
