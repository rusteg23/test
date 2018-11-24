package com.example.myapplication.contactdetail;

import android.support.annotation.NonNull;

import com.example.myapplication.data.Contact;
import com.example.myapplication.data.source.ContactsDataSource;
import com.example.myapplication.data.source.ContactsRepository;

public class ContactDetailPresenter implements ContactDetailContract.Presenter {

    private final ContactsRepository contactsRepository;
    private final ContactDetailContract.View contactView;


    public ContactDetailPresenter(@NonNull ContactsRepository contactsRepository,
                                  @NonNull ContactDetailContract.View contactsView) {
        this.contactsRepository = contactsRepository;
        this.contactView = contactsView;

        this.contactView.setPresenter(this);
    }

    @Override
    public void start() {
        openContactDetails("");
    }

    @Override
    public void openContactDetails(final String requestedContactId) {
        contactsRepository.getContact(requestedContactId, new ContactsDataSource.GetContactCallback() {
            @Override
            public void onContactLoaded(Contact contact) {
                contactView.showContactDetails(contact);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }
}
