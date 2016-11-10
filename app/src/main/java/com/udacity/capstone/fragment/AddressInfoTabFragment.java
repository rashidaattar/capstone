package com.udacity.capstone.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.udacity.capstone.R;
import com.udacity.capstone.database.AddressTable;
import com.udacity.capstone.database.InventoryProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class AddressInfoTabFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.lbl_address_line1)
    EditText address_line1;

    @BindView(R.id.lbl_address_line2)
    EditText address_line2;

    @BindView(R.id.lbl_city)
    EditText city;

    @BindView(R.id.lbl_state)
    EditText state;

    @BindView(R.id.lbl_pincode)
    EditText pincode;

    @BindView(R.id.lbl_contactno)
    EditText mobile;

    @BindView(R.id.save_address)
    Button button;

    private Unbinder unbinder;

    public static AddressInfoTabFragment newInstance() {
        AddressInfoTabFragment fragment = new AddressInfoTabFragment();
       /* Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }
    public AddressInfoTabFragment() {
        // Required empty public constructor
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
        View view = inflater.inflate(R.layout.fragment_address_info_tab, container, false);
        unbinder= ButterKnife.bind(this,view);
        return view;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        unbinder.unbind();
    }

    @OnClick(R.id.save_address)
    public void saveAddress(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(AddressTable.ADDRESS_LINE1,address_line1.getText().toString());
        contentValues.put(AddressTable.ADDRESS_LINE2,address_line2.getText().toString());
        contentValues.put(AddressTable.ADDRESS_TYPE,AddressTable.ADDRESS_BILLING);
        contentValues.put(AddressTable.CITY,city.getText().toString());
        contentValues.put(AddressTable.STATE,state.getText().toString());
        contentValues.put(AddressTable.CONTACT_NO,mobile.getText().toString());
        contentValues.put(AddressTable.PERSON_ID,1);
        getActivity().getContentResolver().insert(InventoryProvider.Addreses.ADDRESSES_URI,contentValues);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
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



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
