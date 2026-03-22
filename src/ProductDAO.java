import java.util.List;

/**
 * DAO Interface for Product persistence operations.
 * Demonstrates: Abstraction, Design Pattern (DAO), Polymorphism.
 */
public interface ProductDAO {

    void addProduct(Product product) throws Exception;

    void updateProduct(Product product) throws Exception;

    void deleteProduct(int id) throws Exception;

    Product getProductById(int id) throws Exception;

    List<Product> getAllProducts() throws Exception;

    List<Product> searchByName(String keyword) throws Exception;

    List<Product> searchByCategory(String category) throws Exception;

    List<Product> getLowStockProducts() throws Exception;
}