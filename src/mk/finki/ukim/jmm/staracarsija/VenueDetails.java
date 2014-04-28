package mk.finki.ukim.jmm.staracarsija;

import android.graphics.Bitmap;

public class VenueDetails {
	public String name;
	float rating;
	
	public String foursquare;
	public String twitter;
	public String website;
	
	public String kategorii;
	public String[] komentari;
	
	Bitmap image;
	Bitmap image2;
	
	public VenueDetails(String name, float rating, String foursquare,
			String twitter, String website, String kategorii,
			String[] komentari, Bitmap image, Bitmap image2) {
		this.name = name;
		this.rating = rating;
		this.foursquare = foursquare;
		this.twitter = twitter;
		this.website = website;
		this.kategorii = kategorii;
		this.image = image;
		this.image2 = image2;
		this.komentari = komentari;
	}


	@Override
	public String toString() {		
		return name;
	}
	
}
