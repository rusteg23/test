package com.example.myapplication.contacts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.myapplication.Injection;
import com.example.myapplication.R;
import com.example.myapplication.data.source.ContactsRepository;

public class ContactListActivity extends AppCompatActivity {

    private ContactsPresenter contactsPresenter;

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

        ContactsRepository contactsRepository =
                Injection.provideContactsRepository(getApplicationContext());

        contactsPresenter = new ContactsPresenter(contactsRepository, contactListFragment);
    }
}
