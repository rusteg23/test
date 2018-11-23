package com.example.myapplication.data.source.local;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.content.CursorLoader;

import com.example.myapplication.data.Contact;
import com.example.myapplication.data.source.ContactsDataSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class ContactsLocalDataSource implements ContactsDataSource {

    private static ContactsLocalDataSource INSTANCE;
    private Context context;

    private ContactsLocalDataSource(Context context) {
        this.context = context.getApplicationContext();

    }

    public static ContactsLocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ContactsLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getContacts(ContactsDataSource.LoadContactsCallback callback) {
        ArrayList<Contact> listContacts = new ArrayList<>();
        String[] projectionFields = new String[]{
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
        };


        CursorLoader cursorLoader = new CursorLoader(context,
                ContactsContract.Contacts.CONTENT_URI,
                projectionFields,
                null,
                null,
                null
        );

        Cursor cursor = cursorLoader.loadInBackground();

        if (cursor != null) {
            final Map<String, Contact> contactsMap = new HashMap<>(cursor.getCount());
            if (cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

                do {
                    String contactId = cursor.getString(idIndex);
                    String contactDisplayName = cursor.getString(nameIndex);
                    Contact contact = new Contact(contactId, contactDisplayName);
                    contactsMap.put(contactId, contact);
                    listContacts.add(contact);
                } while (cursor.moveToNext());
            }

            cursor.close();

            matchContactNumbers(contactsMap);
            matchContactEmails(contactsMap);

            callback.onContactsLoaded(listContacts);
        } else {
            callback.onDataNotAvailable();
        }


    }

    @Override
    public void getContact(final UUID contactId, final GetContactCallback callback) {
        this.getContacts(new LoadContactsCallback() {
            @Override
            public void onContactsLoaded(List<Contact> contacts) {
                //todo
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });

    }

    public void matchContactNumbers(Map<String, Contact> contactsMap) {
        // Get numbers
        final String[] numberProjection = new String[]{
                Phone.NUMBER,
                Phone.TYPE,
                Phone.CONTACT_ID,
        };

        Cursor phone = new CursorLoader(context,
                Phone.CONTENT_URI,
                numberProjection,
                null,
                null,
                null).loadInBackground();

        if (phone.moveToFirst()) {
            final int contactNumberColumnIndex = phone.getColumnIndex(Phone.NUMBER);
            final int contactTypeColumnIndex = phone.getColumnIndex(Phone.TYPE);
            final int contactIdColumnIndex = phone.getColumnIndex(Phone.CONTACT_ID);

            while (!phone.isAfterLast()) {
                final String number = phone.getString(contactNumberColumnIndex);
                final String contactId = phone.getString(contactIdColumnIndex);
                Contact contact = contactsMap.get(contactId);
                if (contact == null) {
                    continue;
                }
                final int type = phone.getInt(contactTypeColumnIndex);
                String customLabel = "Custom";
                CharSequence phoneType = ContactsContract.CommonDataKinds
                        .Phone.getTypeLabel(context.getResources(), type, customLabel);

                contact.addNumber(number, phoneType.toString());
                phone.moveToNext();
            }
        }

        phone.close();
    }

    public void matchContactEmails(Map<String, Contact> contactsMap) {
        // Get email
        final String[] emailProjection = new String[]{
                Email.DATA,
                Email.TYPE,
                Email.CONTACT_ID,
        };

        Cursor email = new CursorLoader(context,
                Email.CONTENT_URI,
                emailProjection,
                null,
                null,
                null).loadInBackground();

        if (email.moveToFirst()) {
            final int contactEmailColumnIndex = email.getColumnIndex(Email.DATA);
            final int contactTypeColumnIndex = email.getColumnIndex(Email.TYPE);
            final int contactIdColumnsIndex = email.getColumnIndex(Email.CONTACT_ID);

            while (!email.isAfterLast()) {
                final String address = email.getString(contactEmailColumnIndex);
                final String contactId = email.getString(contactIdColumnsIndex);
                final int type = email.getInt(contactTypeColumnIndex);
                String customLabel = "Custom";
                Contact contact = contactsMap.get(contactId);
                if (contact == null) {
                    continue;
                }
                CharSequence emailType = ContactsContract.CommonDataKinds.Email.getTypeLabel(context.getResources(), type, customLabel);
                contact.addEmail(address, emailType.toString());
                email.moveToNext();
            }
        }

        email.close();
    }
}
