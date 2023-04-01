package py.com.progweb.api.model;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "concept_point_use")
@Getter
@Setter
public class ConceptPointUse {
	@Id
    @Column(name = "id")
    @GeneratedValue(generator = "conceptSec")
    @SequenceGenerator(name = "conceptSec", sequenceName = "conceptSec", allocationSize = 0)
    private Integer id;

    @Column(name = "description", length = 100, nullable = false)
    private String description;

    @Column(name = "points", length = 100, nullable = false)
    private Integer points;
}
