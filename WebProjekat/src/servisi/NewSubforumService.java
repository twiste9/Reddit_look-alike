package servisi;

import java.io.FileNotFoundException;
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
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import modeli.*;

@Path("/noviPodforum")
public class NewSubforumService {
	@Context
	HttpServletRequest request;
	@Context
	HttpServletResponse response;
	@Context
	ServletContext ctx;
	
	
	
	@GET
	@Path("/userExists/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean daLiKorisnikPostoji(@PathParam("username") String userName){
		if(Korisnici.getInstance().getKorisnici().containsKey(userName)){
			return true;
		}
		return false;
	}
	
	@POST
	@Path("/dodaj")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public int dodajPodforum(Podforum p) throws FileNotFoundException, IOException{

		Korisnik user = (Korisnik) request.getSession().getAttribute("korisnik");
		p.setOdgovornimoderator(user.getKorisnicko());
		p.setTeme(new ArrayList<Tema>());
		
		
		return PodForumi.getInstance().addForums(p);
	}
	
	@POST
	@Path("/dodajIkonicu/{naziv}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public void dodajSliku(InputStream inputStream, @PathParam("naziv") String idPodforuma) throws IOException{
		PodForumi lista = PodForumi.getInstance();
		String naziv = "";
		
		for(Podforum p : lista.getPodForumi()){
			if(p.getNaziv().equals(idPodforuma)){
				naziv = p.getIkonica();
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
				System.out.println("error1");
			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
						System.out.println("error2");
					}
				}
				if (outputStream != null) {
					try {
						outputStream.close();
					} catch (IOException e) {
						System.out.println("error3");
					}
	
				}
			}
			
			for(Podforum p : lista.getPodForumi()){
				if(!naziv.equals("")){
					if(p.getNaziv().equals(idPodforuma)){
						p.setIkonica("slike\\"+naziv);
						lista.save();
					}
				}
			}
	}
	
}
