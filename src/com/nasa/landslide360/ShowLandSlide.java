package com.nasa.landslide360;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.ProgressDialog;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;


public class ShowLandSlide extends Activity {
	 
    // Google Map
    private GoogleMap googleMap;
    ProgressDialog pDialog;
    List<MarkerOptions> markers = new LinkedList<MarkerOptions>();
    List<LandSlideData> datas = new LinkedList<LandSlideData>();
    JSONArray dataArray = null;
    
    MarkerOptions marker;
    JSONParser jParser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landslidemap);
 jParser = new JSONParser();
        try {
        	
            // Loading map
        	GetPlaceAsyncTask categoryTask = new GetPlaceAsyncTask();
			categoryTask.execute();
 
        } catch (Exception e) {
            e.printStackTrace();
        }
        /** AsyncTask to download json data */
   
    }
 
    /**
     * function to load map. If map is not created it will create it for you
     * */
    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();
 
            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }else{
            	
            	
            	/*double latitude = 23.806863;
            	double longitude = 90.36039 ;*/
            	 
            	// create marker
            	
            	for(LandSlideData lsd: datas){
            		
            		
            		marker = new MarkerOptions().position(new LatLng(Double.parseDouble(lsd.getLatitude()), Double.parseDouble(lsd.getLongitude()))).title(lsd.getEvent_type());
               	 marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.m));
               	 
               	 
               	
               	
               	 
               //	markers.add(marker);
               	
               	
               	googleMap.addMarker(marker);
            		
            		
            	}
            	 
            	googleMap.setMyLocationEnabled(true);
            	googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            	
            	
            	googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
            		
                    

					@Override
					public void onInfoWindowClick(Marker arg0) {
						Toast.makeText(getApplicationContext(), ""+arg0.getPosition(),Toast.LENGTH_LONG).show();
						
						
					}
                });
            	
            	
            }
        }
    }
 
    @Override
    protected void onResume() {
        super.onResume();
       
    }
 	private class GetPlaceAsyncTask extends AsyncTask<String, Integer, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ShowLandSlide.this);
			pDialog.setMessage("Listing Categories...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();

		}

		
		@Override
		protected Void doInBackground(String... url) {
			try {
				
				JSONObject json = jParser.getJSONFromUrl("http://mobioapp.net/apps/landslide360/history_landslide.php");

			
					dataArray = json.getJSONArray("mpa_data");
					
					/*Toast.makeText(getApplicationContext(), dataArray.toString(), Toast.LENGTH_LONG).show();
					*/
					for (int i = 0; i < dataArray.length(); i++) {
						JSONObject c = dataArray.getJSONObject(i);
						
						LandSlideData  lsd = new LandSlideData();
						lsd.setId(c.getString("id"));
						lsd.setCountry(c.getString("country"));
						lsd.setYear(c.getString("year"));
						lsd.setDate(c.getString("date"));
						lsd.setLatitude(c.getString("latitude"));
						lsd.setLongitude(c.getString("longitude"));
						lsd.setEvent_type(c.getString("event_type"));
						lsd.setTrigger(c.getString("trigger"));
						lsd.setFatalities(c.getString("fatalities"));
						lsd.setLocation(c.getString("location"));
						lsd.setSource(c.getString("source"));
						lsd.setRegion(c.getString("fatalities"));
						
						
						datas.add(lsd);
						
					}
					

					
				

			} catch (Exception e) {
				System.out.println("Background Task ::");
				e.printStackTrace();
			}

			return null;

		}

		// OnPostExecute - Set the ListAdapter & Start ImageLoader AsyncTask
		@Override
		protected void onPostExecute(Void params) {

			
			pDialog.dismiss();

			//Toast.makeText(getApplicationContext(), datas.toString(), Toast.LENGTH_LONG).show();
		
			initilizeMap();

		}
	}
	
 
}