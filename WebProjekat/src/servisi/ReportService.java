package servisi;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import modeli.*;

@Path("/zalbe")
public class ReportService {
	@Context
	HttpServletResponse response;
	@Context
	HttpServletRequest request;
	@Context
	ServletContext ctx;
	
	@POST
	@Path("/podforumZalba")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean novaPodforumZalba(Zalba z) throws IOException{
		if(z.getTekst() == null || z.getPodforum() == null || z.getTekst().trim().equals("") ||
				z.getPodforum().trim().equals("")){
			return false;
		}
		Korisnik user = (Korisnik) request.getSession().getAttribute("korisnik");
		if(user == null)
			return false;
		
		z.setTema(null);
		z.setKomentar(-2);
		z.setKomentarTekst(null);
		z.setAutor(user.getKorisnicko());
		for(Korisnik k : Korisnici.getInstance().getKorisnici().values()){
			if(k.getUloga().equals("administrator")){
				k.getZalbe().add(z);
			}
			Korisnici.getInstance().save();
		}
		return true;
	}
	
	@POST
	@Path("/temaZalba")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean novaTemaZalba(Zalba z) throws IOException{
		if(z.getTekst() == null || z.getPodforum() == null || z.getTema()==null ||
				z.getTema().trim().equals("") || z.getTekst().trim().equals("") ||
				z.getPodforum().trim().equals("")){
			return false;
		}
		Korisnik user = (Korisnik) request.getSession().getAttribute("korisnik");
		if(user == null)
			return false;
		
		z.setAutor(user.getKorisnicko());
		z.setKomentar(-2);
		z.setKomentarTekst(null);
		//odgovorni mod
		String mod = null;
		for(Podforum p : PodForumi.getInstance().getPodForumi()){
			if(p.getNaziv().equals(z.getPodforum())){
				mod = p.getOdgovornimoderator();
				break;
			}
		}
		
		
		for(Korisnik k : Korisnici.getInstance().getKorisnici().values()){
			if(k.getUloga().equals("administrator") || (mod!=null && k.getKorisnicko().equals(mod))){
				k.getZalbe().add(z);
			}
		}
		Korisnici.getInstance().save();
		return true;
	}
	
	
	@POST
	@Path("/komentarZalba")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean novaKomentarZalba(Zalba z) throws IOException{
		if(z.getTekst() == null || z.getPodforum() == null || z.getTema()==null ||
				z.getTema().trim().equals("") || z.getTekst().trim().equals("") ||
				z.getPodforum().trim().equals("")){
			return false;
		}
		Korisnik user = (Korisnik) request.getSession().getAttribute("korisnik");
		if(user == null)
			return false;
		
		z.setAutor(user.getKorisnicko());
		z.setKomentarTekst(null);
		for(Podforum p : PodForumi.getInstance().getPodForumi()){
			if(p.getNaziv().equals(z.getPodforum())){
				for(Tema t : p.getTeme()){
					if(t.getNaslov().equals(z.getTema())){
						for(Komentar k : t.getKomentari()){
							if(k.findComment(z.getKomentar()) != null){
								z.setKomentarTekst(k.findComment(z.getKomentar()).getTekst());
							}
						}
					}
				}
			}
		}
		if(z.getKomentarTekst() != null){
			//odgovorni mod
			String mod = null;
			for(Podforum p : PodForumi.getInstance().getPodForumi()){
				if(p.getNaziv().equals(z.getPodforum())){
					mod = p.getOdgovornimoderator();
					break;
				}
			}
			
			
			for(Korisnik k : Korisnici.getInstance().getKorisnici().values()){
				if(k.getUloga().equals("administrator") || (mod!=null && k.getKorisnicko().equals(mod))){
					k.getZalbe().add(z);
				}
			}
			Korisnici.getInstance().save();
			return true;
		}
		
		return false;
	}
	
	@DELETE
	@Path("/obrisiEntitet/{index}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean obrisiEntitet(@PathParam("index") int index) throws IOException{
		Korisnik user = (Korisnik) request.getSession().getAttribute("korisnik");
		if(user == null)
			return false;
		
		if(index > user.getZalbe().size()-1)
			return false;
		
		if(user.getZalbe().get(index).isResena())
			return false;
		
		for(Podforum p : PodForumi.getInstance().getPodForumi()){
			if(p.getNaziv().equals(user.getZalbe().get(index).getPodforum())){
				if(user.getZalbe().get(index).getTema() == null && user.getZalbe().get(index).getKomentar() == -2){
					PodForumi.getInstance().getPodForumi().remove(p);
					user.getZalbe().get(index).setResena(true);
					for(Korisnik k : Korisnici.getInstance().getKorisnici().values()){
						if(k.getKorisnicko().equals(user.getZalbe().get(index).getAutor())){
							Poruka por = new Poruka();
							por.setPrimalac(k.getKorisnicko());
							por.setPosaljilac(user.getKorisnicko());
							por.setProcitana(false);
							por.setSadrzaj("Your report has been received and the subforum was deleted.");
							k.getPoruke().add(por);
						}
						if(k.getKorisnicko().equals(p.getOdgovornimoderator())){
							Poruka por = new Poruka();
							por.setPrimalac(k.getKorisnicko());
							por.setPosaljilac(user.getKorisnicko());
							por.setProcitana(false);
							por.setSadrzaj("Your subforum has been reported. In result the subforum has been deleted.");
							k.getPoruke().add(por);
						}
					}
					PodForumi.getInstance().save();
					Korisnici.getInstance().save();
					return true;
					
				}else if(user.getZalbe().get(index).getTema() != null && user.getZalbe().get(index).getKomentar() == -2){
					for(Tema t : p.getTeme()){
						if(t.getNaslov().equals(user.getZalbe().get(index).getTema())){
							p.getTeme().remove(t);
							user.getZalbe().get(index).setResena(true);
							for(Korisnik k : Korisnici.getInstance().getKorisnici().values()){
								if(k.getKorisnicko().equals(user.getZalbe().get(index).getAutor())){
									Poruka por = new Poruka();
									por.setPrimalac(k.getKorisnicko());
									por.setPosaljilac(user.getKorisnicko());
									por.setProcitana(false);
									por.setSadrzaj("Your report has been received and the topic was deleted.");
									k.getPoruke().add(por);
								}
								if(k.getKorisnicko().equals(t.getAutor())){
									Poruka por = new Poruka();
									por.setPrimalac(k.getKorisnicko());
									por.setPosaljilac(user.getKorisnicko());
									por.setProcitana(false);
									por.setSadrzaj("Your topic has been reported. In result the topic has been deleted.");
									k.getPoruke().add(por);
								}
							}
							PodForumi.getInstance().save();
							Korisnici.getInstance().save();
							return true;
						}
					}
				}else if(user.getZalbe().get(index).getTema() != null && user.getZalbe().get(index).getKomentar() != -2){
					for(Tema t : p.getTeme()){
						if(t.getNaslov().equals(user.getZalbe().get(index).getTema())){
							for(Komentar kom : t.getKomentari()){
								if(kom.findComment(user.getZalbe().get(index).getKomentar()) != null){
									kom.findComment(user.getZalbe().get(index).getKomentar()).setObrisan(true);
									user.getZalbe().get(index).setResena(true);
									for(Korisnik k : Korisnici.getInstance().getKorisnici().values()){
										if(k.getKorisnicko().equals(user.getZalbe().get(index).getAutor())){
											Poruka por = new Poruka();
											por.setPrimalac(k.getKorisnicko());
											por.setPosaljilac(user.getKorisnicko());
											por.setProcitana(false);
											por.setSadrzaj("Your report has been received and the comment was deleted.");
											k.getPoruke().add(por);
										}
										if(k.getKorisnicko().equals(kom.findComment(user.getZalbe().get(index).getKomentar()).getAutor())){
											Poruka por = new Poruka();
											por.setPrimalac(k.getKorisnicko());
											por.setPosaljilac(user.getKorisnicko());
											por.setProcitana(false);
											por.setSadrzaj("Your comment has been reported. In result the comment has been deleted.");
											k.getPoruke().add(por);
										}
									} 
									Korisnici.getInstance().save();
									return true;
								}
							}
							return false;
						}
					}
				}
			}
		}
		
		return false;
	}
	
	@POST
	@Path("/upozorenje/{index}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean posaljiUpozorenje(@PathParam("index") int index) throws IOException{
		Korisnik user = (Korisnik) request.getSession().getAttribute("korisnik");
		if(user == null)
			return false;
		
		if(index > user.getZalbe().size()-1)
			return false;
		
		if(user.getZalbe().get(index).isResena())
			return false;
		
		for(Podforum p : PodForumi.getInstance().getPodForumi()){
			if(p.getNaziv().equals(user.getZalbe().get(index).getPodforum())){
				if(user.getZalbe().get(index).getTema() == null && user.getZalbe().get(index).getKomentar() == -2){
					for(Korisnik k : Korisnici.getInstance().getKorisnici().values()){
						if(k.getKorisnicko().equals(user.getZalbe().get(index).getAutor())){
							Poruka por = new Poruka();
							por.setPrimalac(k.getKorisnicko());
							por.setPosaljilac(user.getKorisnicko());
							por.setProcitana(false);
							por.setSadrzaj("Your report has been accepted, the user is warned.");
							k.getPoruke().add(por);
						}
						if(k.getKorisnicko().equals(p.getOdgovornimoderator())){
							Poruka por = new Poruka();
							por.setPrimalac(k.getKorisnicko());
							por.setPosaljilac(user.getKorisnicko());
							por.setProcitana(false);
							por.setSadrzaj("Warning, your subforum has been reported, please follow the rules.");
							k.getPoruke().add(por);
						}
					}
					user.getZalbe().get(index).setResena(true);
					Korisnici.getInstance().save();
					return true;
				}else if(user.getZalbe().get(index).getTema() != null && user.getZalbe().get(index).getKomentar() == -2){
					for(Tema t : p.getTeme()){
						if(t.getNaslov().equals(user.getZalbe().get(index).getTema())){
							for(Korisnik k : Korisnici.getInstance().getKorisnici().values()){
								if(k.getKorisnicko().equals(user.getZalbe().get(index).getAutor())){
									Poruka por = new Poruka();
									por.setPrimalac(k.getKorisnicko());
									por.setPosaljilac(user.getKorisnicko());
									por.setProcitana(false);
									por.setSadrzaj("Your report has been accepted, the user is warned.");
									k.getPoruke().add(por);
								}
								if(k.getKorisnicko().equals(t.getAutor())){
									Poruka por = new Poruka();
									por.setPrimalac(k.getKorisnicko());
									por.setPosaljilac(user.getKorisnicko());
									por.setProcitana(false);
									por.setSadrzaj("Warning, your topic has been reported, please follow the rules.");
									k.getPoruke().add(por);
								}
							}
							user.getZalbe().get(index).setResena(true);
							Korisnici.getInstance().save();
							return true;
						}
					}
				}else if(user.getZalbe().get(index).getTema() != null && user.getZalbe().get(index).getKomentar() != -2){
					for(Tema t : p.getTeme()){
						if(t.getNaslov().equals(user.getZalbe().get(index).getTema())){
							for(Komentar kom : t.getKomentari()){
								if(kom.findComment(user.getZalbe().get(index).getKomentar())!=null){
									for(Korisnik k : Korisnici.getInstance().getKorisnici().values()){
										if(k.getKorisnicko().equals(user.getZalbe().get(index).getAutor())){
											Poruka por = new Poruka();
											por.setPrimalac(k.getKorisnicko());
											por.setPosaljilac(user.getKorisnicko());
											por.setProcitana(false);
											por.setSadrzaj("Your report has been accepted, the user is warned.");
											k.getPoruke().add(por);
										}
										if(k.getKorisnicko().equals(kom.findComment(user.getZalbe().get(index).getKomentar()).getAutor())){
											Poruka por = new Poruka();
											por.setPrimalac(k.getKorisnicko());
											por.setPosaljilac(user.getKorisnicko());
											por.setProcitana(false);
											por.setSadrzaj("Warning, your comment has been reported, please follow the rules.");
											k.getPoruke().add(por);
										}
									}
									user.getZalbe().get(index).setResena(true);
									Korisnici.getInstance().save();
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
	
	
	@POST
	@Path("/odbijZalbu/{index}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean odbijZalbu(@PathParam("index") int index) throws IOException{
		Korisnik user = (Korisnik) request.getSession().getAttribute("korisnik");
		if(user == null)
			return false;
		
		if(index > user.getZalbe().size()-1)
			return false;
		
		if(user.getZalbe().get(index).isResena())
			return false;
		
		for(Korisnik k : Korisnici.getInstance().getKorisnici().values()){
			if(k.getKorisnicko().equals(user.getZalbe().get(index).getAutor())){
				Poruka por = new Poruka();
				por.setPrimalac(k.getKorisnicko());
				por.setPosaljilac(user.getKorisnicko());
				por.setProcitana(false);
				por.setSadrzaj("Your report has been rejected");
				k.getPoruke().add(por);
				user.getZalbe().get(index).setResena(true);
				Korisnici.getInstance().save();
				return true;
			}
		}
		return false;
	}
}
