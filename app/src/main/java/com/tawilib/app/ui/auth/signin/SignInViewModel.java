package com.tawilib.app.ui.auth.signin;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tawilib.app.ui.common.Resource;

import javax.inject.Inject;

public class SignInViewModel extends ViewModel {

    private static final String TAG = "SignInViewModel";

    private FirebaseAuth firebaseAuth;
    private MutableLiveData<Resource<FirebaseUser>> firebaseUser;

    @Inject
    public SignInViewModel(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
        this.firebaseUser = new MutableLiveData<>();
    }

    public LiveData<Resource<FirebaseUser>> getFirebaseUser() {
        return firebaseUser;
    }

    public void login(String username, String password) {
        firebaseUser.postValue(Resource.loading(null));
        firebaseAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        firebaseUser.postValue(Resource.success(firebaseAuth.getCurrentUser()));
                    } else {
                        firebaseUser.postValue(Resource.error(task.getException().getMessage(), null));
                    }
                });
    }
}
