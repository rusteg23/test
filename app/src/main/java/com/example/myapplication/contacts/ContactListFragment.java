package com.example.myapplication.contacts;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.data.Contact;

import java.util.List;

public class ContactListFragment extends Fragment implements ContactListContract.View {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 5678;
    private OnContactListFragmentInteractionListener listener;
    private ContactListContract.Presenter presenter;
    private RecyclerView contactsRV;
    private ContactAdapter adapter;

    public static ContactListFragment newInstance() {
        return new ContactListFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnContactListFragmentInteractionListener) {
            listener = (OnContactListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        contactsRV = (RecyclerView) view.findViewById(R.id.contacts_recycler_view);
        contactsRV.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onResume() {
        super.onResume();

        FragmentActivity activity = requireActivity();
        int checkSelfPermissionResult = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_CONTACTS);

        boolean isContactsReadable = checkSelfPermissionResult == PackageManager.PERMISSION_GRANTED;
        if (!isContactsReadable) {
            String[] permissions = {Manifest.permission.READ_CONTACTS};
            ActivityCompat.requestPermissions(activity, permissions,
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            presenter.start();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS:
                handleRequestPermissionResult(grantResults);
                break;
            default:
                break;
        }
    }

    private void handleRequestPermissionResult(int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            presenter.start();
        } else {
            Toast.makeText(getContext(), R.string.error_access_denied, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDetach() {
        listener = null;
        super.onDetach();
    }

    @Override
    public void showContacts(List<Contact> contacts) {
        if (adapter != null) {
            adapter.setContacts(contacts);
            adapter.notifyDataSetChanged();
        } else {
            adapter = new ContactAdapter(this, contacts, presenter);
            contactsRV.setAdapter(adapter);
        }
    }

    @Override
    public void showContactDetails(Contact contact) {
        if (listener != null) {
            listener.onContactClicked(contact);
        }
    }

    @Override
    public void setPresenter(ContactListContract.Presenter presenter) {
        this.presenter = presenter;
    }

    public interface OnContactListFragmentInteractionListener {

        void onContactClicked(Contact contact);
    }

}
