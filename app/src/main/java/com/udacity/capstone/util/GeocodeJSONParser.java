package com.udacity.capstone.util;

import android.content.Context;

import com.udacity.capstone.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Rashida on 09-01-2017.
 */

public class GeocodeJSONParser {

    private Context mContext;

    public GeocodeJSONParser(Context mContext) {
        this.mContext = mContext;
    }

    /** Receives a JSONObject and returns a list */
    public List<HashMap<String,String>> parse(JSONObject jObject){

        JSONArray jPlaces = null;
        try {
            /** Retrieves all the elements in the 'places' array */
            jPlaces = jObject.getJSONArray(mContext.getString(R.string.result_string));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /** Invoking getPlaces with the array of json object
         * where each json object represent a place
         */
        return getPlaces(jPlaces);
    }

    private List<HashMap<String, String>> getPlaces(JSONArray jPlaces){
        int placesCount = jPlaces.length();
        List<HashMap<String, String>> placesList = new ArrayList<>();
        HashMap<String, String> place = null;

        /** Taking each place, parses and adds to list object */
        for(int i=0; i<placesCount;i++){
            try {
                /** Call getPlace with place JSON object to parse the place */
                place = getPlace((JSONObject)jPlaces.get(i));
                placesList.add(place);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return placesList;
    }

    /** Parsing the Place JSON object */
    private HashMap<String, String> getPlace(JSONObject jPlace){

        HashMap<String, String> place = new HashMap<String, String>();
        String formatted_address = mContext.getString(R.string.na_string);
        String lat="";
        String lng="";
        String southwestLongitude="";
        String southwestLatitude="";
        String northEastLongitude="";
        String northEastLatitude="";
        try {
            // Extracting formatted address, if available
            if(!jPlace.isNull(mContext.getString(R.string.formatted_add))){
                formatted_address = jPlace.getString(mContext.getString(R.string.formatted_add));
            }

            lat = jPlace.getJSONObject(mContext.getString(R.string.geometry_string)).getJSONObject(mContext.getString(R.string.location_string)).getString(mContext.getString(R.string.lat_string));
            lng = jPlace.getJSONObject(mContext.getString(R.string.geometry_string)).getJSONObject(mContext.getString(R.string.location_string)).getString(mContext.getString(R.string.lon_string));
            southwestLongitude=jPlace.getJSONObject(mContext.getString(R.string.geometry_string)).getJSONObject(mContext.getString(R.string.bounds_string)).getJSONObject(mContext.getString(R.string.southwest_string)).getString(mContext.getString(R.string.lon_string));
            southwestLatitude=jPlace.getJSONObject(mContext.getString(R.string.geometry_string)).getJSONObject(mContext.getString(R.string.bounds_string)).getJSONObject(mContext.getString(R.string.southwest_string)).getString(mContext.getString(R.string.lat_string));
            northEastLongitude=jPlace.getJSONObject(mContext.getString(R.string.geometry_string)).getJSONObject(mContext.getString(R.string.bounds_string)).getJSONObject(mContext.getString(R.string.northeast_string)).getString(mContext.getString(R.string.lon_string));
            northEastLatitude=jPlace.getJSONObject(mContext.getString(R.string.geometry_string)).getJSONObject(mContext.getString(R.string.bounds_string)).getJSONObject(mContext.getString(R.string.northeast_string)).getString(mContext.getString(R.string.lat_string));
            place.put(mContext.getString(R.string.formatted_add), formatted_address);
            place.put(mContext.getString(R.string.lat_string), lat);
            place.put(mContext.getString(R.string.lon_string), lng);
            place.put(mContext.getString(R.string.southwest_lng),southwestLongitude);
            place.put(mContext.getString(R.string.southwest_lat),southwestLatitude);
            place.put(mContext.getString(R.string.northeast_lng),northEastLongitude);
            place.put(mContext.getString(R.string.northeast_alt),northEastLatitude);

        }catch (JSONException e) {
            e.printStackTrace();
        }
        return place;
    }
}
