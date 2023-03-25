package py.com.progweb.api.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import py.com.progweb.api.model.Persona;

public class PersonaDTO {
  private Integer idPersona;
  private String nombre;
  private String apellido;
  private List<AgendaDTO> listaAgendas;

  public Integer getIdPersona() {
    return idPersona;
  }

  public void setIdPersona(Integer idPersona) {
    this.idPersona = idPersona;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getApellido() {
    return apellido;
  }

  public void setApellido(String apellido) {
    this.apellido = apellido;
  }

  public List<AgendaDTO> getListaAgendas() {
    return listaAgendas;
  }

  public void setListaAgendas(List<AgendaDTO> agendas) {
    this.listaAgendas = agendas;
  }

  public Persona toCreateEntity() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.addMixInAnnotations(PersonaDTO.class, IgnoreIdMixin.class);
    return mapper.convertValue(this, Persona.class);
  }

  public static PersonaDTO fromEntity(Persona persona) {
    ObjectMapper mapper = new ObjectMapper();
    PersonaDTO dto = mapper.convertValue(persona, PersonaDTO.class);
    System.out.println(persona.getListaAgendas());
    dto.setListaAgendas(persona.getListaAgendas().stream().map(AgendaDTO::fromEntity).collect(Collectors.toList()));
    return dto;

  }

}
