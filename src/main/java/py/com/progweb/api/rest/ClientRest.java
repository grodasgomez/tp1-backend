package py.com.progweb.api.rest;

import java.text.SimpleDateFormat;
import java.util.Date;
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

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Integer id) {
        return Response.ok(clientDao.getById(id)).build();
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

    @GET
    @Path("name/{name}")
    public Response getByNameLike(@PathParam("name") String name) {
        return Response.ok(clientDao.getByNameLike(name)).build();
    }

    @GET
    @Path("last_name/{lastName}")
    public Response getByLastNameLike(@PathParam("lastName") String lastName) {
        return Response.ok(clientDao.getByLastNameLike(lastName)).build();
    }

    @GET
    @Path("birth_date/{date}")
    public Response getByBirthDate(@PathParam("date") String date) throws ApiException {
        try {
			Date dateFormatted= new SimpleDateFormat("yyyy-MM-dd").parse(date);
			return Response.ok(clientDao.getByBirthDate(dateFormatted)).build();
		} catch (Exception e) {
			throw new ApiException("Incorrect Date", 500);
		}
    }
}
