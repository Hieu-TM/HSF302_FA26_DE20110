package fu.de200110.pojo;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table("employees")
public class Employee {

    @id
    @GeneratedValue(strategy  = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String fullname;

    @Column(unique = true)
    private String email;

    @Column(precision = 10, scale = 2)
    private BigDecimal salary;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate hireDate;

    private boolean active;

    @Transient
    private int yearsOfService;


}
