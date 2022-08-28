package com.tawilib.app.ui.main.list;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.tawilib.app.data.model.Book;
import com.tawilib.app.ui.common.Resource;
import com.tawilib.app.util.JsonUtils;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

public class ListViewModel extends ViewModel {

    private static final String TAG = "ListViewModel";

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private MutableLiveData<Resource<List<Book>>> bookCollection;
    private MutableLiveData<Resource<FirebaseUser>> firebaseUser;

    @Inject
    public ListViewModel(
            FirebaseAuth firebaseAuth,
            DatabaseReference databaseReference,
            StorageReference storageReference
    ) {
        this.firebaseAuth = firebaseAuth;
        this.databaseReference = databaseReference;
        this.storageReference = storageReference;

        this.firebaseUser = new MutableLiveData<>();
        this.bookCollection = new MutableLiveData<>();

        initDataReferenceListener();
    }

    private void initDataReferenceListener() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, List<Book>> map = (HashMap<String, List<Book>>) dataSnapshot.getValue();
                List<Book> bookList = JsonUtils.fromMap(map);
                bookCollection.postValue(Resource.success(bookList));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                bookCollection.postValue(Resource.error(databaseError.getMessage(), null));

            }
        };
        databaseReference.addValueEventListener(postListener);
    }

    public LiveData<Resource<List<Book>>> getBookCollection() {
        return bookCollection;
    }

    // Call only once user is loaded successfully
    public void loadBookCollection() {
        bookCollection.postValue(Resource.loading(null));

        Log.d(TAG, "loadBookCollection: " + firebaseUser.getValue().data.getUid());

        databaseReference.child(firebaseUser.getValue().data.getUid()).get();
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
