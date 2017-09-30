package servisi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import modeli.*;

@Path("/korisnicka")
public class UserService {
	@Context
	HttpServletRequest request;
	@Context
	HttpServletResponse response;
	@Context
	ServletContext ctx;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/posaljiPoruku")
	public int posaljiPoruku(Poruka p) throws IOException{
		//-1 nije ulogovan
		//0 ne postoji primalac
		//1 nije uneo poruku
		//2 poslato
		Korisnik user = (Korisnik) request.getSession().getAttribute("korisnik");
		if(user==null)
			return -1;
		if(p.getSadrzaj() == null || p.getSadrzaj().trim().equals("")){
			return 1;
		}
		for(Korisnik k : Korisnici.getInstance().getKorisnici().values()){
			if(k.getKorisnicko().equals(p.getPrimalac())){
				p.setPosaljilac(user.getKorisnicko());
				p.setProcitana(false);
				user.getPoruke().add(p);
				k.getPoruke().add(p);
				Korisnici.getInstance().save();
				return 2;
			}
		}
		return 0;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/primljenePoruke")
	public List<Poruka> primljenePoruke(){
		List<Poruka> res = new ArrayList<Poruka>();
		Korisnik user = (Korisnik) request.getSession().getAttribute("korisnik");
		if(user == null)
			return null;
		for(Poruka p : user.getPoruke()){
			if(p.getPrimalac().equals(user.getKorisnicko()))
				res.add(p);
		}
		return res;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/poslatePoruke")
	public List<Poruka> poslatePoruke(){
		List<Poruka> res = new ArrayList<Poruka>();
		Korisnik user = (Korisnik) request.getSession().getAttribute("korisnik");
		if(user == null)
			return null;
		for(Poruka p : user.getPoruke()){
			if(p.getPosaljilac().equals(user.getKorisnicko()))
				res.add(p);
		}
		return res;
	}
	
	@PUT
	@Path("/procitane")
	public void setProcitane() throws IOException{
		Korisnik user = (Korisnik) request.getSession().getAttribute("korisnik");
		if(user!=null){
			for(Poruka p : user.getPoruke()){
				if(p.getPrimalac().equals(user.getKorisnicko())){
					p.setProcitana(true);
					Korisnici.getInstance().save();
				}
			}
		}
	}
	
	@GET
	@Path("/korisnik")
	@Produces(MediaType.APPLICATION_JSON)
	public Korisnik ulogovaniKorisnik(){
		return (Korisnik) request.getSession().getAttribute("korisnik");
	}
	
	
	@GET
	@Path("/lajkovaneTeme")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Tema> lajkovaneTemeKorisnika(){
		Korisnik user = (Korisnik) request.getSession().getAttribute("korisnik");
		if(user == null)
			return null;
		
		List<Tema> res = new ArrayList<Tema>();
		for(Podforum p : PodForumi.getInstance().getPodForumi()){
			for(Tema t : p.getTeme()){
				if(t.getLajkovi().contains(user.getKorisnicko())){
					res.add(t);
				}
			}
		}
		
		return res;
	}
	
	@GET
	@Path("/dislajkovaneTeme")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Tema> dislajkovaneTemeKorisnika(){
		Korisnik user = (Korisnik) request.getSession().getAttribute("korisnik");
		if(user == null)
			return null;
		
		List<Tema> res = new ArrayList<Tema>();
		for(Podforum p : PodForumi.getInstance().getPodForumi()){
			for(Tema t : p.getTeme()){
				if(t.getDislajkovi().contains(user.getKorisnicko())){
					res.add(t);
				}
			}
		}
		
		return res;
	}
	
	@PUT
	@Path("/promeniTip/{kor}/{tip}")
	@Produces(MediaType.APPLICATION_JSON) 
	public boolean promeniTipKorisnika(@PathParam("kor") String kor, @PathParam("tip") int tip) throws IOException{
		if(tip <1 && tip>3)
			return false;
		
		Korisnik user = (Korisnik) request.getSession().getAttribute("korisnik");
		if(user == null)
			return false;
		
		for(Korisnik k : Korisnici.getInstance().getKorisnici().values()){
			if(k.getKorisnicko().equals(kor)){
				if(tip == 1){
					k.setUloga("obican");
					Poruka por = new Poruka();
					por.setPrimalac(k.getKorisnicko());
					por.setPosaljilac(user.getKorisnicko());
					por.setProcitana(false);
					por.setSadrzaj("Your type is changed to regular user");
					k.getPoruke().add(por);
					Korisnici.getInstance().save();
					return true;
				}else if(tip==2){
					k.setUloga("moderator");
					Poruka por = new Poruka();
					por.setPrimalac(k.getKorisnicko());
					por.setPosaljilac(user.getKorisnicko());
					por.setProcitana(false);
					por.setSadrzaj("Your type is changed to moderator");
					k.getPoruke().add(por);
					Korisnici.getInstance().save();
					return true;
				}else if(tip==3){
					k.setUloga("administrator");
					Poruka por = new Poruka();
					por.setPrimalac(k.getKorisnicko());
					por.setPosaljilac(user.getKorisnicko());
					por.setProcitana(false);
					por.setSadrzaj("Your type is changed to administrator");
					k.getPoruke().add(por);
					Korisnici.getInstance().save();
					return true;
				}
			}
		}
		
		return false;
	}
}
