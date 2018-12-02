package com.example.myapplication.contactdetail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.data.Contact;

public class ContactDetailFragment extends Fragment implements ContactDetailContract.View {

    private static final String REQUIRED_CONTACT_ID = "param1";
    private String requiredContactID;
    private TextView contactDetailInfoView;
    private ContactDetailContract.Presenter presenter;

    public static ContactDetailFragment newInstance(String requiredContactID) {
        ContactDetailFragment fragment = new ContactDetailFragment();
        Bundle args = new Bundle();
        args.putString(REQUIRED_CONTACT_ID, requiredContactID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            requiredContactID = getArguments().getString(REQUIRED_CONTACT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater li, ViewGroup container, Bundle savedInstanceState) {
        return li.inflate(R.layout.fragment_contact_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        contactDetailInfoView = (TextView) view.findViewById(R.id.contact_detail_info);
    }

    @Override
    public void onResume() {
        super.onResume();

        presenter.openContactDetails(requiredContactID);
    }

    @Override
    public void showContactDetails(Contact contact) {
        contactDetailInfoView.setText(contact.toString());
    }

    @Override
    public void setPresenter(ContactDetailContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
