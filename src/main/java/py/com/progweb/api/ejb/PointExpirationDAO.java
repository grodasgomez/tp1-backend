package py.com.progweb.api.ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import py.com.progweb.api.exceptions.ApiException;
import py.com.progweb.api.model.PointExpiration;

import java.util.Date;
import java.util.List;

@Stateless
public class PointExpirationDAO {
    @PersistenceContext(unitName = "pruebaPU")
    private EntityManager em;

    public PointExpiration create(PointExpiration pointExpiration) throws ApiException {
        pointExpiration.setId(null);

        if (pointExpiration.getValidStartDate().compareTo(pointExpiration.getValidEndDate()) > 0) {
            throw new ApiException("Start date after end date", 422);
        }

        this.em.persist(pointExpiration);
        return pointExpiration;
    }

    public PointExpiration update(Integer id, PointExpiration payload) throws ApiException {
        PointExpiration pointExp = this.getById(id);
        if (pointExp == null) {
            throw new ApiException("PointExpiration not found", 404);
        }
        if (payload.getValidStartDate() != null) {
            pointExp.setValidStartDate(payload.getValidStartDate());
        }
        if (payload.getValidEndDate() != null) {
            pointExp.setValidEndDate(payload.getValidEndDate());
        }
        if (payload.getValidDaysCount() != null) {
            pointExp.setValidDaysCount(payload.getValidDaysCount());
        }

        if (pointExp.getValidStartDate().compareTo(pointExp.getValidEndDate()) > 0) {
            throw new ApiException("Start date after end date", 422);
        }

        this.em.merge(pointExp);

        return pointExp;
    }

    public PointExpiration getById(Integer id) {
        return this.em.find(PointExpiration.class, id);
    }

    public PointExpiration getForDate(Date date) {
        List<PointExpiration> result = this.em.createQuery("select c from PointExpiration c where c.valid_start_date <= :date and c.valid_stop_date >= :date",PointExpiration.class)
            .setParameter(":date", date)
            .setMaxResults(1)
            .getResultList();
        
        return !result.isEmpty() ? result.get(0) : null;
    }

    public List<PointExpiration> getAll() {
        return this.em.createQuery("select c from PointExpiration c", PointExpiration.class).getResultList();
    }

    public PointExpiration delete(Integer id) throws ApiException {
        PointExpiration pointExp = this.getById(id);
        if (pointExp == null) {
            throw new ApiException("PointExpiration not found", 404);
        }
        this.em.remove(pointExp);
        return pointExp;

    }
}