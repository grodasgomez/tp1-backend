/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package py.com.progweb.api.ejb;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.progweb.api.exceptions.ApiException;
import py.com.progweb.api.model.Client;
import py.com.progweb.api.model.PointBag;

/**
 *
 * @author Snake
 */
@Stateless
public class PointBagDAO {
    @PersistenceContext(unitName = "pruebaPU")
    private EntityManager em;

    private Logger logger = LoggerFactory.getLogger(PointBagDAO.class);

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

    public void updateExpiredPoints() {
        List<PointBag> points = this.em
                .createQuery("SELECT p FROM PointBag p WHERE p.expirationDate < CURRENT_DATE and p.pointsBalance != 0",
                        PointBag.class)
                .getResultList();

        if (points.isEmpty()) {
            this.logger.info("No points to update");
            return;
        }

        for (PointBag pointBag : points) {
            pointBag.setPointsBalance(0);
            this.em.merge(pointBag);
            this.logger.info("Updated pointBag with id: {}", pointBag.getId());
        }
    }

	public List<PointBag> getByClient(Integer id) {
		return this.em.createQuery("select c from PointBag c where c.client.id = :client_id", PointBag.class).setParameter("client_id", id).getResultList();
	}

	public List<PointBag> listByRange(Integer lower, Integer upper) {
        return this.em.createQuery("select c from PointBag c where c.pointsBalance between :lower and :upper", PointBag.class).setParameter("lower", lower).setParameter("upper", upper).getResultList();
	}

	public List<Client> getClientsByExpiration(LocalDate expirationDate) {
		List<PointBag> bags = this.em.createQuery("select c from PointBag c where expiration_date = :expirationDay", PointBag.class).setParameter("expirationDay", expirationDate).getResultList();
        Set<Client> clients = new HashSet<>();
        for (PointBag bag : bags) {
            clients.add(bag.getClient());
        }
        return new ArrayList<>(clients);
	}
}
