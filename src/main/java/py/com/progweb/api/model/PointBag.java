package py.com.progweb.api.model;

import java.util.Date;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "point_bag")
@Getter
@Setter
public class PointBag {

    @Id
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    @GeneratedValue(generator = "pointBagSec")
    @SequenceGenerator(name = "pointBagSec", sequenceName = "point_bag_sec", allocationSize = 0)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id", nullable = false)
    private Client client;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "assignment_date", nullable = false)
    private Date assignmentDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "expiration_date", nullable = false)
    private Date expirationDate;

    @Column(name = "points", nullable = false)
    private Integer points;

    @Column(name = "used_points", nullable = false)
    private Integer usedPoints;

    @Column(name = "points_balance", nullable = false)
    private Integer pointsBalance;

    @Column(name = "operation_amount", nullable = false)
    private Integer operationAmount;

}