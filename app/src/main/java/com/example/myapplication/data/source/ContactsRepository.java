package com.example.myapplication.data.source;

import android.support.annotation.NonNull;

import com.example.myapplication.data.Contact;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ContactsRepository implements ContactsDataSource {

    private static ContactsRepository INSTANCE = null;
    private final ContactsDataSource contactsLocalDataSource;
    Map<UUID, Contact> cachedContacts;

    private ContactsRepository(@NonNull ContactsDataSource contactsLocalDataSource) {
        this.contactsLocalDataSource = contactsLocalDataSource;
    }

    public static ContactsRepository getInstance(ContactsDataSource contactsLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new ContactsRepository(contactsLocalDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getContacts(final LoadContactsCallback callback) {
        contactsLocalDataSource.getContacts(new LoadContactsCallback() {
            @Override
            public void onContactsLoaded(List<Contact> contacts) {
                callback.onContactsLoaded(contacts);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public void getContact(final UUID contactId, final GetContactCallback callback) {
        contactsLocalDataSource.getContact(contactId, new GetContactCallback() {
            @Override
            public void onContactLoaded(Contact contact) {
                callback.onContactLoaded(contact);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });

    }
}
