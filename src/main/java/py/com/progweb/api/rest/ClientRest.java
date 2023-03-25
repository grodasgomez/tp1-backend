package py.com.progweb.api.rest;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import py.com.progweb.api.ejb.ClientDAO;
import py.com.progweb.api.exceptions.ApiException;
import py.com.progweb.api.model.Client;

@Path("clients")
@Produces("application/json")
@Consumes("application/json")
@RequestScoped
public class ClientRest {

    @Inject
    ClientDAO clientDao;

    @GET
    @Path("/")
    public Response getAll() {
        List<Client> clients = clientDao.getAll();
        return Response.ok(clients).build();
    }

    @POST
    @Path("/")
    public Response create(Client body) {
        Client client = clientDao.create(body);
        return Response.ok(client).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Integer id, Client body) throws ApiException {
        Client client = clientDao.update(id, body);
        return Response.ok(client).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Integer id) throws ApiException {
        Client client = clientDao.delete(id);
        return Response.ok(client).build();
    }
}