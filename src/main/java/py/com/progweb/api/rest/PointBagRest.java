/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package py.com.progweb.api.rest;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import py.com.progweb.api.dto.CreateBag;
import py.com.progweb.api.dto.CustomDateUtils;
import py.com.progweb.api.ejb.ClientDAO;
import py.com.progweb.api.ejb.PointBagDAO;
import py.com.progweb.api.ejb.PointExpirationDAO;
import py.com.progweb.api.ejb.PointRuleDAO;
import py.com.progweb.api.exceptions.ApiException;
import py.com.progweb.api.model.PointBag;
import py.com.progweb.api.model.PointExpiration;
import py.com.progweb.api.model.PointRule;

/**
 *
 * @author Snake
 */
@Path("point_bags")
@Produces("application/json")
@Consumes("application/json")
@RequestScoped
public class PointBagRest {
    @Inject
    PointBagDAO pointBagDao;

    @Inject
    ClientDAO clientDao;

    @Inject
    PointRuleDAO pointRuleDao;

    @Inject
    PointExpirationDAO pointExpirationDao;

    @POST
    @Path("/")
    public Response create(CreateBag body) throws ApiException {
        Integer id = body.getId();
        Integer amount = body.getAmount();

        PointBag pointBag = new PointBag();
        pointBag.setClient(clientDao.getById(id));
        pointBag.setOperationAmount(amount);

        PointRule pointRule = pointRuleDao.getByAmount(amount);

        if (pointRule != null){
            pointBag.setPoints(amount/pointRule.getConversionRate());
        } else {
            throw new ApiException ("No hay una regla para este monto",500);
        }

        pointBag.setPointsBalance(pointBag.getPoints());
        pointBag.setUsedPoints(0);

        Date date = CustomDateUtils.sumDaysToDate(0);
        PointExpiration pointExpiration= pointExpirationDao.getForDate(date);

        if (pointExpiration != null){
            pointBag.setExpirationDate(CustomDateUtils.sumDaysToDate(pointExpiration.getValidDaysCount()));
        } else {
            pointBag.setExpirationDate(CustomDateUtils.sumDaysToDate(3));
        }

        pointBag.setAssignmentDate(date);
        pointBag = pointBagDao.create(pointBag);
        pointBag.getClient();
        return Response.ok(pointBag).build();
    }

    @GET
	@Path("/client/{id}")
	public Response getByClient(@PathParam("id") Integer id) {
		return Response.ok(pointBagDao.getByClient(id)).build();
	}

    @GET
    @Path("/range/")
    public Response listByRange(@QueryParam("lower") Integer lower, @QueryParam("upper") Integer upper) throws ApiException {
        if (lower == null || upper == null)
            throw new ApiException("Debe especificar un rango", 400);
        return Response.ok(pointBagDao.listByRange(lower, upper)).build();
    }

    @GET
	@Path("/expires/{days}")
	public Response getClientsByExpiration(@PathParam("days") Integer days) {
        Date expiration = CustomDateUtils.sumDaysToDate(days);
        LocalDate dateFormatted = expiration.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return Response.ok(pointBagDao.getClientsByExpiration(dateFormatted)).build();
	}
}
