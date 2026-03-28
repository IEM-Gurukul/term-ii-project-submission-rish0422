import javax.swing.*;
import java.awt.*;


public class MainFrame extends JFrame {

    private ProductTablePanel tablePanel;
    private ProductFormPanel formPanel;
    private SearchPanel searchPanel;
    private final ProductService service;

    public MainFrame() {
        this.service = new ProductService();
        initUI();
    }

    private void initUI() {
        setTitle("Inventory Management System — rish0422");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 680);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        JLabel header = new JLabel("📦 Inventory Management System", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.setBorder(BorderFactory.createEmptyBorder(12, 0, 4, 0));
        add(header, BorderLayout.NORTH);

        JPanel centerWrapper = new JPanel(new BorderLayout(4, 4));
        searchPanel = new SearchPanel(service, this);
        centerWrapper.add(searchPanel, BorderLayout.NORTH);

        tablePanel = new ProductTablePanel(service, this);
        centerWrapper.add(tablePanel, BorderLayout.CENTER);
        add(centerWrapper, BorderLayout.CENTER);

        formPanel = new ProductFormPanel(service, this);
        add(formPanel, BorderLayout.EAST);

        JLabel status = new JLabel(" Ready  |  Double-click a row to edit", SwingConstants.LEFT);
        status.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        status.setBorder(BorderFactory.createEtchedBorder());
        add(status, BorderLayout.SOUTH);

        refreshTable();
        setVisible(true);
    }

    public void refreshTable() {
        tablePanel.loadProducts();
        checkLowStock();
    }

    public void editProduct(int id) {
        try {
            formPanel.loadProduct(service.getProductById(id));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public ProductTablePanel getTablePanel() {
        return tablePanel;
    }

    private void checkLowStock() {
        try {
            var low = service.getLowStockProducts();
            if (!low.isEmpty()) {
                tablePanel.highlightLowStock();
            }
        } catch (Exception ignored) {}
    }
}