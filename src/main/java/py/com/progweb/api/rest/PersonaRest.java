package py.com.progweb.api.rest;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import py.com.progweb.api.dto.PersonaDTO;
import py.com.progweb.api.ejb.PersonaDAO;
import py.com.progweb.api.model.Persona;

@Path("persona")
@Produces("application/json")
@Consumes("application/json")
@RequestScoped
public class PersonaRest {

  @Inject
  PersonaDAO personaDAO;

  @GET
  @Path("saludo")
  public Response hola() {
    return Response.ok("hola").build();
  }

  @POST
  @Path("/")
  public Response agregar(PersonaDTO entidad) {
    PersonaDTO dto = personaDAO.agregar(entidad);
    return Response.ok(dto).build();
  }

  @GET
  @Path("/")
  public Response lista(@QueryParam("nombre") String nombre) {
    return Response.ok(personaDAO.lista(nombre)).build();
  }

  @GET
  @Path("/{id}")
  public Response getById(@PathParam("id") Integer id) {
    return Response.ok(personaDAO.getById(id)).build();
  }
}