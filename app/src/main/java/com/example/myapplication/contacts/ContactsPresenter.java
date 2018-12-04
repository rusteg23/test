package com.example.myapplication.contacts;

import android.support.annotation.NonNull;

import com.example.myapplication.data.Contact;
import com.example.myapplication.data.source.ContactsDataSource;
import com.example.myapplication.data.source.ContactsRepository;

import java.util.List;

public class ContactsPresenter implements ContactListContract.Presenter {

    private final ContactsRepository contactsRepository;
    private final ContactListContract.View contactsView;


    public ContactsPresenter(@NonNull ContactsRepository contactsRepository,
                             @NonNull ContactListContract.View contactsView) {
        this.contactsRepository = contactsRepository;
        this.contactsView = contactsView;

        this.contactsView.setPresenter(this);
    }

    @Override
    public void start() {
        loadContacts();
    }

    @Override
    public void loadContacts() {
        contactsRepository.getContacts(new ContactsDataSource.LoadContactsCallback() {
            @Override
            public void onContactsLoaded(List<Contact> contacts) {
                contactsView.showContacts(contacts);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public void openContactDetails(Contact requestedContact) {
        contactsRepository.getContact(requestedContact.id, new ContactsDataSource.GetContactCallback() {
            @Override
            public void onContactLoaded(Contact contact) {
                contactsView.showContactDetails(contact);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }
}
