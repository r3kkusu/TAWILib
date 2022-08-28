package com.tawilib.app.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

public class MainViewModel extends ViewModel {

    private static final String TAG = "MainViewModel";

    private FirebaseAuth firebaseAuth;
    private MutableLiveData<Boolean> signInStatus;

    @Inject
    public MainViewModel(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
        this.signInStatus = new MutableLiveData<>(firebaseAuth.getCurrentUser() != null);
    }

    public LiveData<Boolean> getSignInStatus() {
        return signInStatus;
    }

    public void logout() {
        this.firebaseAuth.signOut();
        this.signInStatus.postValue(false);
    }

}
