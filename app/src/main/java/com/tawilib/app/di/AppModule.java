package com.tawilib.app.di;

import android.app.Application;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.tawilib.app.R;
import com.tawilib.app.ui.common.Constants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    @Singleton
    @Provides
    static FirebaseAuth provideFirebaseAuth(){
        return FirebaseAuth.getInstance();
    }

    @Singleton
    @Provides
    static DatabaseReference provideDatabaseReference() {
        return  FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_BOOKS);
    }

    @Singleton
    @Provides
    static StorageReference provideStorageReference() {
        return  FirebaseStorage.getInstance().getReference(Constants.FIREBASE_DB_IMAGES);
    }

    @Singleton
    @Provides
    static RequestOptions provideRequestOptions(){
        return RequestOptions
                .placeholderOf(R.drawable.ic_baseline_book_24)
                .error(R.drawable.ic_baseline_broken_image_24);
    }

    @Singleton
    @Provides
    static RequestManager provideGlideInstance(Application application, RequestOptions requestOptions){
        return Glide.with(application)
                .setDefaultRequestOptions(requestOptions);
    }
}
