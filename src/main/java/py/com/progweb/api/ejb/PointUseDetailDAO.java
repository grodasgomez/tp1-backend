/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package py.com.progweb.api.ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import py.com.progweb.api.model.PointUseDetail;

/**
 *
 * @author diemanuel
 */
@Stateless
public class PointUseDetailDAO {
    @PersistenceContext(unitName = "pruebaPU")
    private EntityManager em;

    public List<PointUseDetail> getAll() {
        return this.em.createQuery("select c from PointUseDetail c", PointUseDetail.class).getResultList();
    }

    public PointUseDetail create(PointUseDetail pointUseDetail) {
        pointUseDetail.setId(null);
        this.em.persist(pointUseDetail);
        return pointUseDetail;
    }
}
