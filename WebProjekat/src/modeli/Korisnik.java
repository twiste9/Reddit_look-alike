package modeli;

import java.io.Serializable;
import java.util.ArrayList;

public class Korisnik implements Serializable {
	/**
	 * 
	 */
	public Korisnik(){
		
	}
	private static final long serialVersionUID = 1L;
	private String korisnicko;
	private String lozinka;
	private String ime;
	private String prezime;
	private String uloga;
	private String telefon;
	private String datumreg;
	private String email;
	private ArrayList<Podforum> forumi;
	private ArrayList<Tema> teme;
	private ArrayList<Komentar> komentari;
	private ArrayList<Komentar> lajkovaniKomentari;
	private ArrayList<Komentar> dislajkovaniKomentari;
	private ArrayList<Poruka> poruke;
	private ArrayList<Zalba> zalbe;
	
	public Korisnik(String korisnicko, String lozinka, String ime,
			String prezime, String uloga, String telefon, String datumreg,
			String email, ArrayList<Podforum> forumi, ArrayList<Tema> teme,
			ArrayList<Komentar> komentari, ArrayList<Poruka> poruke,
			ArrayList<Zalba> zalbe, int brojpozlajkova, int brojneglajkova) {
		super();
		this.korisnicko = korisnicko;
		this.lozinka = lozinka;
		this.ime = ime;
		this.prezime = prezime;
		this.uloga = uloga;
		this.telefon = telefon;
		this.datumreg = datumreg;
		this.email = email;
		this.forumi = forumi;
		this.teme = teme;
		this.komentari = komentari;
		this.poruke = poruke;
		this.zalbe = zalbe;
		this.lajkovaniKomentari = new ArrayList<Komentar>();
		this.dislajkovaniKomentari = new ArrayList<Komentar>();
	}
	public ArrayList<Komentar> getLajkovaniKomentari() {
		return lajkovaniKomentari;
	}
	public void setLajkovaniKomentari(ArrayList<Komentar> lajkovaniKomentari) {
		this.lajkovaniKomentari = lajkovaniKomentari;
	}
	public ArrayList<Komentar> getDislajkovaniKomentari() {
		return dislajkovaniKomentari;
	}
	public void setDislajkovaniKomentari(ArrayList<Komentar> dislajkovaniKomentari) {
		this.dislajkovaniKomentari = dislajkovaniKomentari;
	}
	public ArrayList<Zalba> getZalbe() {
		return zalbe;
	}
	public void setZalbe(ArrayList<Zalba> zalbe) {
		this.zalbe = zalbe;
	}
	
	public ArrayList<Poruka> getPoruke() {
		return poruke;
	}
	public void setPoruke(ArrayList<Poruka> poruke) {
		this.poruke = poruke;
	}
	public String getUloga() {
		return uloga;
	}
	public void setUloga(String uloga) {
		this.uloga = uloga;
	}
	
	
	public String getKorisnicko() {
		return korisnicko;
	}
	public void setKorisnicko(String korisnicko) {
		this.korisnicko = korisnicko;
	}
	public String getLozinka() {
		return lozinka;
	}
	public void setLozinka(String lozinka) {
		this.lozinka = lozinka;
	}
	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		this.ime = ime;
	}
	public String getPrezime() {
		return prezime;
	}
	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}
	
	public String getTelefon() {
		return telefon;
	}
	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}
	public String getDatumreg() {
		return datumreg;
	}
	public void setDatumreg(String datumreg) {
		this.datumreg = datumreg;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public ArrayList<Podforum> getForumi() {
		return forumi;
	}
	public void setForumi(ArrayList<Podforum> forumi) {
		this.forumi = forumi;
	}
	public ArrayList<Tema> getTeme() {
		return teme;
	}
	public void setTeme(ArrayList<Tema> teme) {
		this.teme = teme;
	}
	public ArrayList<Komentar> getKomentari() {
		return komentari;
	}
	public void setKomentari(ArrayList<Komentar> komentari) {
		this.komentari = komentari;
	}
	
}
