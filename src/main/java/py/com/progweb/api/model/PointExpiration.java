package py.com.progweb.api.model;

import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "point_expiration")
@Getter
@Setter
public class PointExpiration {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "pointExpGen")
    @SequenceGenerator(name = "pointExpGen", sequenceName = "point_exp_sec", allocationSize = 0)
    private Integer id;

    @Temporal(TemporalType.DATE)
    @Column(name = "valid_start_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date validStartDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "valid_end_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date validEndDate;

    @Column(name = "valid_days_count", nullable = false)
    private Integer validDaysCount;
}
