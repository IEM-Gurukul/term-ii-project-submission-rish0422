import java.util.List;


public class ProductService {

    private final ProductDAO dao;

    public ProductService() {
        this.dao = new ProductDAOImpl();
    }

    public ProductService(ProductDAO dao) {
        this.dao = dao;
    }

   
    public void addProduct(String name, String category, double price,
                           int quantity, int lowStockThreshold) throws Exception {
        validateName(name);
        validatePrice(price);
        validateQuantity(quantity);
        Product p = new Product(0, name.trim(), category.trim(), price, quantity, lowStockThreshold);
        dao.addProduct(p);
    }

    public void updateProduct(int id, String name, String category, double price,
                              int quantity, int lowStockThreshold) throws Exception {
        validateName(name);
        validatePrice(price);
        validateQuantity(quantity);
        Product p = new Product(id, name.trim(), category.trim(), price, quantity, lowStockThreshold);
        dao.updateProduct(p);
    }

    public void deleteProduct(int id) throws Exception {
        dao.deleteProduct(id);
    }

    public Product getProductById(int id) throws Exception {
        return dao.getProductById(id);
    }

    public List<Product> getAllProducts() throws Exception {
        return dao.getAllProducts();
    }

   
    public void restockProduct(int id, int amount) throws Exception {
        if (amount <= 0) throw new Exception("Restock amount must be positive.");
        Product p = dao.getProductById(id);
        p.setQuantity(p.getQuantity() + amount);
        dao.updateProduct(p);
    }

    public void deductStock(int id, int amount) throws Exception {
        if (amount <= 0) throw new Exception("Deduct amount must be positive.");
        Product p = dao.getProductById(id);
        if (p.getQuantity() < amount)
            throw new Exception("Insufficient stock. Available: " + p.getQuantity());
        p.setQuantity(p.getQuantity() - amount);
        dao.updateProduct(p);
    }

    public List<Product> searchByName(String keyword) throws Exception {
        if (keyword == null || keyword.isBlank()) return getAllProducts();
        return dao.searchByName(keyword);
    }

    public List<Product> searchByCategory(String category) throws Exception {
        if (category == null || category.isBlank()) return getAllProducts();
        return dao.searchByCategory(category);
    }

    public List<Product> getLowStockProducts() throws Exception {
        return dao.getLowStockProducts();
    }

     
    private void validateName(String name) throws Exception {
        if (name == null || name.isBlank())
            throw new Exception("Product name cannot be empty.");
    }

    private void validatePrice(double price) throws Exception {
        if (price < 0)
            throw new Exception("Price cannot be negative.");
    }

    private void validateQuantity(int quantity) throws Exception {
        if (quantity < 0)
            throw new Exception("Quantity cannot be negative.");
    }
}