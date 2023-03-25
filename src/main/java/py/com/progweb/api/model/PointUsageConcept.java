package py.com.progweb.api.model;


import javax.persistence.*;

@Entity
@Table(name = "point_usage_concept")
public class PointUsageConcept {
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(generator = "pointUsageConceptSec")
    @SequenceGenerator(name = "pointUsageConceptSec", sequenceName = "point_usage_concept_sec", allocationSize = 0)
    private Integer id;

    @Basic(optional = false)
    @Column(name = "name", length = 100)
    private String description;

    @Basic(optional = false)
    @Column(name = "required_points")
    private Integer requiredPoints;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRequiredPoints() {
        return requiredPoints;
    }

    public void setRequiredPoints(Integer requiredPoints) {
        this.requiredPoints = requiredPoints;
    }

}