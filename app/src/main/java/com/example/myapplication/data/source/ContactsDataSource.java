package com.example.myapplication.data.source;

import com.example.myapplication.data.Contact;

import java.util.List;

public interface ContactsDataSource {

    void getContacts(LoadContactsCallback callback);

    void getContact(String contactId, GetContactCallback callback);

    interface LoadContactsCallback {

        void onContactsLoaded(List<Contact> contacts);

        void onDataNotAvailable();

    }

    interface GetContactCallback {

        void onContactLoaded(Contact contact);

        void onDataNotAvailable();
    }

}
