package com.example.myapplication.contacts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.myapplication.Injection;
import com.example.myapplication.R;
import com.example.myapplication.contactdetail.ContactDetailFragment;
import com.example.myapplication.contactdetail.ContactDetailPresenter;
import com.example.myapplication.data.Contact;
import com.example.myapplication.data.source.ContactsRepository;

public class ContactListActivity extends AppCompatActivity implements ContactListFragment.OnContactListFragmentInteractionListener {

    private ContactsPresenter contactsPresenter;
    private ContactsRepository contactsRepository;
    private ContactDetailPresenter contactDetailPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        addContactListFragment();
    }

    private void addContactListFragment() {
        ContactListFragment contactListFragment =
                (ContactListFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_container);

        if (contactListFragment == null) {
            contactListFragment = ContactListFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, contactListFragment)
                    .commit();
        }

        contactsRepository =
                Injection.provideContactsRepository(getApplicationContext());

        contactsPresenter = new ContactsPresenter(contactsRepository, contactListFragment);
    }

    @Override
    public void onContactClicked(Contact contact) {
        ContactDetailFragment contactDetailFragment = ContactDetailFragment.newInstance(contact.id);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, contactDetailFragment)
                .addToBackStack(null)
                .commit();

        contactDetailPresenter =
                new ContactDetailPresenter(contactsRepository, contactDetailFragment);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
