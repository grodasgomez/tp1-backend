package py.com.progweb.api.model;

import java.util.Date;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "point_use_detail")
@Getter
@Setter
public class PointUseDetail {

    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(generator = "pointUseDetailSec")
    @SequenceGenerator(name = "pointUseDetailSec", sequenceName = "point_use_detail_sec", allocationSize = 0)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "point_use_id", referencedColumnName = "id", nullable = false)
    private PointUse pointUse;

    @ManyToOne
    @JoinColumn(name = "point_bag_id", referencedColumnName = "id", nullable = false)
    private PointBag pointBag;

    @Column(name = "used_points", nullable = false)
    private Integer usedPoints;

}