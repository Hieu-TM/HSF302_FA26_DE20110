package hieutm.chapter4_ex1.specification;

import hieutm.chapter4_ex1.pojo.Student;
import org.springframework.data.jpa.domain.Specification;

public class StudentSpecs {

    public static Specification<Student> nameContains(String kw) {
        return (root, query, cb) -> {
            if (kw == null || kw.trim().isEmpty()) {
                return null;
            }
            return cb.like(cb.lower(root.get("fullName")), "%" + kw.trim().toLowerCase() + "%");
        };
    }

    public static Specification<Student> inDepartment(String code) {
        return (root, query, cb) -> {
            if (code == null || code.trim().isEmpty()) {
                return null;
            }
            return cb.equal(root.get("department").get("code"), code.trim());
        };
    }

    public static Specification<Student> gpaAtLeast(Double min) {
        return (root, query, cb) -> {
            if (min == null) {
                return null;
            }
            return cb.greaterThanOrEqualTo(root.get("gpa"), min);
        };
    }

    public static Specification<Student> isActive(Boolean active) {
        return (root, query, cb) -> {
            if (active == null) {
                return null;
            }
            return cb.equal(root.get("active"), active);
        };
    }
}
