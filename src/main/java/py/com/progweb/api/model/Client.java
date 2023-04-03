package py.com.progweb.api.model;

import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "client")
@Getter
@Setter
public class Client {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "clientSec")
    @SequenceGenerator(name = "clientSec", sequenceName = "client_sec", allocationSize = 0)
    private Integer id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "last_name", length = 100, nullable = false)
    private String lastName;

    @Column(name = "document_number", length = 100, nullable = false)
    private String documentNumber;

    @Column(name = "document_type", length = 100, nullable = false)
    private String documentType;

    @Column(name = "nationality", length = 100, nullable = false)
    private String nationality;

    @Column(name = "email", length = 100, nullable = false)
    private String email;

    @Column(name = "phone", length = 100, nullable = false)
    private String phone;

    @Temporal(TemporalType.DATE)
    @Column(name = "birth_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

    @OneToMany (mappedBy="client", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<PointBag> listPointBag;
}