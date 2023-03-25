package py.com.progweb.api.model;

import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "point_use")
@Getter
@Setter
public class PointUse {

    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(generator = "pointUseSec")
    @SequenceGenerator(name = "pointUseSec", sequenceName = "point_use_sec", allocationSize = 0)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id", nullable = false)
    private Client client;

    @Column(name = "used_points", nullable = false)
    private Integer used_points;

    @Column(name = "date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;

    @Column(name = "concept", nullable = false)
    private String concept;

}