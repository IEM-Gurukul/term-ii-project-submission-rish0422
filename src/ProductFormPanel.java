import javax.swing.*;
import java.awt.*;


public class ProductFormPanel extends JPanel {

    private final ProductService service;
    private final MainFrame parent;

    private JTextField idField, nameField, categoryField, priceField, qtyField, thresholdField;
    private JButton saveBtn, clearBtn;

    private boolean editMode = false;

    public ProductFormPanel(ProductService service, MainFrame parent) {
        this.service = service;
        this.parent = parent;
        initUI();
    }

    private void initUI() {
        setPreferredSize(new Dimension(260, 0));
        setBorder(BorderFactory.createTitledBorder("Add / Edit Product"));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridwidth = 2;

        idField        = new JTextField();
        nameField      = new JTextField();
        categoryField  = new JTextField();
        priceField     = new JTextField();
        qtyField       = new JTextField();
        thresholdField = new JTextField();

        idField.setEditable(false);
        idField.setBackground(new Color(240, 240, 240));

        addLabeledField(gbc, "ID", idField, 0);
        addLabeledField(gbc, "Name", nameField, 2);
        addLabeledField(gbc, "Category", categoryField, 4);
        addLabeledField(gbc, "Price (₹)", priceField, 6);
        addLabeledField(gbc, "Quantity", qtyField, 8);
        addLabeledField(gbc, "Low Stock @", thresholdField, 10);

        saveBtn  = new JButton("💾 Save");
        clearBtn = new JButton("✖ Clear");

        saveBtn.addActionListener(e -> saveProduct());
        clearBtn.addActionListener(e -> clearForm());

        JPanel btnRow = new JPanel(new GridLayout(1, 2, 6, 0));
        btnRow.add(saveBtn);
        btnRow.add(clearBtn);

        gbc.gridy = 12;
        gbc.insets = new Insets(12, 8, 6, 8);
        add(btnRow, gbc);
    }

    private void addLabeledField(GridBagConstraints gbc, String label, JTextField field, int row) {
        gbc.gridx = 0; gbc.gridwidth = 2; gbc.gridy = row;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        add(lbl, gbc);

        gbc.gridy = row + 1;
        add(field, gbc);
    }

    private void saveProduct() {
        try {
            String name     = nameField.getText().trim();
            String category = categoryField.getText().trim();
            double price    = Double.parseDouble(priceField.getText().trim());
            int qty         = Integer.parseInt(qtyField.getText().trim());
            int threshold   = thresholdField.getText().isBlank() ? 5
                              : Integer.parseInt(thresholdField.getText().trim());

            if (editMode) {
                int id = Integer.parseInt(idField.getText().trim());
                service.updateProduct(id, name, category, price, qty, threshold);
                JOptionPane.showMessageDialog(parent, "Product updated!");
            } else {
                service.addProduct(name, category, price, qty, threshold);
                JOptionPane.showMessageDialog(parent, "Product added!");
            }
            clearForm();
            parent.refreshTable();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(parent, "Please enter valid numeric values.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parent, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void loadProduct(Product p) {
        editMode = true;
        idField.setText(String.valueOf(p.getId()));
        nameField.setText(p.getName());
        categoryField.setText(p.getCategory());
        priceField.setText(String.valueOf(p.getPrice()));
        qtyField.setText(String.valueOf(p.getQuantity()));
        thresholdField.setText(String.valueOf(p.getLowStockThreshold()));
        saveBtn.setText("✏ Update");
    }

    public void clearForm() {
        editMode = false;
        idField.setText("");
        nameField.setText("");
        categoryField.setText("");
        priceField.setText("");
        qtyField.setText("");
        thresholdField.setText("");
        saveBtn.setText("Save");
    }
}