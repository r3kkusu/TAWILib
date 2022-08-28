package com.tawilib.app.ui.main.add;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.tawilib.app.data.model.Book;
import com.tawilib.app.ui.common.Resource;

import javax.inject.Inject;

public class EditBookViewModel extends ViewModel {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private MutableLiveData<Resource<Book>> mutableBookData;

    @Inject
    public EditBookViewModel(
            FirebaseAuth firebaseAuth,
            DatabaseReference databaseReference,
            StorageReference storageReference
            ) {
        this.firebaseAuth = firebaseAuth;
        this.storageReference = storageReference;
        this.databaseReference = databaseReference;
        this.mutableBookData = new MutableLiveData<>();
    }

    public LiveData<Resource<Book>> getLiveBookData() {
        return mutableBookData;
    }

    private boolean isAttemptRetry() {
        try {
            Resource<Book> resource = mutableBookData.getValue();
            return resource.data != null && resource.data.getId() != null;
        } catch (Exception e) {}
        return false;
    }

    public void update(Book book, Uri source) {
        String userId = firebaseAuth.getCurrentUser().getUid();

        mutableBookData.postValue(Resource.loading(null));
        databaseReference.child(userId).child(book.getId()).setValue(book, (error, ref) -> {
            if (error == null) {
                uploadImage(book, source);
            } else {
                mutableBookData.postValue(Resource.error(error.getMessage(), null));
            }
        });
    }

    public void delete(Book book) {
        String userId = firebaseAuth.getCurrentUser().getUid();

        mutableBookData.postValue(Resource.loading(null));
        databaseReference.child(userId).child(book.getId()).removeValue((error, ref) -> {
            if (error == null) {
                removeImage(book);
            } else {
                mutableBookData.postValue(Resource.error(error.getMessage(), null));
            }
        });
    }

    public void addBook(Book book, Uri source) {
        String userId = firebaseAuth.getCurrentUser().getUid();
        boolean isAttemptRetry = isAttemptRetry();

        if (isAttemptRetry) {
            // Retry
            update(book, source);
        } else {
            // Add new
            mutableBookData.postValue(Resource.loading(null));
            databaseReference.child(userId).push().setValue(book, (error, ref) -> {
                if (error == null) {
                    book.setId(ref.getKey());
                    uploadImage(book, source);
                } else {
                    mutableBookData.postValue(Resource.error(error.getMessage(), null));
                }
            });
        }
    }

    public StorageReference getStorageReference() {
        return storageReference;
    }

    private void removeImage(Book book) {
        storageReference = storageReference.child(book.getId());
        storageReference.delete().addOnSuccessListener(taskSnapshot -> {
            mutableBookData.postValue(Resource.success(null));
            })
            .addOnFailureListener(error -> {
                mutableBookData.postValue(Resource.error(error.getMessage(), null));
            });;
    }

    private void uploadImage(Book book, Uri source) {

        if (source != null) {

            storageReference = storageReference.child(book.getId());
            storageReference.putFile(source)
                    .addOnSuccessListener(taskSnapshot -> {
                        mutableBookData.postValue(Resource.success(book));
                    })
                    .addOnFailureListener(error -> {
                        mutableBookData.postValue(Resource.error(error.getMessage(), null));
                    });
        } else {
            mutableBookData.postValue(Resource.success(book)); // If image is not uploaded the still emit success
        }

    }

//    public void setDefaultBook(Book book) {
//        mutableBookData.postValue(Resource.success(book));
//    }
}
