package modeli;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Komentar implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Komentar(){
		
	}
	
	public String podforum;
	public String tema;
	public String autor;
	public String datum;
	public int roditelj;
	public List<Komentar> podkomentari;
	public String tekst;
	public int like;
	public int dislike;
	public boolean izmenjen;
	public int id;
	public boolean obrisan;
	public List<String> reagovali;
	public List<String> lajkovi;
	public List<String> dislajkovi;


	public List<String> getLajkovi() {
		return lajkovi;
	}

	public void setLajkovi(List<String> lajkovi) {
		this.lajkovi = lajkovi;
	}

	public List<String> getDislajkovi() {
		return dislajkovi;
	}

	public void setDislajkovi(List<String> dislajkovi) {
		this.dislajkovi = dislajkovi;
	}

	public Komentar(String tema, String autor, String datum, int roditelj,
			List<Komentar> podkomentari, String tekst, int like,
			int dislike, boolean izmenjen, int id,
			boolean obrisan, List<String> reagovali) {
		super();
		this.tema = tema;
		this.autor = autor;
		this.datum = datum;
		this.roditelj = roditelj;
		this.podkomentari = podkomentari;
		this.tekst = tekst;
		this.like = like;
		this.dislike = dislike;
		this.izmenjen = izmenjen;
		this.id = id;
		this.obrisan = obrisan;
		this.reagovali = reagovali;
	}
	
	public String getPodforum() {
		return podforum;
	}
	public void setPodforum(String podforum) {
		this.podforum = podforum;
	}
	
	public List<String> getReagovali() {
		return reagovali;
	}
	public void setReagovali(ArrayList<String> reagovali) {
		this.reagovali = reagovali;
	}
	public boolean isObrisan() {
		return obrisan;
	}
	public void setObrisan(boolean obrisan) {
		this.obrisan = obrisan;
	}
	public String getTema() {
		return tema;
	}
	public void setTema(String tema) {
		this.tema = tema;
	}
	public String getAutor() {
		return autor;
	}
	public void setAutor(String autor) {
		this.autor = autor;
	}
	public String getDatum() {
		return datum;
	}
	public void setDatum(String datum) {
		this.datum = datum;
	}
	public int getRoditelj() {
		return roditelj;
	}
	public void setRoditelj(int roditelj) {
		this.roditelj = roditelj;
	}
	public List<Komentar> getPodkomentari() {
		return podkomentari;
	}
	public void setPodkomentari(List<Komentar> podkomentari) {
		this.podkomentari = podkomentari;
	}
	public String getTekst() {
		return tekst;
	}
	public void setTekst(String tekst) {
		this.tekst = tekst;
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
	public boolean isIzmenjen() {
		return izmenjen;
	}
	public void setIzmenjen(boolean izmenjen) {
		this.izmenjen = izmenjen;
	}
	public int getId() {
		return id;
	}
	public void setId() {
		for(Podforum p : PodForumi.getInstance().getPodForumi()){
			if(p.getNaziv().equals(this.podforum)){
				for(Tema t : p.getTeme()){
					if(t.getNaslov().equals(this.tema)){
						this.id = t.countComments() + 1;
					}
				}
			}
		}
	}
	
	public Komentar findComment(int index){
		Komentar res = null;
		if(this.id == index)
			return this;
		
		for(int i=0; i<this.podkomentari.size(); i++){
			res = podkomentari.get(i).findComment(index);
		}
		
		return res;
	}
	
}

