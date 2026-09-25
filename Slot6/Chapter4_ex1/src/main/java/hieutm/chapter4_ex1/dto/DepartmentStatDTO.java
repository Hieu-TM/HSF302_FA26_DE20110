package hieutm.chapter4_ex1.dto;

public class DepartmentStatDTO {
    private Long departmentId;
    private String departmentName;
    private Long studentCount;
    private Long maleCount;
    private Long femaleCount;

    public DepartmentStatDTO() {
    }

    public DepartmentStatDTO(Long departmentId, String departmentName, Long studentCount, Long maleCount, Long femaleCount) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.studentCount = studentCount;
        this.maleCount = maleCount;
        this.femaleCount = femaleCount;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Long getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(Long studentCount) {
        this.studentCount = studentCount;
    }

    public Long getMaleCount() {
        return maleCount;
    }

    public void setMaleCount(Long maleCount) {
        this.maleCount = maleCount;
    }

    public Long getFemaleCount() {
        return femaleCount;
    }

    public void setFemaleCount(Long femaleCount) {
        this.femaleCount = femaleCount;
    }

    @Override
    public String toString() {
        return "DepartmentStatDTO{" +
                "departmentId=" + departmentId +
                ", departmentName='" + departmentName + '\'' +
                ", studentCount=" + studentCount +
                ", maleCount=" + maleCount +
                ", femaleCount=" + femaleCount +
                '}';
    }
}
