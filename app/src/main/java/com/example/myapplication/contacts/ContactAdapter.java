package com.example.myapplication.contacts;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.data.Contact;

import java.util.List;

class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder> {

    private ContactListFragment contactListFragment;
    private List<Contact> contacts;
    private ContactListContract.Presenter presenter;

    public ContactAdapter(ContactListFragment contactListFragment, List<Contact> contacts,
                          ContactListContract.Presenter presenter) {
        this.contactListFragment = contactListFragment;
        this.contacts = contacts;
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(contactListFragment.getActivity());
        View view = layoutInflater.inflate(R.layout.list_item_contact, parent, false);
        return new ContactHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
        Contact contact = contacts.get(position);
        holder.bindContact(contact);
    }

    @Override
    public int getItemCount() {
        return this.contacts.size();
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public class ContactHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView firstNameTextView;
        private Contact contact;

        public ContactHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            firstNameTextView = itemView.findViewById(R.id.list_item_contact_first_name);
        }

        public void bindContact(Contact contact) {
            this.contact = contact;
            firstNameTextView.setText(contact.name);

        }

        @Override
        public void onClick(View v) {
            presenter.openContactDetails(contact);
        }
    }
}
