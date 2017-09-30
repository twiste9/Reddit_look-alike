package modeli;

import java.io.Serializable;

public class Zalba implements Serializable {
	
	public Zalba(){
		
	}
	public String tekst;
	public String podforum;
	public String tema;
	public int komentar;
	public String komentarTekst;
	public int roditeljKomentara;
	public String datum;
	public String autor;
	public boolean resena;
	
	
	public boolean isResena() {
		return resena;
	}

	public void setResena(boolean resena) {
		this.resena = resena;
	}

	public String getKomentarTekst() {
		return komentarTekst;
	}

	public void setKomentarTekst(String komentarTekst) {
		this.komentarTekst = komentarTekst;
	}
	
	public String getPodforum() {
		return podforum;
	}

	public void setPodforum(String podforum) {
		this.podforum = podforum;
	}

	public String getTema() {
		return tema;
	}

	public void setTema(String tema) {
		this.tema = tema;
	}

	public int getKomentar() {
		return komentar;
	}

	public void setKomentar(int komentar) {
		this.komentar = komentar;
	}
	
	public String getTekst() {
		return tekst;
	}

	public void setTekst(String tekst) {
		this.tekst = tekst;
	}


	public String getDatum() {
		return datum;
	}

	public void setDatum(String datum) {
		this.datum = datum;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}


	
	
	
}
