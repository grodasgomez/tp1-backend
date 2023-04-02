package py.com.progweb.api.rest;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import py.com.progweb.api.ejb.PointUseDAO;

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
}
