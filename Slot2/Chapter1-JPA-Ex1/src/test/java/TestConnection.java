import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class TestConnection {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hsf302");
        System.out.println("EMF tao thanh cong!");
    }
}