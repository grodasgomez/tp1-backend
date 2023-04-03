/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package py.com.progweb.api.rest;

import java.util.Calendar;
import java.util.Date;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import py.com.progweb.api.dto.CreateBag;
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
    // @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Response create(CreateBag body) throws ApiException {
        Integer id = body.getId();
        Integer amount = body.getAmount();

        PointBag pointBag = new PointBag();
        pointBag.setClient(clientDao.getById(id));
        pointBag.setOperationAmount(amount);
        
        PointRule pointRule = pointRuleDao.getByAmount(amount);
        
        if (pointRule!=null){
            pointBag.setPoints(amount/pointRule.getConversionRate());
        }else{
            throw new ApiException ("No hay una regla para este monto",500);
        }
        
        pointBag.setPointsBalance(pointBag.getPoints());
        pointBag.setUsedPoints(0);
        
        Date date = new Date();
        PointExpiration pointExpiration= pointExpirationDao.getForDate(date);
        
        if (pointExpiration!=null){
            pointBag.setExpirationDate(this.sumDaysToDate(date, pointExpiration.getValidDaysCount()));
        }else{
            pointBag.setExpirationDate(this.sumDaysToDate(date, 3));
        }
        
        pointBag.setAssignmentDate(date);
        
        
        pointBag = pointBagDao.create(pointBag);

        pointBag.getClient();
        
        return Response.ok(pointBag).build();
    }
    
    public Date sumDaysToDate(Date date, int days){
      if (days==0) return date;
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date); 
      calendar.add(Calendar.DAY_OF_YEAR, days);  
      return calendar.getTime(); 
    }
    
}
