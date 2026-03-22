import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class ProductDAOImpl implements ProductDAO {

    private static final String FILE_PATH = "products.dat";

    @SuppressWarnings("unchecked")
    private List<Product> loadAll() throws Exception {
        File file = new File(FILE_PATH);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Product>) ois.readObject();
        } catch (EOFException e) {
            return new ArrayList<>();
        }
    }

    private void saveAll(List<Product> products) throws Exception {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(products);
        }
    }

    private int generateId(List<Product> products) {
        return products.stream()
                       .mapToInt(Product::getId)
                       .max()
                       .orElse(0) + 1;
    }

    @Override
    public void addProduct(Product product) throws Exception {
        List<Product> products = loadAll();
        product.setId(generateId(products));
        products.add(product);
        saveAll(products);
    }

    @Override
    public void updateProduct(Product product) throws Exception {
        List<Product> products = loadAll();
        boolean found = false;
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == product.getId()) {
                products.set(i, product);
                found = true;
                break;
            }
        }
        if (!found) throw new Exception("Product with ID " + product.getId() + " not found.");
        saveAll(products);
    }

    @Override
    public void deleteProduct(int id) throws Exception {
        List<Product> products = loadAll();
        boolean removed = products.removeIf(p -> p.getId() == id);
        if (!removed) throw new Exception("Product with ID " + id + " not found.");
        saveAll(products);
    }

    @Override
    public Product getProductById(int id) throws Exception {
        return loadAll().stream()
                        .filter(p -> p.getId() == id)
                        .findFirst()
                        .orElseThrow(() -> new Exception("Product with ID " + id + " not found."));
    }

    @Override
    public List<Product> getAllProducts() throws Exception {
        return loadAll();
    }

    @Override
    public List<Product> searchByName(String keyword) throws Exception {
        String lower = keyword.toLowerCase();
        return loadAll().stream()
                        .filter(p -> p.getName().toLowerCase().contains(lower))
                        .collect(Collectors.toList());
    }

    @Override
    public List<Product> searchByCategory(String category) throws Exception {
        String lower = category.toLowerCase();
        return loadAll().stream()
                        .filter(p -> p.getCategory().toLowerCase().contains(lower))
                        .collect(Collectors.toList());
    }

    @Override
    public List<Product> getLowStockProducts() throws Exception {
        return loadAll().stream()
                        .filter(Product::isLowStock)
                        .collect(Collectors.toList());
    }
}