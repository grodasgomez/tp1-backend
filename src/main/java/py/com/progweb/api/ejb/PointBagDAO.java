/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package py.com.progweb.api.ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
    
}
