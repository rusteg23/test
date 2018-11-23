package com.example.myapplication.data.source;

import com.example.myapplication.data.Contact;

import java.util.List;
import java.util.UUID;

public interface ContactsDataSource {

    void getContacts(LoadContactsCallback callback);

    void getContact(UUID contactId, GetContactCallback callback);

    interface LoadContactsCallback {

        void onContactsLoaded(List<Contact> contacts);

        void onDataNotAvailable();

    }

    interface GetContactCallback {

        void onContactLoaded(Contact contact);

        void onDataNotAvailable();
    }

}
