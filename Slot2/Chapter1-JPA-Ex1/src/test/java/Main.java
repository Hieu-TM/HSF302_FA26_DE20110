import fe.de200110.dao.EmployeeDAO;
import fe.de200110.pojo.Employee;
import fe.de200110.pojo.Gender;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        EmployeeDAO dao = new EmployeeDAO();


        // ===== 1. CREATE =====
        System.out.println("--- 1. CREATE ---");
        Employee emp = new Employee("Tran Minh Hieu", "a@fpt.edu.vn",
                new BigDecimal("2000000"), Gender.MALE, LocalDate.of(2026, 9, 1));
        // [Lifecycle] emp is NEW/TRANSIENT - just created, not yet persisted
        System.out.println("Before save: " + emp + " [Lifecycle: NEW/TRANSIENT]");

        dao.save(emp);
        // [Lifecycle] emp is now DETACHED - after persist() in transaction and EntityManager.close()
        System.out.println("After save: " + emp + " [Lifecycle: DETACHED]");
        System.out.println("ID generated: " + emp.getId() + "\n");

        // ===== 2. READ =====
        System.out.println("--- 2. READ ---");
        Employee found = dao.findById(emp.getId());
        // [Lifecycle] found is DETACHED - EntityManager already closed after findById
        System.out.println("Found employee: " + found + " [Lifecycle: DETACHED]");
        System.out.println("Salary before update: " + found.getSalary() + "\n");

        // ===== 3. UPDATE =====
        System.out.println("--- 3. UPDATE ---");
        found.setSalary(new BigDecimal("17000000"));
        // [Lifecycle] found is still DETACHED - local modification doesn't sync to DB
        System.out.println("After modify (DETACHED): " + found + " [Lifecycle: DETACHED]");

        Employee updated = dao.update(found);
        // [Lifecycle] inside update(): merge() converts DETACHED to MANAGED and persists changes
        // "updated" is MANAGED during transaction, then DETACHED after method returns
        System.out.println("After merge/update: " + updated + " [Lifecycle: DETACHED after return]");
        System.out.println("Salary after update: " + updated.getSalary() + "\n");

        // ===== 4. READ AGAIN (verify update) =====
        System.out.println("--- 4. READ AGAIN (verify update) ---");
        Employee reChecked = dao.findById(emp.getId());
        // [Lifecycle] reChecked is DETACHED
        System.out.println("After re-read: " + reChecked + " [Lifecycle: DETACHED]");
        System.out.println("Salary verified: " + reChecked.getSalary() + "\n");

        // ===== 5. DELETE =====
        System.out.println("--- 5. DELETE ---");
        dao.delete(emp.getId());
        // [Lifecycle] inside delete(): entity is REMOVED - marked for deletion then deleted on commit()
        System.out.println("Record deleted [Lifecycle: REMOVED in transaction, removed from DB after commit]\n");

        // ===== 6. READ AFTER DELETE (verify deletion) =====
        System.out.println("--- 6. READ AFTER DELETE (verify deletion) ---");
        Employee afterDelete = dao.findById(emp.getId());
        // [Lifecycle] N/A - no record found
        if (afterDelete == null) {
            System.out.println("Result: null - Record successfully deleted! ✓\n");
        } else {
            System.out.println("ERROR: Record still exists! " + afterDelete + "\n");
        }

        // ===== 7. VERIFY UNIQUE CONSTRAINT ON EMAIL =====
        System.out.println("--- 7. VERIFY UNIQUE CONSTRAINT ON EMAIL ---");
        Employee dup1 = new Employee("User 1", "trung@fpt.edu.vn",
                new BigDecimal("10000000"), Gender.FEMALE, LocalDate.now());
        // [Lifecycle] dup1 is NEW/TRANSIENT
        System.out.println("Created dup1: " + dup1 + " [Lifecycle: NEW/TRANSIENT]");

        dao.save(dup1);
        // [Lifecycle] dup1 is now DETACHED
        System.out.println("Saved dup1 successfully [Lifecycle: DETACHED]\n");

        Employee dup2 = new Employee("User 2", "trung@fpt.edu.vn",
                new BigDecimal("11000000"), Gender.MALE, LocalDate.now());
        // [Lifecycle] dup2 is NEW/TRANSIENT
        System.out.println("Created dup2 with SAME email: " + dup2 + " [Lifecycle: NEW/TRANSIENT]");

        try {
            dao.save(dup2);
            // [Lifecycle] if reaches here, constraint check failed
            System.out.println("ERROR: No exception thrown - unique constraint NOT enforced! ✗\n");
        } catch (RuntimeException ex) {
            // [Lifecycle] dup2 remains NEW/TRANSIENT (rollback prevents persist)
            System.out.println("✓ Caught expected exception: " + ex.getClass().getSimpleName());
            System.out.println("Message: " + ex.getMessage());
            System.out.println("Reason: Unique constraint on email violated [Lifecycle: dup2 still NEW/TRANSIENT]\n");
        }

        System.out.println("===== ALL TESTS COMPLETED =====");
    }
}
