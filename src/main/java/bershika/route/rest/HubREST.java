package bershika.route.rest;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import bershika.route.entities.HubEntity;
import bershika.route.entities.MemberEntity;

@Path("/hub")
@RequestScoped
public class HubREST {
	 @Inject
	   private EntityManager em;
	
//	@GET
//	   @Path("/{id:[0-9][0-9]*}")
//	   @Produces("text/xml")
//	   public String getHub(@PathParam("id") String id) {
//	      return em.find(HubEntity.class, id);
//	   }

}
