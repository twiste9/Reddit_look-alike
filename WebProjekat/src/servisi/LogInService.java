package servisi;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import modeli.Korisnici;
import modeli.Korisnik;

@Path("/logIn")
public class LogInService {
	@Context
	HttpServletRequest request;
	@Context
	HttpServletResponse response;
	@Context
	ServletContext ctx;
	
	
	@POST
	@Path("/log")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean logIn(Korisnik k) throws IOException, ClassNotFoundException{
		Korisnici sacuvani=(Korisnici) ctx.getAttribute("korisnici");
		if(sacuvani==null){
				ctx.setAttribute("korisnici",Korisnici.getInstance());
				sacuvani=(Korisnici) ctx.getAttribute("korisnici");
		}
		HashMap<String, Korisnik> korisnici=sacuvani.getKorisnici();
		String username=k.getKorisnicko();
		String password=k.getLozinka();
		if(username==null || password==null || username.trim().equals("") || password.trim().equals("")){
			return false;
		}
		if(korisnici.containsKey(username)){
			Korisnik user=korisnici.get(username);

			if(password.equals(user.getLozinka())){
				request.getSession().setAttribute("korisnik",user);
				return true;
			}
			return false;
		}else
			return false;
	}
}
