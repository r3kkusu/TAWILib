package com.tawilib.app.ui.auth.signup;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tawilib.app.ui.common.Resource;

import javax.inject.Inject;

public class SignUpViewModel extends ViewModel {

    private static final String TAG = "SignUpViewModel";

    private FirebaseAuth firebaseAuth;
    private MutableLiveData<Resource<FirebaseUser>> firebaseUser;

    @Inject
    public SignUpViewModel(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
        this.firebaseUser = new MutableLiveData<>();
    }

    public LiveData<Resource<FirebaseUser>> getFirebaseUser() {
        return firebaseUser;
    }

    public void register(String username, String password) {
        firebaseUser.postValue(Resource.loading(null));
        firebaseAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        firebaseUser.postValue(Resource.success(firebaseAuth.getCurrentUser()));
                    } else {
                        firebaseUser.postValue(Resource.error(task.getException().getMessage(), null));
                    }
                });
    }

    public void logout() {
        this.firebaseAuth.signOut();
    }
}
