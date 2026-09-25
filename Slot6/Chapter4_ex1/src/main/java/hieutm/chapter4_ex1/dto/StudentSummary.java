package hieutm.chapter4_ex1.dto;

import java.time.LocalDate;

public class StudentSummary {
    private Long id;
    private String name;
    private String email;
    private LocalDate birthDate;
    private String departmentName;

    public StudentSummary() {
    }

    public StudentSummary(Long id, String name, String email, LocalDate birthDate, String departmentName) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
        this.departmentName = departmentName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    @Override
    public String toString() {
        return "StudentSummary{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", birthDate=" + birthDate +
                ", departmentName='" + departmentName + '\'' +
                '}';
    }
}
