package com.udacity.capstone.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.udacity.capstone.R;
import com.udacity.capstone.database.InventoryProvider;
import com.udacity.capstone.database.PersonTable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import android.provider.ContactsContract.CommonDataKinds.*;


public class CustomerInfoTabFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "1";
    private static final String ARG_PARAM2 = "2";
    public static final int PICK_CONTACT = 1;

    private String mParam1;
    private boolean mParam2;

    private OnFragmentInteractionListener mListener;

    private static final int READ_CONTACTS_PERMISSIONS_REQUEST = 1;
    private boolean isContactsPermission = false;
    private Cursor mCursor;

    private String fname;
    private String lname;
    private String company_name;
    private String mobile;
    private String email;

    @BindView(R.id.txt_fname)
    EditText firstName_text;

    @BindView(R.id.txt_lname)
    EditText lastName_text;

    @BindView(R.id.txt_company)
    EditText companyName_text;

    @BindView(R.id.txt_email)
    EditText email_text;

    @BindView(R.id.txt_mobile)
    EditText mobile_text;

    @BindView(R.id.main_view)
    LinearLayout linearLayout;
   /* @BindView(R.id.save_customer)
    Button saveButton;*/


    private Unbinder unbinder;
    public CustomerInfoTabFragment() {
        // Required empty public constructor
    }



    public static CustomerInfoTabFragment newInstance(String customerID, boolean isCustomer) {
        CustomerInfoTabFragment fragment = new CustomerInfoTabFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, customerID);
        args.putBoolean(ARG_PARAM2, isCustomer);
        fragment.setArguments(args);
        return fragment;
    }

    public static CustomerInfoTabFragment newInstance1( boolean isCustomer) {
        CustomerInfoTabFragment fragment = new CustomerInfoTabFragment();
        Bundle args = new Bundle();
       // args.putString(ARG_PARAM1, customerID);
        args.putBoolean(ARG_PARAM2, isCustomer);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().size()==2) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getBoolean(ARG_PARAM2);
            mCursor = getActivity().getContentResolver().query(InventoryProvider.Persons.PERSONS_URI,null,PersonTable._ID+"="+Integer.parseInt(mParam1),null,null);
        }
        else if(getArguments()!=null && getArguments().size()==1){
            mParam2 = getArguments().getBoolean(ARG_PARAM2);
        }

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_customer_info_tab, container, false);
        unbinder = ButterKnife.bind(this,view);
        if(mCursor!=null)
            populateData();
        return view;
    }

    @OnClick(R.id.save_customer)
    public void addCustomer(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(PersonTable.PERSON_NAME,firstName_text.getText()!=null?firstName_text.getText().toString():"" + "_" + lastName_text.getText()!=null?lastName_text.getText().toString():"" );
        if(mParam2){
            contentValues.put(PersonTable.PERSON_TYPE,PersonTable.CUSTOMER_PERSON);
        }
        else{
            contentValues.put(PersonTable.PERSON_TYPE,PersonTable.VENDOR_PERSON);
        }
        contentValues.put(PersonTable.EMAIL,email_text.getText()!=null?email_text.getText().toString():"");
        contentValues.put(PersonTable.CONTACT_NO,mobile_text.getText()!=null?mobile_text.getText().toString():"");
        contentValues.put(PersonTable.COMPANY_NAME,companyName_text.getText()!=null?companyName_text.getText().toString():"");
        if(mParam1!=null){
            getActivity().getContentResolver().update(InventoryProvider.Persons.PERSONS_URI,contentValues,PersonTable._ID+"="+Integer.parseInt(mParam1),null);
        }
        else{
            Uri uri =getActivity().getContentResolver().insert(InventoryProvider.Persons.PERSONS_URI,contentValues);
            onCustomerInserted(uri);
        }
        Snackbar snackbar = Snackbar
                .make(linearLayout, getResources().getString(R.string.add_toast), Snackbar.LENGTH_LONG);
        snackbar.show();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        unbinder.unbind();
    }


    private void populateData() {
        if(mCursor.getCount()>0){
            while (mCursor.moveToNext()){

                if(mCursor.getString(mCursor.getColumnIndex(PersonTable.PERSON_NAME)).contains("_")){

                    String[] personName = mCursor.getString(mCursor.getColumnIndex(PersonTable.PERSON_NAME)).split("_");
                    fname = personName[0];
                    if(personName.length==2)
                    lname = personName[1];
                }
                else{
                    fname =  mCursor.getString(mCursor.getColumnIndex(PersonTable.PERSON_NAME));
                    lname = "";
                }

                email = mCursor.getString(mCursor.getColumnIndex(PersonTable.EMAIL))!=null?mCursor.getString(mCursor.getColumnIndex(PersonTable.EMAIL)):"";
                company_name=mCursor.getString(mCursor.getColumnIndex(PersonTable.COMPANY_NAME))!=null?mCursor.getString(mCursor.getColumnIndex(PersonTable.COMPANY_NAME)):"";
                mobile = mCursor.getString(mCursor.getColumnIndex(PersonTable.CONTACT_NO))!=null?mCursor.getString(mCursor.getColumnIndex(PersonTable.CONTACT_NO)):"";
                updateUI();
            }
        }
    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteractionCustomer(Uri uri);
    }


    public void onCustomerInserted(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteractionCustomer(uri);
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

    @OnClick(R.id.import_contacts)
    public void getContacts(View v){
        if(!isContactsPermission)
        getPermissionToReadUserContacts();
        if(isContactsPermission){
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, PICK_CONTACT);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_CONTACT)
        {
            if(resultCode == Activity.RESULT_OK)
            {
               Uri contacts_id_uri = data.getData();
                //String projection[] = {Phone.DATA1, Phone.DATA, Phone.DISPLAY_NAME, Phone.PHOTO_URI};
                Cursor c= getActivity().getContentResolver().query(contacts_id_uri,null,null,null,null);
                if((c != null ? c.getCount() : 0) >0){
                    while(c.moveToNext()){
                      //  String email = c.getString(c.getColumnIndex(Email.ADDRESS));
                         mobile = "";
                         email= "";
                         fname = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        String has_phone_no = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        String contactsID = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                        if(has_phone_no.equals("1")){
                           mobile =  getMobileNumber(contactsID);
                        }
                        email = getEmailId(contactsID);

                    }
                }
                updateUI();
            }

        }
    }

    private void updateUI() {

        firstName_text.setText(fname!=null?fname:"");
        lastName_text.setText(lname!=null?lname:"");
        companyName_text.setText(company_name!=null?company_name:"");
        mobile_text.setText(mobile!=null?mobile:"");
        email_text.setText(email!=null?email:"");
    }

    private String getEmailId(String contactsID) {
        Cursor c = getActivity().getContentResolver().query(Email.CONTENT_URI,null,Email.CONTACT_ID + "=" + contactsID,null,null);
        String email = "";
        if(c.getCount()>0){
            while (c.moveToNext()){
                email =  c.getString(c.getColumnIndex(Email.DATA));
            }
        }
        return email;
    }

    private String getMobileNumber(String contactsID) {
        Cursor c = getActivity().getContentResolver().query(Phone.CONTENT_URI,null,Phone.CONTACT_ID + "=" + contactsID,null,null);
        String mobile = "";
        if((c != null ? c.getCount() : 0) >0){
            while(c.moveToNext()){
                mobile =  c.getString(c.getColumnIndex(Phone.NUMBER));
            }
        }
        return mobile;
    }

    public void getPermissionToReadUserContacts() {

        if (ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                    READ_CONTACTS_PERMISSIONS_REQUEST);
        }
        else{
            isContactsPermission=true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == READ_CONTACTS_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
                isContactsPermission=true;
            } else {
                isContactsPermission=false;
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
