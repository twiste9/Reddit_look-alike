package servisi;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import modeli.*;

@Path("/podforum")
public class SubforumService {
	@Context
	HttpServletRequest request;
	@Context
	HttpServletResponse response;
	@Context
	ServletContext ctx;
	
	@GET
	@Path("/teme/{naziv}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Tema> teme(@PathParam("naziv") String naziv){
		for(int i=0; i<PodForumi.getInstance().getPodForumi().size(); i++){
			if(PodForumi.getInstance().getPodForumi().get(i).getNaziv().equals(naziv)){
				return PodForumi.getInstance().getPodForumi().get(i).getTeme();
			}
		}
		return null;
	}
	
	@GET
	@Path("/{naziv}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Podforum vratiPodforum(@PathParam("naziv") String naziv){
		for(Podforum p : PodForumi.getInstance().getPodForumi()){
			if(p.getNaziv().equals(naziv)){
				return p;
			}
		}
		return null;
	}
	
	@POST
	@Path("/tema/dodaj")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public int kreirajTemu(Tema t) throws IOException{
		//0 ako nije ulogovan
		//1 ako nije popunio sve
		//2 valja
		//3 ne postoji podforum
		//4 vec postoji naziv teme
		Korisnik user = (Korisnik) request.getSession().getAttribute("korisnik");
		if(user==null)
			return 0;
		
		if(t.getNaslov() == null || t.getSadrzaj() == null || t.getTip() == null || t.getPodforum() == null || t.getPodforum().trim().equals("") ||
				t.getNaslov().trim().equals("") || t.getSadrzaj().trim().equals("") || (!t.getTip().equals("slika") && !t.getTip().equals("link") &&!t.getTip().equals("text")))
			return 1;
		
		t.setAutor(user.getKorisnicko());
		t.setLajkovi(new ArrayList<String>());
		t.setDislajkovi(new ArrayList<String>());
		t.setKomentari(new ArrayList<Komentar>());
		t.setOdreagovali(new ArrayList<String>());
		t.setLike(0);
		t.setDislike(0);
		
		for(int i=0; i<PodForumi.getInstance().getPodForumi().size(); i++){
			if(PodForumi.getInstance().getPodForumi().get(i).getNaziv().equals(t.getPodforum())){
				if(PodForumi.getInstance().getPodForumi().get(i).postojiTema(t.getNaslov()))
					return 4;
				PodForumi.getInstance().getPodForumi().get(i).getTeme().add(t);
				PodForumi.getInstance().save();
				return 2;
			}
		}
		return 3;
	}
	
	@GET
	@Path("/zaprati/{pf}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public int zapratiPodforum(@PathParam("pf") String p) throws IOException{
		//0 nije ulogovan
		//1 vec prati
		//2 zapratio uspesno
		
		Korisnik user = (Korisnik) request.getSession().getAttribute("korisnik");
		if(user==null){
			return 0;
		}
		for(int i=0; i<user.getForumi().size(); i++){
			if(user.getForumi().get(i).getNaziv().equals(p)){
				return 1;
			}
		}
		for(int i=0; i<PodForumi.getInstance().getPodForumi().size(); i++){
			if(PodForumi.getInstance().getPodForumi().get(i).getNaziv().equals(p)){
				user.getForumi().add(PodForumi.getInstance().getPodForumi().get(i));
				Korisnici.getInstance().save();
				return 2;
			}
		}
		return 0;
	}
	
	@POST
	@Path("/tema/dodajSliku/{podforum}/{tema}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public void dodajSliku(InputStream inputStream, @PathParam("podforum") String podforum, @PathParam("tema") String tema) throws IOException{
		PodForumi lista = PodForumi.getInstance();
		String naziv = "";
		
		for(Podforum p : lista.getPodForumi()){
			if(p.getNaziv().equals(podforum)){
				for(Tema t: p.getTeme()){
					if(t.getNaslov().equals(tema)){
						naziv = t.getSadrzaj();
					}
				}
			}
		}
		
		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(new java.io.File(ctx.getRealPath("slike\\" + naziv)));
			//outputStream = new FileOutputStream(new java.io.File("C:\\Users\\Radanovic\\Desktop\\javaEE_projects\\WebProjekat\\WebContent\\slike\\" + naziv));
			//System.out.println(ctx.getRealPath("slike\\" + naziv));
			//System.out.println("C:\\Users\\Radanovic\\Desktop\\javaEE_projects\\WebProjekat\\WebContent\\slike\\" + naziv);
			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}

		} catch (IOException e) {
			System.out.println("error1slikaTema");
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					System.out.println("error2slikaTema");
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					System.out.println("error3SlikaTema");
				}

			}
		}
		for(Podforum p : lista.getPodForumi()){
			if(!naziv.equals("")){
				if(p.getNaziv().equals(podforum)){
					for(Tema t : p.getTeme()){
						t.setSadrzaj("slike\\"+naziv);
						PodForumi.getInstance().save();
					}
				}
			}
		}
	}
	
	@GET
	@Path("tema/{podforum}/{tema}")
	@Produces(MediaType.APPLICATION_JSON)
	public Tema vratiTemu(@PathParam("podforum") String podforum, @PathParam("tema") String tema){
		for(Podforum p : PodForumi.getInstance().getPodForumi()){
			if(p.getNaziv().equals(podforum)){
				for(Tema t : p.getTeme()){
					if(t.getNaslov().equals(tema)){
						return t;
					}	
				}
			}
		}
		return null;
	}
	
	@GET
	@Path("/smeObrisati/{naziv}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean daLiSmeObrisatiPF(@PathParam("naziv") String naziv){
		Korisnik user = (Korisnik) request.getSession().getAttribute("korisnik");
		if(user==null){
			return false;
		}
		Podforum pf = null;
		for(Podforum p : PodForumi.getInstance().getPodForumi()){
			if(p.getNaziv().equals(naziv)){
				pf = p;
			}
		}
		if(pf == null){
			return false;
		}
		if(pf.getOdgovornimoderator().equals(user.getKorisnicko()) || user.getUloga().equals("administrator")){
			return true;
		}
		return false;
	}
	
	@GET
	@Path("/tema/smeEditovati/{pf}/{tema}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean smeEditovatiTemu(@PathParam("pf") String pf, @PathParam("tema") String tema){
		Korisnik user = (Korisnik) request.getSession().getAttribute("korisnik");
		if(user == null)
			return false;
		if(user.getUloga().equals("administrator"))
			return true;
		for(Podforum p : PodForumi.getInstance().getPodForumi()){
			if(p.getNaziv().equals(pf)){
				if(p.getModeratori().contains(user.getKorisnicko()) || p.getOdgovornimoderator().equals(user.getKorisnicko())){
					return true;
				}
				for(Tema t : p.getTeme()){
					if(t.getNaslov().equals(tema)){
						if(t.getAutor().equals(user.getKorisnicko())){
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	
	@DELETE
	@Path("/obrisi/{naziv}")
	public void obrisiPF(@PathParam("naziv") String naziv) throws IOException{
		for(int i=0; i<PodForumi.getInstance().getPodForumi().size(); i++){
			if(PodForumi.getInstance().getPodForumi().get(i).getNaziv().equals(naziv)){
				PodForumi.getInstance().getPodForumi().remove(i);
				PodForumi.getInstance().save();
				for(Korisnik k : Korisnici.getInstance().getKorisnici().values()){
					for(int j=0; j<k.getForumi().size(); j++){
						if(k.getForumi().get(j).getNaziv().equals(naziv)){
							k.getForumi().remove(j);
							break;
						}
					}
					for(int j=0; j<k.getTeme().size(); j++){
						if(k.getTeme().get(j).getPodforum().equals(naziv)){
							k.getTeme().remove(j);
						}
					}
				}
				Korisnici.getInstance().save();
			}
		}
	}
	
	@POST
	@Path("/tema/sacuvaj")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean sacuvajTemu(Tema t) throws IOException{
		Korisnik user = (Korisnik) request.getSession().getAttribute("korisnik");
		if(user == null)
			return false;
		for(Tema tema : user.getTeme()){
			if(tema.getPodforum().equals(t.getPodforum()) && tema.getNaslov().equals(t.getNaslov())){
				return true;
			}
		}
		user.getTeme().add(t);
		Korisnici.getInstance().save();
		return true;
	}
	
	@DELETE
	@Path("/tema/obrisi")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean obrisiTemu(Tema t) throws IOException{
		Korisnik user = (Korisnik) request.getSession().getAttribute("korisnik");
		if(user == null)
			return false;
		
		boolean res = false;
		if(user.getUloga().equals("administrator"))
			res = true;
		if(!res){
			for(Podforum p : PodForumi.getInstance().getPodForumi()){
				if(p.getNaziv().equals(t.getPodforum())){
					if(p.getModeratori().contains(user.getKorisnicko()) || p.getOdgovornimoderator().equals(user.getKorisnicko())){
						res = true;
						break;
					}
					for(Tema tema : p.getTeme()){
						if(tema.getNaslov().equals(t.getNaslov())){
							if(t.getAutor().equals(user.getKorisnicko())){
								res = true;
								break;
							}
						}
					}
				}
			}
		}
		if(res){
			for(int i=0; i<PodForumi.getInstance().getPodForumi().size(); i++){
				if(PodForumi.getInstance().getPodForumi().get(i).getNaziv().equals(t.getPodforum())){
					for(int j=0; j<PodForumi.getInstance().getPodForumi().get(i).getTeme().size(); j++){
						if(PodForumi.getInstance().getPodForumi().get(i).getTeme().get(j).getNaslov().equals(t.getNaslov())){
							PodForumi.getInstance().getPodForumi().get(i).getTeme().remove(j);
							for(int k=0; k<user.getTeme().size(); k++){
								if(user.getTeme().get(k).getNaslov().equals(t.getNaslov()) && user.getTeme().get(k).getPodforum().equals(t.getPodforum())){
									user.getTeme().remove(k);
									break;
								}
							}
							Korisnici.getInstance().save();
							PodForumi.getInstance().save();
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	@PUT
	@Path("/tema/izmeni/{podforum}/{staraTema}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public int izmeniTemu(@PathParam("podforum") String podforum, @PathParam("staraTema") String staraTema, Tema novaTema) throws IOException{
		//1 ako nije sve popunio
		//2 ako valja
		//3 ako vec postoji taj naslov
		if(novaTema.getNaslov() == null || novaTema.getSadrzaj() == null || novaTema.getTip() == null || novaTema.getPodforum() == null || novaTema.getPodforum().trim().equals("") ||
				novaTema.getNaslov().trim().equals("") || novaTema.getSadrzaj().trim().equals("") || (!novaTema.getTip().equals("slika") && !novaTema.getTip().equals("link") &&!novaTema.getTip().equals("text")))
			return 1;
		
		for(Podforum p : PodForumi.getInstance().getPodForumi()){
			if(p.getNaziv().equals(podforum)){
				for(Tema t : p.getTeme()){
					if(t.getNaslov().equals(novaTema.getNaslov()) && !t.getNaslov().equals(staraTema)){
						return 3;
					}
					if(t.getNaslov().equals(staraTema)){
						t.setNaslov(novaTema.getNaslov());
						t.setSadrzaj(novaTema.getSadrzaj());
						t.setTip(novaTema.getTip());
						Korisnici.getInstance().save();
						PodForumi.getInstance().save();
						return 2;
					}
				}
			}
		}
		
		return -1;
	}
	
	
	
	
	
	
	
	
	
	
}
