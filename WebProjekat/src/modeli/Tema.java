package modeli;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Tema implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public String podforum;
	public String naslov;
	public String tip;
	public String autor;
	public List<Komentar> komentari;
	public List<String> odreagovali;
	public List<String> lajkovi;
	public List<String> dislajkovi;
	public String sadrzaj;
	public String datum;
	public int like;
	public int dislike;

	
	public List<String> getLajkovi() {
		return lajkovi;
	}
	public void setLajkovi(ArrayList<String> lajkovi) {
		this.lajkovi = lajkovi;
	}
	public List<String> getDislajkovi() {
		return dislajkovi;
	}
	public void setDislajkovi(ArrayList<String> dislajkovi) {
		this.dislajkovi = dislajkovi;
	}

	public List<String> getOdreagovali() {
		return odreagovali;
	}
	public void setOdreagovali(ArrayList<String> odreagovali) {
		this.odreagovali = odreagovali;
	}
	public String getPodforum() {
		return podforum;
	}
	public void setPodforum(String podforum) {
		this.podforum = podforum;
	}
	public String getNaslov() {
		return naslov;
	}
	public void setNaslov(String naslov) {
		this.naslov = naslov;
	}
	public String getTip() {
		return tip;
	}
	public void setTip(String tip) {
		this.tip = tip;
	}
	public String getAutor() {
		return autor;
	}
	public void setAutor(String autor) {
		this.autor = autor;
	}
	public List<Komentar> getKomentari() {
		return komentari;
	}
	public void setKomentari(ArrayList<Komentar> komentari) {
		this.komentari = komentari;
	}
	public String getSadrzaj() {
		return sadrzaj;
	}
	public void setSadrzaj(String sadrzaj) {
		this.sadrzaj = sadrzaj;
	}
	public String getDatum() {
		return datum;
	}
	public void setDatum(String datum) {
		this.datum = datum;
	}
	public int getLike() {
		return like;
	}
	public void setLike(int like) {
		this.like = like;
	}
	public int getDislike() {
		return dislike;
	}
	public void setDislike(int dislike) {
		this.dislike = dislike;
	}
	public Tema(){
		this.lajkovi = new ArrayList<String>();
		this.dislajkovi = new ArrayList<String>();
	}
	public Tema(String podforum, String naslov, String tip, String autor,
			ArrayList<Komentar> komentari, ArrayList<String> odreagovali,
			String sadrzaj, String datum, int like, int dislike) {
		super();
		this.podforum = podforum;
		this.naslov = naslov;
		this.tip = tip;
		this.autor = autor;
		this.komentari = komentari;
		this.odreagovali = odreagovali;
		this.sadrzaj = sadrzaj;
		this.datum = datum;
		this.like = like;
		this.dislike = dislike;
		this.lajkovi = new ArrayList<String>();
		this.dislajkovi = new ArrayList<String>();
	}
	
	
	public int countComments(){
		return countRec(this.komentari);
	}
	private int countRec(List<Komentar> lista){
		int res = 0;
		res += lista.size();
		for(Komentar k : lista){
			res+=countRec(k.getPodkomentari());
		}
		return res;
	}
	public Komentar findParent(int roditelj) {
		// TODO Auto-generated method stub
		for(int j=0; j<this.getKomentari().size(); j++){
			if(myMethod(this.getKomentari().get(j), roditelj) != null){
				return myMethod(this.getKomentari().get(j), roditelj); 
			}
		}
		return null;
	}
	private Komentar myMethod(Komentar k, int roditelj) {
		// TODO Auto-generated method stub
		if(k.getId() == roditelj){
			return k;
		}
		Komentar res;
		for(int i=0; i<k.getPodkomentari().size(); i++){
			if(k.getPodkomentari().get(i).getId() == roditelj){
				return k.getPodkomentari().get(i);
			}else{
				res = myMethod(k.getPodkomentari().get(i), roditelj);
				if(res != null){
					return res;
				}
			}
		}
		return null;
	}
	
	public int popularity(){
		return this.countComments() + 5*(this.like - this.dislike);
	}
	
}

