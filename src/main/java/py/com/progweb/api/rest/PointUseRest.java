package py.com.progweb.api.rest;

import java.util.Set;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import py.com.progweb.api.dto.CreateUsedPoints;
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
    public Response create(CreateUsedPoints body) throws ApiException {
        Integer clientId = body.getClientId();
        Integer conceptPointUseId = body.getConceptPointUseId();
        PointUse pointUse = new PointUse();
        Client client = clientDao.getById(clientId);
        Set<PointBag> listPointBag = client.getListPointBag();
        int totalPoints=this.totalPoints(listPointBag);

        ConceptPointUse concept = conceptDao.getById(conceptPointUseId);

        if (concept == null){
            throw new ApiException ("No existe el concepto",422);
        }

        if (totalPoints < concept.getPoints()){
            throw new ApiException ("No existen puntos suficientes",500);
        }

        pointUse.setClient(client);
        pointUse.setConcept(concept);
        pointUse.setUsed_points(concept.getPoints());

        int totalPointsConcept=concept.getPoints();

        pointUse.setDate(new Date());

        pointUse = pointUseDao.create(pointUse);

        Set<PointUseDetail> details = new HashSet<PointUseDetail>();

        for (PointBag pointBag : listPointBag) {
            int pointsBag = pointBag.getPointsBalance();
            PointBag pointBagNew = pointBag;
            int usedPointsBag=0;
            if ( pointsBag>totalPointsConcept ){
                pointsBag = pointsBag - totalPointsConcept;
                usedPointsBag=totalPointsConcept;
                totalPointsConcept=0;

            } else  {
                totalPointsConcept = totalPointsConcept - pointsBag;
                usedPointsBag=pointsBag;
                pointsBag=0;
            }

            pointBagNew.setPointsBalance(pointsBag);
            pointBagNew.setUsedPoints(pointBagNew.getPoints()-pointsBag);

            pointBagNew = pointBagDao.update(pointBagNew.getId(), pointBagNew);

            PointUseDetail pointUseDetail = new PointUseDetail();
            pointUseDetail.setPointBag(pointBagNew);
            pointUseDetail.setPointUse(pointUse);
            pointUseDetail.setUsedPoints(usedPointsBag);

            pointUseDetail = pointUseDetailDao.create(pointUseDetail);
            details.add(pointUseDetail);
        }
        pointUse.setDetails(details);
        return Response.ok(pointUse).build();
    }

    public Integer totalPoints (Set<PointBag> listPointBag){
        int total=0;

        for(PointBag pointBag : listPointBag){
            total=total + pointBag.getPointsBalance();
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
