package py.com.progweb.api.rest;

import java.util.List;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import py.com.progweb.api.ejb.ClientDAO;
import py.com.progweb.api.ejb.ConceptDAO;
import py.com.progweb.api.ejb.PointBagDAO;

import py.com.progweb.api.ejb.PointUseDAO;
import py.com.progweb.api.ejb.PointUseDetailDAO;
import py.com.progweb.api.exceptions.ApiException;
import py.com.progweb.api.model.Client;
import py.com.progweb.api.model.ConceptPointUse;
import py.com.progweb.api.model.PointBag;
import py.com.progweb.api.model.PointUse;
import py.com.progweb.api.model.PointUseDetail;

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
        
        @Inject
        PointBagDAO pointBagDao;
        
        @Inject
        PointUseDetailDAO pointUseDetailDao;

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
                PointBag pointBagNew = listPointBag.get(i);
                int usedPointsBag=0;
                if ( pointsBag>totalPointsConcept ){
                    pointsBag = pointsBag - totalPointsConcept;
                    usedPointsBag=totalPointsConcept;
                    totalPointsConcept=0;
                    
                }else{
                    totalPointsConcept = totalPointsConcept - pointsBag;
                    usedPointsBag=pointsBag;
                    pointsBag=0;
                }
                
                pointBagNew.setPointsBalance(pointsBag);
                pointBagNew.setUsedPoints(pointBagNew.getPoints()-pointsBag);
                
                pointBagNew = pointBagDao.update(pointBagNew.getId(), pointBagNew);
                
                //createUseDetail
                PointUseDetail pointUseDetail = new PointUseDetail();
                pointUseDetail.setPointBag(pointBagNew);
                pointUseDetail.setPointUse(pointUse);
                pointUseDetail.setUsedPoints(usedPointsBag);
                
                pointUseDetail = pointUseDetailDao.create(pointUseDetail);
                
                
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

	@GET
	@Path("/concept/{id}")
	public Response getByConcept(@PathParam("id") Integer id) {
		return Response.ok(pointUseDao.getByConcept(id)).build();
	}
	@GET
	@Path("/client/{id}")
	public Response getByClient(@PathParam("id") Integer id) {
		return Response.ok(pointUseDao.getByClient(id)).build();
	}

	@GET
	@Path("/used_date/{date}")
	public Response getByUsedDate(@PathParam("date") String date) throws ApiException {
		try {
			Date dateFormatted= new SimpleDateFormat("yyyy-MM-dd").parse(date);
			return Response.ok(pointUseDao.getByUsedDate(dateFormatted)).build();
		} catch (Exception e) {
			throw new ApiException("Incorrect Date", 500);
		}
	}
}
