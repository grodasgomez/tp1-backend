package py.com.progweb.api.ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import py.com.progweb.api.exceptions.ApiException;
import py.com.progweb.api.model.Client;

import java.util.Date;
import java.util.List;

@Stateless
public class ClientDAO {
    @PersistenceContext(unitName = "pruebaPU")
    private EntityManager em;

    public Client create(Client client) {
        client.setId(null);
        this.em.persist(client);
        return client;
    }

    public Client update(Integer id, Client payload) throws ApiException {
        Client client = this.getById(id);
        if (client == null) {
            throw new ApiException("Client not found", 404);
        }
        if (payload.getName() != null) {
            client.setName(payload.getName());
        }
        if (payload.getLastName() != null) {
            client.setLastName(payload.getLastName());
        }
        if (payload.getBirthDate() != null) {
            client.setBirthDate(payload.getBirthDate());
        }
        if (payload.getPhone() != null) {
            client.setPhone(payload.getPhone());
        }
        if (payload.getEmail() != null) {
            client.setEmail(payload.getEmail());
        }
        if (payload.getDocumentNumber() != null) {
            client.setDocumentNumber(payload.getDocumentNumber());
        }
        if (payload.getDocumentType() != null) {
            client.setDocumentType(payload.getDocumentType());
        }
        if (payload.getNationality() != null) {
            client.setNationality(payload.getNationality());
        }

        this.em.merge(client);

        return client;
    }

    public Client getById(Integer id) {
        return this.em.find(Client.class, id);
    }

    public List<Client> getAll() {
        return this.em.createQuery("select c from Client c", Client.class).getResultList();
    }

    public Client delete(Integer id) throws ApiException {
        Client client = this.getById(id);
        if (client == null) {
            throw new ApiException("Client not found", 404);
        }
        this.em.remove(client);
        return client;
    }

    public List<Client> getByNameLike(String name){
        return this.em.createQuery("select c from Client c where name like :name", Client.class).setParameter("name", "%" + name + "%").getResultList();
    }

    public List<Client> getByLastNameLike(String lastName){
        return this.em.createQuery("select c from Client c where last_name like :lastName", Client.class).setParameter("lastName", "%" + lastName + "%").getResultList();
    }

    public List<Client> getByBirthDate(Date birthDate){
        return this.em.createQuery("select c from Client c where birth_date = :birthDate", Client.class).setParameter("birthDate", birthDate).getResultList();
    }
}