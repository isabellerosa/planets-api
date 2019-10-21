package rosa.isa.planetsapi.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Planet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String name;

    @Column(length = 60)
    private String type;

    private Double radius;

    private Integer qtdMoons;

    private Double rotationPeriod;

    private Double orbitalPeriod;
}
