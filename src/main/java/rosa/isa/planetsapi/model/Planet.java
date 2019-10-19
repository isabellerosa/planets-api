package rosa.isa.planetsapi.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Planet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String type;

    private double radius;

    private int qtdMoons;

    private double rotationPeriod;

    private double orbitalPeriod;
}
