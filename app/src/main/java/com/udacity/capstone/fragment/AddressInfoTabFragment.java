package com.udacity.capstone.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.udacity.capstone.R;
import com.udacity.capstone.database.AddressTable;
import com.udacity.capstone.database.InventoryProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class AddressInfoTabFragment extends Fragment  {

    private static final String ARG_PARAM1 = "";
    private static final String ARG_PARAM2 = "";


    private String mParam1;
    private String mParam2;

    private Cursor mCursor;

    private String addline1;
    private String addline2;
    private String city;
    private String state;
    private String pincode;
    private String conatct;



    private OnFragmentInteractionListener mListener;

    @BindView(R.id.main_view)
    LinearLayout linearLayout;

    @BindView(R.id.txt_addressline1)
    EditText address_line1_txt;

    @BindView(R.id.txt_addressline2)
    EditText address_line2_txt;

    @BindView(R.id.txt_city)
    EditText city_txt;

    @BindView(R.id.txt_state)
    EditText state_txt;

    @BindView(R.id.txt_pincode)
    EditText pincode_txt;

    @BindView(R.id.txt_contact)
    EditText contact_txt;

    @BindView(R.id.save_address)
    Button button;

    private Unbinder unbinder;

    private String personId;

    public static AddressInfoTabFragment newInstance(String addressID,String personID) {
        AddressInfoTabFragment fragment = new AddressInfoTabFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, addressID);
        args.putString(ARG_PARAM2, personID);
        fragment.setArguments(args);
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
            personId = mParam2; //coming from edit
            mCursor = getActivity().getContentResolver().query(InventoryProvider.Addreses.ADDRESSES_URI,null,AddressTable._ID+"="+Integer.parseInt(mParam1),null,null);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_address_info_tab, container, false);
        unbinder= ButterKnife.bind(this,view);
        if(mCursor!=null){
            populateData();
        }
        return view;
    }

    private void populateData() {
        if(mCursor.getCount()>0){
            while(mCursor.moveToNext()){
                addline1 = mCursor.getString(mCursor.getColumnIndex(AddressTable.ADDRESS_LINE1))!=null?mCursor.getString(mCursor.getColumnIndex(AddressTable.ADDRESS_LINE1)):"";
                addline2 = mCursor.getString(mCursor.getColumnIndex(AddressTable.ADDRESS_LINE2))!=null? mCursor.getString(mCursor.getColumnIndex(AddressTable.ADDRESS_LINE2)):"";
                city = mCursor.getString(mCursor.getColumnIndex(AddressTable.CITY))!=null?mCursor.getString(mCursor.getColumnIndex(AddressTable.CITY)):"";
                state = mCursor.getString(mCursor.getColumnIndex(AddressTable.STATE))!=null?mCursor.getString(mCursor.getColumnIndex(AddressTable.STATE)):"";
                pincode = mCursor.getString(mCursor.getColumnIndex(AddressTable.PINCODE))!=null?mCursor.getString(mCursor.getColumnIndex(AddressTable.PINCODE)):"";
                conatct = mCursor.getString(mCursor.getColumnIndex(AddressTable.CONTACT_NO))!=null?mCursor.getString(mCursor.getColumnIndex(AddressTable.CONTACT_NO)):"";
                updateUI();
            }
        }
    }

    private void updateUI() {

        address_line1_txt.setText(addline1);
        address_line2_txt.setText(addline2);
        city_txt.setText(city);
        state_txt.setText(state);
        pincode_txt.setText(pincode);
        contact_txt.setText(conatct);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        unbinder.unbind();
    }

    @OnClick(R.id.save_address)
    public void saveAddress(){
        if(personId==null) //not coming from edit
        personId=getPersonId();

        if(personId!=null && personId.length()>0){

            ContentValues contentValues = new ContentValues();
            contentValues.put(AddressTable.ADDRESS_LINE1,address_line1_txt.getText().toString());
            contentValues.put(AddressTable.ADDRESS_LINE2,address_line2_txt.getText().toString());
            contentValues.put(AddressTable.ADDRESS_TYPE,AddressTable.ADDRESS_BILLING);
            contentValues.put(AddressTable.CITY,city_txt.getText().toString());
            contentValues.put(AddressTable.STATE,state_txt.getText().toString());
            contentValues.put(AddressTable.CONTACT_NO,contact_txt.getText().toString());
            contentValues.put(AddressTable.PINCODE,pincode_txt.getText().toString());
            contentValues.put(AddressTable.PERSON_ID, Integer.parseInt(personId));
            if(mParam1!=null){
                getActivity().getContentResolver().update(InventoryProvider.Addreses.ADDRESSES_URI,contentValues,AddressTable._ID+"="+Integer.parseInt(mParam1),null);
            }
            else{
                getActivity().getContentResolver().insert(InventoryProvider.Addreses.ADDRESSES_URI,contentValues);

            }
            Snackbar snackbar = Snackbar
                    .make(linearLayout, getResources().getString(R.string.add_toast), Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        else{
            Toast.makeText(this.getActivity(),getString(R.string.customer_save),Toast.LENGTH_SHORT).show();
        }

    }

    public String getPersonId() {
        if (mListener != null) {
            return mListener.onFragmentInteractionAddress();
        }
        return null;
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
        String onFragmentInteractionAddress();
    }
}
