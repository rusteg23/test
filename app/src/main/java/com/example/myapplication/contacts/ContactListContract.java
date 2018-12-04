package com.example.myapplication.contacts;

import com.example.myapplication.BasePresenter;
import com.example.myapplication.BaseView;
import com.example.myapplication.data.Contact;

import java.util.List;

public interface ContactListContract {
    interface View extends BaseView<Presenter> {

        void showContacts(List<Contact> contacts);

        void showContactDetails(Contact contact);

    }

    interface Presenter extends BasePresenter {

        void loadContacts();

        void openContactDetails(Contact requestedContact);

    }

}
