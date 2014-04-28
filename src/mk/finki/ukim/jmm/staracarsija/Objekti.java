package mk.finki.ukim.jmm.staracarsija;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class Objekti extends ListActivity {
	String nekojString ;
	private boolean daliTrebaUpgrade;
	private static Context context;
	private static final String TAG = "Objekti";
	ProgressDialog progress ;
		
	public final static String EXTRA_MESSAGE1 = "mk.finki.ukim.jmm.staracarsija.MESSAGE";
	float[] ratings ;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getApplicationContext();
		
		//VAZNO!
		//DA SE TESTIRA NA DEVICE, NA EMULATOROT "change" PRI PRVOTO VKLUCHUVANJE NA APLIKACIJATA
		//IMA VREDNOST TRUE
		//BI TREBALO DA IMA VREDNOST FALSE!
		
		SharedPreferences nPrefs = getSharedPreferences("dali_baza", MODE_PRIVATE);
		String nString = nPrefs.getString("dali_baza", "true");
		if (nString.equals("true")) {
			daliTrebaUpgrade = true;
		} else {
			daliTrebaUpgrade = false;
		}
		
		progress = new ProgressDialog(this);
		progress.setTitle("Loading");
		progress.setMessage("Локалите се вчитуваат ...");
		progress.show();
		
		Log.w("preferences", "OnCreate od Objekti");
	}
		
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String item = (String) getListAdapter().getItem(position);
		String[] parts = item.split(">>");
		String venueId = parts[3];
		
		Intent intent = new Intent(this, Details.class);
		intent.putExtra(EXTRA_MESSAGE1, venueId);
		startActivity(intent);
		
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		readWebpage();	
		
		//Metodot vo koj kje se postavi konekcija do bazata na podatoci.
		//Konekcijata se vospostavuva vo ovoj metod zatoa shto ova e metodot koj sledi po rekreiranjeto na
		//aktivnosta, odnosno po onStop(). Kodot za metodot onRestart kje bide postaven vo ovoj metod
		//zatoa shto neposredno po onRestart aktivnosta preminuva vo onStart
	}
	  
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	    
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		//Metod vo koj se zachuvuvaat podatocite na koj korisnikot ochekuva da
		//im bide napraveno auto-save
	}
	  	
	@Override
	protected void onStop() {
		super.onStop();
		
		/*SharedPreferences mPrefs = getSharedPreferences("change", MODE_PRIVATE);
		SharedPreferences.Editor mEditor = mPrefs.edit();
		mEditor.putString("change", "false").commit();*/
		
		//se zachuvuvaat site podatoci vo baza zatoa shto ova e posledniot metod pred da aktivnosta da 
		//bide unishtena so onDestroy ili da bide prenesena do onStart
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		Log.w("Lifecycle", "onDestroy od Objekti");
	}
	  	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}
	  
	@Override
	protected void onRestoreInstanceState(Bundle state) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(state);
	}	
				
	private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
		private LokaliDataSource datasource;
		
		@Override
		protected String doInBackground(String... urls) {
						
			datasource = new LokaliDataSource(Objekti.this);
			datasource.open();
			
			Intent intent = getIntent();
			String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
			
			String my = "";
			SharedPreferences mPrefs = getSharedPreferences("change", MODE_PRIVATE);
			String mString = mPrefs.getString("change", "false");    	        
			Log.w("change", mString);
				
			if(mString.equals("false")) {
				Log.w("Kluchno", "Vleze");
				if (daliTrebaUpgrade == true) {
					datasource.upgrade();
					SharedPreferences nPrefs = getSharedPreferences("dali_baza", MODE_PRIVATE);
					SharedPreferences.Editor nEditor = nPrefs.edit();
					nEditor.putString("dali_baza", "true").commit();
					daliTrebaUpgrade = false;
				}
				
				try {
					JSONParser jParser = new JSONParser();
        	        // getting JSON string from URL
        	        JSONObject json = jParser.getJSONFromUrl(urls[0]);
    	            // Getting Array of Contacts 
    	            //1JSONArray response = json.getJSONObject("response").getJSONArray("groups");
    	            JSONObject response = json.getJSONObject("response");
    	            JSONArray venues = response.getJSONArray("venues");
        	        
    	            // looping through All Venues
    	            for(int i = 0; i < venues.length(); i++){
    	            	//1treba c
    	            	JSONObject cc = venues.getJSONObject(i);
    	 
    	                // Storing each json item in variable
    	                //1JSONArray venues = c.getJSONArray("items");
    	                //1for(int j = 0; j < venues.length(); j++){
    	                	
    	                	//1JSONObject cc = venues.getJSONObject(j);
    	                	
    	                	String id = cc.getString("id");
    	                	String name = cc.getString("name");
    	                	JSONObject loc = cc.getJSONObject("location");
    	                	String ll = loc.getString("lat")+","+loc.getString("lng");
    	                	JSONObject stats = cc.getJSONObject("stats");
    	                	float rating = stats.getInt("checkinsCount")/2;
    	                	JSONArray cat = cc.getJSONArray("categories");
    	                	JSONObject obj0 = cat.getJSONObject(0);
    	                	String imeCat = obj0.getString("name");
    	                	
    	                	Venue venue = new Venue();
    	                	venue.setIme(name);
    	                	venue.setVenueId(id);
    	                	venue.setKategorija(imeCat);
    	    	
    	    			    if(imeCat.contains("Restaurant") || imeCat.contains("Pizza") ||
    	    			    		imeCat.contains("Snack") || imeCat.contains("Dessert") || imeCat.contains("BBQ")) {
    	    			    	venue.setTip(0);
    	    			    }
    	    			    else if(imeCat.contains("Caf") || imeCat.contains("Coffee") || imeCat.contains("Bar") ||
    	    			    		imeCat.contains("Tea") || imeCat.contains("Beer") || imeCat.contains("Lounge") 
    	    			    		|| imeCat.contains("Pub") || imeCat.contains("Nightlife") || imeCat.contains("Club")) {
    	    			    	venue.setTip(1);
    	    			    }
    	    			    else if(imeCat.contains("Antique") || imeCat.contains("Gold")) {
    	    			    	venue.setTip(2);
    	    			    } else if (imeCat.contains("Bridge") || imeCat.contains("Opera") || 
    	    			    		imeCat.contains("Theater") || imeCat.contains("Historic")) {
    	    			    	venue.setTip(3);
    	    			    }
    	    			    else {
    	    			    	venue.setTip(4);
    	    			    }
    	    			  
    	    			    venue.setRating(rating);
    	    			    venue.setCoords(ll);
    	    			    
    	    			    datasource.createVenue(venue);
    	    			    
    	                	if(i ==0 ){
    	                		my = my + name;
    	                	}
    	                	my = my + " - " + name ;
    	                }
    	                
       	            //1}
    	            
    	        } catch (JSONException e) {
    	            e.printStackTrace();
    	        }
				SharedPreferences.Editor mEditor = mPrefs.edit();
				mEditor.putString("change", "true").commit();
			}
    	        

			List<Venue> values1 = datasource.getAllVenues();
			for (int i = 0; i < values1.size(); i++) {
			}
			ratings = new float[values1.size()];
			
			String[] values = new String[]{};
			my = "";
			
			
			if (message.equals("Сите")) {
				for (int i = 0; i < values1.size(); i++) {
					if (i == 0) {
						my = values1.get(i).getIme() + ">>" + values1.get(i).getKategorija() + ">>" +
								values1.get(i).getCoords() + ">>" + values1.get(i).getVenueId() + 
								">>" + String.valueOf(values1.get(i).getRating()/2);
						ratings[0] = (float) (values1.get(i).getRating()/2);
					} else {
						my += "--" + values1.get(i).getIme() + ">>" + values1.get(i).getKategorija() + ">>" +
								values1.get(i).getCoords() + ">>" + values1.get(i).getVenueId() + 
								">>" + String.valueOf(values1.get(i).getRating()/2);
						ratings[i] = (float) (values1.get(i).getRating()/2);
					}
				}
			} else if (message.equals("Ресторани")) {
				int brojac = 0;
				for (int i = 0; i < values1.size(); i++) {
					if (values1.get(i).getTip() == 0) {
						if (brojac == 0) {
							my = values1.get(i).getIme() + ">>" + values1.get(i).getKategorija() + ">>" +
									values1.get(i).getCoords() + ">>" + values1.get(i).getVenueId() + 
									">>" + String.valueOf(values1.get(i).getRating()/2);
							brojac++;
							ratings[0] = (float) (values1.get(i).getRating()/2);
						} else {
							my += "--" + values1.get(i).getIme() + ">>" + values1.get(i).getKategorija() + ">>" +
									values1.get(i).getCoords() + ">>" + values1.get(i).getVenueId() + 
									">>" + String.valueOf(values1.get(i).getRating()/2);
							ratings[i] = (float) (values1.get(i).getRating()/2);
						}
					}
				}
			} else if (message.equals("Кафулиња и барови")) {
				int brojac = 0;
				for (int i = 0; i < values1.size(); i++) {
					if (values1.get(i).getTip() == 1) {
						if (brojac == 0) {
							my = values1.get(i).getIme() + ">>" + values1.get(i).getKategorija() + ">>" +
									values1.get(i).getCoords() + ">>" + values1.get(i).getVenueId() + 
									">>" + String.valueOf(values1.get(i).getRating()/2);
							brojac++;
							ratings[0] = (float) (values1.get(i).getRating()/2);
						} else {
							my += "--" + values1.get(i).getIme() + ">>" + values1.get(i).getKategorija() + ">>" +
									values1.get(i).getCoords() + ">>" + values1.get(i).getVenueId() + 
									">>" + String.valueOf(values1.get(i).getRating()/2);
							ratings[i] = (float) (values1.get(i).getRating()/2);
						}
					}
				}
			} else if (message.equals("Занаетчии и Антикварници")) {
				int brojac = 0;
				for (int i = 0; i < values1.size(); i++) {
					if (values1.get(i).getTip() == 2) {
						if (brojac == 0) {
							my = values1.get(i).getIme() + ">>" + values1.get(i).getKategorija() + ">>" +
									values1.get(i).getCoords() + ">>" + values1.get(i).getVenueId() + 
									">>" + String.valueOf(values1.get(i).getRating()/2);
							brojac++;
							ratings[0] = (float) (values1.get(i).getRating()/2);
						} else {
							my += "--" + values1.get(i).getIme() + ">>" + values1.get(i).getKategorija() + ">>" +
									values1.get(i).getCoords() + ">>" + values1.get(i).getVenueId() + 
									">>" + String.valueOf(values1.get(i).getRating()/2);
							ratings[i] = (float) (values1.get(i).getRating()/2);
						}
					}
				}
			} else if (message.equals("Споменици, Музеи, Култура")) {
				int brojac = 0;
				for (int i = 0; i < values1.size(); i++) {
					if (values1.get(i).getTip() == 3) {
						if (brojac == 0) {
							my = values1.get(i).getIme() + ">>" + values1.get(i).getKategorija() + ">>" +
									values1.get(i).getCoords() + ">>" + values1.get(i).getVenueId() + 
									">>" + String.valueOf(values1.get(i).getRating()/2);
							brojac++;
							ratings[0] = (float) (values1.get(i).getRating()/2);
						} else {
							my += "--" + values1.get(i).getIme() + ">>" + values1.get(i).getKategorija() + ">>" +
									values1.get(i).getCoords() + ">>" + values1.get(i).getVenueId() + 
									">>" + String.valueOf(values1.get(i).getRating()/2);
							ratings[i] = (float) (values1.get(i).getRating()/2);
						}
					}
				}
			} else {
				for (int i = 0; i < values1.size(); i++) {
					int brojac = 0;
					if (values1.get(i).getTip() == 4) {
						if (brojac == 0) {
							my = values1.get(i).getIme() + ">>" + values1.get(i).getKategorija() + ">>" +
									values1.get(i).getCoords() + ">>" + values1.get(i).getVenueId() + 
									">>" + String.valueOf(values1.get(i).getRating()/2);
							brojac++;
							ratings[0] = (float) (values1.get(i).getRating()/2);
						} else {
							my += "--" + values1.get(i).getIme() + ">>" + values1.get(i).getKategorija() + ">>" +
									values1.get(i).getCoords() + ">>" + values1.get(i).getVenueId() + 
									">>" + String.valueOf(values1.get(i).getRating()/2);
							ratings[i] = (float) (values1.get(i).getRating()/2);
						}
					}
				}
			}
			return my;
		}

		@Override
		protected void onPostExecute(String result) {
			
			RatingAdapter adapter = new RatingAdapter(Objekti.this, R.layout.activity_objekti, result.split("--"));
			setListAdapter(adapter);
				
			progress.dismiss();
		}
	}

	public void readWebpage() {
		DownloadWebPageTask task = new DownloadWebPageTask();
		
		Calendar cal = Calendar.getInstance();
		int den = cal.get(Calendar.DATE);
		int mesec = cal.get(Calendar.MONTH) + 1;
		String month=String.format("%02d", mesec);
		int godina = cal.get(Calendar.YEAR);
		String datum = String.valueOf(godina) + month + String.valueOf(den);
		
		String clientId = "OQ3UZKC5P33EEWKP3CWRCQYZZZ020LZ5I5EN4BLRR4MK23IC"; 
		String clientSecret = "CY0GGCJ0CY25DMGYIGEWBJ1LRF3AVCTS1M2N3KMLWR1UJ5QA"; 
		task.execute(new String[] { "https://api.foursquare.com/v2/venues/search?ll=42.000453,21.436043" +
				"&radius=550&intent=browse" + "&v=" + datum +
				"&client_id=" + clientId + "&client_secret=" + clientSecret });
		//task.execute(new String[] { "https://api.foursquare.com/v2/venues/search?ll=42.000453,21.436043&client_id=OQ3UZKC5P33EEWKP3CWRCQYZZZ020LZ5I5EN4BLRR4MK23IC&client_secret=CY0GGCJ0CY25DMGYIGEWBJ1LRF3AVCTS1M2N3KMLWR1UJ5QA" });

	}
	
	public class ViewHolder {
		public TextView tv;
		public TextView tv2;
		public ImageView imageView;
		public RatingBar rb;
	}
			  
	class RatingAdapter extends ArrayAdapter<String> {
		Context ctx;
		String[] values;
		
		public RatingAdapter(Context context, int textViewResourceId, String[] objects) {
			super(context, textViewResourceId, objects);
			ctx = context;
			values = objects;
		}
		
		ViewHolder vh = new ViewHolder();
		
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			Intent intent = getIntent();
			String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
			
			if(row==null) { // Object reuse
				LayoutInflater inflater = getLayoutInflater();
				row = inflater.inflate(R.layout.activity_objekti, parent, false);
			}
			
			
			ImageView imageView = (ImageView) row.findViewById(R.id.icona);
			if (message.startsWith("Ресторан")) {
				imageView.setImageResource(R.drawable.restoran);
			} 
			/*else if (message.startsWith("Златар")) {
				imageView.setImageResource(R.drawable.gold);
			}*/ else if (message.startsWith("Споменици")) {
				imageView.setImageResource(R.drawable.spomenik);
			} else if (message.startsWith("Занаетчии")) {
				imageView.setImageResource(R.drawable.zanaetcii50);
			} else if (message.startsWith("Останато")) {
				imageView.setImageResource(R.drawable.ic_launcher);
			} else {
				imageView.setImageResource(R.drawable.kafe);
			}
			
			TextView tv = (TextView)row.findViewById(R.id.textView1);
			TextView tv2 = (TextView) row.findViewById(R.id.textView2);
			String str = values[position];
			String[] parts = str.split(">>");
			try {
				tv.setText(parts[0]);
				tv2.setText(parts[1]);
			} catch (ArrayIndexOutOfBoundsException e) {
				tv.setText("Zosto?");
				tv2.setText("Ama stvarno, zoshto?");
			}
			//tv.setText(values[position]);
			
			//float rating = Float.parseFloat(parts[4]);
			/*RatingBar rb = (RatingBar) row.findViewById(R.id.ratingBar1);
			//rb.setRating(ratings[position]);
			rb.setRating(rating);
			rb.setTag(position);*/
			/*rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
				public void onRatingChanged(RatingBar ratingBar, float rating,
						boolean fromUser) {
					if(!fromUser) return;
					int index = (Integer)(ratingBar.getTag());
					ratings[index] = rating;
				}
			});*/
			return row;
		}
		
	}
}
