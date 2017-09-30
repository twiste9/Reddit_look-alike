package servisi;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import modeli.*;

@Path("/register")
public class RegistrationService{
	@Context
	HttpServletRequest request;
	@Context
	HttpServletResponse response;
	@Context
	ServletContext ctx;
	
	@POST
	@Path("/reg")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean register(Korisnik user) throws FileNotFoundException, IOException, ClassNotFoundException{
		Korisnici sacuvani= Korisnici.getInstance();
//		if(sacuvani==null){
//			ctx.setAttribute("korisnici",Korisnici.getInstance());
//			sacuvani=(Korisnici) ctx.getAttribute("korisnici");
//		}
		
		String username=user.getKorisnicko();
		String password=user.getLozinka();
		String ime = user.getIme();
		String prezime = user.getPrezime();
		String email = user.getEmail();
		String telefon = user.getTelefon();
		
		if(username==null || password==null || ime==null || prezime==null || email==null ||
				telefon==null || username.trim().equals("") || password.trim().equals("") ||
				ime.trim().equals("") || prezime.trim().equals("") || email.trim().equals("") || telefon.trim().equals("")){
			return false;
		}
		
		if(sacuvani.getKorisnici().containsKey(user.getKorisnicko())){
			return false;
		}
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String s = sdf.format(date);
		user.setDatumreg(s);
		user.setPoruke(new ArrayList<Poruka>());
		user.setUloga("obican");
		user.setForumi(new ArrayList<Podforum>());
		user.setTeme(new ArrayList<Tema>());
		user.setKomentari(new ArrayList<Komentar>());
		user.setLajkovaniKomentari(new ArrayList<Komentar>());
		user.setDislajkovaniKomentari(new ArrayList<Komentar>());
		user.setZalbe(new ArrayList<Zalba>());
		sacuvani.addUsers(user);
		return true;
	}
}
