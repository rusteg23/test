package com.example.myapplication.contacts;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.data.Contact;

import java.util.List;

public class ContactListFragment extends Fragment implements ContactListContract.View {

    private OnContactListFragmentInteractionListener mListener;
    private ContactListContract.Presenter presenter;
    private RecyclerView contactsRV;
    private ContactAdapter adapter;

    public ContactListFragment() {
        // Required empty public constructor
    }

    public static ContactListFragment newInstance() {
        return new ContactListFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnContactListFragmentInteractionListener) {
            mListener = (OnContactListFragmentInteractionListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentContactListView = inflater.inflate(R.layout.fragment_contact_list, container,
                false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        contactsRV = (RecyclerView) fragmentContactListView.findViewById(R.id.contacts_recycler_view);
        contactsRV.setLayoutManager(linearLayoutManager);

        return fragmentContactListView;
    }


    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void showContacts(List<Contact> contacts) {
        if (adapter != null) {
            adapter.setContacts(contacts);
            adapter.notifyDataSetChanged();
        } else {
            adapter = new ContactAdapter(contacts);
            contactsRV.setAdapter(adapter);
        }
    }

    @Override
    public void showContactDetails(String id) {
        if (mListener != null) {
            mListener.onConctactClicked(null);
        }
    }

    @Override
    public void setPresenter(ContactListContract.Presenter presenter) {
        this.presenter = presenter;
    }

    public interface OnContactListFragmentInteractionListener {

        void onConctactClicked(Uri uri);
    }

    private class ContactHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView firstNameTextView;
        private Contact contact;

        public ContactHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            firstNameTextView = (TextView) itemView.findViewById(R.id.list_item_contact_first_name);
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

    private class ContactAdapter extends RecyclerView.Adapter<ContactHolder> {

        private List<Contact> contacts;

        public ContactAdapter(List<Contact> contacts) {
            this.contacts = contacts;
        }

        @Override
        public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_contact, parent, false);
            return new ContactHolder(view);
        }

        @Override
        public void onBindViewHolder(ContactHolder holder, int position) {
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
    }
}
