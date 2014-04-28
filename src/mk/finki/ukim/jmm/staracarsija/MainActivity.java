package mk.finki.ukim.jmm.staracarsija;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import mk.finki.ukim.jmm.staracarsija.ListaFragment.OnItemLongClickedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//ListActivity
public class MainActivity extends Activity implements OnItemLongClickedListener {

	public final static String EXTRA_MESSAGE = "mk.finki.ukim.jmm.staracarsija.MESSAGE";
	private LokaliDataSource datasource;

	private GoogleMap map;
	private LocationManager locationManager;
	private String provider;
	
	public HashMap<String, ArrayList<Marker>> markeriFinal;
	ArrayList<Marker> restorani;
	ArrayList<Marker> kafulinja;
	ArrayList<Marker> spomenici;
	ArrayList<Marker> zanaetcii;
	ArrayList<Marker> ostanato;
	
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);    
		setContentView(R.layout.activity_main_glavna);	   

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		map.getUiSettings().setMyLocationButtonEnabled(true);
		map.setMyLocationEnabled(true);
		map.setBuildingsEnabled(false);
	  
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		//Define the criteria how to select the location provider - use default
		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		Location location = locationManager.getLastKnownLocation(provider);
		
		markeriFinal = new HashMap<String, ArrayList<Marker>>();
		restorani = new ArrayList<Marker>();
		kafulinja = new ArrayList<Marker>();
		spomenici = new ArrayList<Marker>();
		zanaetcii = new ArrayList<Marker>();
		ostanato = new ArrayList<Marker>();
	  
	    // Use the SimpleCursorAdapter to show the
	    // elements in a ListView

		/*String[] values = new String[] { "Ресторани", "Кафулиња и барови",
        "Споменици, Музеи, Култура",
        "Занаетчии и Антикварници",
        "Останато (Пазар, Бутици)", "Сите"};
    
	  	MySimpleAdapter adapter = new MySimpleAdapter(this, values);
	  	setListAdapter(adapter);
		 */  
	  
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		map.clear();
		Log.w("MainActivity", "onStart");
		  
		//MarkersTask task = new MarkersTask();
		//task.execute();
		datasource = new LokaliDataSource(MainActivity.this);
		datasource.open();
		List<Venue> venues = datasource.getAllVenues();
		
		int restoraniCount = 0;
		int kafulinjaCount = 0;
		int spomeniciCount = 0;
		int zanaetciiCount = 0;
		int ostanatoCount = 0;
		
		for (int i = 0; i < venues.size(); i++) {
			if (venues.get(i).getTip() == 0 ) {
				String ll = venues.get(i).getCoords();
				String[] parts = ll.split(",");
				double lat = Double.parseDouble(parts[0]);
				double lng = Double.parseDouble(parts[1]);
				
				Marker marker;
				if (restoraniCount < 3) {
					marker = map.addMarker(new MarkerOptions()
						.position(new LatLng(lat, lng))
						.title(venues.get(i).getIme())
						.snippet(venues.get(i).getKategorija()));
				} else {
					marker = map.addMarker(new MarkerOptions()
						.position(new LatLng(lat, lng))
						.title(venues.get(i).getIme())
						.snippet(venues.get(i).getKategorija())
						.visible(false));
				}
				restoraniCount++;
				restorani.add(marker);
				markeriFinal.put("restorani", restorani);
			} else if (venues.get(i).getTip() == 1) {
				String ll = venues.get(i).getCoords();
				String[] parts = ll.split(",");
				double lat = Double.parseDouble(parts[0]);
				double lng = Double.parseDouble(parts[1]);
				
				Marker marker;
				if (kafulinjaCount < 3) {
					marker = map.addMarker(new MarkerOptions()
						.position(new LatLng(lat, lng))
						.title(venues.get(i).getIme())
						.snippet(venues.get(i).getKategorija())
						.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
				} else {
					marker = map.addMarker(new MarkerOptions()
						.position(new LatLng(lat, lng))
						.title(venues.get(i).getIme())
						.snippet(venues.get(i).getKategorija())
						.visible(false)
						.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
				}
				kafulinjaCount++;
				kafulinja.add(marker);
				markeriFinal.put("kafulinja", kafulinja);
			} else if (venues.get(i).getTip() == 2) {
				String ll = venues.get(i).getCoords();
				String[] parts = ll.split(",");
				double lat = Double.parseDouble(parts[0]);
				double lng = Double.parseDouble(parts[1]);
				
				Marker marker;
				if (zanaetciiCount < 3) {
					marker = map.addMarker(new MarkerOptions()
						.position(new LatLng(lat, lng))
						.title(venues.get(i).getIme())
						.snippet(venues.get(i).getKategorija())
						.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
				} else {
					marker = map.addMarker(new MarkerOptions()
						.position(new LatLng(lat, lng))
						.title(venues.get(i).getIme())
						.snippet(venues.get(i).getKategorija())
						.visible(false)
						.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
				}
				zanaetciiCount++;
				zanaetcii.add(marker);
				markeriFinal.put("zanaetcii", zanaetcii);
			} else if (venues.get(i).getTip() == 3) {
				String ll = venues.get(i).getCoords();
				String[] parts = ll.split(",");
				double lat = Double.parseDouble(parts[0]);
				double lng = Double.parseDouble(parts[1]);
				
				Marker marker;
				if (spomeniciCount < 3) {
					marker = map.addMarker(new MarkerOptions()
						.position(new LatLng(lat, lng))
						.title(venues.get(i).getIme())
						.snippet(venues.get(i).getKategorija())
						.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
				} else {
					marker = map.addMarker(new MarkerOptions()
						.position(new LatLng(lat, lng))
						.title(venues.get(i).getIme())
						.snippet(venues.get(i).getKategorija())
						.visible(false)
						.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
				}
				spomeniciCount++;
				spomenici.add(marker);
				markeriFinal.put("spomenici", spomenici);
			} else {
				String ll = venues.get(i).getCoords();
				String[] parts = ll.split(",");
				double lat = Double.parseDouble(parts[0]);
				double lng = Double.parseDouble(parts[1]);
				
				Marker marker;
				if (ostanatoCount < 3) {
					marker = map.addMarker(new MarkerOptions()
						.position(new LatLng(lat, lng))
						.title(venues.get(i).getIme())
						.snippet(venues.get(i).getKategorija())
						.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
				} else {
					marker = map.addMarker(new MarkerOptions()
						.position(new LatLng(lat, lng))
						.title(venues.get(i).getIme())
						.snippet(venues.get(i).getKategorija())
						.visible(false)
						.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
				}
				ostanatoCount++;
				ostanato.add(marker);
				markeriFinal.put("ostanato", ostanato);
			}
		}
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
			// TODO Auto-generated method stub
			super.onStop();
			datasource.close();
			
			//se zachuvuvaat site podatoci vo baza zatoa shto ova e posledniot metod pred da aktivnosta da 
			//bide unishtena so onDestroy ili da bide prenesena do onStart
		}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		Log.w("Lifecycle", "onDestroy od MainActivity");
		
		SharedPreferences mPrefs = getSharedPreferences("change", MODE_PRIVATE);
		SharedPreferences.Editor mEditor = mPrefs.edit();
		mEditor.putString("change", "false").commit();
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

	@Override
	public void onItemLongClicked(String kategorija) {
		
		for (String key : markeriFinal.keySet()) {
			ArrayList<Marker> markers = markeriFinal.get(key);
			for (Marker marker : markers) {
				marker.setVisible(false);
			}
		}
		if (kategorija == "Ресторани") {
			ArrayList<Marker> markers = markeriFinal.get("restorani");
			String str = "Goleminata e :" + markers.size();
			Log.w("ArrayList size", str);
			for (Marker marker : markers) {
				marker.setVisible(true);
				Log.w("restorani", "restorani: " + marker.getTitle());
			}
		} else if (kategorija.startsWith("Кафулиња")) {
			ArrayList<Marker> markers = markeriFinal.get("kafulinja");
			String str = "Goleminata e :" + markers.size();
			Log.w("ArrayList size", str);
			for (Marker marker : markers) {
				marker.setVisible(true);
				Log.w("kafulinja", "kafulinja: " + marker.getTitle());
				Log.w("Marker", marker.getTitle());
			}
		} else if (kategorija.startsWith("Споменици")) {
			ArrayList<Marker> markers = markeriFinal.get("spomenici");
			String str = "Goleminata e :" + markers.size();
			Log.w("ArrayList size", str);
			for (Marker marker : markers) {
				marker.setVisible(true);
				Log.w("spomenici", "spomenici: " + marker.getTitle());
			}
		} else if (kategorija.startsWith("Занаетчии")) {
			ArrayList<Marker> markers = markeriFinal.get("zanaetcii");
			String str = "Goleminata e :" + markers.size();
			Log.w("ArrayList size", str);
			for (Marker marker : markers) {
				marker.setVisible(true);
				Log.w("zanaetcii", "zanaetcii: " + marker.getTitle());
			}
		} else if (kategorija.startsWith("Останато")) {
			ArrayList<Marker> markers = markeriFinal.get("ostanato");
			String str = "Goleminata e :" + markers.size();
			Log.w("ArrayList size", str);
			for (Marker marker : markers) {
				marker.setVisible(true);
				Log.w("ostanato", "ostanato: " + marker.getTitle());
			}
		} 
	}

  /*@Override
  protected void onListItemClick(ListView l, View v,int position, long id) {
	 
	        
	        String item = (String) getListAdapter().getItem(position);
            
    		Intent intent = new Intent(MainActivity.this, Objekti.class);
    		intent.putExtra(EXTRA_MESSAGE, item);
    		startActivity(intent);
  }*/
} 