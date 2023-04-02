package py.com.progweb.api.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import py.com.progweb.api.exceptions.ApiException;
import py.com.progweb.api.model.PointUse;

@Stateless
public class PointUseDAO {
	@PersistenceContext(unitName = "pruebaPU")
    private EntityManager em;

	public List<PointUse> getAll() {
        return this.em.createQuery("select c from PointUse c", PointUse.class).getResultList();
    }
}
