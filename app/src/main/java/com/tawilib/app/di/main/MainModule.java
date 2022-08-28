package com.tawilib.app.di.main;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {

    @Provides
    static FirebaseAuth provideFirebaseAuth(){
        return FirebaseAuth.getInstance();
    }

    @Provides
    static DatabaseReference provideDatabaseReference(FirebaseAuth firebaseAuth) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser(); // using account id as document name
        return  FirebaseDatabase.getInstance().getReference(firebaseUser.getUid());
    }

    @Provides
    static StorageReference provideStorageReference(FirebaseAuth firebaseAuth) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser(); // using account id as document name
        return  FirebaseStorage.getInstance().getReference(firebaseUser.getUid());
    }
}