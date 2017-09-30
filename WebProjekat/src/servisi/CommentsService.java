package servisi;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import modeli.*;

@Path("/komentari")
public class CommentsService {
	@Context
	HttpServletRequest request;
	@Context
	HttpServletResponse response;
	@Context
	ServletContext ctx;
	
	@POST
	@Path("/noviKomentar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean dodajNoviKomentar(Komentar k) throws IOException{
		Korisnik user = (Korisnik) request.getSession().getAttribute("korisnik");
		if(user==null)
			return false;
		
		if(k.getTekst() == null || k.getTema() == null || k.getTekst().trim().equals(""))
			return false;
		
		k.setAutor(user.getKorisnicko());
		k.setLike(0);
		k.setDislike(0);
		k.setIzmenjen(false);
		k.setObrisan(false);
		k.setPodkomentari(new ArrayList<Komentar>());
		k.setRoditelj(-1);
		k.setReagovali(new ArrayList<String>());
		
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String s = sdf.format(date);
		k.setDatum(s);
		k.setId();
		k.setLajkovi(new ArrayList<String>());
		k.setDislajkovi(new ArrayList<String>());
		k.setReagovali(new ArrayList<String>());
		
		for(Podforum p : PodForumi.getInstance().getPodForumi()){
			if(p.getNaziv().equals(k.getPodforum())){
				for(Tema t : p.getTeme()){
					if(t.getNaslov().equals(k.getTema())){
						t.getKomentari().add(k);
						PodForumi.getInstance().save();
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	@POST
	@Path("/noviPodkomentar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public int noviPodkomentar(Komentar k) throws IOException{
		Korisnik user = (Korisnik) request.getSession().getAttribute("korisnik");
		if(user == null)
			return -1;
		if(k.getTekst() == null || k.getTekst().trim().equals(""))
			return 0;
		
		k.setAutor(user.getKorisnicko());
		k.setLike(0);
		k.setDislike(0);
		k.setIzmenjen(false);
		k.setObrisan(false);
		k.setPodkomentari(new ArrayList<Komentar>());
		k.setReagovali(new ArrayList<String>());
		
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String s = sdf.format(date);
		k.setDatum(s);
		k.setId();
		k.setLajkovi(new ArrayList<String>());
		k.setDislajkovi(new ArrayList<String>());
		k.setReagovali(new ArrayList<String>());
		
		for(Podforum p : PodForumi.getInstance().getPodForumi()){
			if(p.getNaziv().equals(k.getPodforum())){
				for(Tema t : p.getTeme()){
					if(t.getNaslov().equals(k.getTema())){
						t.findParent(k.getRoditelj()).getPodkomentari().add(k);
						PodForumi.getInstance().save();
						return 2;
					}
				}
			}
		}
		
		return 0;
	}
	
	@PUT
	@Path("/like/{pf}/{tema}/{id}/{parent}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public int likeKomentar(@PathParam("pf") String pf, @PathParam("tema") String tema, @PathParam("id") int id, @PathParam("parent") int parent) throws IOException{
		Korisnik user = (Korisnik) request.getSession().getAttribute("korisnik");
		if(user==null)
			return -20000;
		
		for(Podforum p : PodForumi.getInstance().getPodForumi()){
			if(p.getNaziv().equals(pf)){
				for(Tema t : p.getTeme()){
					if(t.getNaslov().equals(tema)){
						if(parent==-1){
							for(Komentar k : t.getKomentari()){
								if(k.getId() == id){
									if(k.getLajkovi().contains(user.getKorisnicko())){
										return k.getLike() - k.getDislike();
									}else if(k.getDislajkovi().contains(user.getKorisnicko())){
										k.getDislajkovi().remove(user.getKorisnicko());
										k.getLajkovi().add(user.getKorisnicko());
										k.setLike(k.getLike()+1);
										k.setDislike(k.getDislike()-1);
										PodForumi.getInstance().save();
										user.getDislajkovaniKomentari().remove(k);
										user.getLajkovaniKomentari().add(k);
										Korisnici.getInstance().save();
										return k.getLike() - k.getDislike();
									}else{
										k.getLajkovi().add(user.getKorisnicko());
										k.setLike(k.getLike()+1);
										PodForumi.getInstance().save();
										user.getLajkovaniKomentari().add(k);
										Korisnici.getInstance().save();
										return k.getLike() - k.getDislike();
									}
								}
							}
						}else{
							for(Komentar k : t.findParent(parent).getPodkomentari()){
								if(k.getId() == id){
									if(k.getLajkovi().contains(user.getKorisnicko())){
										return k.getLike() - k.getDislike();
									}else if(k.getDislajkovi().contains(user.getKorisnicko())){
										k.getDislajkovi().remove(user.getKorisnicko());
										k.getLajkovi().add(user.getKorisnicko());
										k.setLike(k.getLike()+1);
										k.setDislike(k.getDislike()-1);
										PodForumi.getInstance().save();
										user.getDislajkovaniKomentari().remove(k);
										user.getLajkovaniKomentari().add(k);
										Korisnici.getInstance().save();
										return k.getLike() - k.getDislike();
									}else{
										k.getLajkovi().add(user.getKorisnicko());
										k.setLike(k.getLike()+1);
										PodForumi.getInstance().save();
										user.getLajkovaniKomentari().add(k);
										Korisnici.getInstance().save();
										return k.getLike() - k.getDislike();
									}
								}
							}
						}
					}
				}
			}
		}
		
		return -20000;
		
	}
	
	@PUT
	@Path("/dislike/{pf}/{tema}/{id}/{parent}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public int dislikeKomentar(@PathParam("pf") String pf, @PathParam("tema") String tema, @PathParam("id") int id, @PathParam("parent") int parent) throws IOException{
		Korisnik user = (Korisnik) request.getSession().getAttribute("korisnik");
		if(user==null)
			return -20000;
		
		for(Podforum p : PodForumi.getInstance().getPodForumi()){
			if(p.getNaziv().equals(pf)){
				for(Tema t : p.getTeme()){
					if(t.getNaslov().equals(tema)){
						if(parent==-1){
							for(Komentar k : t.getKomentari()){
								if(k.getId() == id){
									if(k.getDislajkovi().contains(user.getKorisnicko())){
										return k.getLike() - k.getDislike();
									}else if(k.getLajkovi().contains(user.getKorisnicko())){
										k.getDislajkovi().add(user.getKorisnicko());
										k.getLajkovi().remove(user.getKorisnicko());
										k.setLike(k.getLike()-1);
										k.setDislike(k.getDislike()+1);
										PodForumi.getInstance().save();
										user.getLajkovaniKomentari().remove(k);
										user.getDislajkovaniKomentari().add(k);
										Korisnici.getInstance().save();
										return k.getLike() - k.getDislike();
									}else{
										k.getDislajkovi().add(user.getKorisnicko());
										k.setDislike(k.getLike()+1);
										PodForumi.getInstance().save();
										user.getDislajkovaniKomentari().add(k);
										Korisnici.getInstance().save();
										return k.getLike() - k.getDislike();
									}
								}
							}
						}else{
							for(Komentar k : t.findParent(parent).getPodkomentari()){
								if(k.getId() == id){
									if(k.getDislajkovi().contains(user.getKorisnicko())){
										return k.getLike() - k.getDislike();
									}else if(k.getLajkovi().contains(user.getKorisnicko())){
										k.getDislajkovi().add(user.getKorisnicko());
										k.getLajkovi().remove(user.getKorisnicko());
										k.setLike(k.getLike()-1);
										k.setDislike(k.getDislike()+1);
										PodForumi.getInstance().save();
										user.getLajkovaniKomentari().remove(k);
										user.getDislajkovaniKomentari().add(k);
										Korisnici.getInstance().save();
										return k.getLike() - k.getDislike();
									}else{
										k.getDislajkovi().add(user.getKorisnicko());
										k.setDislike(k.getLike()+1);
										PodForumi.getInstance().save();
										user.getDislajkovaniKomentari().add(k);
										Korisnici.getInstance().save();
										return k.getLike() - k.getDislike();
									}
								}
							}
						}
					}
				}
			}
		}
		
		return -20000;
		
	}
	
	@POST
	@Path("/sacuvaj/{pf}/{tema}/{id}/{parent}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean sacuvajKomentar(@PathParam("pf") String pf, @PathParam("tema") String tema, @PathParam("id") int id, @PathParam("parent") int parent) throws IOException{
		Korisnik user = (Korisnik) request.getSession().getAttribute("korisnik");
		if(user==null)
			return false;
		
		for(Podforum p : PodForumi.getInstance().getPodForumi()){
			if(p.getNaziv().equals(pf)){
				for(Tema t : p.getTeme()){
					if(t.getNaslov().equals(tema)){
						if(parent==-1){
							for(Komentar k : t.getKomentari()){
								if(k.getId() == id){
									if(!user.getKomentari().contains(k)){
										user.getKomentari().add(k);
										Korisnici.getInstance().save();
									}
									return true;
								}
							}
						}else{
							for(Komentar k : t.findParent(parent).getPodkomentari()){
								if(k.getId() == id){
									if(!user.getKomentari().contains(k)){
										user.getKomentari().add(k);
										Korisnici.getInstance().save();
									}
									return true;
								}
							}
						}
					}
				}
			}
		}
		
		return false;
	}
	
	@DELETE
	@Path("/obrisi/{pf}/{tema}/{id}/{parent}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean obrisiKomentar(@PathParam("pf") String pf, @PathParam("tema") String tema, @PathParam("id") int id, @PathParam("parent") int parent) throws IOException{
		Korisnik user = (Korisnik) request.getSession().getAttribute("korisnik");
		if(user==null)
			return false;
	
		for(Podforum p : PodForumi.getInstance().getPodForumi()){
			if(p.getNaziv().equals(pf)){
				for(Tema t : p.getTeme()){
					if(t.getNaslov().equals(tema)){
						if(parent==-1){
							for(Komentar k : t.getKomentari()){
								if(k.getId() == id){
									if(user.getUloga().equals("administrator") || t.getAutor().equals(user.getKorisnicko()) ||
											k.getAutor().equals(user.getKorisnicko()) || user.getUloga().equals("moderator")){
										k.setAutor("[DELETED]");
										k.setTekst("[DELETED]");
										k.setObrisan(true);
										PodForumi.getInstance().save();
										return true;
									}else{
										return false;
									}
								}
							}
						}else{
							for(Komentar k : t.findParent(parent).getPodkomentari()){
								if(k.getId() == id){
									if(user.getUloga().equals("administrator") || t.getAutor().equals(user.getKorisnicko()) ||
											k.getAutor().equals(user.getKorisnicko()) || user.getUloga().equals("moderator")){
										k.setAutor("[DELETED]");
										k.setTekst("[DELETED]");
										k.setObrisan(true);
										PodForumi.getInstance().save();
										return true;
									}else{
										return false;
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	@PUT
	@Path("/izmeni")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean izmeniTemu(Komentar k) throws IOException{
		Korisnik user = (Korisnik) request.getSession().getAttribute("korisnik");
		if(user==null)
			return false;
		
		for(Podforum p : PodForumi.getInstance().getPodForumi()){
			if(p.getNaziv().equals(k.getPodforum())){
				if(p.getOdgovornimoderator().equals(user.getKorisnicko())){
					for(Tema t : p.getTeme()){
						if(t.getNaslov().equals(k.getTema())){
							if(k.getRoditelj() == -1){
								for(Komentar stariKom : t.getKomentari()){
									if(stariKom.getId() == k.getId()){
										if(stariKom.getTekst().equals(k.getTekst())){
											return false;
										}
										stariKom.setTekst(k.getTekst());
										PodForumi.getInstance().save();
										Korisnici.getInstance().save();
										return true;
									}
								}
							}else{
								for(Komentar stariKom : t.findParent(k.getRoditelj()).getPodkomentari()){
									if(stariKom.getId() == k.getId()){
										if(stariKom.getTekst().equals(k.getTekst())){
											return false;
										}
										stariKom.setTekst(k.getTekst());
										PodForumi.getInstance().save();
										Korisnici.getInstance().save();
										return true;
									}
								}
							}
						}
					}
				}else{
					for(Tema t : p.getTeme()){
						if(t.getNaslov().equals(k.getTema())){
							if(k.getRoditelj() == -1){
								for(Komentar stariKom : t.getKomentari()){
									if(stariKom.getId() == k.getId()){
										if(stariKom.getTekst().equals(k.getTekst()) || !stariKom.getAutor().equals(user.getKorisnicko())){
											return false;
										}
										stariKom.setTekst(k.getTekst());
										stariKom.setIzmenjen(true);
										PodForumi.getInstance().save();
										Korisnici.getInstance().save();
										return true;
									}
								}
							}else{
								for(Komentar sk : t.getKomentari()){
									Komentar stariKom = sk.findComment(k.getId());
									if(stariKom == null){
										continue;
									}
									if(stariKom.getId() == k.getId()){
										if(stariKom.getTekst().equals(k.getTekst()) || !stariKom.getAutor().equals(user.getKorisnicko())){
											return false;
										}
										stariKom.setTekst(k.getTekst());
										stariKom.setIzmenjen(true);
										PodForumi.getInstance().save();
										Korisnici.getInstance().save();
										return true;
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
}
