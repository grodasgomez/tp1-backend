package py.com.progweb.api.ejb;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import py.com.progweb.api.model.PointUse;

@Stateless
public class PointUseDAO {
	@PersistenceContext(unitName = "pruebaPU")
        private EntityManager em;

	public List<PointUse> getAll() {
        return this.em.createQuery("select c from PointUse c", PointUse.class).getResultList();

        }
        public PointUse create(PointUse pointUse) {
            pointUse.setId(null);
            this.em.persist(pointUse);
            return pointUse;
        }
        
	public List<PointUse> getByConcept(Integer id) {
		return this.em.createQuery("select c from PointUse c where c.concept.id = :concept_id", PointUse.class).setParameter("concept_id", id).getResultList();
	}

	public List<PointUse> getByClient(Integer id) {
		return this.em.createQuery("select c from PointUse c where c.client.id = :client_id", PointUse.class).setParameter("client_id", id).getResultList();
	}

	public List<PointUse> getByUsedDate(Date usedDate) {
		return this.em.createQuery("select c from PointUse c where c.date = :date", PointUse.class).setParameter("date", usedDate).getResultList();
	}
}
