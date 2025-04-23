import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;

class ParkingSpace {
    int slotId;
    int basePrice;
    boolean isOccupied;
    String slotType; // NORMAL, VIP, EV

    public ParkingSpace(int slotId, int basePrice, String slotType) {
        this.slotId = slotId;
        this.basePrice = basePrice;
        this.slotType = slotType;
        this.isOccupied = false;
    }

    public void occupy() {
        isOccupied = true;
    }

    public void vacate() {
        isOccupied = false;
    }

    @Override
    public String toString() {
        return String.format("Slot %02d - %-9s - ₹%-5d - Type: %s", slotId, isOccupied ? "Occupied" : "Available", basePrice, slotType);
    }
}

class TreeNode {
    ParkingSpace space;
    TreeNode left, right;

    public TreeNode(ParkingSpace space) {
        this.space = space;
        this.left = this.right = null;
    }
}

class ParkingBST {
    TreeNode root;

    public void insert(ParkingSpace space) throws Exception {
        if (findById(space.slotId) != null) {
            throw new Exception("Slot ID already exists!");
        }
        root = insertRec(root, space);
    }

    private TreeNode insertRec(TreeNode node, ParkingSpace space) {
        if (node == null) return new TreeNode(space);
        if (space.slotId < node.space.slotId) node.left = insertRec(node.left, space);
        else node.right = insertRec(node.right, space);
        return node;
    }

    public ParkingSpace findById(int id) {
        TreeNode cur = root;
        while (cur != null) {
            if (id == cur.space.slotId) return cur.space;
            cur = id < cur.space.slotId ? cur.left : cur.right;
        }
        return null;
    }

    public String getSlotDetails() {
        StringBuilder sb = new StringBuilder();
        inorder(root, sb);
        return sb.toString();
    }

    private void inorder(TreeNode node, StringBuilder sb) {
        if (node != null) {
            inorder(node.left, sb);
            sb.append(node.space.toString()).append("\n");
            inorder(node.right, sb);
        }
    }
}

public class ParkingSystemGUI {
    ParkingBST tree = new ParkingBST();
    JTextArea displayArea;

    public ParkingSystemGUI() {
        JFrame frame = new JFrame("Smart Parking Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(850, 650);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(10, 10));

        Font baseFont = new Font("Segoe UI", Font.PLAIN, 14);
        Color primary = new Color(220, 235, 245);
        Color light = new Color(250, 250, 250);

        displayArea = new JTextArea();
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        displayArea.setEditable(false);
        displayArea.setBackground(light);
        displayArea.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Parking Slots Overview",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                baseFont
        ));

        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridBagLayout());
        controlPanel.setBackground(light);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField slotField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField releaseField = new JTextField();
        JTextField updateSlotIdField = new JTextField();
        JTextField updateNewPriceField = new JTextField();
        JTextField allocField = new JTextField();

        // Dropdown for Slot Type
        String[] slotTypes = {"NORMAL", "VIP", "EV"};
        JComboBox<String> slotTypeDropdown = new JComboBox<>(slotTypes);

        JButton addBtn = new JButton("Add Slot");
        JButton allocBtn = new JButton("Allocate Slot");
        JButton releaseBtn = new JButton("Release Slot");
        JButton updatePriceBtn = new JButton("Update Slot Price");
        JButton showBtn = new JButton("Show Slots");
        JButton clearBtn = new JButton("Clear Display");

        JButton[] buttons = {addBtn, allocBtn, releaseBtn, updatePriceBtn, showBtn, clearBtn};
        for (JButton btn : buttons) {
            btn.setBackground(primary);
            btn.setFont(baseFont);
            btn.setFocusPainted(false);
        }

        int y = 0;
        gbc.gridx = 0; gbc.gridy = y++; controlPanel.add(new JLabel("New Slot ID:"), gbc);
        gbc.gridx = 1; controlPanel.add(slotField, gbc);
        gbc.gridx = 0; gbc.gridy = y++; controlPanel.add(new JLabel("Base Price ₹:"), gbc);
        gbc.gridx = 1; controlPanel.add(priceField, gbc);
        gbc.gridx = 0; gbc.gridy = y++; controlPanel.add(new JLabel("Slot Type:"), gbc);
        gbc.gridx = 1; controlPanel.add(slotTypeDropdown, gbc);
        gbc.gridx = 0; gbc.gridy = y++; controlPanel.add(new JLabel("Allocate Slot ID:"), gbc);
        gbc.gridx = 1; controlPanel.add(allocField, gbc);
        gbc.gridx = 0; gbc.gridy = y++; controlPanel.add(new JLabel("Release Slot ID:"), gbc);
        gbc.gridx = 1; controlPanel.add(releaseField, gbc);
        gbc.gridx = 0; gbc.gridy = y++; controlPanel.add(new JLabel("Update Slot ID:"), gbc);
        gbc.gridx = 1; controlPanel.add(updateSlotIdField, gbc);
        gbc.gridx = 0; gbc.gridy = y++; controlPanel.add(new JLabel("New Price ₹:"), gbc);
        gbc.gridx = 1; controlPanel.add(updateNewPriceField, gbc);
        gbc.gridx = 0; gbc.gridy = y; controlPanel.add(addBtn, gbc);
        gbc.gridx = 1; controlPanel.add(allocBtn, gbc);
        gbc.gridy = ++y; gbc.gridx = 0; controlPanel.add(releaseBtn, gbc);
        gbc.gridx = 1; controlPanel.add(updatePriceBtn, gbc);
        gbc.gridy = ++y; gbc.gridx = 0; controlPanel.add(showBtn, gbc);
        gbc.gridx = 1; controlPanel.add(clearBtn, gbc);

        // Action Listeners
        addBtn.addActionListener(e -> {
            try {
                int slotId = Integer.parseInt(slotField.getText().trim());
                int price = Integer.parseInt(priceField.getText().trim());
                String slotType = (String) slotTypeDropdown.getSelectedItem();
                tree.insert(new ParkingSpace(slotId, price, slotType));
                updateDisplay("Slot added successfully.");
            } catch (NumberFormatException nfe) {
                showError("Please enter valid numbers for slot ID and price.");
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        allocBtn.addActionListener(e -> {
            try {
                int slotId = Integer.parseInt(allocField.getText().trim());
                ParkingSpace s = tree.findById(slotId);
                if (s != null && !s.isOccupied) {
                    s.occupy();
                    updateDisplay("Slot " + slotId + " has been allocated.");
                } else if (s != null && s.isOccupied) {
                    showError("Slot " + slotId + " is already occupied.");
                } else {
                    showError("Slot ID not found.");
                }
            } catch (Exception ex) {
                showError("Invalid slot ID.");
            }
        });

        releaseBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(releaseField.getText().trim());
                ParkingSpace s = tree.findById(id);
                if (s != null && s.isOccupied) {
                    s.vacate();
                    updateDisplay("Slot " + id + " released.");
                } else {
                    showError("Slot not found or already free.");
                }
            } catch (Exception ex) {
                showError("Invalid slot ID.");
            }
        });

        updatePriceBtn.addActionListener(e -> {
            try {
                int slotId = Integer.parseInt(updateSlotIdField.getText().trim());
                int newPrice = Integer.parseInt(updateNewPriceField.getText().trim());
                ParkingSpace s = tree.findById(slotId);
                if (s != null) {
                    s.basePrice = newPrice;
                    updateDisplay("Slot " + slotId + " price updated to ₹" + newPrice);
                } else {
                    showError("Slot ID not found.");
                }
            } catch (Exception ex) {
                showError("Invalid slot ID or price.");
            }
        });

        showBtn.addActionListener(e -> updateDisplay("All Slots:\n" + tree.getSlotDetails()));

        clearBtn.addActionListener(e -> {
            tree = new ParkingBST(); // Reset the tree
            displayArea.setText("");
            JOptionPane.showMessageDialog(null, "System has been reset. All slots removed.");
        });

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private void updateDisplay(String msg) {
        displayArea.setText(tree.getSlotDetails());
        JOptionPane.showMessageDialog(null, msg);
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ParkingSystemGUI::new);
    }
}
