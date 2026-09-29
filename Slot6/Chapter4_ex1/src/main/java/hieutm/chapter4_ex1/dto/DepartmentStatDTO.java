package hieutm.chapter4_ex1.dto;

public record DepartmentStatDTO(
        String code,
        String name,
        Long studentCount,
        Double avgGpa
) {
}
