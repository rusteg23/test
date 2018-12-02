package com.example.myapplication.data.source.local;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;

import com.example.myapplication.data.Contact;
import com.example.myapplication.data.source.ContactsDataSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ContactsLocalDataSource implements ContactsDataSource {

    private static final String[] EMAIL_PROJECTION = {
            Email.DATA,
            Email.TYPE,
            Email.CONTACT_ID,
    };
    private static final String[] NUMBER_PROJECTION = {
            Phone.NUMBER,
            Phone.TYPE,
            Phone.CONTACT_ID,
    };
    private static ContactsLocalDataSource INSTANCE;
    private Context context;
    private String[] contactsProjectionFields = {
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
    };

    private ContactsLocalDataSource(Context context) {
        this.context = context.getApplicationContext();

    }

    public static ContactsLocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ContactsLocalDataSource(context);
        }
        return INSTANCE;
    }

    private CursorLoader getCursorLoader(String[] projection, String selection,
                                         String[] selectionArgs) {
        return new CursorLoader(context,
                ContactsContract.Contacts.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null
        );
    }

    private List<Contact> loadContactList() {
        ArrayList<Contact> listContacts = new ArrayList<>();
        CursorLoader cursorLoader = getCursorLoader(contactsProjectionFields,
                null,
                null);
        Cursor cursor = cursorLoader.loadInBackground();
        if (cursor != null) {
            Map<String, Contact> contactsMap = new HashMap<>(cursor.getCount());
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

            return listContacts;
        } else {
            return null;
        }
    }

    @Override
    public void getContacts(ContactsDataSource.LoadContactsCallback callback) {
        List<Contact> listContacts = loadContactList();
        if (listContacts == null) {
            callback.onDataNotAvailable();
        } else {
            callback.onContactsLoaded(listContacts);
        }
    }

    @Override
    public void getContact(String requestedContactId, GetContactCallback callback) {
        Contact contact = findContactById(loadContactList(), requestedContactId);
        if (contact == null) {
            callback.onDataNotAvailable();
        } else {
            callback.onContactLoaded(contact);
        }
    }

    @Nullable
    private Contact findContactById(List<Contact> contacts, String requestedContactId) {
        if (requestedContactId != null && contacts != null) {
            for (Contact contact : contacts) {
                if (contact.id.equals(requestedContactId)) {
                    return contact;
                }
            }
        }

        return null;
    }

    public void matchContactNumbers(Map<String, Contact> contactsMap) {
        Cursor phoneCursor = new CursorLoader(context,
                Phone.CONTENT_URI,
                NUMBER_PROJECTION,
                null,
                null,
                null).loadInBackground();

        if (phoneCursor.moveToFirst()) {
            int contactNumberColumnIndex = phoneCursor.getColumnIndex(Phone.NUMBER);
            int contactTypeColumnIndex = phoneCursor.getColumnIndex(Phone.TYPE);
            int contactIdColumnIndex = phoneCursor.getColumnIndex(Phone.CONTACT_ID);

            while (!phoneCursor.isAfterLast()) {
                String number = phoneCursor.getString(contactNumberColumnIndex);
                String contactId = phoneCursor.getString(contactIdColumnIndex);
                Contact contact = contactsMap.get(contactId);
                if (contact == null) {
                    continue;
                }

                int type = phoneCursor.getInt(contactTypeColumnIndex);
                String customLabel = "Custom";
                CharSequence phoneType = ContactsContract.CommonDataKinds
                        .Phone.getTypeLabel(context.getResources(), type, customLabel);

                contact.addNumber(number, phoneType.toString());
                phoneCursor.moveToNext();
            }
        }

        phoneCursor.close();
    }

    public void matchContactEmails(Map<String, Contact> contactsMap) {
        Cursor emailCursor = new CursorLoader(context,
                Email.CONTENT_URI,
                EMAIL_PROJECTION,
                null,
                null,
                null).loadInBackground();

        if (emailCursor.moveToFirst()) {
            int contactEmailColumnIndex = emailCursor.getColumnIndex(Email.DATA);
            int contactTypeColumnIndex = emailCursor.getColumnIndex(Email.TYPE);
            int contactIdColumnsIndex = emailCursor.getColumnIndex(Email.CONTACT_ID);

            while (!emailCursor.isAfterLast()) {
                String address = emailCursor.getString(contactEmailColumnIndex);
                String contactId = emailCursor.getString(contactIdColumnsIndex);
                int type = emailCursor.getInt(contactTypeColumnIndex);
                String customLabel = "Custom";
                Contact contact = contactsMap.get(contactId);
                if (contact == null) {
                    continue;
                }

                CharSequence emailType = ContactsContract.CommonDataKinds.Email.getTypeLabel(context.getResources(), type, customLabel);
                contact.addEmail(address, emailType.toString());
                emailCursor.moveToNext();
            }
        }

        emailCursor.close();
    }
}
