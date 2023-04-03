package py.com.progweb.api.rest;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import py.com.progweb.api.ejb.PointUseDAO;
import py.com.progweb.api.exceptions.ApiException;

@Path("points_used")
@Produces("application/json")
@Consumes("application/json")
@RequestScoped
public class PointUseRest {
	@Inject
	PointUseDAO pointUseDao;

	@GET
	@Path("/")
	public Response getAll() {
		return Response.ok(pointUseDao.getAll()).build();
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
