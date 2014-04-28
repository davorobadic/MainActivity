package mk.finki.ukim.jmm.staracarsija;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.PriorityQueue;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

public class Details extends Activity {

	private String fsqLink;
	private String twitterLink;
	private String websiteLink;
	
	TextView tvKategorii;
	RatingBar ratingBar;
	Button foursquare;
	Button twitter;
	Button website;
	Spinner spinner;
	
	LinearLayout ll;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		ll = (LinearLayout) findViewById(R.id.layout);
		/*TextView tv = new TextView(this);
		tv.setText("Proba");
		ll.addView();*/
		tvKategorii = (TextView) findViewById(R.id.kategorii);
		ratingBar = (RatingBar) findViewById(R.id.rating);
		foursquare = (Button) findViewById(R.id.foursquare);
		foursquare.setText("Foursquare");
		spinner = (Spinner) findViewById(R.id.spinner);
		/*twitter = (Button) findViewById(R.id.twitter);
		website = (Button) findViewById(R.id.website);
		website.setVisibility(View.INVISIBLE);*/
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		readWebPage();
	}

	private void readWebPage() {
		DownloadWebPageTask task = new DownloadWebPageTask();
		
		Calendar cal = Calendar.getInstance();
		int den = cal.get(Calendar.DATE);
		int mesec = cal.get(Calendar.MONTH) + 1;
		String month=String.format("%02d", mesec);
		int godina = cal.get(Calendar.YEAR);
		String datum = String.valueOf(godina) + month + String.valueOf(den);
		
		Intent intent = getIntent();
		String venueId = intent.getStringExtra(Objekti.EXTRA_MESSAGE1); 
		Log.w("THE MESSAGE", venueId);
		
		String clientId = "OQ3UZKC5P33EEWKP3CWRCQYZZZ020LZ5I5EN4BLRR4MK23IC"; 
		String clientSecret = "CY0GGCJ0CY25DMGYIGEWBJ1LRF3AVCTS1M2N3KMLWR1UJ5QA";
		
		task.execute(new String[] { "https://api.foursquare.com/v2/venues/" + venueId +
				"?v=" + datum +
				"&client_id=" + clientId + "&client_secret=" + clientSecret });
	}
	
	private class DownloadWebPageTask extends AsyncTask<String, Void, VenueDetails> {

		@Override
		protected VenueDetails doInBackground(String... params) {
			
			VenueDetails vd = null;
			
			try {
				Log.w("doInBackground", "Pred konekcijata");
				JSONParser jParser = new JSONParser();
    	        // getting JSON string from URL
				Log.w("ConnectionString", params[0]);
    	        JSONObject json = jParser.getJSONFromUrl(params[0]);
    	        Log.w("doInBackground", "Uspeshen obid za konekcija");
	            JSONObject venue = json.getJSONObject("response").getJSONObject("venue");
	            String name = venue.getString("name");
	            JSONObject contact = venue.getJSONObject("contact");
	            if (contact.has("twitter")) {
	            	twitterLink = "https://twitter.com/" + contact.getString("twitter");
	            } else {
	            	twitterLink = "nema";
	            }
	            fsqLink = venue.getString("canonicalUrl");
	            if (venue.has("url")) {
	            	websiteLink = venue.getString("url");
	            } else {
	            	websiteLink = "nema";
	            }
	            Log.w("websiteLink", websiteLink);
	            
	           float rating;
	           if (venue.has("rating")) {
	            	rating = (float) venue.getDouble("rating");
	            	Log.w("rating", String.valueOf(rating));
	           } else {
	        	   rating = 0;
	           }
	           
	            
	            //KATEGORII
	            JSONArray categories =  venue.getJSONArray("categories");
	            String[] kategorii = new String[categories.length()];
	            for (int i = 0; i < categories.length(); i++) {
	            	JSONObject kategorija = categories.getJSONObject(i);
	            	kategorii[i] = kategorija.getString("name");
	            }
	            String rez = "";
	            for (int i = 0; i < kategorii.length; i++) {
	            	if (i == 0) {
	            		rez = kategorii[0];
	            	} else {
	            		rez += ", " + kategorii[i];
	            	}
	            }
	            
	            //SLIKI
	            JSONObject photos = venue.getJSONObject("photos");
	            JSONArray photosGroups = photos.getJSONArray("groups");
	            JSONObject photosObject = photosGroups.getJSONObject(0);
	            JSONArray photosItems = photosObject.getJSONArray("items");
	            int photosCount = photosItems.length();
	            //se zemaat samo najnovite 10 sliki
	            int zaRandom;
	            if (photosCount < 10) {
	            	zaRandom = photosCount;
	            } else {
	            	zaRandom = 10;
	            }
	            Random random = new Random();
	            int rand = random.nextInt(zaRandom);
	            int rand2 = random.nextInt(zaRandom);
	            JSONObject photo = photosItems.getJSONObject(rand);
	            String prefix = photo.getString("prefix");
	            String suffix = photo.getString("suffix");
	            JSONObject photo2 = photosItems.getJSONObject(rand2);
	            String prefix2 = photo.getString("prefix");
	            String suffix2 = photo.getString("suffix");
	            
				//za dimenziite na ekranot
				Display display = getWindowManager().getDefaultDisplay();
				Point size = new Point();
				display.getSize(size);
				int width = size.x;
				int height = size.y;
				String dimenzii = width + "x" + height;
				
				int width2 = width * 2/3;
				int height2 = height * 2/3;
				String dimenzii2 = width2 + "x" + height2;
				
				String imageURL = prefix + dimenzii + suffix;
				Bitmap image = null; 
				InputStream is = new java.net.URL(imageURL).openStream();
				image = BitmapFactory.decodeStream(is);
				
				String imageURL2 = prefix + dimenzii + suffix;
				Bitmap image2 = null; 
				InputStream is2 = new java.net.URL(imageURL).openStream();
				image2 = BitmapFactory.decodeStream(is);
				
	            //TIPS
	            class Komentar implements Comparable<Komentar> {
	            	String text;
	            	int likes;
					
	            	public Komentar (String text, int likes) {
	            		this.text = text;
	            		this.likes = likes;
	            	}
	            	
	            	@Override
					public int compareTo(Komentar another) {
						return another.likes - this.likes;
					} 
	            	
	            	@Override
	            	public String toString() {
	            		return text;
	            	}
	            }
	            
	            PriorityQueue<Komentar> pq = new PriorityQueue<Komentar>();
	            JSONObject tips = venue.getJSONObject("tips");
	            int count = tips.getInt("count");
	            
	            JSONArray groups = tips.getJSONArray("groups");
	            String asd = groups.length() + "";
	            Log.w("Groups", asd);
	            if(groups.length() == 0) {
	            	String[] komentari = new String[1];
	            	komentari[0] = "Нема коментари за локалот";
	            	vd = new VenueDetails(name, rating, fsqLink, twitterLink, websiteLink, rez, komentari,
		            		image, image2);
	            } else {
		            JSONObject object = groups.getJSONObject(0);
		            JSONArray items = object.getJSONArray("items");
		            if (items.length() == 0) {
		            	Komentar komentar = new Komentar("Нема коментари за локалот", 0);
		            } 
		            for (int i = 0; i < items.length(); i++) {
		            	JSONObject tip = items.getJSONObject(i);
		            	String text = tip.getString("text");
		            	int likes = tip.getJSONObject("likes").getInt("count");
		            	Komentar komentar = new Komentar(text, likes);
		            	pq.add(komentar);
		            }
		            String[] komentari = new String[pq.size()];
		            int i = 0;
		            for (Komentar komentar : pq) {
						komentari[i] = komentar.toString();
						i++;
					}
		            vd = new VenueDetails(name, rating, fsqLink, twitterLink, websiteLink, rez, komentari,
		            		image, image2);
	            }
	            
	            
	            
  			} catch (JSONException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return vd;
		}
		
		@Override
		protected void onPostExecute(VenueDetails result) {
			tvKategorii.setText(result.kategorii);
			ratingBar.setRating(result.rating);
			//foursquare.setText(result.foursquare);
			
			Log.w("onPostExecute", "onPostExecute");
			
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(Details.this,
					R.layout.spinner_layout, R.id.text_for_spinner, result.komentari);
			//adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
			spinner.setAdapter(adapter);
			BitmapDrawable dr2 = new BitmapDrawable(result.image2);
			//spinner.setBackgroundColor(Color.GRAY);
			
			float rating = result.rating;
			Log.w("rating - onpostexecute", String.valueOf(rating));
			
			BitmapDrawable dr = new BitmapDrawable(result.image);
			LinearLayout ll_pozadina = (LinearLayout) findViewById(R.id.zaPozadina);
			ll_pozadina.setBackground(dr);
	
			if (!(result.twitter.equals("nema"))) {
				twitter = new Button(getApplicationContext());
				twitter.setText("Twitter");
				//twitter.setPadding(10, 10, 10, 10);
				//twitter.setOnClickListener(l)
				ll.addView(twitter);
			}
			if (!(result.website.equals("nema"))) {
				website = new Button(getApplicationContext());
				website.setText("Website");
				//website.setPadding(10, 10, 10, 10);
				//website.setOnClickListener(l)
				ll.addView(website);
			}
		}

	}
		
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.details, menu);
		return true;
	}

}
