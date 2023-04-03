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
        return this.em.createQuery("select c from PointRule c order by lowerLimit", PointRule.class).getResultList();
    }

    public PointRule getByAmount(Integer amount) {
        List<PointRule> result= this.em.createQuery("select c from PointRule c where c.lowerLimit<= :custMount and c.upperLimit>= :custMount", PointRule.class).setParameter("custMount", amount).setMaxResults(1).getResultList();
        return !result.isEmpty() ? result.get(0) : null;
    }

    public PointRule getById(Integer id) {
        return this.em.find(PointRule.class, id);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public PointRule create(PointRule pointRule) throws ApiException {

        if (pointRule.getLowerLimit() == null || pointRule.getUpperLimit() == null || pointRule.getConversionRate() == null) {
            throw new ApiException("Se deben llenar todos los valores", 400);
        }

        if (pointRule.getLowerLimit() > pointRule.getUpperLimit()) {
            throw new ApiException("El limite inferior debe ser menor que el limite superior", 400);
        }
        if (pointRule.getLowerLimit() <= 0 || pointRule.getUpperLimit() <= 0 || pointRule.getConversionRate() <= 0) {
            throw new ApiException("El limite inferior, superior y el valor de conversion deben ser mayores que cero respectivamente", 400);
        }

        List<PointRule> pointRuleList = this.em.createQuery("select c from PointRule c order by lowerLimit", PointRule.class).getResultList();

        /* Si antes de insertar no hay valores en la lista */
        if (pointRuleList.isEmpty()) {
            pointRule.setId(null);
            pointRule.setUpperLimit(Integer.MAX_VALUE);
            this.em.persist(pointRule);
            return pointRule;
        }

        PointRule firstRule = pointRuleList.get(0);
        PointRule lastRule = pointRuleList.get(pointRuleList.size() - 1);

        /* Si el valor a insertar pose el mayor rango (fuera de rango inferior) */
        if (pointRule.getLowerLimit() > lastRule.getLowerLimit()) {
            if (pointRule.getLowerLimit() > lastRule.getLowerLimit() + 1) {
                if (pointRule.getConversionRate() < lastRule.getConversionRate()) {
                    lastRule.setUpperLimit(pointRule.getLowerLimit() - 1);
                    this.em.merge(lastRule);

                    pointRule.setId(null);
                    pointRule.setUpperLimit(Integer.MAX_VALUE);
                    this.em.persist(pointRule);
                    return pointRule;
                } else {
                    throw new ApiException("Valor de conversion es mayor que rango anterior", 400);
                }
            } else {
                throw new ApiException("Debe haber por lo menos un valor entre rangos", 400);
            }
        }

        /* Si el valor a insertar pose el menor rango (fuera de rango superior)*/
        if (pointRule.getUpperLimit() < firstRule.getLowerLimit()) {
            if (pointRule.getUpperLimit() == firstRule.getLowerLimit() - 1) {
                if (pointRule.getConversionRate() > firstRule.getConversionRate()) {
                    pointRule.setId(null);
                    this.em.persist(pointRule);
                    return pointRule;
                } else {
                    throw new ApiException("Valor de conversion es menor que rango siguiente", 400);
                }
            } else {
                throw new ApiException("Rango insertado crea un hueco", 400);
            }
        }

        int index = 0;
        /* Para valores que son insertados entre un rango, no puede estar entre dos rangos */
        for (PointRule currRule : pointRuleList) {
            if (pointRule.getLowerLimit() > currRule.getLowerLimit() && pointRule.getUpperLimit() == currRule.getUpperLimit()) {
                if (pointRule.getConversionRate() < currRule.getConversionRate() && pointRule.getConversionRate() > pointRuleList.get(index + 1).getConversionRate()) {
                    currRule.setUpperLimit(pointRule.getLowerLimit() - 1);
                    this.em.merge(currRule);

                    pointRule.setId(null);
                    this.em.persist(pointRule);
                    return pointRule;
                } else {
                    throw new ApiException("Conflicto con valor de conversion", 400);
                }
            }

            if (pointRule.getLowerLimit() == currRule.getLowerLimit() && pointRule.getUpperLimit() < currRule.getUpperLimit()) {
                if (pointRule.getConversionRate() > currRule.getConversionRate() && (index == 0 || pointRule.getConversionRate() < pointRuleList.get(index - 1).getConversionRate())) {
                    currRule.setLowerLimit(pointRule.getUpperLimit() + 1);
                    this.em.merge(currRule);

                    pointRule.setId(null);
                    this.em.persist(pointRule);
                    return pointRule;
                } else {
                    throw new ApiException("Conflicto con valor de conversion", 400);
                }
            }

            index++;
        }

        throw new ApiException("No se definieron correctamente los rangos", 400);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public PointRule delete(Integer id) throws ApiException {
        PointRule pointRule = this.getById(id);

        if (pointRule == null) {
            throw new ApiException("Point Rule not found", 404);
        }

        List<PointRule> pointRuleList = this.em.createQuery("select c from PointRule c order by lowerLimit", PointRule.class).getResultList();
        PointRule prev = null;
        PointRule next = null;

        for (PointRule currRule : pointRuleList) {
            if (currRule.getUpperLimit() == pointRule.getLowerLimit() - 1) {
                prev = currRule;
            }

            if (currRule.getLowerLimit() == pointRule.getUpperLimit() + 1) {
                next = currRule;
            }

            if (prev != null && next != null) {
                break;
            }
        }

        if (prev != null && next == null){
            prev.setUpperLimit(Integer.MAX_VALUE);
            this.em.merge(prev);
        }
        else if (prev != null && next != null) {
            prev.setUpperLimit(pointRule.getUpperLimit());
            this.em.merge(prev);
        }

        this.em.remove(pointRule);
        return pointRule;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public PointRule update(Integer id, PointRule payload) throws ApiException {
        PointRule oldRule = this.getById(id);

        if (oldRule == null) {
            throw new ApiException("Point Rule not found", 404);
        }

        List<PointRule> pointRuleList = this.em.createQuery("select c from PointRule c order by lowerLimit", PointRule.class).getResultList();
        PointRule prev = null;
        PointRule next = null;

        for (PointRule currRule : pointRuleList) {
            if (currRule.getUpperLimit() == oldRule.getLowerLimit() - 1) {
                prev = currRule;
            }

            if (currRule.getLowerLimit() == oldRule.getUpperLimit() + 1) {
                next = currRule;
            }

            if (prev != null && next != null) {
                break;
            }
        }

        if (payload.getLowerLimit() == null && payload.getUpperLimit() == null && payload.getConversionRate() == null) {
            throw new ApiException("Debe definir algun valor de cambio", 400);
        }

        if (payload.getLowerLimit() != null && payload.getUpperLimit() != null && payload.getLowerLimit() > payload.getUpperLimit()) {
            throw new ApiException("El limite inferior debe ser menor que el limite superior", 400);
        }

        if (payload.getLowerLimit() != null && payload.getLowerLimit().compareTo(oldRule.getLowerLimit()) != 0) {
            if (payload.getLowerLimit() <= 0) {
                throw new ApiException("El limite inferior debe ser mayor que cero", 400);
            }

            /* Crece hacia adentro */
            if (payload.getLowerLimit() > oldRule.getLowerLimit() && payload.getLowerLimit() < oldRule.getUpperLimit()) {
                if (prev != null) {
                    prev.setUpperLimit(payload.getLowerLimit() - 1);
                }
                oldRule.setLowerLimit(payload.getLowerLimit());
            }
            /* Crece hacia afuera */
            else if (payload.getLowerLimit() < oldRule.getLowerLimit() && (prev == null || payload.getLowerLimit() > prev.getLowerLimit() + 1)) {
                if (prev != null) {
                    prev.setUpperLimit(payload.getLowerLimit() - 1);
                }

                oldRule.setLowerLimit(payload.getLowerLimit());
            }
            else {
                throw new ApiException("El limite inferior no es consistente", 400);
            }
        }
        if (payload.getUpperLimit() != null && payload.getUpperLimit().compareTo(oldRule.getUpperLimit()) != 0) {
            if (payload.getUpperLimit() <= 0) {
                throw new ApiException("El limite superior debe ser mayor que cero", 400);
            }

            /* Crece hacia adentro */
            if (payload.getUpperLimit() < oldRule.getUpperLimit() && payload.getUpperLimit() > oldRule.getLowerLimit()) {
                if (next != null) {
                    next.setLowerLimit(payload.getUpperLimit() + 1);
                    oldRule.setUpperLimit(payload.getUpperLimit());
                }
                else {
                    throw new ApiException("El limite superior maximo es el maximo decimal, no se puede cambiar", 400);
                }
            }
            /* Crece hacia afuera */
            else if (payload.getUpperLimit() > oldRule.getUpperLimit() && (payload.getUpperLimit() < next.getUpperLimit() - 1)) {
                next.setLowerLimit(payload.getUpperLimit() + 1);
                oldRule.setUpperLimit(payload.getUpperLimit());
            }
            else {
                throw new ApiException("El limite superior no es consistente", 400);
            }
        }

        if (payload.getConversionRate() != null) {
            if (payload.getConversionRate() <= 0) {
                throw new ApiException("El valor de conversion debe ser mayor que cero", 400);
            }

            if (next != null) {
                if (payload.getConversionRate() > next.getConversionRate()) {
                    oldRule.setConversionRate(payload.getConversionRate());
                }
                else {
                    throw new ApiException("El cambio debe ser mayor que el del siguiente rango", 400);
                }
            }
            if (prev != null) {
                if (payload.getConversionRate() < prev.getConversionRate()) {
                    oldRule.setConversionRate(payload.getConversionRate());
                }
                else {
                    throw new ApiException("El cambio debe ser menor que el del rango anterior", 400);
                }
            }
        }

        this.em.merge(oldRule);

        if(prev != null) {
            this.em.merge(prev);
        }

        if (next != null) {
            this.em.merge(next);
        }

        return oldRule;
    }
}
