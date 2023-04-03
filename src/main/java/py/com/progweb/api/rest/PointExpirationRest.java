package py.com.progweb.api.rest;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import py.com.progweb.api.ejb.PointExpirationDAO;
import py.com.progweb.api.exceptions.ApiException;
import py.com.progweb.api.model.PointExpiration;

@Path("point_expiration")
@Produces("application/json")
@Consumes("application/json")
@RequestScoped
public class PointExpirationRest {

    @Inject
    PointExpirationDAO pointExpDao;

    @GET
    @Path("/")
    public Response getAll() {
        List<PointExpiration> pointExps = pointExpDao.getAll();
        return Response.ok(pointExps).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Integer id) {
        return Response.ok(pointExpDao.getById(id)).build();
    }

    @GET
    @Path("/{date}")
    public Response getForDate(@PathParam("date") Date date) {
        return Response.ok(pointExpDao.getForDate(date)).build();
    }

    @POST
    @Path("/")
    public Response create(PointExpiration body) throws ApiException {
        PointExpiration client = pointExpDao.create(body);
        return Response.ok(client).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Integer id, PointExpiration body) throws ApiException {
        PointExpiration client = pointExpDao.update(id, body);
        return Response.ok(client).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Integer id) throws ApiException {
        PointExpiration client = pointExpDao.delete(id);
        return Response.ok(client).build();
    }
}