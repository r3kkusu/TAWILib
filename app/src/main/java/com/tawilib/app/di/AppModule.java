package com.tawilib.app.di;

import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    @Singleton
    @Provides
    static FirebaseAuth privateFirebaseAuth(){
        return FirebaseAuth.getInstance();
    }
}
