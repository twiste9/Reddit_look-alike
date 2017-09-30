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
import java.util.List;



public class PodForumi {
	private List<Podforum> podforumi;
	private Object lock=new Object();
	private static PodForumi instance;

	private PodForumi() {
		loadPodForums();
	}
	
	public static PodForumi getInstance(){
		if(instance == null){
			instance = new PodForumi();
		}
		return instance;
	}
	
	@SuppressWarnings("unchecked")
	public void loadPodForums(){
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(new File("podforumi.txt"))));
			try {
				this.podforumi=(List<Podforum>) ois.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			createDefaultForums();
		}finally {
			try {
				if(ois!=null)
					ois.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void createDefaultForums(){
		Podforum p = new Podforum("FCLiverpool","All about the best club in the world" , "slike\\lfc-icon.ico","1. YNNWA","admin",new ArrayList<String>(),new ArrayList<Tema>());
		Tema t= new Tema("FCLiverpool","Steven Gerrard","text","k1", new ArrayList<Komentar>(), new ArrayList<String>(),"Greatest LFC player of all time?" ,"03/07/2017" ,0,0);
		Tema t1= new Tema("FCLiverpool","Squad against Burnley","slika","k2", new ArrayList<Komentar>(), new ArrayList<String>(),"slike\\lfc-squad.jpg" ,"03/07/2017" ,0,0);
		p.getTeme().add(t);
		p.getTeme().add(t1);
		Podforum p1 = new Podforum("Fun","Everything that makes you laugh" , "slike\\people.png","1. no sad topics","admin",new ArrayList<String>(),new ArrayList<Tema>());
		Tema t2= new Tema("Fun","Joke","text","k2", new ArrayList<Komentar>(), new ArrayList<String>(),"Zdravo je jesti povrce" ,"03/07/2017" ,0,0);
		Tema t3= new Tema("Fun","Reddit fun page","link","k3", new ArrayList<Komentar>(), new ArrayList<String>(),"https://reddit.com/r/funny" ,"03/07/2017" ,0,0);
		p1.getTeme().add(t2);
		p1.getTeme().add(t3);
		
		Podforum p2 = new Podforum("TodayILearned","You learn something new every day; what did you learn today?" , "slike\\smart-icon.png","1. No personal opinions, anecdotes or subjective statements","moderator",new ArrayList<String>(),new ArrayList<Tema>());
		Tema t5= new Tema("TodayILearned","TIL Mark Twain traveled extensively and once said Travel is fatal to prejudice, bigotry, and narrow-mindedness, and many of our people need it sorely on these accounts. Broad, wholesome, charitable views of men and things cannot be acquired by vegetating in one little corner of the earth...","link","k1", new ArrayList<Komentar>(), new ArrayList<String>(),"https://en.wikipedia.org/w/index.php?title=Mark_Twain&mobileaction=toggle_view_desktop" ,"03/07/2017" ,0,0);
		
		p2.getTeme().add(t5);
        	
		synchronized (lock) {
			podforumi=new ArrayList<>();
			podforumi.add(p);
			podforumi.add(p1);
			podforumi.add(p2);
			}
	}
	
	public List<Podforum> getPodForumi(){
		return podforumi;
	}
	
	public int addForums(Podforum p) throws FileNotFoundException, IOException{
		synchronized (lock) {
			if(p.getNaziv() == null || p.getOpis()==null || p.getSpisakpravila() == null || p.getIkonica() == null ||
					p.getNaziv().trim().equals("") || p.getOpis().trim().equals("") || p.getSpisakpravila().trim().equals("") || p.getIkonica().trim().equals("")){
				return 0;
			}
			List<Podforum> lista = PodForumi.getInstance().getPodForumi();
			for(int i=0; i<lista.size(); i++){
				if(lista.get(i).getNaziv().equals(p.getNaziv())){
					return 1;
				}
			}
			
			podforumi.add(p);
			File file=new File("podforumi.txt");
			ObjectOutputStream oos=new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
			oos.writeObject(podforumi);
			if(oos!=null)
				oos.close();
			return 2;
		}
	}
	
	public void setPodForumi(List<Podforum> podforumi) {
		this.podforumi = podforumi;
	}

	public void save() throws IOException{
		synchronized (lock) {
			File file=new File("podforumi.txt");
			ObjectOutputStream oos=new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
			oos.writeObject(podforumi);
			if(oos!=null)
				oos.close();
		}
	}
}

