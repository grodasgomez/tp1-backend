package py.com.progweb.api.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "point_rule")
@Getter
@Setter
public class PointRule {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "pointRuleSec")
    @SequenceGenerator(name = "pointRuleSec", sequenceName = "point_rule_sec", allocationSize = 0)
    private Integer id;

    @Column(name = "lower_limit", nullable = false)
    private Integer lowerLimit;

    @Column(name = "upper_limit", nullable = false)
    private Integer upperLimit;

    @Column(name = "conversion_rate", nullable = false)
    private Integer conversionRate;
}
