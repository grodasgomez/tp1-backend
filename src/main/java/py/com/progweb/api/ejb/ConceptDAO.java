package py.com.progweb.api.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import py.com.progweb.api.exceptions.ApiException;
import py.com.progweb.api.model.ConceptPointUse;

@Stateless
public class ConceptDAO {
	@PersistenceContext(unitName = "pruebaPU")
    private EntityManager em;

	public ConceptPointUse create(ConceptPointUse concept) {
        concept.setId(null);
        this.em.persist(concept);
        return concept;
    }

    /*
     * TODO: EXCEPTION IF NOT FOUND??
     */
	public ConceptPointUse getById(Integer id) {
        return this.em.find(ConceptPointUse.class, id);
    }

    public List<ConceptPointUse> getAll() {
        return this.em.createQuery("select c from ConceptPointUse c", ConceptPointUse.class).getResultList();
    }

    public ConceptPointUse update(Integer id, ConceptPointUse payload) throws ApiException {
        ConceptPointUse concept = this.getById(id);
        if (concept == null) {
            throw new ApiException("Concept not found", 404);
        }
        if (payload.getDescription() != null) {
            concept.setDescription(payload.getDescription());
        }
        if (payload.getPoints() != null) {
            concept.setPoints(payload.getPoints());
        }

        this.em.merge(concept);

        return concept;
    }

    public ConceptPointUse delete(Integer id) throws ApiException {
        ConceptPointUse concept = this.getById(id);
        if (concept == null) {
            throw new ApiException("Concept not found", 404);
        }
        this.em.remove(concept);
        return concept;
    }
}
