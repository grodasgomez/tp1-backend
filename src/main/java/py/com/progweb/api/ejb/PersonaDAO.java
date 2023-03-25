package py.com.progweb.api.ejb;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import py.com.progweb.api.dto.AgendaDTO;
import py.com.progweb.api.dto.PersonaDTO;
import py.com.progweb.api.model.Agenda;
import py.com.progweb.api.model.Persona;

import java.util.ArrayList;
import java.util.List;

@Stateless
public class PersonaDAO {
  @PersistenceContext(unitName = "pruebaPU")
  private EntityManager em;

  @Inject
  AgendaDAO agendaDAO;

  // eesta anotacion es cuando requerimos que sea atomico el metodo
  @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
  public PersonaDTO agregar(PersonaDTO dto) {
    Persona entidad = dto.toCreateEntity();
    this.em.persist(entidad);
    // para agregar una persona y todas sus agendas de una vez, es decir
    // se manda con el atributo listaAgendasJson cargado
    List<Agenda> agendas = new ArrayList<Agenda>();
    for (AgendaDTO a : dto.getListaAgendas()) {
      Agenda agenda =  a.toCreateEntity();
      agenda.setIdPersona(entidad);
      agendaDAO.agregar(agenda);
      agendas.add(agenda);
    }
    entidad.setListaAgendas(agendas);
    return PersonaDTO.fromEntity(entidad);
  }

  public List<Persona> todos() {
    return this.em.createQuery("select p from Persona p").getResultList();
  }

  public List<Persona> lista(String nombreLike) {
    return this.em.createQuery("select p from Persona p where nombre like :param")
        .setParameter("param", "%" + nombreLike + "%").getResultList();
  }

  public Persona getById(Integer id) {
    Persona p = this.em.find(Persona.class, id);
    // List<Agenda> agendas = p.getListaAgendas();
    // p.setListaAgendasJson(new ArrayList<Agenda>());
    // for (Agenda a : agendas) {
    //   Agenda a2 = new Agenda();
    //   a2.setActividad(a.getActividad());
    //   a2.setFecha(a.getFecha());
    //   p.getListaAgendasJson().add(a2);
    // }
    return p;
  }
}