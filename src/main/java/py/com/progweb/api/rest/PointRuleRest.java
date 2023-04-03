package py.com.progweb.api.rest;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import py.com.progweb.api.ejb.PointRuleDAO;
import py.com.progweb.api.exceptions.ApiException;
import py.com.progweb.api.model.PointRule;

@Path("point_rule")
@Produces("application/json")
@Consumes("application/json")
@RequestScoped
public class PointRuleRest {
    @Inject
    PointRuleDAO pointRuleDao;

    @GET
    @Path("/")
    public Response getAll() {
        List<PointRule> pointRules = pointRuleDao.getAll();
        return Response.ok(pointRules).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Integer id) {
        PointRule pointRule = pointRuleDao.getById(id);
        return Response.ok(pointRule).build();
    }

    @POST
    @Path("/")
    public Response create(PointRule pointRule) throws ApiException {
        pointRuleDao.create(pointRule);
        return Response.ok(pointRule).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Integer id, PointRule body) throws ApiException {
        PointRule pointRule = pointRuleDao.update(id, body);
        return Response.ok(pointRule).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Integer id) throws ApiException {
        PointRule pointRule = pointRuleDao.delete(id);
        return Response.ok(pointRule).build();
    }
}
