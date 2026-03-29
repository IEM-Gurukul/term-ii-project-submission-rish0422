import javax.swing.*;
import java.awt.*;
import java.util.List;


public class SearchPanel extends JPanel {

    private final ProductService service;
    private final MainFrame parent;

    private JTextField searchField;
    private JComboBox<String> filterCombo;

    public SearchPanel(ProductService service, MainFrame parent) {
        this.service = service;
        this.parent = parent;
        initUI();
    }

    private void initUI() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 8, 6));
        setBorder(BorderFactory.createTitledBorder("Search & Filter"));

        JLabel searchLabel = new JLabel("🔍 Search:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));

        searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        filterCombo = new JComboBox<>(new String[]{"By Name", "By Category"});
        filterCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JButton searchBtn = new JButton("Search");
        JButton resetBtn  = new JButton("Reset");

        searchBtn.addActionListener(e -> doSearch());
        resetBtn.addActionListener(e -> {
            searchField.setText("");
            parent.refreshTable();
        });

        searchField.addActionListener(e -> doSearch());

        add(searchLabel);
        add(searchField);
        add(filterCombo);
        add(searchBtn);
        add(resetBtn);
    }

    private void doSearch() {
        String keyword = searchField.getText().trim();
        String filter  = (String) filterCombo.getSelectedItem();
        try {
            List<Product> results;
            if ("By Category".equals(filter)) {
                results = service.searchByCategory(keyword);
            } else {
                results = service.searchByName(keyword);
            }
            parent.getTablePanel().loadProducts(results);

            if (results.isEmpty()) {
                JOptionPane.showMessageDialog(parent,
                    "No products found for: \"" + keyword + "\"",
                    "Search Result", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}