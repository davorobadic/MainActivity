package mk.finki.ukim.jmm.staracarsija;

public class Venue {

	private long id;
	protected String ime;
	protected int tip;
	protected double rating;
	protected String coords;
	protected String venueId;
	protected String kategorija;
	
	public Venue(long id, String ime, int tip, double rating, String coords,
			String venueId, String kategorija) {
		super();
		this.id = id;
		this.ime = ime;
		this.tip = tip;
		this.rating = rating;
		this.coords = coords;
		this.venueId = venueId;
		this.kategorija = kategorija;
	}

	public Venue() {}

	public long getId() {
		return id;
	}

	public String getKategorija() {
		return kategorija;
	}

	public void setKategorija(String kategorija) {
		this.kategorija = kategorija;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public int getTip() {
		return tip;
	}

	public void setTip(int tip) {
		this.tip = tip;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public String getCoords() {
		return coords;
	}

	public void setCoords(String coords) {
		this.coords = coords;
	}

	public String getVenueId() {
		return venueId;
	}

	public void setVenueId(String venueId) {
		this.venueId = venueId;
	}
	
	//for the arrayadapter in the listview?
	@Override
	public String toString() {
		return ime + ", " + kategorija + ", " + venueId;
	}
	
}
