/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package py.com.progweb.api.ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import py.com.progweb.api.exceptions.ApiException;
import py.com.progweb.api.model.PointBag;

/**
 *
 * @author Snake
 */
@Stateless
public class PointBagDAO {
    @PersistenceContext(unitName = "pruebaPU")
    private EntityManager em;
    
    public PointBag create(PointBag pointBag) {
        pointBag.setId(null);
        this.em.persist(pointBag);
        return pointBag;
    }
    
    public PointBag update(Integer id, PointBag payload) throws ApiException {
        PointBag pointBag = this.getById(id);
        if (pointBag == null) {
            throw new ApiException("Client not found", 404);
        }
        if (payload.getUsedPoints() != null) {
            pointBag.setUsedPoints(payload.getUsedPoints());
        }
        if (payload.getPointsBalance() != null) {
            pointBag.setPointsBalance(payload.getPointsBalance());
        }

        this.em.merge(pointBag);

        return pointBag;
    }
    
    public PointBag getById(Integer id) {
        return this.em.find(PointBag.class, id);
    }
    
}
