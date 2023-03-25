package py.com.progweb.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import py.com.progweb.api.dto.AgendaDTO;
import py.com.progweb.api.dto.PersonaDTO;
import py.com.progweb.api.helpers.Helper;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "client")
public class Client {
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(generator = "clientSec")
    @SequenceGenerator(name = "clientSec", sequenceName = "client_sec", allocationSize = 0)
    private Integer id;

    @Basic(optional = false)
    @Column(name = "name", length = 100)
    private String name;

    @Basic(optional = false)
    @Column(name = "last_name", length = 100)
    private String lastName;

    @Basic(optional = false)
    @Column(name = "document_number", length = 100)
    private String documentNumber;

    @Basic(optional = false)
    @Column(name = "document_type", length = 100)
    private String documentType;

    @Basic(optional = false)
    @Column(name = "nationality", length = 100)
    private String nationality;

    @Basic(optional = false)
    @Column(name = "email", length = 100)
    private String email;

    @Basic(optional = false)
    @Column(name = "phone", length = 100)
    private String phone;

    @Basic(optional = false)
    @Temporal(TemporalType.DATE)
    @Column(name = "birth_date", length = 100)
    private String birthDate;

}