package com.example.myapplication.contactdetail;

import com.example.myapplication.BasePresenter;
import com.example.myapplication.BaseView;
import com.example.myapplication.data.Contact;

public interface ContactDetailContract {
    interface View extends BaseView<Presenter> {

        void showContactDetails(Contact contact);

    }

    interface Presenter extends BasePresenter {

        void openContactDetails(final String contactId);

    }

}
