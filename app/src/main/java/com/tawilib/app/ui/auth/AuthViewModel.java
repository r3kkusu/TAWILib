package com.tawilib.app.ui.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tawilib.app.ui.common.Resource;

import javax.inject.Inject;

public class AuthViewModel extends ViewModel {

    private static final String TAG = "AuthViewModel";

    private FirebaseAuth firebaseAuth;
    private MutableLiveData<Resource<FirebaseUser>> firebaseUser;

    @Inject
    public AuthViewModel(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
        this.firebaseUser = new MutableLiveData<>();
    }

    public LiveData<Resource<FirebaseUser>> getFirebaseUser() {
        return firebaseUser;
    }

    public void authenticate() {
        firebaseUser.postValue(Resource.loading(null));
        if (firebaseAuth.getCurrentUser() != null) {
            firebaseUser.postValue(Resource.success(firebaseAuth.getCurrentUser()));
        }
    }

    public void authenticate(String username, String password) {
        firebaseUser.postValue(Resource.loading(null));
        firebaseAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        firebaseUser.postValue(Resource.success(firebaseAuth.getCurrentUser()));
                    } else {
                        firebaseUser.postValue(Resource.error("Invalid account", null));
                    }
                });
    }
}
