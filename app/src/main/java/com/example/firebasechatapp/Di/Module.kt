package com.example.firebasechatapp.Di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    @Singleton

    fun provideFireBaseAuthentication()  : FirebaseAuth{
        return FirebaseAuth.getInstance()

    }


    @Provides
    @Singleton
    fun provideFireDataBase()  : DatabaseReference{
        return FirebaseDatabase.getInstance().getReference("database")
    }

}