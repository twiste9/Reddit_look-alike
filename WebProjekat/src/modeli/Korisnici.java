package modeli;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
//import java.util.ArrayList;
import java.util.HashMap;

import modeli.Korisnik;



public class Korisnici {
	private HashMap<String, Korisnik> korisnici;
	private Object lock=new Object();
	private static Korisnici instance;
	
	private Korisnici() {
		loadUsers();
	}
	
	public static Korisnici getInstance(){
		if(instance==null){
			instance = new Korisnici();
		}
		return instance;
	}
	
	@SuppressWarnings("unchecked")
	public void loadUsers(){
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(new File("korisnici.txt"))));
			try {
				this.korisnici=(HashMap<String, Korisnik>) ois.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			createDefaultUsers();
		}finally {
			try {
				if(ois!=null)
					ois.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void createDefaultUsers(){
		Korisnik k = new Korisnik("k1","k1" , "Stefan","Radanovic","obican","12345","10/09/2017","rstefan@gmail.com", new ArrayList<Podforum>(),new ArrayList<Tema>(), new ArrayList<Komentar>(),new ArrayList<Poruka>(),new ArrayList<Zalba>(),0,0);
		Korisnik k1 = new Korisnik("k2","k2" , "Marko","markovic","obican","11111","10/09/2017","marko@gmail.com", new ArrayList<Podforum>(),new ArrayList<Tema>(), new ArrayList<Komentar>(),new ArrayList<Poruka>(),new ArrayList<Zalba>(),0,0);
		Korisnik k2 = new Korisnik("k3","k3" , "Petar","Petrovic","obican","1588","10/09/2017","ppetar@gmail.com", new ArrayList<Podforum>(),new ArrayList<Tema>(), new ArrayList<Komentar>(),new ArrayList<Poruka>(),new ArrayList<Zalba>(),0,0);
		Korisnik k3 = new Korisnik("admin","admin" , "Stefan","Radanovic","administrator","1855","10/09/2017","stefanr2@gmail.com", new ArrayList<Podforum>(),new ArrayList<Tema>(), new ArrayList<Komentar>(),new ArrayList<Poruka>(),new ArrayList<Zalba>(),0,0);
		Korisnik k4 = new Korisnik("moderator","moderator" , "Jovana","Jovanovic","moderator","151551","10/09/2017","mod@gmail.com", new ArrayList<Podforum>(),new ArrayList<Tema>(), new ArrayList<Komentar>(),new ArrayList<Poruka>(),new ArrayList<Zalba>(),0,0);
		k4.getForumi().add(PodForumi.getInstance().getPodForumi().get(0));
		synchronized (lock) {
			korisnici=new HashMap<>();
			korisnici.put(k.getKorisnicko(),k);
			korisnici.put(k1.getKorisnicko(),k1);
			korisnici.put(k2.getKorisnicko(),k2);
			korisnici.put(k3.getKorisnicko(),k3);
			korisnici.put(k4.getKorisnicko(),k4);
		}
	}
	
	public HashMap<String, Korisnik> getKorisnici(){
		return korisnici;
	}
	
	
	public void setKorisnici(HashMap<String, Korisnik> korisnici) {
		this.korisnici = korisnici;
	}
	
	public void addUsers(Korisnik kor) throws FileNotFoundException, IOException{
		synchronized (lock) {
			korisnici.put(kor.getKorisnicko(), kor);
			File file=new File("korisnici.txt");
			ObjectOutputStream oos=new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
			oos.writeObject(korisnici);
			if(oos!=null)
				oos.close();
		}
	}

	public void save() throws IOException{
		synchronized (lock) {
			File file=new File("korisnici.txt");
			ObjectOutputStream oos=new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
			oos.writeObject(korisnici);
			if(oos!=null)
				oos.close();
		}
	}
}
