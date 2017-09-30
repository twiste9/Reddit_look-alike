package modeli;

import java.io.Serializable;

public class Poruka implements Serializable{
	public Poruka(){
		
	}
	
	public String posaljilac;
	public String primalac;
	public String sadrzaj;
	public boolean procitana;
	public Poruka(String posaljilac, String primalac, String sadrzaj,
			boolean procitana) {
		super();
		this.posaljilac = posaljilac;
		this.primalac = primalac;
		this.sadrzaj = sadrzaj;
		this.procitana = procitana;
	}
	public String getPosaljilac() {
		return posaljilac;
	}
	public void setPosaljilac(String posaljilac) {
		this.posaljilac = posaljilac;
	}
	public String getPrimalac() {
		return primalac;
	}
	public void setPrimalac(String primalac) {
		this.primalac = primalac;
	}
	public String getSadrzaj() {
		return sadrzaj;
	}
	public void setSadrzaj(String sadrzaj) {
		this.sadrzaj = sadrzaj;
	}
	public boolean isProcitana() {
		return procitana;
	}
	public void setProcitana(boolean procitana) {
		this.procitana = procitana;
	}
	
}

