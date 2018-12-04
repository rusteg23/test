package com.example.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.myapplication.data.source.ContactsRepository;
import com.example.myapplication.data.source.local.ContactsLocalDataSource;

public class Injection {

    public static ContactsRepository provideContactsRepository(@NonNull Context context) {
        ContactsLocalDataSource instance = ContactsLocalDataSource
                .getInstance(context.getApplicationContext());

        return ContactsRepository
                .getInstance(instance);
    }
}