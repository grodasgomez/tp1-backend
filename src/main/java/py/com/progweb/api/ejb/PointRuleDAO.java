package py.com.progweb.api.ejb;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import py.com.progweb.api.exceptions.ApiException;
import py.com.progweb.api.model.PointRule;

import java.util.List;

@Stateless
public class PointRuleDAO {
    @PersistenceContext(unitName = "pruebaPU")
    private EntityManager em;

    public List<PointRule> getAll() {
        return this.em.createQuery("select c from PointRule c", PointRule.class).getResultList();
    }
    
    public PointRule getByAmount(Integer amount) {
        List<PointRule> result= this.em.createQuery("select c from PointRule c where c.lowerLimit<= :custMount and c.upperLimit>= :custMount", PointRule.class).setParameter("custMount", amount).setMaxResults(1).getResultList();
        System.out.println("resul "+result);
        return !result.isEmpty() ? result.get(0) : null;
    }

    public PointRule getById(Integer id) {
        return this.em.find(PointRule.class, id);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public PointRule create(PointRule pointRule) {
        pointRule.setId(null);
        this.em.persist(pointRule);
        return pointRule;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public PointRule delete(Integer id) throws ApiException {
        PointRule pointRule = this.getById(id);
        if (pointRule == null) {
            throw new ApiException("Point Rule not found", 404);
        }
        this.em.remove(pointRule);
        return pointRule;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public PointRule update(Integer id, PointRule payload) throws ApiException {
        PointRule pointRule = this.getById(id);
        if (pointRule == null) {
            throw new ApiException("Point Rule not found", 404);
        }
        if (payload.getLowerLimit() != null) {
            pointRule.setLowerLimit(payload.getLowerLimit());
        }
        if (payload.getUpperLimit() != null) {
            pointRule.setUpperLimit(payload.getUpperLimit());
        }
        if (payload.getConversionRate() != null) {
            pointRule.setConversionRate(payload.getConversionRate());
        }

        this.em.merge(pointRule);

        return pointRule;
    }
}
