package com.tawilib.app.di.auth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import dagger.Module;
import dagger.Provides;

@Module
public class AuthModule {

    @Provides
    static FirebaseAuth provideFirebaseAuth(){
        return FirebaseAuth.getInstance();
    }

}