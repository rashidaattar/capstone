package com.udacity.capstone.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.udacity.capstone.R;
import com.udacity.capstone.database.InventoryProvider;
import com.udacity.capstone.database.PersonTable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class CustomerInfoTabFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.lbl_firstname)
    EditText firstName;

    @BindView(R.id.lbl_lastname)
    EditText lastName;

    @BindView(R.id.lbl_company)
    EditText companyName;

    @BindView(R.id.lbl_email)
    EditText email;

    @BindView(R.id.lbl_mobile)
    EditText mobile;

    @BindView(R.id.save_customer)
    Button saveButton;

    private Unbinder unbinder;
    public CustomerInfoTabFragment() {
        // Required empty public constructor
    }



    public static CustomerInfoTabFragment newInstance() {
        CustomerInfoTabFragment fragment = new CustomerInfoTabFragment();
       /* Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_customer_info_tab, container, false);
        unbinder = ButterKnife.bind(this,view);
        return view;
    }

    @OnClick(R.id.save_customer)
    public void addCustomer(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(PersonTable.PERSON_NAME,firstName.getText().toString() + "_" + lastName.getText().toString() );
        contentValues.put(PersonTable.PERSON_TYPE,PersonTable.CUSTOMER_PERSON);
        contentValues.put(PersonTable.EMAIL,email.getText().toString());
        contentValues.put(PersonTable.CONTACT_NO,mobile.getText().toString());
        contentValues.put(PersonTable.COMPANY_NAME,companyName.getText().toString());
        Uri uri =getActivity().getContentResolver().insert(InventoryProvider.Persons.PERSONS_URI,contentValues);
       onCustomerInserted(uri);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        unbinder.unbind();
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onCustomerInserted(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
}
