package py.com.progweb.api.rest;

import java.util.Date;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import py.com.progweb.api.ejb.ClientDAO;
import py.com.progweb.api.ejb.ConceptDAO;

import py.com.progweb.api.ejb.PointUseDAO;
import py.com.progweb.api.exceptions.ApiException;
import py.com.progweb.api.model.Client;
import py.com.progweb.api.model.ConceptPointUse;
import py.com.progweb.api.model.PointBag;
import py.com.progweb.api.model.PointUse;

@Path("points_used")
@Produces("application/json")
@Consumes("application/json")
@RequestScoped
public class PointUseRest {
	@Inject
	PointUseDAO pointUseDao;
        
        @Inject
        ClientDAO clientDao;
        
        @Inject
        ConceptDAO conceptDao;

	@GET
	@Path("/")
	public Response getAll() {
		return Response.ok(pointUseDao.getAll()).build();
	}
        
        @POST
        @Path("/")
        public Response create(Integer clientId, Integer conceptPointUseId) throws ApiException {
            PointUse pointUse = new PointUse();
            Client client = clientDao.getById(clientId);
            List<PointBag> listPointBag = client.getListPointBag();
            int totalPoints=this.totalPoints(listPointBag);
            
            
            ConceptPointUse concept = conceptDao.getById(conceptPointUseId);
            
            if (totalPoints>concept.getPoints()){
                throw new ApiException ("No existen puntos suficientes",500);
            }
            
            pointUse.setClient(client);
            if (concept==null){
                throw new ApiException ("No existe el concepto elegido",500);
            }else{
                pointUse.setConcept(concept);
                pointUse.setUsed_points(concept.getPoints());
            }
            
            int totalPointsConcept=concept.getPoints();
            
            pointUse.setDate(new Date());

            pointUse = pointUseDao.create(pointUse);
            
            for (int i=0;i<listPointBag.size() && totalPointsConcept>0;i++){
                int pointsBag=listPointBag.get(i).getPointsBalance();
                
                if ( pointsBag>totalPointsConcept ){
                    pointsBag = pointsBag - totalPointsConcept;
                    totalPointsConcept=0;
                }else{
                    totalPointsConcept = totalPointsConcept - pointsBag;
                    pointsBag=0;
                }
                
                
                
            }
            
            return Response.ok(pointUse).build();
        }
        
        public Integer totalPoints (List<PointBag> listPointBag){
            int total=0;
            for (int i=0;i<listPointBag.size();i++){
                total=total + listPointBag.get(i).getPointsBalance();
            }
            return total; 
        }
}
