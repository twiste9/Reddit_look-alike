package modeli;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//import javax.ws.rs.PathParam;

public class Podforum implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Podforum(){
		
	}
	
	public String naziv;
	public String opis;
	public String ikonica;
	public String spisakpravila;
	public String odgovornimoderator;
	public List<String> moderatori;
	public List<Tema> teme;
	public String getNaziv() {
		return naziv;
	}
	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	public String getOpis() {
		return opis;
	}
	public void setOpis(String opis) {
		this.opis = opis;
	}
	public String getIkonica() {
		return ikonica;
	}
	public void setIkonica(String ikonica) {
		this.ikonica = ikonica;
	}
	public String getSpisakpravila() {
		return spisakpravila;
	}
	public void setSpisakpravila(String spisakpravila) {
		this.spisakpravila = spisakpravila;
	}
	public String getOdgovornimoderator() {
		return odgovornimoderator;
	}
	public void setOdgovornimoderator(String odgovornimoderator) {
		this.odgovornimoderator = odgovornimoderator;
	}
	public List<String> getModeratori() {
		return moderatori;
	}
	public void setModeratori(ArrayList<String> moderatori) {
		this.moderatori = moderatori;
	}
	public List<Tema> getTeme() {
		return teme;
	}
	public void setTeme(ArrayList<Tema> teme) {
		this.teme = teme;
	}
	public Podforum(String naziv, String opis, String ikonica,
			String spisakpravila, String odgovornimoderator,
			ArrayList<String> moderatori, ArrayList<Tema> teme) {
		super();
		this.naziv = naziv;
		this.opis = opis;
		this.ikonica = ikonica;
		this.spisakpravila = spisakpravila;
		this.odgovornimoderator = odgovornimoderator;
		this.moderatori = moderatori;
		this.teme = teme;
	}
	
	public boolean postojiTema(String s){
		for(int i=0; i<teme.size(); i++){
			if(teme.get(i).getNaslov().equals(s)){
				return true;
			}
		}
		return false;
	}
	
}

