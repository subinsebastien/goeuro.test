package com.xtel.goeuro.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.Location;
import android.widget.ArrayAdapter;
import android.widget.Filter;

public class PlacesAdapter extends ArrayAdapter<String> {

	private String currentText;
	private Context context;
	
	public PlacesAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		currentText = "";
		this.context = context;
	}

	@Override
	public Filter getFilter() {
		return new PlacesFilter();
	}
	
	public void setText(String text)	{
		this.currentText = text;
	}

	final class PlacesFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults result = new FilterResults();
			try {
				/*
				 * Uses apache stack for now, the Android's latest client is better for this
				 * type of purposes.
				 */
				HttpClient client = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(context.getString(R.string.api_endpoint) + currentText);
				HttpResponse response = client.execute(httpGet);
				String responseBody = EntityUtils.toString(response.getEntity());
				int statusCode = response.getStatusLine().getStatusCode();
				
				if(statusCode == 200)	{
					JSONObject responseJSON = new JSONObject(responseBody);
					JSONArray places = responseJSON.getJSONArray("results");
					//Create a list of sorted places by distance from current location
					List<String> suggestionList = getPlacesInOrder(places);
					result.values = suggestionList;
					result.count = suggestionList.size();
					return result;
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			clear();
			if(results != null)	{
				if(results.count > 0)	{
					@SuppressWarnings("unchecked")
					List<String> resultsStrings = (List<String>) results.values;
					for(String s : resultsStrings)	{
						add(s);	//addAll(); not working for some reason
					}
				}
			}
			notifyDataSetChanged();
		}

		/**
		 * Takes in the json array and returns a sorted array list
		 * @param places
		 * @return sorted list of places
		 */
		private ArrayList<String> getPlacesInOrder(JSONArray places)	{
			//JSONArray cannot be sorted, so build an arraylist from it
			ArrayList<JSONObject> results = new ArrayList<JSONObject>();
			for(int i = 0; i < places.length(); i++)	{
				JSONObject o = places.optJSONObject(i);
				results.add(o);
			}
			//Sort by distance
			Collections.sort(results, new SortByDistance());
			ArrayList<String> resultStrings = new ArrayList<String>();
			for(JSONObject jO : results)	{
				resultStrings.add(jO.optString("name"));
			}
			//Return the result string list
			return resultStrings;
		}

		/**
		 * This comparator builds the criteria for sorting the places list by distances.
		 * @author Napster
		 */
		private class SortByDistance implements Comparator<JSONObject>	{
			@Override
			public int compare(JSONObject lhs, JSONObject rhs) {
				try {
					JSONObject lhsGeoPosition = lhs.getJSONObject("geo_position");
					JSONObject rhsGeoPosition = rhs.getJSONObject("geo_position");
					/**
					 * Getting location values needs integration with Google Play service
					 * which takes more time and thus skipping current location with GoEuro
					 * location, since this test app's test scope will be from GoEuro office.
					 * 
					 * 		(52.525641,13.403733)	GoEuro Lat & long
					 */
					float distanceFromLHS = distance(52.525641,13.403733, lhsGeoPosition.getDouble("latitude"), lhsGeoPosition.getDouble("longitude"));
					float distanceFromRHS = distance(52.525641,13.403733, rhsGeoPosition.getDouble("latitude"), rhsGeoPosition.getDouble("longitude"));
					if(distanceFromLHS < distanceFromRHS)
						return -1;
					if(distanceFromLHS > distanceFromRHS)
						return 1;
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return 0;
			}
		}

		/**
		 * Calculates distance from one location to other in meters
		 * @param latOne
		 * @param longOne
		 * @param latTwo
		 * @param longTwo
		 * @return the distance in meters
		 */
		private float distance(double latOne, double longOne, double latTwo, double longTwo) {
			Location locOne = new Location("");
			locOne.setLatitude(latOne);
			locOne.setLongitude(longOne);
			Location locTwo = new Location("");
			locTwo.setLatitude(latTwo);
			locTwo.setLongitude(longTwo);
			return locOne.distanceTo(locTwo);
		}
	}
}
