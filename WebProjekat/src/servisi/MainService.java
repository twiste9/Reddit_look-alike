package servisi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import modeli.*;

@Path("/pocetna")
public class MainService {
	@Context
	HttpServletRequest request;
	@Context
	HttpServletResponse response;
	@Context
	ServletContext ctx;
	
	
	@GET
	@Path("/sviKor")
	@Produces(MediaType.APPLICATION_JSON)
	public HashMap<String, Korisnik> kor(){
		return Korisnici.getInstance().getKorisnici();
	}
	
	@GET
	@Path("/sviPodforumi")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Podforum> sviPodforumi(){
		Korisnik user = (Korisnik) request.getSession().getAttribute("korisnik");
		if(user==null)
			return PodForumi.getInstance().getPodForumi();
		if(user.getForumi().size() > 0)
			return user.getForumi();
		else
			return PodForumi.getInstance().getPodForumi();
	}
	
	
	
	@GET
	@Path("/isLogged")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean jeUlogovan(){
		Korisnik user = (Korisnik) request.getSession().getAttribute("korisnik");
		if(user == null)
			return false;
		return true;
	}
	
	@POST
	@Path("/logOut")
	@Produces(MediaType.APPLICATION_JSON)
	public void logOut(){
		request.getSession().setAttribute("korisnik", null);
	}
	
	@GET
	@Path("/sveTeme")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Tema> sveTeme(){
		List<Tema> res = new ArrayList<Tema>();
		Korisnik user = (Korisnik) request.getSession().getAttribute("korisnik");
		if(user == null){
			List<Podforum> podforumi = PodForumi.getInstance().getPodForumi();
			for(int i=0; i<podforumi.size(); i++){
				for(int j = 0; j<podforumi.get(i).getTeme().size(); j++){
					res.add(podforumi.get(i).getTeme().get(j));
				}
			}
		}else{
			List<Podforum> podforumi = user.getForumi();
			for(int i=0; i<podforumi.size(); i++){
				if(user.getForumi().contains(podforumi.get(i))){
					for(int j = 0; j<podforumi.get(i).getTeme().size(); j++){
						res.add(podforumi.get(i).getTeme().get(j));
					}
				}
			}
		}
		Collections.shuffle(res);
		return res;
	}
	
	
	@GET
	@Path("/preporuceneTeme")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Tema> preporucene(){
		List<Tema> res = new ArrayList<Tema>();
		for(Podforum p : PodForumi.getInstance().getPodForumi()){
			for(Tema t : p.getTeme()){
				if(res.size() == 0){
					res.add(t);
				}else{
					for(int i=0; i<res.size(); i++){
						if(i == res.size()-1){
							res.add(t);
							break;
						}
						if(t.popularity() >= res.get(i).popularity()){
							res.add(i, t);
							break;
						}
					}
				}
			}
		}
		
		return res;
	}
	
	@POST
	@Path("/upvote")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public int lajk(Tema t) throws IOException{
		Korisnik user = (Korisnik) request.getSession().getAttribute("korisnik");
		if(user!=null){
			for(int i=0; i<PodForumi.getInstance().getPodForumi().size(); i++){
				if(PodForumi.getInstance().getPodForumi().get(i).getNaziv().equals(t.getPodforum())){
					for(int j=0; j<PodForumi.getInstance().getPodForumi().get(i).getTeme().size(); j++){
						if(PodForumi.getInstance().getPodForumi().get(i).getTeme().get(j).getNaslov().equals(t.getNaslov())){
							if(PodForumi.getInstance().getPodForumi().get(i).getTeme().get(j).getLajkovi().contains(user.getKorisnicko())){
								return PodForumi.getInstance().getPodForumi().get(i).getTeme().get(j).getLajkovi().size() - PodForumi.getInstance().getPodForumi().get(i).getTeme().get(j).getDislajkovi().size();
							}else if(PodForumi.getInstance().getPodForumi().get(i).getTeme().get(j).getDislajkovi().contains(user.getKorisnicko())){
								PodForumi.getInstance().getPodForumi().get(i).getTeme().get(j).getDislajkovi().remove(user.getKorisnicko());
								PodForumi.getInstance().getPodForumi().get(i).getTeme().get(j).setDislike(PodForumi.getInstance().getPodForumi().get(i).getTeme().get(j).getDislike()-1);
								PodForumi.getInstance().getPodForumi().get(i).getTeme().get(j).getLajkovi().add(user.getKorisnicko());
								PodForumi.getInstance().getPodForumi().get(i).getTeme().get(j).setLike(PodForumi.getInstance().getPodForumi().get(i).getTeme().get(j).getLike()+1);
								PodForumi.getInstance().save();
								return PodForumi.getInstance().getPodForumi().get(i).getTeme().get(j).getLajkovi().size() - PodForumi.getInstance().getPodForumi().get(i).getTeme().get(j).getDislajkovi().size();
							}else{
								PodForumi.getInstance().getPodForumi().get(i).getTeme().get(j).getLajkovi().add(user.getKorisnicko());
								PodForumi.getInstance().getPodForumi().get(i).getTeme().get(j).setLike(PodForumi.getInstance().getPodForumi().get(i).getTeme().get(j).getLike()+1);
								PodForumi.getInstance().save();
								return PodForumi.getInstance().getPodForumi().get(i).getTeme().get(j).getLajkovi().size() - PodForumi.getInstance().getPodForumi().get(i).getTeme().get(j).getDislajkovi().size();
							}
						}
						
						
					}
				}
			}
		}else{
			for(int i=0; i<PodForumi.getInstance().getPodForumi().size(); i++){
				if(PodForumi.getInstance().getPodForumi().get(i).getNaziv().equals(t.getPodforum())){
					for(int j=0; j<PodForumi.getInstance().getPodForumi().get(i).getTeme().size(); j++){
						if(PodForumi.getInstance().getPodForumi().get(i).getTeme().get(j).getNaslov().equals(t.getNaslov())){
							return PodForumi.getInstance().getPodForumi().get(i).getTeme().get(j).getLajkovi().size() - PodForumi.getInstance().getPodForumi().get(i).getTeme().get(j).getDislajkovi().size();
						}
					}
				}
			}
		}
		return 0;
	}
	
	@POST
	@Path("/downvote")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public int dislajk(Tema t) throws IOException{
		Korisnik user = (Korisnik) request.getSession().getAttribute("korisnik");
		if(user!=null){
			for(int i=0; i<PodForumi.getInstance().getPodForumi().size(); i++){
				if(PodForumi.getInstance().getPodForumi().get(i).getNaziv().equals(t.getPodforum())){
					for(int j=0; j<PodForumi.getInstance().getPodForumi().get(i).getTeme().size(); j++){
						if(PodForumi.getInstance().getPodForumi().get(i).getTeme().get(j).getNaslov().equals(t.getNaslov())){
							if(PodForumi.getInstance().getPodForumi().get(i).getTeme().get(j).getDislajkovi().contains(user.getKorisnicko())){
								return PodForumi.getInstance().getPodForumi().get(i).getTeme().get(j).getLajkovi().size() - PodForumi.getInstance().getPodForumi().get(i).getTeme().get(j).getDislajkovi().size();
							}else if(PodForumi.getInstance().getPodForumi().get(i).getTeme().get(j).getLajkovi().contains(user.getKorisnicko())){
								PodForumi.getInstance().getPodForumi().get(i).getTeme().get(j).getLajkovi().remove(user.getKorisnicko());
								PodForumi.getInstance().getPodForumi().get(i).getTeme().get(j).setLike(PodForumi.getInstance().getPodForumi().get(i).getTeme().get(j).getLike()-1);
								PodForumi.getInstance().getPodForumi().get(i).getTeme().get(j).getDislajkovi().add(user.getKorisnicko());
								PodForumi.getInstance().getPodForumi().get(i).getTeme().get(j).setDislike(PodForumi.getInstance().getPodForumi().get(i).getTeme().get(j).getDislike()+1);
								PodForumi.getInstance().save();
								return PodForumi.getInstance().getPodForumi().get(i).getTeme().get(j).getLajkovi().size() - PodForumi.getInstance().getPodForumi().get(i).getTeme().get(j).getDislajkovi().size();
							}else{
								PodForumi.getInstance().getPodForumi().get(i).getTeme().get(j).getDislajkovi().add(user.getKorisnicko());
								PodForumi.getInstance().getPodForumi().get(i).getTeme().get(j).setDislike(PodForumi.getInstance().getPodForumi().get(i).getTeme().get(j).getDislike()+1);
								PodForumi.getInstance().save();
								return PodForumi.getInstance().getPodForumi().get(i).getTeme().get(j).getLajkovi().size() - PodForumi.getInstance().getPodForumi().get(i).getTeme().get(j).getDislajkovi().size();
							}
						}
						
						
					}
				}
			}
		}else{
			for(int i=0; i<PodForumi.getInstance().getPodForumi().size(); i++){
				if(PodForumi.getInstance().getPodForumi().get(i).getNaziv().equals(t.getPodforum())){
					for(int j=0; j<PodForumi.getInstance().getPodForumi().get(i).getTeme().size(); j++){
						if(PodForumi.getInstance().getPodForumi().get(i).getTeme().get(j).getNaslov().equals(t.getNaslov())){
							return PodForumi.getInstance().getPodForumi().get(i).getTeme().get(j).getLajkovi().size() - PodForumi.getInstance().getPodForumi().get(i).getTeme().get(j).getDislajkovi().size();
						}
					}
				}
			}
		}
		return 0;
	}
	
	@GET
	@Path("/userType")
	@Produces(MediaType.APPLICATION_JSON)
	public int tipKorisnika(){
		Korisnik user = (Korisnik) request.getSession().getAttribute("korisnik");
		if(user == null){
			return 0;
		}
		else if(user.getUloga().equals("administrator"))
			return 3;
		else if(user.getUloga().equals("moderator"))
			return 2;
		else
			return 1;
	}

}
