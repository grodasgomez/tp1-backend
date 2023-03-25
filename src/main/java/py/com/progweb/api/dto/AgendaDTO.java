package py.com.progweb.api.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;

import py.com.progweb.api.model.Agenda;

public class AgendaDTO {
  private Integer idAgenda;
  private String actividad;
  @JsonFormat(pattern = "yyyy-MM-dd")
  private Date fecha;

  public Integer getIdAgenda() {
    return idAgenda;
  }

  public void setIdAgenda(Integer idAgenda) {
    this.idAgenda = idAgenda;
  }

  public String getActividad() {
    return actividad;
  }

  public void setActividad(String actividad) {
    this.actividad = actividad;
  }

  public Date getFecha() {
    return fecha;
  }

  public void setFecha(Date fecha) {
    this.fecha = fecha;
  }

  public Agenda toCreateEntity() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.addMixInAnnotations(AgendaDTO.class, IgnoreIdMixin.class);
    return mapper.convertValue(this, Agenda.class);
  }

  public Agenda toEntity() {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.convertValue(this, Agenda.class);
  }

  public static AgendaDTO fromEntity(Agenda agenda) {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.convertValue(agenda, AgendaDTO.class);
  }

}
