import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;


public class ProductTablePanel extends JPanel {

    private final ProductService service;
    private final MainFrame parent;

    private JTable table;
    private DefaultTableModel tableModel;

    private static final String[] COLUMNS = {
        "ID", "Name", "Category", "Price (₹)", "Qty", "Low Stock?"
    };

    public ProductTablePanel(ProductService service, MainFrame parent) {
        this.service = service;
        this.parent = parent;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(4, 4));
        setBorder(BorderFactory.createTitledBorder("Product List"));

        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(24);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row >= 0) {
                        int id = (int) tableModel.getValueAt(row, 0);
                        parent.editProduct(id);
                    }
                }
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));

        JButton deleteBtn  = new JButton("🗑 Delete");
        JButton restockBtn = new JButton("➕ Restock");
        JButton deductBtn  = new JButton("➖ Deduct");
        JButton lowStockBtn = new JButton("⚠ Low Stock");

        deleteBtn.addActionListener(e -> deleteSelected());
        restockBtn.addActionListener(e -> adjustStock(true));
        deductBtn.addActionListener(e -> adjustStock(false));
        lowStockBtn.addActionListener(e -> showLowStock());

        btnPanel.add(deleteBtn);
        btnPanel.add(restockBtn);
        btnPanel.add(deductBtn);
        btnPanel.add(lowStockBtn);
        add(btnPanel, BorderLayout.SOUTH);
    }

    public void loadProducts() {
        try {
            populate(service.getAllProducts());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void loadProducts(List<Product> products) {
        populate(products);
    }

    private void populate(List<Product> products) {
        tableModel.setRowCount(0);
        for (Product p : products) {
            tableModel.addRow(new Object[]{
                p.getId(),
                p.getName(),
                p.getCategory(),
                String.format("%.2f", p.getPrice()),
                p.getQuantity(),
                p.isLowStock() ? "⚠ YES" : "OK"
            });
        }
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(parent, "Select a product first."); return; }
        int id = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(parent,
            "Delete product ID " + id + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            service.deleteProduct(id);
            parent.refreshTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void adjustStock(boolean isRestock) {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(parent, "Select a product first."); return; }
        int id = (int) tableModel.getValueAt(row, 0);
        String action = isRestock ? "Restock" : "Deduct";
        String input = JOptionPane.showInputDialog(parent, action + " amount:");
        if (input == null || input.isBlank()) return;
        try {
            int amount = Integer.parseInt(input.trim());
            if (isRestock) service.restockProduct(id, amount);
            else           service.deductStock(id, amount);
            parent.refreshTable();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(parent, "Enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parent, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showLowStock() {
        try {
            List<Product> low = service.getLowStockProducts();
            if (low.isEmpty()) {
                JOptionPane.showMessageDialog(parent, "No low-stock products! 🎉");
            } else {
                populate(low);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void highlightLowStock() {
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object value, boolean isSelected,
                    boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, col);
                String flag = (String) tableModel.getValueAt(row, 5);
                if ("⚠ YES".equals(flag) && !isSelected) {
                    c.setBackground(new Color(255, 220, 220));
                } else if (!isSelected) {
                    c.setBackground(Color.WHITE);
                }
                return c;
            }
        });
        table.repaint();
    }
}