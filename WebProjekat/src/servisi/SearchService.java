package servisi;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import modeli.*;

@Path("/pretraga")
public class SearchService {
	@Context
	HttpServletRequest request;
	@Context
	HttpServletResponse response;
	@Context
	ServletContext ctx;
	
	@GET
	@Path("/podforumi/{naziv}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Podforum> podforumi(@PathParam("naziv") String text){
		List<Podforum> res = new ArrayList<Podforum>();
		for(Podforum p : PodForumi.getInstance().getPodForumi()){
			if(p.getOdgovornimoderator().equals(text) || p.getOpis().contains(text) || p.getNaziv().contains(text)){
				res.add(p);
			}
		}
		return res;
	}
	
	@GET
	@Path("/teme/{naziv}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Tema> teme(@PathParam("naziv") String text){
		List<Tema> res = new ArrayList<Tema>();
			for(Podforum p : PodForumi.getInstance().getPodForumi()){
				for(Tema t : p.getTeme()){
					if(t.getNaslov().contains(text) || t.getSadrzaj().contains(text) ||
							t.getAutor().equals(text) || t.getPodforum().equals(text)){
						res.add(t);
					}
				}
			}
		return res;
	} 
	@GET
	@Path("/korisnici/{naziv}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Korisnik> korisnici(@PathParam("naziv") String text){
		List<Korisnik> res = new ArrayList<Korisnik>();
			for(Korisnik k : Korisnici.getInstance().getKorisnici().values()){
				if(k.getKorisnicko().contains(text)){
					res.add(k);
				}
			}
		return res;
	}
}
