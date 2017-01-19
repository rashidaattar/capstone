package com.udacity.capstone.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.udacity.capstone.R;
import com.udacity.capstone.util.Constants;
import com.udacity.capstone.util.GeocodeJSONParser;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String LOG_TAG = MapsActivity.class.getSimpleName();
    private static final String UTF_8 = "utf-8";
    private GoogleMap mMap;
    private ProgressDialog dialog;
    private Context mContext;
    public  String url = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mContext=MapsActivity.this;
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        String location=getIntent().getStringExtra(Constants.MAP_EXTRA);
        url =  getString(R.string.maps_url);
        try {
            // encoding special characters like space in the user input place
            location = URLEncoder.encode(location, UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String address = getString(R.string.addresss_literal,location);


        // url , from where the geocoding data is fetched
        //url = url + address + "&key=" + getString(R.string.maps_key);
        url = url + address + "&key=" + getString(R.string.maps_key);

        // Instantiating DownloadTask to get places from Google Geocoding service
        // in a non-ui thread
        DownloadTask downloadTask = new DownloadTask();

        // Start downloading the geocoding places
        downloadTask.execute(url);
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            // Connecting to url
            urlConnection.connect();
            // Reading data from url
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();

        } catch (Exception e) {
            Log.d(LOG_TAG, e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        /*// Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }

    /** A class, to download Places from Geocoding webservice */
    private class DownloadTask extends AsyncTask<String, Integer, String> {

        String data = null;
        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(String... url) {
            try{
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d(LOG_TAG,e.toString());
            }
            return data;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result){
            // Instantiating ParserTask which parses the json data from Geocoding webservice
            // in a non-ui thread
            ParserTask parserTask = new ParserTask();
            // Start parsing the places in JSON format
            // Invokes the "doInBackground()" method of the class ParseTask
            parserTask.execute(result);
        }
    }


    /** A class to parse the Geocoding Places in non-ui thread */
    class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>> {

        JSONObject jObject;

        // Invoked by execute() method of this object
        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;
            GeocodeJSONParser parser = new GeocodeJSONParser(mContext);

            try {
                jObject = new JSONObject(jsonData[0]);

                /** Getting the parsed data as a an ArrayList */
                places = parser.parse(jObject);

            } catch (Exception e) {
                Log.d(LOG_TAG, e.toString());
            }
            return places;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(List<HashMap<String, String>> list) {

            // Clears all the existing markers
            mMap.clear();
            for (int i = 0; i < list.size(); i++) {
                MarkerOptions markerOptions = new MarkerOptions();// Creating a marker
                HashMap<String, String> hmPlace = list.get(i);// Getting a place from the places list
                double lat = Double.parseDouble(hmPlace.get(getString(R.string.lat_string)));// Getting latitude of the place
                double lng = Double.parseDouble(hmPlace.get(getString(R.string.lon_string)));// Getting longitude of the place
                double SWlat = Double.parseDouble(hmPlace.get(getString(R.string.southwest_lat)));// Getting latitude of the place
                double SWlng = Double.parseDouble(hmPlace.get(getString(R.string.southwest_lng)));// Getting longitude of the place
                double NElat = Double.parseDouble(hmPlace.get(getString(R.string.northeast_alt)));// Getting latitude of the place
                double NElng = Double.parseDouble(hmPlace.get(getString(R.string.northeast_lng)));// Getting longitude of the place

                String name = hmPlace.get(getString(R.string.formatted_add));// Getting name
                LatLng latLng = new LatLng(lat, lng);// Setting the position for the marker
                LatLng SWlatLng = new LatLng(SWlat, SWlng);// Setting the position for the marker
                LatLng NElatLng = new LatLng(NElat, NElng);// Setting the position for the marker
                markerOptions.position(latLng);

                LatLngBounds bounds=new LatLngBounds(SWlatLng,NElatLng);
                markerOptions.title(name);// Setting the title for the marker
                // Placing a marker on the touched position
                mMap.addMarker(markerOptions);

                // Locate the first location
                if (i == 0)
                    //mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,70));

            }
        }
    }

}
