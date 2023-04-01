package py.com.progweb.api.rest;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import py.com.progweb.api.ejb.ConceptDAO;
import py.com.progweb.api.exceptions.ApiException;
import py.com.progweb.api.model.ConceptPointUse;

@Path("concepts")
@Produces("application/json")
@Consumes("application/json")
@RequestScoped
public class ConceptRest {
	@Inject
	ConceptDAO conceptDao;

	@GET
    @Path("/")
    public Response getAll() {
        List<ConceptPointUse> concepts = conceptDao.getAll();
        return Response.ok(concepts).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Integer id) {
        ConceptPointUse concept = conceptDao.getById(id);
        return Response.ok(concept).build();
    }

    @POST
    @Path("/")
    public Response create(ConceptPointUse body) {
        ConceptPointUse concept = conceptDao.create(body);
        return Response.ok(concept).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Integer id, ConceptPointUse body) throws ApiException {
        ConceptPointUse concept = conceptDao.update(id, body);
        return Response.ok(concept).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Integer id) throws ApiException {
        ConceptPointUse concept = conceptDao.delete(id);
        return Response.ok(concept).build();
    }
}
