package onlineshopping;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader; // ✅ ADD THIS
import javax.swing.table.DefaultTableCellRenderer; // ✅ ADD THIS

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import java.util.Map;
import java.util.HashMap;

// ================= MODELS ================= //
class Product {
	int id;
	String name;
	double price;
	int stock;

	Product(int id, String name, double price, int stock) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.stock = stock;
	}
}

class CartItem {
	Product product;
	int quantity;

	CartItem(Product p, int q) {
		product = p;
		quantity = q;
	}

	double getTotal() {
		return product.price * quantity;
	}
}

class User {
	String email;
	String phone;
	List<CartItem> cart = new ArrayList<>();

	User(String e, String p) {
		email = e;
		phone = p;
	}
}

/* ================= MAIN SYSTEM ================= */

public class OnlineShoppingGUI extends JFrame {

	static List<Product> products = new ArrayList<>();
	static User currentUser = null;
	static Map<String, User> users = new HashMap<>();
	/* ===== DB CONNECTION (UNCHANGED) ===== */
	static Connection getConnection() throws Exception {
		return DriverManager.getConnection("jdbc:mysql://localhost:3307/onlineshopping", "root", "Nagasri@9999");
	}

	static void loadProductsFromDB() {
		try {
			products.clear();

			Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3307/onlineshopping?useSSL=false",
					"root", "Nagasri@9999");

			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT id, name, price, stock FROM products");

			while (rs.next()) {
				products.add(
						new Product(rs.getInt("id"), rs.getString("name"), rs.getDouble("price"), rs.getInt("stock")));
			}
			System.out.println("Products loaded: " + products.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* ================= MAIN UI ================= */

	public OnlineShoppingGUI() {

		setTitle("Online Shopping System");

		// 🔥 FULL SCREEN
		setExtendedState(JFrame.MAXIMIZED_BOTH);

		setLayout(new GridBagLayout()); // 🔥 CENTER EVERYTHING
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// ===== Main Card Panel =====
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBackground(Color.WHITE);

		// 🔥 BIGGER CARD (MORE PROFESSIONAL)
		mainPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)),
				BorderFactory.createEmptyBorder(80, 120, 80, 120)));

		// ===== Title =====
		JLabel title = new JLabel("Online Shopping Cart Simulation");
		title.setFont(new Font("Segoe UI", Font.BOLD, 36)); // 🔥 BIGGER
		title.setForeground(new Color(44, 62, 80));
		title.setAlignmentX(Component.CENTER_ALIGNMENT);

		// ===== Subtitle =====
		JLabel subtitle = new JLabel("Welcome to Smart Shopping");
		subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		subtitle.setForeground(Color.GRAY);
		subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

		// ===== Buttons =====
		JButton userBtn = new JButton("User Login");
		JButton adminBtn = new JButton("Admin Login");
		JButton exitBtn = new JButton("Exit");

		Font btnFont = new Font("Segoe UI", Font.BOLD, 16);

		JButton[] buttons = { userBtn, adminBtn, exitBtn };

		// 🔥 BIGGER BUTTONS
		for (JButton btn : buttons) {
			btn.setFont(btnFont);
			btn.setFocusPainted(false);
			btn.setForeground(Color.WHITE);
			btn.setMaximumSize(new Dimension(260, 50)); // 🔥 BIGGER
			btn.setAlignmentX(Component.CENTER_ALIGNMENT);
		}

		// Colors
		userBtn.setBackground(new Color(52, 152, 219));
		adminBtn.setBackground(new Color(46, 204, 113));
		exitBtn.setBackground(new Color(231, 76, 60));

		// Hover Effects
		addHoverEffect(userBtn, new Color(41, 128, 185), new Color(52, 152, 219));
		addHoverEffect(adminBtn, new Color(39, 174, 96), new Color(46, 204, 113));
		addHoverEffect(exitBtn, new Color(192, 57, 43), new Color(231, 76, 60));

		// ===== Add Components (VERTICAL CENTER STYLE) =====
		mainPanel.add(title);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
		mainPanel.add(subtitle);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));

		for (JButton btn : buttons) {
			mainPanel.add(btn);
			mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		}

		// ===== CENTER USING GRIDBAG =====
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.CENTER;

		add(mainPanel, gbc);

		// ===== BACKGROUND COLOR (OPTIONAL NICE TOUCH) =====
		getContentPane().setBackground(new Color(240, 245, 250));

		// ===== Actions =====
		userBtn.addActionListener(e -> userLogin());
		adminBtn.addActionListener(e -> adminLogin());
		exitBtn.addActionListener(e -> System.exit(0));

		setVisible(true);
	}

	/* ================= USER LOGIN ================= */

	void userLogin() {

		JDialog dialog = new JDialog(this, "User Login", true);
		dialog.setSize(400, 280);
		dialog.setLayout(new BorderLayout());
		dialog.setLocationRelativeTo(this);

		// ===== Main Panel =====
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// ===== Email =====
		JLabel emailLabel = new JLabel("Email:");
		emailLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));

		JTextField emailField = new JTextField(15);
		emailField.setPreferredSize(new Dimension(180, 30));

		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(emailLabel, gbc);

		gbc.gridx = 1;
		panel.add(emailField, gbc);

		// ===== Mobile =====
		JLabel mobileLabel = new JLabel("Mobile:");
		mobileLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));

		JTextField mobileField = new JTextField(15);
		mobileField.setPreferredSize(new Dimension(180, 30));

		gbc.gridx = 0;
		gbc.gridy = 1;
		panel.add(mobileLabel, gbc);

		gbc.gridx = 1;
		panel.add(mobileField, gbc);

		// ===== Buttons =====
		RoundedButton loginBtn = new RoundedButton("Login", new Color(52, 152, 219), new Color(41, 128, 185));

		RoundedButton cancelBtn = new RoundedButton("Cancel", new Color(231, 76, 60), new Color(192, 57, 43));

		loginBtn.setPreferredSize(new Dimension(110, 35));
		cancelBtn.setPreferredSize(new Dimension(110, 35));

		JPanel btnPanel = new JPanel();
		btnPanel.setBackground(Color.WHITE);
		btnPanel.add(loginBtn);
		btnPanel.add(cancelBtn);

		// ===== Add Panels =====
		dialog.add(panel, BorderLayout.CENTER);
		dialog.add(btnPanel, BorderLayout.SOUTH);

		// ===== Actions =====
		loginBtn.addActionListener(e -> {

			String email = emailField.getText();
			String mobile = mobileField.getText();

			if (email.isEmpty() || mobile.isEmpty()) {
				JOptionPane.showMessageDialog(dialog, "All fields required!");
				return;
			}

			if (mobile.length() != 10) {
				JOptionPane.showMessageDialog(dialog, "Enter valid 10-digit mobile number!");
				return;
			}

			try {
				Connection con = getConnection();

				// 🔍 Check if user exists in DB
				PreparedStatement ps = con.prepareStatement("SELECT * FROM customers WHERE email=?");
				ps.setString(1, email);

				ResultSet rs = ps.executeQuery();

				if (users.containsKey(email)) {
				    // ✅ USER ALREADY IN MEMORY → KEEP CART
				    currentUser = users.get(email);
				    currentUser.cart.clear();
				    // 🔥 LOAD CART FROM DATABASE
				    loadProductsFromDB();

				    PreparedStatement cartPs = con.prepareStatement(
				            "SELECT * FROM cart WHERE user_email=?");

				    cartPs.setString(1, email);

				    ResultSet cartRs = cartPs.executeQuery();

				    while (cartRs.next()) {

				        int productId = cartRs.getInt("product_id");
				        int qty = cartRs.getInt("quantity");

				        for (Product p : products) {

				            if (p.id == productId) {
				                currentUser.cart.add(new CartItem(p, qty));
				                break;
				            }
				        }
				    }

				} else {

				    if (rs.next()) {
				        // ✅ EXISTING USER FROM DB
				        currentUser = new User(email, rs.getString("phone"));
				        loadProductsFromDB();

				        PreparedStatement cartPs = con.prepareStatement(
				                "SELECT * FROM cart WHERE user_email=?");

				        cartPs.setString(1, email);

				        ResultSet cartRs = cartPs.executeQuery();

				        while (cartRs.next()) {

				            int productId = cartRs.getInt("product_id");
				            int qty = cartRs.getInt("quantity");

				            for (Product p : products) {

				                if (p.id == productId) {
				                    currentUser.cart.add(new CartItem(p, qty));
				                    break;
				                }
				            }
				        }

				    } else {
				        // 🆕 NEW USER → INSERT INTO DB
				        PreparedStatement insert = con.prepareStatement(
				                "INSERT INTO customers(email, phone) VALUES (?, ?)");
				        insert.setString(1, email);
				        insert.setString(2, mobile);
				        insert.executeUpdate();

				        currentUser = new User(email, mobile);
				    }

				    // 🔥 STORE USER IN MAP (VERY IMPORTANT)
				    users.put(email, currentUser);
				}

			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(dialog, "Database Error!");
			}

			// ===== Success Dialog =====
			JDialog successDialog = new JDialog(this, "Success", true);
			successDialog.setSize(300, 180);
			successDialog.setLayout(new BorderLayout());
			successDialog.setLocationRelativeTo(null);
			successDialog.setResizable(false);

			JPanel successPanel = new JPanel(); // ✅ FIXED NAME
			successPanel.setBackground(Color.WHITE);
			successPanel.setLayout(new BoxLayout(successPanel, BoxLayout.Y_AXIS));
			successPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

			JLabel msg = new JLabel("Login Successful!");
			msg.setFont(new Font("Segoe UI", Font.BOLD, 16));
			msg.setForeground(new Color(46, 204, 113));
			msg.setAlignmentX(Component.CENTER_ALIGNMENT);

			RoundedButton okBtn = new RoundedButton("OK", new Color(46, 204, 113), new Color(39, 174, 96));
			okBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
			okBtn.setPreferredSize(new Dimension(100, 35));

			successPanel.add(msg);
			successPanel.add(Box.createRigidArea(new Dimension(0, 20)));
			successPanel.add(okBtn);

			successDialog.add(successPanel);

			okBtn.addActionListener(ev -> {
				successDialog.dispose();
				dialog.dispose();
				new UserMenu();
				
			});

			successDialog.setVisible(true);
		});

		cancelBtn.addActionListener(e -> dialog.dispose());

		dialog.setVisible(true);
	}

	void adminLogin() {

		JDialog dialog = new JDialog(this, "Admin Login", true);
		dialog.setSize(400, 260);
		dialog.setLayout(new BorderLayout());
		dialog.setLocationRelativeTo(this);
		dialog.setResizable(false);

		// ===== PANEL =====
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// ===== USERNAME =====
		JLabel userLabel = new JLabel("Username:");
		userLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));

		JTextField userField = new JTextField();
		userField.setPreferredSize(new Dimension(180, 30));

		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(userLabel, gbc);

		gbc.gridx = 1;
		panel.add(userField, gbc);

		// ===== PASSWORD =====
		JLabel passLabel = new JLabel("Password:");
		passLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));

		JPasswordField passField = new JPasswordField();
		passField.setPreferredSize(new Dimension(180, 30));

		gbc.gridx = 0;
		gbc.gridy = 1;
		panel.add(passLabel, gbc);

		gbc.gridx = 1;
		panel.add(passField, gbc);

		// ===== BUTTONS =====
		RoundedButton loginBtn = new RoundedButton("Login", new Color(52, 152, 219), new Color(41, 128, 185));

		RoundedButton cancelBtn = new RoundedButton("Cancel", new Color(231, 76, 60), new Color(192, 57, 43));

		loginBtn.setPreferredSize(new Dimension(110, 35));
		cancelBtn.setPreferredSize(new Dimension(110, 35));

		JPanel btnPanel = new JPanel();
		btnPanel.setBackground(Color.WHITE);
		btnPanel.add(loginBtn);
		btnPanel.add(cancelBtn);

		// ===== ADD =====
		dialog.add(panel, BorderLayout.CENTER);
		dialog.add(btnPanel, BorderLayout.SOUTH);

		// ===== ACTIONS =====
		loginBtn.addActionListener(e -> {

			String u = userField.getText();
			String p = new String(passField.getPassword());

			if (u.isEmpty() || p.isEmpty()) {
				JOptionPane.showMessageDialog(dialog, "All fields required!");
				return;
			}

			try {
			    Connection con = getConnection();

			    PreparedStatement ps = con.prepareStatement(
			        "SELECT * FROM admin WHERE username=? AND password=?");

			    ps.setString(1, u);
			    ps.setString(2, p);

			    ResultSet rs = ps.executeQuery();

			    if (rs.next()) {
			        // ✅ LOGIN SUCCESS
			        dialog.dispose();
			        new AdminMenu();
			      
			    } else {
			        // ❌ INVALID LOGIN
			        JOptionPane.showMessageDialog(dialog, "Invalid Credentials!");
			    }

			} catch (Exception ex) {
			    ex.printStackTrace();
			    JOptionPane.showMessageDialog(dialog, "Database Error!");
			}
		});

		cancelBtn.addActionListener(e -> dialog.dispose());

		dialog.setVisible(true);
	}
	/* ================= USER MENU ================= */

	class UserMenu extends JFrame {

	    UserMenu() {

	        setTitle("User Dashboard");
	        setSize(800, 600);
	        setLayout(new BorderLayout());
	        setLocationRelativeTo(null);

	        // ===== HEADER PANEL =====

	        JPanel header = new JPanel(new BorderLayout());
	        header.setBackground(Color.WHITE);
	        header.setBorder(
	                BorderFactory.createEmptyBorder(
	                        10, 15, 10, 15));

	        // ===== LOGOUT BUTTON =====

	        RoundedButton logoutBtn =
	                new RoundedButton(
	                        "← Logout",
	                        new Color(231, 76, 60),
	                        new Color(192, 57, 43));

	        logoutBtn.setPreferredSize(
	                new Dimension(120, 35));

	        // ===== WELCOME LABEL =====

	        String username =
	                currentUser.email.split("@")[0];

	        JLabel welcomeLabel =
	                new JLabel(
	                        "Welcome, " + username);

	        welcomeLabel.setFont(
	                new Font(
	                        "Segoe UI",
	                        Font.BOLD,
	                        20));

	        welcomeLabel.setForeground(
	                new Color(44, 62, 80));

	        welcomeLabel.setHorizontalAlignment(
	                JLabel.RIGHT);

	        header.add(
	                logoutBtn,
	                BorderLayout.WEST);

	        header.add(
	                welcomeLabel,
	                BorderLayout.EAST);

	        // ===== LOGOUT ACTION =====

	        logoutBtn.addActionListener(
	                e -> dispose());

	        // ===== MAIN PANEL =====

	        JPanel mainPanel = new JPanel();

	        mainPanel.setLayout(
	                new BoxLayout(
	                        mainPanel,
	                        BoxLayout.Y_AXIS));

	        mainPanel.setBackground(Color.WHITE);

	        mainPanel.setBorder(
	                BorderFactory.createCompoundBorder(
	                        BorderFactory.createLineBorder(
	                                new Color(200, 200, 200)),
	                        BorderFactory.createEmptyBorder(
	                                40, 70, 40, 70)));

	        // ===== BUTTONS =====

	        RoundedButton productsBtn =
	                new RoundedButton(
	                        "Products",
	                        new Color(52, 152, 219),
	                        new Color(41, 128, 185));

	        RoundedButton cartBtn =
	                new RoundedButton(
	                        "Cart",
	                        new Color(155, 89, 182),
	                        new Color(142, 68, 173));

	        RoundedButton checkoutBtn =
	                new RoundedButton(
	                        "Checkout",
	                        new Color(46, 204, 113),
	                        new Color(39, 174, 96));

	        RoundedButton ordersBtn =
	                new RoundedButton(
	                        "Orders",
	                        new Color(241, 196, 15),
	                        new Color(243, 156, 18));

	        // ===== BUTTON STYLE =====

	        Dimension btnSize =
	                new Dimension(260, 50);

	        RoundedButton[] buttons = {
	                productsBtn,
	                cartBtn,
	                checkoutBtn,
	                ordersBtn
	        };

	        for (RoundedButton btn : buttons) {

	            btn.setMaximumSize(btnSize);

	            btn.setFont(
	                    new Font(
	                            "Segoe UI",
	                            Font.BOLD,
	                            16));

	            btn.setAlignmentX(
	                    Component.CENTER_ALIGNMENT);
	        }

	        // ===== ADD BUTTONS =====

	        mainPanel.add(
	                Box.createVerticalGlue());

	        for (RoundedButton btn : buttons) {

	            mainPanel.add(btn);

	            mainPanel.add(
	                    Box.createRigidArea(
	                            new Dimension(0, 20)));
	        }

	        mainPanel.add(
	                Box.createVerticalGlue());

	        // ===== ADD PANELS =====

	        add(header, BorderLayout.NORTH);
	        add(mainPanel, BorderLayout.CENTER);

	        // ===== ACTIONS =====

	        productsBtn.addActionListener(e -> {

	            System.out.println(
	                    "Products clicked");

	            new ProductUI();
	        });

	        cartBtn.addActionListener(e -> {

	            if (currentUser == null) {

	                JOptionPane.showMessageDialog(
	                        this,
	                        "Please login first!");

	                return;
	            }

	            new CartUI();
	        });

	        checkoutBtn.addActionListener(e -> {

	            if (currentUser == null) {

	                JOptionPane.showMessageDialog(
	                        this,
	                        "Please login first!");

	                return;
	            }

	            new CheckoutUI(this);
	        });

	        ordersBtn.addActionListener(
	                e -> new OrdersUI());

	        setVisible(true);
	    }
	}
	
	/* ================= PRODUCT UI (WITH SEARCH) ================= */

	class ProductUI extends JFrame {

		DefaultTableModel model = new DefaultTableModel(new String[] { "ID", "Name", "Price (₹)", "Stock" }, 0);

		ProductUI() {

			setTitle("Products");
			setSize(800, 500);
			setLayout(new BorderLayout());
			setLocationRelativeTo(null);

			// ===== Top Panel (Search) =====
			JPanel topPanel = new JPanel(new BorderLayout());
			topPanel.setBackground(Color.WHITE);
			topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

			JTextField search = new JTextField();
			search.setFont(new Font("Segoe UI", Font.PLAIN, 14));
			search.setPreferredSize(new Dimension(200, 35));
			search.setBorder(BorderFactory.createTitledBorder("Search Products"));

			topPanel.add(search, BorderLayout.CENTER);

			// ===== Table =====
			JTable table = new JTable(model);
			table.getColumnModel().getColumn(0).setMinWidth(0);
			table.getColumnModel().getColumn(0).setMaxWidth(0);
			table.getColumnModel().getColumn(0).setPreferredWidth(0);
			table.setRowHeight(28);
			table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

			// Header style
			JTableHeader header = table.getTableHeader();
			header.setFont(new Font("Segoe UI", Font.BOLD, 15));
			header.setBackground(new Color(52, 152, 219));
			header.setForeground(Color.WHITE);

			// Zebra rows
			table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
				public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int row,
						int col) {

					Component c = super.getTableCellRendererComponent(t, v, sel, foc, row, col);

					if (!sel) {
						c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 245, 250));
					}
					return c;
				}
			});

			loadProducts();

			// ===== Button =====
			RoundedButton addBtn = new RoundedButton("Add to Cart", new Color(46, 204, 113), new Color(39, 174, 96));
			addBtn.setPreferredSize(new Dimension(150, 40));
			// ===== Bottom Panel =====
			JPanel bottomPanel = new JPanel(new BorderLayout());
			bottomPanel.setBackground(Color.WHITE);
			bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

			// 🔴 Back Button (LEFT)
			RoundedButton backBtn = new RoundedButton("← Back", new Color(231, 76, 60), new Color(192, 57, 43));
			backBtn.setPreferredSize(new Dimension(120, 40));

			// 🟢 Add to Cart (RIGHT)
			addBtn.setPreferredSize(new Dimension(160, 40));

			// Add buttons
			bottomPanel.add(backBtn, BorderLayout.WEST);
			bottomPanel.add(addBtn, BorderLayout.EAST);

			// Back action
			backBtn.addActionListener(e -> dispose());

			// ===== Layout =====
			add(topPanel, BorderLayout.NORTH);
			add(new JScrollPane(table), BorderLayout.CENTER);
			add(bottomPanel, BorderLayout.SOUTH);
			// ===== Search Filter =====
			search.addKeyListener(new KeyAdapter() {
				public void keyReleased(KeyEvent e) {
					filter(search.getText());
				}
			});

			// ===== Add to Cart =====
			addBtn.addActionListener(e -> {
				if (currentUser == null) {
					JOptionPane.showMessageDialog(this, "Please login first!");
					return;
				}
				int row = table.getSelectedRow();
				if (row == -1) {
					JOptionPane.showMessageDialog(this, "Select a product!");
					return;
				}

				int id = (int) model.getValueAt(row, 0);

				// ===== Quantity Dialog =====
				JDialog qtyDialog = new JDialog(this, "Add Quantity", true);
				qtyDialog.setSize(300, 200);
				qtyDialog.setLayout(new BorderLayout());
				qtyDialog.setLocationRelativeTo(this);
				qtyDialog.setResizable(false);

				JPanel panel = new JPanel();
				panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
				panel.setBackground(Color.WHITE);
				panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

				JLabel label = new JLabel("Enter Quantity:");
				label.setFont(new Font("Segoe UI", Font.BOLD, 14));
				label.setAlignmentX(Component.CENTER_ALIGNMENT);

				JTextField qtyField = new JTextField(10);
				qtyField.setMaximumSize(new Dimension(200, 30));
				qtyField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

				RoundedButton okBtn = new RoundedButton("OK", new Color(52, 152, 219), new Color(41, 128, 185));

				RoundedButton cancelBtn = new RoundedButton("Cancel", new Color(231, 76, 60), new Color(192, 57, 43));

				JPanel btnPanel = new JPanel();
				btnPanel.setBackground(Color.WHITE);
				btnPanel.add(okBtn);
				btnPanel.add(cancelBtn);

				panel.add(label);
				panel.add(Box.createRigidArea(new Dimension(0, 10)));
				panel.add(qtyField);
				panel.add(Box.createRigidArea(new Dimension(0, 15)));

				qtyDialog.add(panel, BorderLayout.CENTER);
				qtyDialog.add(btnPanel, BorderLayout.SOUTH);

				final int[] qtyHolder = { -1 };

				okBtn.addActionListener(ev -> {
					try {
						qtyHolder[0] = Integer.parseInt(qtyField.getText());
						qtyDialog.dispose();
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(qtyDialog, "Enter valid number!");
					}
				});

				cancelBtn.addActionListener(ev -> qtyDialog.dispose());

				qtyDialog.setVisible(true);

				if (qtyHolder[0] == -1)
					return;
				int qty = qtyHolder[0];
				if (qty <= 0) {
				    JOptionPane.showMessageDialog(
				            this,
				            "Quantity must be greater than 0!");
				    return;
				}

				// ===== Add to Cart Logic =====
				for (Product p : products) {

				    if (p.id == id) {   // ✅ FIRST find selected product

				        if (qty > p.stock) {
				            JOptionPane.showMessageDialog(this, "Stock unavailable!");
				            return;
				        }

				        // 🔥 CHECK IF PRODUCT ALREADY IN CART
				        boolean found = false;

				        for (CartItem item : currentUser.cart) {
				            if (item.product.id == id) {

				                // OPTIONAL: prevent exceeding stock
				                if (item.quantity + qty > p.stock) {
				                    JOptionPane.showMessageDialog(this, "Not enough stock!");
				                    return;
				                }

				                item.quantity += qty;

				                try {

				                    Connection con = getConnection();

				                    PreparedStatement psUpdate = con.prepareStatement(
				                        "UPDATE cart SET quantity=? WHERE user_email=? AND product_id=?");

				                    psUpdate.setInt(1, item.quantity);
				                    psUpdate.setString(2, currentUser.email);
				                    psUpdate.setInt(3, p.id);

				                    psUpdate.executeUpdate();

				                } catch(Exception ex) {
				                    ex.printStackTrace();
				                }

				                found = true;
				                break;  
				            }
				        }

				        // 🔥 ADD NEW ITEM IF NOT FOUND
				        if (!found) {

				            currentUser.cart.add(new CartItem(p, qty));

				            try {
				                Connection con = getConnection();

				                PreparedStatement psCart = con.prepareStatement(
				                    "INSERT INTO cart(user_email, product_id, quantity) VALUES(?,?,?)");

				                psCart.setString(1, currentUser.email);
				                psCart.setInt(2, p.id);
				                psCart.setInt(3, qty);

				                psCart.executeUpdate();

				            } catch(Exception ex) {
				                ex.printStackTrace();
				            }
				        }

				        // ===== SUCCESS DIALOG (YOUR SAME CODE) =====
				        JDialog successDialog = new JDialog(this, "Success", true);
				        successDialog.setSize(320, 180);
				        successDialog.setLayout(new BorderLayout());
				        successDialog.setLocationRelativeTo(this);
				        successDialog.setResizable(false);

				        JPanel successPanel = new JPanel();
				        successPanel.setBackground(Color.WHITE);
				        successPanel.setLayout(new BoxLayout(successPanel, BoxLayout.Y_AXIS));
				        successPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

				        JLabel msg = new JLabel("Item Added to Cart!");
				        msg.setFont(new Font("Segoe UI", Font.BOLD, 16));
				        msg.setForeground(new Color(46, 204, 113));
				        msg.setAlignmentX(Component.CENTER_ALIGNMENT);

				        RoundedButton okBtn2 = new RoundedButton("OK", new Color(46, 204, 113), new Color(39, 174, 96));
				        okBtn2.setAlignmentX(Component.CENTER_ALIGNMENT);
				        okBtn2.setPreferredSize(new Dimension(100, 35));

				        successPanel.add(msg);
				        successPanel.add(Box.createRigidArea(new Dimension(0, 20)));
				        successPanel.add(okBtn2);

				        successDialog.add(successPanel);

				        okBtn2.addActionListener(e2 -> successDialog.dispose());
				        successDialog.setVisible(true);

				        break;   // 🔥 IMPORTANT (stop loop after match)
				    }
				}
			});
			setVisible(true);
		}

		void loadProducts() {
			loadProductsFromDB();
			model.setRowCount(0);

			for (Product p : products) {
				model.addRow(new Object[] { p.id, p.name, p.price, p.stock });
			}
		}

		void filter(String text) {
			model.setRowCount(0);

			for (Product p : products) {
				if (p.name.toLowerCase().contains(text.toLowerCase())) {
					model.addRow(new Object[] { p.id, p.name, p.price, p.stock });
				}
			}
		}
	}
	/* ================= CART UI ================= */

	class CartUI extends JFrame {
		JLabel totalLabel;
		DefaultTableModel model = new DefaultTableModel(new String[] { "Product", "Qty", "Total (₹)" }, 0);

	

		CartUI() {
			if (currentUser == null) {
				JOptionPane.showMessageDialog(null, "Please login first!");
				return;
			}
			totalLabel = new JLabel("Total: ₹0");
			setTitle("My Cart");
			setSize(600, 400);
			setLayout(new BorderLayout());
			setLocationRelativeTo(null);

			// ===== Table =====
			JTable table = new JTable(model);
			table.setRowHeight(30);
			table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

			// Header Styling
			JTableHeader header = table.getTableHeader();
			header.setFont(new Font("Segoe UI", Font.BOLD, 15));
			header.setBackground(new Color(52, 152, 219));
			header.setForeground(Color.WHITE);

			// Zebra rows
			table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
				public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int row,
						int col) {

					Component c = super.getTableCellRendererComponent(t, v, sel, foc, row, col);

					if (!sel) {
						c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 245, 250));
					}
					return c;
				}
			});

			// ===== Bottom Panel =====
			JPanel bottomPanel = new JPanel(new BorderLayout());
			bottomPanel.setBackground(Color.WHITE);
			bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

			// Total Label
			totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
			totalLabel.setForeground(new Color(44, 62, 80));

			// Remove Button
			RoundedButton removeBtn = new RoundedButton("Remove Item", new Color(231, 76, 60), new Color(192, 57, 43));
			removeBtn.setPreferredSize(new Dimension(150, 40));

			// Left side panel (Back + Total)
			JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			leftPanel.setBackground(Color.WHITE);

			// 🔴 Back Button
			RoundedButton backBtn = new RoundedButton("← Back", new Color(231, 76, 60), new Color(192, 57, 43));
			backBtn.setPreferredSize(new Dimension(110, 35));

			// Add to left
			leftPanel.add(backBtn);
			leftPanel.add(Box.createRigidArea(new Dimension(10, 0)));
			leftPanel.add(totalLabel);

			// Add panels
			bottomPanel.add(leftPanel, BorderLayout.WEST);
			bottomPanel.add(removeBtn, BorderLayout.EAST);

			// Action
			backBtn.addActionListener(e -> dispose());
			// ===== Layout =====
			// ===== TABLE PANEL (CARD LOOK) =====
			JPanel tablePanel = new JPanel(new BorderLayout());
			tablePanel.setBorder(
					BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)),
							BorderFactory.createEmptyBorder(10, 10, 10, 10)));
			tablePanel.add(new JScrollPane(table));

			// ===== FINAL LAYOUT =====
			add(tablePanel, BorderLayout.CENTER);
			add(bottomPanel, BorderLayout.SOUTH);
			loadCart();

			// ===== Remove Action =====
			removeBtn.addActionListener(e -> {
				int row = table.getSelectedRow();
				if (row != -1) {

				    try {

				        CartItem item = currentUser.cart.get(row);

				        Connection con = getConnection();

				        PreparedStatement ps = con.prepareStatement(
				                "DELETE FROM cart WHERE user_email=? AND product_id=?");

				        ps.setString(1, currentUser.email);
				        ps.setInt(2, item.product.id);

				        ps.executeUpdate();

				        currentUser.cart.remove(row);

				        loadCart();

				        JOptionPane.showMessageDialog(
				                this,
				                "Item removed from cart successfully!");

				    } catch (Exception ex) {

				        ex.printStackTrace();

				        JOptionPane.showMessageDialog(
				                this,
				                "Unable to remove item!");
				    }

				} else {
					JDialog warnDialog = new JDialog(this, "Warning", true);
					warnDialog.setSize(320, 180);
					warnDialog.setLayout(new BorderLayout());
					warnDialog.setLocationRelativeTo(this);
					warnDialog.setResizable(false);

					// Panel
					JPanel panel = new JPanel();
					panel.setBackground(Color.WHITE);
					panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
					panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

					// Message
					JLabel msg = new JLabel("Please select an item!");
					msg.setFont(new Font("Segoe UI", Font.BOLD, 15));
					msg.setForeground(new Color(241, 196, 15));
					msg.setAlignmentX(Component.CENTER_ALIGNMENT);

					// Button
					RoundedButton okBtn = new RoundedButton("OK", new Color(241, 196, 15), new Color(243, 156, 18));
					okBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
					okBtn.setPreferredSize(new Dimension(100, 35));

					// Add components
					panel.add(msg);
					panel.add(Box.createRigidArea(new Dimension(0, 20)));
					panel.add(okBtn);

					warnDialog.add(panel);

					// Action
					okBtn.addActionListener(ev -> warnDialog.dispose());
					warnDialog.setVisible(true);
				}
			});

			setVisible(true);
		}

		void loadCart() {
			
			model.setRowCount(0);
			
			if (currentUser.cart.isEmpty()) {
				totalLabel.setText("Total: ₹0");
				return;
			}

			double total = 0;

			for (CartItem c : currentUser.cart) {
				model.addRow(new Object[] { c.product.name, c.quantity, c.getTotal() });
				total += c.getTotal(); 
			}

			totalLabel.setText("Total: ₹" + total);
		}

	}

	class CheckoutUI extends JDialog {

		CheckoutUI(JFrame parent) {
			super(parent, true);
			setTitle("Checkout");
			setSize(600, 400);
			setLayout(new BorderLayout());
			setLocationRelativeTo(null);

			// ===== EMPTY CART =====
			if (currentUser.cart.isEmpty()) {
				showModernDialog("Cart Empty", "Your cart is empty!", new Color(231, 76, 60));
				new ProductUI();
				dispose();
				return;
			}

			// ===== TABLE =====
			DefaultTableModel model = new DefaultTableModel(new String[] { "Select", "Product", "Qty", "Total" }, 0) {
            
				public Class<?> getColumnClass(int col) {
					return col == 0 ? Boolean.class : Object.class;
				}
				public boolean isCellEditable(int row, int col) {
			        return col == 0 || col == 2;
			    }
			};

			JTable table = new JTable(model);
			table.setRowHeight(32);
			table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
			
			double total = 0;
			for (CartItem c : currentUser.cart) {
				model.addRow(new Object[] { false, c.product.name, c.quantity, c.getTotal() });
				total += c.getTotal();
			}

			JLabel totalLabel = new JLabel("Total: ₹" + total);
			totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
			totalLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            
			model.addTableModelListener(e -> {

			    int row = e.getFirstRow();
			    int col = e.getColumn();

			    if (col == 2) {

			        try {

			            int qty = Integer.parseInt(
			                    model.getValueAt(row, 2).toString());

			            if (qty < 1) {

			                JOptionPane.showMessageDialog(
			                        null,
			                        "Quantity must be greater than 0");

			                model.setValueAt(1, row, 2);
			                return;
			            }

			            CartItem item = currentUser.cart.get(row);

			            item.quantity = qty;

			            double itemTotal =
			                    qty * item.product.price;

			            model.setValueAt(itemTotal, row, 3);

			            double grandTotal = 0;

			            for (CartItem c : currentUser.cart) {
			                grandTotal += c.getTotal();
			            }

			            totalLabel.setText(
			                    "Total: ₹" + grandTotal);

			        } catch (Exception ex) {

			            JOptionPane.showMessageDialog(
			                    null,
			                    "Enter valid quantity");
			        }
			    }
			});
			
			
			
			JButton backBtn = new JButton("← Back");
			JButton nextBtn = new JButton("Next →");

			styleButton(backBtn, new Color(231, 76, 60));
			styleButton(nextBtn, new Color(46, 204, 113));

			JPanel bottom = new JPanel(new BorderLayout());
			bottom.add(backBtn, BorderLayout.WEST);
			bottom.add(nextBtn, BorderLayout.EAST);

			add(totalLabel, BorderLayout.NORTH);
			add(new JScrollPane(table), BorderLayout.CENTER);
			add(bottom, BorderLayout.SOUTH);

			backBtn.addActionListener(e -> dispose());

			// NEXT
			nextBtn.addActionListener(e -> {

				List<CartItem> selected = new ArrayList<>();
				double totalAmt = 0;

				for (int i = 0; i < model.getRowCount(); i++) {
					Boolean checked = (Boolean) model.getValueAt(i, 0);
					if (checked != null && checked) {
						CartItem c = currentUser.cart.get(i);
						selected.add(c);
						totalAmt += c.getTotal();
					}
				}

				if (selected.isEmpty()) {
					showModernDialog("Warning", "Select at least one item!", new Color(241, 196, 15));
					return;
				}

				showAddressDialog(selected, totalAmt);
			});

			setVisible(true);
		}

		// ================= ADDRESS =================
		void showAddressDialog(List<CartItem> selected, double total) {

			JDialog dialog = new JDialog(this, "Delivery Address", true);
			dialog.setSize(450, 500);
			dialog.setLayout(new BorderLayout());
			dialog.setLocationRelativeTo(this);

			// ===== MAIN PANEL =====
			JPanel mainPanel = new JPanel();
			mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
			mainPanel.setBackground(Color.WHITE);
			mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

			// ===== TITLE =====
			JLabel title = new JLabel("Enter Delivery Address");
			title.setFont(new Font("Segoe UI", Font.BOLD, 18));
			title.setAlignmentX(Component.CENTER_ALIGNMENT);

			// ===== FIELDS =====
			JTextField nameField = new JTextField(); // ✅ NOT using currentUser.name
			nameField.setBorder(BorderFactory.createTitledBorder("Full Name *"));

			JTextField phoneField = new JTextField();
			phoneField.setBorder(BorderFactory.createTitledBorder("Mobile Number *"));
			
			JTextField doorNoField = new JTextField();
			doorNoField.setBorder(BorderFactory.createTitledBorder("House / Door No *"));

			JTextArea address = new JTextArea(3, 20);
			address.setLineWrap(true);
			address.setWrapStyleWord(true);
			JScrollPane addressScroll = new JScrollPane(address);
			addressScroll.setBorder(BorderFactory.createTitledBorder("Address *"));

			JTextField landmarkField = new JTextField();
			landmarkField.setBorder(BorderFactory.createTitledBorder("Landmark (Optional)"));

			JTextField cityField = new JTextField();
			cityField.setBorder(BorderFactory.createTitledBorder("City *"));

			JTextField stateField = new JTextField();
			stateField.setBorder(BorderFactory.createTitledBorder("State *"));

			JTextField pincodeField = new JTextField();
			pincodeField.setBorder(BorderFactory.createTitledBorder("Pincode *"));

			// ===== BUTTONS =====
			JButton backBtn = new JButton("← Back");
			JButton nextBtn = new JButton("Continue →");

			styleButton(backBtn, new Color(231, 76, 60));
			styleButton(nextBtn, new Color(46, 204, 113));

			JPanel btnPanel = new JPanel(new BorderLayout());
			btnPanel.setBackground(Color.WHITE);
			btnPanel.add(backBtn, BorderLayout.WEST);
			btnPanel.add(nextBtn, BorderLayout.EAST);

			// ===== ADD COMPONENTS =====
			mainPanel.add(title);
			mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
			mainPanel.add(nameField);
			mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
			mainPanel.add(phoneField);
			mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
			mainPanel.add(doorNoField);
			mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
			mainPanel.add(addressScroll);
			mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
			mainPanel.add(landmarkField);
			mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
			mainPanel.add(cityField);
			mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
			mainPanel.add(stateField);
			mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
			mainPanel.add(pincodeField);

			dialog.add(mainPanel, BorderLayout.CENTER);
			dialog.add(btnPanel, BorderLayout.SOUTH);

			// ===== BACK =====
			backBtn.addActionListener(e -> dialog.dispose());

			// ===== NEXT =====
			nextBtn.addActionListener(e -> {

				// ✅ VALIDATION
				if (nameField.getText().trim().isEmpty() ||
					    phoneField.getText().trim().isEmpty() ||
					    doorNoField.getText().trim().isEmpty() ||
					    address.getText().trim().isEmpty() ||
					    cityField.getText().trim().isEmpty() ||
					    stateField.getText().trim().isEmpty() ||
					    pincodeField.getText().trim().isEmpty()) {

					    showModernDialog(
					            "Error",
					            "Please fill all required fields!",
					            new Color(231, 76, 60));
					    return;
					}
				
				
				if (!nameField.getText().trim().matches("[a-zA-Z ]{3,50}")) {
				    showModernDialog("Error",
				            "Enter a valid name!",
				            new Color(231, 76, 60));
				    return;
				}
				
				
				if (!phoneField.getText().trim().matches("[6-9][0-9]{9}")) {
				    showModernDialog("Error",
				            "Enter valid 10-digit mobile number!",
				            new Color(231, 76, 60));
				    return;
				}
				
				
				if (!doorNoField.getText().trim().matches("[A-Za-z0-9./\\- ]{1,30}")) {
				    showModernDialog("Error",
				            "Enter valid House / Door Number!",
				            new Color(231, 76, 60));
				    return;
				}
				
                 
				if (address.getText().trim().length() < 10) {
				    showModernDialog("Error",
				            "Address should be at least 10 characters!",
				            new Color(231, 76, 60));
				    return;
				}
				
				
				if (!cityField.getText().trim().matches("[A-Za-z ]{2,30}")) {
				    showModernDialog("Error",
				            "Enter valid city name!",
				            new Color(231, 76, 60));
				    return;
				}
				
			
				if (!stateField.getText().trim().matches("[A-Za-z ]{2,30}")) {
				    showModernDialog("Error",
				            "Enter valid state name!",
				            new Color(231, 76, 60));
				    return;
				}
				
				
				if (!pincodeField.getText().trim().matches("\\d{6}")) {
				    showModernDialog("Error",
				            "Pincode must be 6 digits!",
				            new Color(231, 76, 60));
				    return;
				}

				// ✅ BUILD ADDRESS STRING (NO currentUser.name)
				String fullAddress =
				        "Name: " + nameField.getText() + "\n" +
				        "Mobile: " + phoneField.getText() + "\n" +
				        "Door No: " + doorNoField.getText() + "\n" +
				        "Address: " + address.getText() + "\n" +
				        (landmarkField.getText().trim().isEmpty()
				                ? ""
				                : "Landmark: "
				                + landmarkField.getText()
				                + "\n") +
				        "City: " + cityField.getText() + "\n" +
				        "State: " + stateField.getText() + "\n" +
				        "Pincode: " + pincodeField.getText();

				dialog.dispose();

				showPaymentDialog(selected, total, fullAddress);
			});

			dialog.setVisible(true);
		}

		// ================= PAYMENT =================
		void showPaymentDialog(List<CartItem> selected, double total, String address) {

			JDialog dialog = new JDialog(this, "Payment", true);
			dialog.setSize(420, 350);
			dialog.setLayout(new BorderLayout());
			dialog.setLocationRelativeTo(this);

			// ===== MAIN PANEL =====
			JPanel mainPanel = new JPanel();
			mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
			mainPanel.setBackground(Color.WHITE);
			mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

			// ===== TITLE =====
			JLabel title = new JLabel("Select Payment Method");
			title.setFont(new Font("Segoe UI", Font.BOLD, 18));
			title.setAlignmentX(Component.CENTER_ALIGNMENT);

			// ===== TOTAL =====
			JLabel totalLabel = new JLabel("Total: ₹" + total);
			totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
			totalLabel.setForeground(new Color(46, 204, 113));
			totalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

			// ===== RADIO BUTTONS =====
			JRadioButton upi = new JRadioButton("UPI Payment");
			JRadioButton cod = new JRadioButton("Cash on Delivery");

			upi.setFont(new Font("Segoe UI", Font.PLAIN, 14));
			cod.setFont(new Font("Segoe UI", Font.PLAIN, 14));

			upi.setBackground(Color.WHITE);
			cod.setBackground(Color.WHITE);

			ButtonGroup group = new ButtonGroup();
			group.add(upi);
			group.add(cod);
			cod.setSelected(true);

			// ===== CENTERED PAYMENT BOX =====
			JPanel radioPanel = new JPanel();
			radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.Y_AXIS));
			radioPanel.setBackground(Color.WHITE);
			radioPanel.setBorder(BorderFactory.createTitledBorder("Payment Options"));
			radioPanel.setMaximumSize(new Dimension(250, 100));
			radioPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

			upi.setAlignmentX(Component.CENTER_ALIGNMENT);
			cod.setAlignmentX(Component.CENTER_ALIGNMENT);

			radioPanel.add(Box.createRigidArea(new Dimension(0, 5)));
			radioPanel.add(upi);
			radioPanel.add(Box.createRigidArea(new Dimension(0, 10)));
			radioPanel.add(cod);

			// ===== STATUS =====
			JLabel statusLabel = new JLabel(" ");
			statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
			statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

			// ===== BUTTONS =====
			JButton backBtn = new JButton("← Back");
			JButton payBtn = new JButton("Pay ₹" + total);

			styleButton(backBtn, new Color(231, 76, 60));
			styleButton(payBtn, new Color(46, 204, 113));

			JPanel btnPanel = new JPanel(new BorderLayout());
			btnPanel.setBackground(Color.WHITE);
			btnPanel.add(backBtn, BorderLayout.WEST);
			btnPanel.add(payBtn, BorderLayout.EAST);

			// ===== ADD COMPONENTS =====
			mainPanel.add(title);
			mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
			mainPanel.add(totalLabel);
			mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
			mainPanel.add(radioPanel);
			mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
			mainPanel.add(statusLabel);

			dialog.add(mainPanel, BorderLayout.CENTER);
			dialog.add(btnPanel, BorderLayout.SOUTH);

			// ===== BACK =====
			backBtn.addActionListener(e -> {
				dialog.dispose();
				showAddressDialog(selected, total);
			});

			// ===== PAY =====
			payBtn.addActionListener(e -> {

				// ✅ COD → DIRECT ORDER
				if (cod.isSelected()) {
					String paymentType = cod.isSelected() ? "COD" : "ONLINE";
					placeOrder(total, address, paymentType, selected);
					dialog.dispose();
					showModernDialog("Success", "Order Placed (Cash on Delivery)", new Color(46, 204, 113));
					dispose();
					return;
				}

				// ✅ UPI → PROCESSING
				statusLabel.setText("Processing UPI Payment...");
				statusLabel.setForeground(new Color(52, 152, 219));

				javax.swing.Timer timer = new javax.swing.Timer(1500, ev -> {

					statusLabel.setText("Payment Successful!");
					statusLabel.setForeground(new Color(46, 204, 113));

					String paymentType = "ONLINE";
					placeOrder(total, address, paymentType, selected);

					javax.swing.Timer finalTimer = new javax.swing.Timer(800, e2 -> {
						dialog.dispose();
						showModernDialog("Success", "Order Placed Successfully!", new Color(46, 204, 113));
						dispose();
					});

					finalTimer.setRepeats(false);
					finalTimer.start();

				});

				timer.setRepeats(false);
				timer.start();
			});

			dialog.setVisible(true);
		}
		// ================= MODERN DIALOG =================

		void showModernDialog(String titleText, String messageText, Color color) {

			JDialog dialog = new JDialog(this, true);
			dialog.setSize(350, 180);
			dialog.setLayout(new BorderLayout());
			dialog.setLocationRelativeTo(this);

			JPanel panel = new JPanel();
			panel.setBackground(Color.WHITE);
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

			JLabel title = new JLabel(titleText);
			title.setFont(new Font("Segoe UI", Font.BOLD, 16));
			title.setForeground(color);
			title.setAlignmentX(Component.CENTER_ALIGNMENT);

			JLabel message = new JLabel(messageText);
			message.setFont(new Font("Segoe UI", Font.PLAIN, 14));
			message.setAlignmentX(Component.CENTER_ALIGNMENT);

			JButton actionBtn;

			// 🔥 SPECIAL CASE: CART EMPTY
			if (titleText.equals("Cart Empty")) {

				actionBtn = new JButton("Browse Products");

				actionBtn.addActionListener(e -> {
					dialog.dispose();
					new ProductUI();
				
				});

			} else {
				actionBtn = new JButton("OK");

				actionBtn.addActionListener(e -> dialog.dispose());
			}

			styleButton(actionBtn, color);
			actionBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

			panel.add(title);
			panel.add(Box.createRigidArea(new Dimension(0, 10)));
			panel.add(message);
			panel.add(Box.createRigidArea(new Dimension(0, 20)));
			panel.add(actionBtn);

			dialog.add(panel);

			dialog.setVisible(true);
		}

		// ================= BUTTON STYLE =================
		void styleButton(JButton btn, Color color) {
			btn.setBackground(color);
			btn.setForeground(Color.WHITE);
			btn.setFocusPainted(false);
			btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
		}

		// -------place order-------//
		void placeOrder(double total, String address, String paymentType, List<CartItem> items) {
			try {
				Connection con = getConnection();
				// ===== STEP 2: CHECK STOCK =====
				for (CartItem c : items) {

				    PreparedStatement psCheck = con.prepareStatement(
				        "SELECT stock FROM products WHERE id=?");

				    psCheck.setInt(1, c.product.id);

				    ResultSet rs = psCheck.executeQuery();

				    if (rs.next()) {
				        int stock = rs.getInt("stock");

				        if (stock < c.quantity) {
				            JOptionPane.showMessageDialog(null,
				                "Stock unavailable for: " + c.product.name);
				            return; // STOP ORDER
				        }
				    }
				}
				// ===== INSERT ORDER =====
				String orderQuery ="INSERT INTO orders(user_email, final_amount, address, order_date, order_time, status, payment_type) VALUES (?, ?, ?, ?, ?, ?, ?)";
				PreparedStatement ps = con.prepareStatement(orderQuery, Statement.RETURN_GENERATED_KEYS);

				ps.setString(1, currentUser.email);
				ps.setDouble(2, total);
				ps.setString(3, address);
				ps.setDate(4, java.sql.Date.valueOf(LocalDate.now()));
				ps.setTime( 5, java.sql.Time.valueOf(java.time.LocalTime.now()));
				ps.setString(6, "PLACED");
				ps.setString(7, paymentType);

				ps.executeUpdate();

				// ===== GET ORDER ID =====
				ResultSet rs = ps.getGeneratedKeys();
				int orderId = 0;

				if (rs.next()) {
					orderId = rs.getInt(1);
				}

				// ===== INSERT ORDER ITEMS =====
				String itemQuery = "INSERT INTO order_items(order_id,product_id,product_name, quantity,price) VALUES (?, ?, ?,?,?)";
				PreparedStatement ps2 = con.prepareStatement(itemQuery);

				for (CartItem c : items) {

				    // INSERT ORDER ITEM
				    ps2.setInt(1, orderId);
				    ps2.setInt(2, c.product.id);
				    ps2.setString(3, c.product.name);
				    ps2.setInt(4, c.quantity);
				    ps2.setDouble(5, c.product.price);
				    ps2.addBatch();

				    // ===== STEP 4: UPDATE STOCK =====
				    PreparedStatement psUpdate = con.prepareStatement(
				        "UPDATE products SET stock = stock - ? WHERE id=?");

				    psUpdate.setInt(1, c.quantity);
				    psUpdate.setInt(2, c.product.id);
				    psUpdate.executeUpdate();
				}

				ps2.executeBatch();

				// 🔥 ===== INSERT INTO PAYMENTS TABLE =====
				String paymentQuery = "INSERT INTO payments(order_id, payment_type, payment_date, amount) VALUES (?, ?, ?, ?)";
				PreparedStatement ps3 = con.prepareStatement(paymentQuery);

				ps3.setInt(1, orderId);
				ps3.setString(2, paymentType);
				ps3.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
				ps3.setDouble(4, total);

				ps3.executeUpdate();
				for(CartItem c : items) {

				    PreparedStatement del = con.prepareStatement(
				        "DELETE FROM cart WHERE user_email=? AND product_id=?");

				    del.setString(1, currentUser.email);
				    del.setInt(2, c.product.id);

				    del.executeUpdate();
				}

				currentUser.cart.removeAll(items);

				

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}}
		/* ================= ORDERS ================= */

		class OrdersUI extends JFrame {

			DefaultTableModel model = new DefaultTableModel(
					new String[] { "Order ID", "Amount (₹)", "Status", "Payment", "Date" }, 0);

			OrdersUI() {

				setTitle("My Orders");
				setSize(650, 400);
				setLocationRelativeTo(null);
				setLayout(new BorderLayout());

				// ===== Title =====
				JLabel title = new JLabel("My Orders", JLabel.CENTER);
				title.setFont(new Font("Segoe UI", Font.BOLD, 20));
				title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

				// ===== Table =====
				JLabel totalLabel = new JLabel("Total: ₹0");
				JTable table = new JTable(model);
				
				table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
				table.setRowHeight(32);
				table.setSelectionBackground(new Color(52, 152, 219));
				table.setGridColor(new Color(220, 220, 220));

				JTableHeader header = table.getTableHeader();
				header.setFont(new Font("Segoe UI", Font.BOLD, 14));
				header.setBackground(new Color(52, 152, 219));
				header.setForeground(Color.WHITE);
				table.setRowHeight(30);
				table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

				// Row Styling + Status Color
				table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
					public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc,
							int row, int col) {

						Component c = super.getTableCellRendererComponent(t, v, sel, foc, row, col);

						// Zebra rows
						if (!sel) {
							c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 245));
						}

						// Status column color
						if (col == 2) {
							String status = v.toString();
							if (status.equalsIgnoreCase("delivered")) {
								c.setForeground(new Color(39, 174, 96)); // green
							} else if (status.equalsIgnoreCase("placed")) {
								c.setForeground(new Color(230, 126, 34)); // orange
							} else {
								c.setForeground(Color.BLACK);
							}
						} else {
							c.setForeground(Color.BLACK);
						}

						return c;
					}
				});

				// ===== Load Data =====
				try {
					Connection con = getConnection();

					PreparedStatement ps = con.prepareStatement("SELECT * FROM orders WHERE user_email=?");

					ps.setString(1, currentUser.email);

					ResultSet rs = ps.executeQuery();

					while (rs.next()) {
						model.addRow(new Object[] { rs.getInt("order_id"), rs.getDouble("final_amount"),
								rs.getString("status"), rs.getString("payment_type"), rs.getDate("order_date")// ⭐ ADD
																												// THIS
						});
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				// ===== Back Button =====
				RoundedButton backBtn = new RoundedButton("← Back", new Color(52, 152, 219), new Color(41, 128, 185));
				backBtn.setPreferredSize(new Dimension(120, 35));
				RoundedButton detailsBtn = new RoundedButton(
				        "View Details",
				        new Color(46, 204, 113),
				        new Color(39, 174, 96));

				detailsBtn.setPreferredSize(new Dimension(120, 35));

				JPanel bottomPanel = new JPanel();
				bottomPanel.setBackground(Color.WHITE);
				bottomPanel.add(backBtn);
				bottomPanel.add(detailsBtn);

				backBtn.addActionListener(e -> dispose());
				detailsBtn.addActionListener(e -> {

				    int row = table.getSelectedRow();

				    if (row == -1) {
				        JOptionPane.showMessageDialog(
				                this,
				                "Select an order first!");
				        return;
				    }

				    int orderId =
				            Integer.parseInt(
				                    model.getValueAt(row, 0).toString());

				    new OrderDetailsUI(orderId);
				});

				// ===== Layout =====
				add(title, BorderLayout.NORTH);
				// ===== TOP PANEL =====

				add(title, BorderLayout.NORTH);
				add(new JScrollPane(table), BorderLayout.CENTER);
				add(bottomPanel, BorderLayout.SOUTH);

				setVisible(true);
			}
		}
		class OrderDetailsUI extends JFrame {

		    OrderDetailsUI(int orderId) {

		        setTitle("Order Details");
		        setSize(900, 700);
		        setLocationRelativeTo(null);
		        setLayout(new BorderLayout());

		        try {

		            Connection con =
		                    OnlineShoppingGUI.getConnection();

		            // ===== GET ORDER DETAILS =====

		            PreparedStatement orderPs =
		                    con.prepareStatement(
		                            "SELECT * FROM orders WHERE order_id=?");

		            orderPs.setInt(1, orderId);

		            ResultSet orderRs =
		                    orderPs.executeQuery();

		            if (!orderRs.next()) {
		                JOptionPane.showMessageDialog(
		                        this,
		                        "Order not found!");
		                return;
		            }

		            String status =
		                    orderRs.getString("status");

		            String payment =
		                    orderRs.getString("payment_type");

		            String address =
		                    orderRs.getString("address");

		            String date =
		                    orderRs.getDate("order_date").toString();

		            String time =
		                    orderRs.getTime("order_time").toString();

		            double totalAmount =
		                    orderRs.getDouble("final_amount");

		            // ===== MAIN PANEL =====

		            JPanel mainPanel = new JPanel();
		            mainPanel.setLayout(
		                    new BoxLayout(
		                            mainPanel,
		                            BoxLayout.Y_AXIS));

		            mainPanel.setBackground(Color.WHITE);
		            mainPanel.setBorder(
		                    BorderFactory.createEmptyBorder(
		                            15, 15, 15, 15));

		            // ===== ORDER INFORMATION =====

		            JPanel orderPanel =
		                    new JPanel(
		                            new GridLayout(
		                                    5, 2, 10, 10));

		            orderPanel.setBorder(
		                    BorderFactory.createTitledBorder(
		                            "Order Information"));

		            orderPanel.add(
		                    new JLabel("Order ID"));

		            orderPanel.add(
		                    new JLabel(
		                            String.valueOf(orderId)));

		            orderPanel.add(
		                    new JLabel("Status"));

		            JLabel statusLabel =
		                    new JLabel(status);

		            if (status.equalsIgnoreCase("PLACED")) {

		                statusLabel.setForeground(
		                        new Color(230, 126, 34));

		            } else if (status.equalsIgnoreCase("SHIPPED")) {

		                statusLabel.setForeground(
		                        new Color(52, 152, 219));

		            } else if (status.equalsIgnoreCase("DELIVERED")) {

		                statusLabel.setForeground(
		                        new Color(39, 174, 96));

		            } else if (status.equalsIgnoreCase("CANCELLED")) {

		                statusLabel.setForeground(
		                        Color.RED);
		            }

		            orderPanel.add(statusLabel);

		            orderPanel.add(
		                    new JLabel("Date"));

		            orderPanel.add(
		                    new JLabel(date));

		            orderPanel.add(
		                    new JLabel("Time"));

		            orderPanel.add(
		                    new JLabel(time));

		            orderPanel.add(
		                    new JLabel("Payment"));

		            orderPanel.add(
		                    new JLabel(
		                            payment.equalsIgnoreCase("COD")
		                                    ? "Cash On Delivery"
		                                    : "Online Payment"));

		            // ===== DELIVERY ADDRESS =====

		            JTextArea addressArea =
		                    new JTextArea(address);

		            addressArea.setEditable(false);
		            addressArea.setLineWrap(true);
		            addressArea.setWrapStyleWord(true);
		            addressArea.setBackground(Color.WHITE);

		            addressArea.setFont(
		                    new Font(
		                            "Segoe UI",
		                            Font.PLAIN,
		                            14));

		            JPanel addressPanel =
		                    new JPanel(
		                            new BorderLayout());

		            addressPanel.setBorder(
		                    BorderFactory.createTitledBorder(
		                            "Delivery Address"));

		            addressPanel.add(addressArea);

		            // ===== PRODUCTS TABLE =====

		            DefaultTableModel model =
		                    new DefaultTableModel(
		                            new String[] {
		                                    "Product Name",
		                                    "Quantity",
		                                    "Price"
		                            }, 0) {

		                        @Override
		                        public boolean isCellEditable(
		                                int row,
		                                int column) {

		                            return false;
		                        }
		                    };

		            PreparedStatement ps =
		                    con.prepareStatement(
		                            "SELECT * FROM order_items WHERE order_id=?");

		            ps.setInt(1, orderId);

		            ResultSet rs =
		                    ps.executeQuery();

		            while (rs.next()) {

		                model.addRow(
		                        new Object[] {
		                                rs.getString("product_name"),
		                                rs.getInt("quantity"),
		                                "₹" + rs.getDouble("price")
		                        });
		            }

		            JTable table =
		                    new JTable(model);

		            table.setRowHeight(30);

		            table.setFont(
		                    new Font(
		                            "Segoe UI",
		                            Font.PLAIN,
		                            14));

		            JTableHeader header =
		                    table.getTableHeader();

		            header.setFont(
		                    new Font(
		                            "Segoe UI",
		                            Font.BOLD,
		                            14));

		            header.setBackground(
		                    new Color(52, 152, 219));

		            header.setForeground(
		                    Color.WHITE);

		            JScrollPane tableScroll =
		                    new JScrollPane(table);

		            tableScroll.setBorder(
		                    BorderFactory.createTitledBorder(
		                            "Ordered Products"));

		            // ===== TOTAL AMOUNT =====

		            JLabel totalLabel =
		                    new JLabel(
		                            "Total Amount : "
		                                    + String.format(
		                                            "₹%,.0f",
		                                            totalAmount));

		            totalLabel.setFont(
		                    new Font(
		                            "Segoe UI",
		                            Font.BOLD,
		                            20));

		            totalLabel.setForeground(
		                    new Color(
		                            39,
		                            174,
		                            96));

		            totalLabel.setAlignmentX(
		                    Component.CENTER_ALIGNMENT);

		            // ===== CLOSE BUTTON =====

		            RoundedButton closeBtn =
		                    new RoundedButton(
		                            "Close",
		                            new Color(
		                                    231,
		                                    76,
		                                    60),
		                            new Color(
		                                    192,
		                                    57,
		                                    43));

		            closeBtn.addActionListener(
		                    e -> dispose());

		            JPanel bottomPanel =
		                    new JPanel();

		            bottomPanel.add(closeBtn);

		            // ===== ADD COMPONENTS =====

		            mainPanel.add(orderPanel);
		            mainPanel.add(
		                    Box.createVerticalStrut(10));

		            mainPanel.add(addressPanel);
		            mainPanel.add(
		                    Box.createVerticalStrut(10));

		            mainPanel.add(tableScroll);
		            mainPanel.add(
		                    Box.createVerticalStrut(10));

		            mainPanel.add(totalLabel);

		            add(
		                    new JScrollPane(mainPanel),
		                    BorderLayout.CENTER);

		            add(
		                    bottomPanel,
		                    BorderLayout.SOUTH);

		        } catch (Exception ex) {

		            ex.printStackTrace();
		        }

		        setVisible(true);
		    }
		}

		class AdminMenu extends JFrame {

			AdminMenu() {

				setTitle("Admin Dashboard");
				setSize(800, 600);
				setLayout(new BorderLayout());
				setLocationRelativeTo(null);

				// ===== HEADER PANEL =====
				JPanel header = new JPanel(new BorderLayout());
				header.setBackground(Color.WHITE);
				header.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

				// 🔴 Back Button (LEFT)
				RoundedButton backBtn = new RoundedButton("← Back", new Color(231, 76, 60), new Color(192, 57, 43));
				backBtn.setPreferredSize(new Dimension(100, 35));

				// 🟦 Title (RIGHT)
				JLabel topTitle = new JLabel("Admin Dashboard");
				topTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
				topTitle.setForeground(new Color(44, 62, 80));
				topTitle.setHorizontalAlignment(JLabel.RIGHT);

				header.add(backBtn, BorderLayout.WEST);
				header.add(topTitle, BorderLayout.EAST);

				backBtn.addActionListener(e -> dispose());

				// ===== MAIN PANEL (CARD STYLE SAME AS USER) =====
				JPanel mainPanel = new JPanel();
				mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
				mainPanel.setBackground(Color.WHITE);

				mainPanel.setBorder(
						BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)),
								BorderFactory.createEmptyBorder(40, 70, 40, 70)));

				// ===== OPTIONAL SUBTITLE (MATCH USER STYLE) =====
				JLabel subtitle = new JLabel("Manage Orders & Inventory");
				subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
				subtitle.setForeground(Color.GRAY);
				subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

				// ===== BUTTONS (EXACT STYLE) =====
				RoundedButton viewOrdersBtn = new RoundedButton("View Orders", new Color(52, 152, 219),
						new Color(41, 128, 185));

				RoundedButton updateStatusBtn = new RoundedButton("Update Order Status", new Color(155, 89, 182),
						new Color(142, 68, 173));

				RoundedButton updateStockBtn = new RoundedButton("Update Stock", new Color(46, 204, 113),
						new Color(39, 174, 96));

				// ===== BUTTON SIZE & STYLE =====
				Dimension btnSize = new Dimension(260, 50);

				RoundedButton addProductBtn = new RoundedButton("Add Product", new Color(52, 152, 219),
						new Color(41, 128, 185));

				RoundedButton deleteProductBtn = new RoundedButton("Delete Product", new Color(231, 76, 60),
						new Color(192, 57, 43));

				RoundedButton[] buttons = { viewOrdersBtn, updateStatusBtn, updateStockBtn, addProductBtn,
						deleteProductBtn };

				for (RoundedButton btn : buttons) {
					btn.setMaximumSize(btnSize);
					btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
					btn.setAlignmentX(Component.CENTER_ALIGNMENT);
				}

				// ===== ADD COMPONENTS (SAME SPACING AS USER DASHBOARD) =====
				mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
				mainPanel.add(subtitle);
				mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

				for (RoundedButton btn : buttons) {
					mainPanel.add(btn);
					mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
				}

				// ===== ADD TO FRAME =====
				add(header, BorderLayout.NORTH);
				add(mainPanel, BorderLayout.CENTER);

				// ===== ACTIONS =====
				viewOrdersBtn.addActionListener(e -> viewOrders());
				updateStatusBtn.addActionListener(e -> updateOrderStatus());
				updateStockBtn.addActionListener(e -> updateStock());
				addProductBtn.addActionListener(e -> addProduct());
				deleteProductBtn.addActionListener(e -> deleteProduct());
				setVisible(true);
			}
		}

		void viewOrders() {

			// ===== FRAME =====
		 
			JFrame frame = new JFrame("All Orders");
			frame.setSize(900, 500);
			frame.setLayout(new BorderLayout());
			frame.setLocationRelativeTo(null);

			// ===== TOP PANEL (SEARCH) =====
			JPanel topPanel = new JPanel(new BorderLayout());
			topPanel.setBackground(Color.WHITE);
			topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

			JTextField search = new JTextField();
			search.setFont(new Font("Segoe UI", Font.PLAIN, 14));
			search.setPreferredSize(new Dimension(200, 35));
			search.setBorder(BorderFactory.createTitledBorder("Search by Order ID, Amount, Payment, Status"));
			topPanel.add(search, BorderLayout.CENTER);

			// ===== TABLE MODEL =====
			DefaultTableModel model = new DefaultTableModel(
			        new String[] {
			                "Order ID",
			                "Amount (₹)",
			                "Payment",
			                "Status"
			        }, 0) {

			    @Override
			    public boolean isCellEditable(int row, int column) {
			        return false;
			    }
			};
			
			JTable table = new JTable(model);
			
			table.setRowHeight(30);
			table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

			// ===== HEADER STYLE =====
			JTableHeader header = table.getTableHeader();
			header.setFont(new Font("Segoe UI", Font.BOLD, 15));
			header.setBackground(new Color(52, 152, 219));
			header.setForeground(Color.WHITE);

			// ===== ZEBRA ROWS =====
			table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
				public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int row,
						int col) {

					Component c = super.getTableCellRendererComponent(t, v, sel, foc, row, col);

					if (!sel) {
						c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 245, 250));
					}

					// Optional: status color
					if (col == 3) {
						String status = v.toString();
						if (status.equalsIgnoreCase("delivered")) {
							c.setForeground(new Color(39, 174, 96));
						} else if (status.equalsIgnoreCase("placed")) {
							c.setForeground(new Color(230, 126, 34));
						} else {
							c.setForeground(Color.BLACK);
						}
					} else {
						c.setForeground(Color.BLACK);
					}

					return c;
				}
			});

			// ===== LOAD DATA =====
			List<Object[]> allData = new ArrayList<>();

			try {
				Connection con = getConnection();
				ResultSet rs = con.createStatement().executeQuery("SELECT * FROM orders");

				while (rs.next()) {

					Object[] row = new Object[] {
					        rs.getInt("order_id"),
					        rs.getDouble("final_amount"),
					        rs.getString("payment_type"),
					        rs.getString("status")
					};

					model.addRow(row);
					allData.add(row);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			// ===== SEARCH FUNCTION =====
			search.addKeyListener(new KeyAdapter() {
				public void keyReleased(KeyEvent e) {

					String text = search.getText().toLowerCase();
					model.setRowCount(0);

					for (Object[] row : allData) {

						boolean match = false;

						for (Object cell : row) {
							if (cell.toString().toLowerCase().contains(text)) {
								match = true;
								break;
							}
						}

						if (match) {
							model.addRow(row);
						}
					}
				}
			});

			// ===== BOTTOM PANEL =====
			JPanel bottomPanel = new JPanel(new BorderLayout());
			bottomPanel.setBackground(Color.WHITE);
			bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

			RoundedButton backBtn = new RoundedButton("← Back", new Color(231, 76, 60), new Color(192, 57, 43));
			RoundedButton detailsBtn = new RoundedButton( "View Details",new Color(46, 204, 113),new Color(39, 174, 96));
            detailsBtn.setPreferredSize(new Dimension(150, 40));

			backBtn.setPreferredSize(new Dimension(120, 40));

			bottomPanel.add(backBtn, BorderLayout.WEST);
			bottomPanel.add(detailsBtn, BorderLayout.EAST);

			backBtn.addActionListener(e -> frame.dispose());
			detailsBtn.addActionListener(e -> {

			    int selectedRow = table.getSelectedRow();

			    if (selectedRow == -1) {

			        JOptionPane.showMessageDialog(
			                frame,
			                "Please select an order!");

			        return;
			    }

			    int orderId =
			            (Integer) model.getValueAt(
			                    selectedRow,
			                    0);

			    showOrderDetails(orderId);
			});

			// ===== ADD TO FRAME =====
			frame.add(topPanel, BorderLayout.NORTH);
			frame.add(new JScrollPane(table), BorderLayout.CENTER);
			frame.add(bottomPanel, BorderLayout.SOUTH);

			frame.setVisible(true);
		}
		
		void showOrderDetails(int orderId) {

		    try {

		        Connection con = getConnection();

		        PreparedStatement ps =
		                con.prepareStatement(
		                        "SELECT * FROM orders WHERE order_id=?");

		        ps.setInt(1, orderId);

		        ResultSet rs = ps.executeQuery();

		        if (!rs.next()) {
		            JOptionPane.showMessageDialog(null, "Order not found!");
		            return;
		        }

		        String email = rs.getString("user_email");
		        String payment = rs.getString("payment_type");
		        String status = rs.getString("status");
		        String date = rs.getDate("order_date").toString();
		        Time orderTime = rs.getTime("order_time");

		        String time =
		                (orderTime != null)
		                ? orderTime.toString()
		                : "N/A";
		        String address = rs.getString("address");
		        double amount = rs.getDouble("final_amount");

		        JDialog dialog =
		                new JDialog((Frame) null,
		                        "Order Details",
		                        true);

		        dialog.setSize(900, 700);
		        dialog.setLocationRelativeTo(null);
		        dialog.setLayout(new BorderLayout());

		        JPanel mainPanel = new JPanel();
		        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		        mainPanel.setBackground(Color.WHITE);
		        mainPanel.setBorder(
		                BorderFactory.createEmptyBorder(15, 15, 15, 15));

		        // ================= ORDER INFO =================

		        JPanel orderPanel =
		                new JPanel(new GridLayout(6, 2, 10, 10));

		        orderPanel.setBorder(
		                BorderFactory.createTitledBorder(
		                        "Order Information"));

		        orderPanel.add(new JLabel("Order ID"));
		        orderPanel.add(new JLabel(String.valueOf(orderId)));

		        orderPanel.add(new JLabel("Email"));
		        orderPanel.add(new JLabel(email));

		        orderPanel.add(new JLabel("Payment"));
		        orderPanel.add(new JLabel(payment));

		        orderPanel.add(new JLabel("Status"));

		        JLabel statusLabel = new JLabel(status);

		        if (status.equalsIgnoreCase("PLACED"))
		            statusLabel.setForeground(new Color(230, 126, 34));

		        else if (status.equalsIgnoreCase("SHIPPED"))
		            statusLabel.setForeground(new Color(52, 152, 219));

		        else if (status.equalsIgnoreCase("DELIVERED"))
		            statusLabel.setForeground(new Color(39, 174, 96));

		        orderPanel.add(statusLabel);

		        orderPanel.add(new JLabel("Date"));
		        orderPanel.add(new JLabel(date));

		        orderPanel.add(new JLabel("Time"));
		        orderPanel.add(new JLabel(time));

		        // ================= ADDRESS =================

		        JTextArea addressArea = new JTextArea(address);

		        addressArea.setEditable(false);
		        addressArea.setLineWrap(true);
		        addressArea.setWrapStyleWord(true);
		        addressArea.setFont(
		                new Font("Segoe UI",
		                        Font.PLAIN,
		                        14));

		        JPanel addressPanel =
		                new JPanel(new BorderLayout());

		        addressPanel.setBorder(
		                BorderFactory.createTitledBorder(
		                        "Delivery Address"));

		        addressPanel.add(addressArea);

		        // ================= PRODUCTS TABLE =================

		        DefaultTableModel productModel =
		                new DefaultTableModel(
		                        new String[]{
		                                "Product ID",
		                                "Product Name",
		                                "Quantity",
		                                "Price"
		                        }, 0) {

		                    @Override
		                    public boolean isCellEditable(int r, int c) {
		                        return false;
		                    }
		                };

		        PreparedStatement ps2 =
		                con.prepareStatement(
		                        "SELECT * FROM order_items WHERE order_id=?");

		        ps2.setInt(1, orderId);

		        ResultSet rs2 = ps2.executeQuery();

		        while (rs2.next()) {

		            productModel.addRow(
		                    new Object[]{
		                            rs2.getInt("product_id"),
		                            rs2.getString("product_name"),
		                            rs2.getInt("quantity"),
		                            rs2.getDouble("price")
		                    });
		        }

		        JTable productTable =
		                new JTable(productModel);

		        productTable.setRowHeight(30);
		        productTable.setFont(
		                new Font("Segoe UI",
		                        Font.PLAIN,
		                        14));

		        JTableHeader header =
		                productTable.getTableHeader();

		        header.setFont(
		                new Font("Segoe UI",
		                        Font.BOLD,
		                        14));

		        header.setBackground(
		                new Color(52, 152, 219));

		        header.setForeground(Color.WHITE);

		        JScrollPane productScroll =
		                new JScrollPane(productTable);

		        productScroll.setBorder(
		                BorderFactory.createTitledBorder(
		                        "Ordered Products"));

		        // ================= TOTAL =================

		        JLabel totalLabel =
		                new JLabel(
		                        "Total Amount : ₹" + amount);

		        totalLabel.setFont(
		                new Font("Segoe UI",
		                        Font.BOLD,
		                        20));

		        totalLabel.setForeground(
		                new Color(39, 174, 96));

		        totalLabel.setAlignmentX(
		                Component.CENTER_ALIGNMENT);

		        // ================= CLOSE BUTTON =================

		        JPanel bottomPanel =
		                new JPanel();

		        RoundedButton closeBtn =
		                new RoundedButton(
		                        "Close",
		                        new Color(231, 76, 60),
		                        new Color(192, 57, 43));

		        closeBtn.addActionListener(
		                e -> dialog.dispose());

		        bottomPanel.add(closeBtn);

		        // ================= ADD COMPONENTS =================

		        mainPanel.add(orderPanel);
		        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

		        mainPanel.add(addressPanel);
		        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

		        mainPanel.add(productScroll);
		        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

		        mainPanel.add(totalLabel);

		        dialog.add(
		                new JScrollPane(mainPanel),
		                BorderLayout.CENTER);

		        dialog.add(bottomPanel,
		                BorderLayout.SOUTH);

		        dialog.setVisible(true);

		    } catch (Exception ex) {

		        ex.printStackTrace();
		    }
		}
		
		
		void updateOrderStatus() {
           
			JFrame frame = new JFrame("Update Order Status");
			frame.setSize(950, 520);
			frame.setLayout(new BorderLayout());
			frame.setLocationRelativeTo(null);

			JTextField search = new JTextField();
			search.setFont(new Font("Segoe UI", Font.PLAIN, 14));
			search.setBorder(BorderFactory.createTitledBorder("Search Orders"));

			DefaultTableModel model = new DefaultTableModel(
					new String[] { "Order ID", "User Email", "Amount (₹)", "Date", "Status" }, 0) {
				public boolean isCellEditable(int r, int c) {
					return false;
				}
			};

			JTable table = new JTable(model);
			table.setRowHeight(32);
			table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

			JTableHeader header = table.getTableHeader();
			header.setFont(new Font("Segoe UI", Font.BOLD, 15));
			header.setBackground(new Color(52, 152, 219));
			header.setForeground(Color.WHITE);

			table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
				public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int row,
						int col) {

					Component c = super.getTableCellRendererComponent(t, v, sel, foc, row, col);

					if (!sel) {
						c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 245, 250));
					}

					if (col == 3) {
						String status = v.toString().toLowerCase();

						if (status.contains("delivered"))
							c.setForeground(new Color(39, 174, 96));
						else if (status.contains("placed"))
							c.setForeground(new Color(230, 126, 34));
						else if (status.contains("shipped"))
							c.setForeground(new Color(41, 128, 185));
						else
							c.setForeground(Color.BLACK);
					} else {
						c.setForeground(Color.BLACK);
					}

					return c;
				}
			});

			List<Object[]> allData = new ArrayList<>();

			try {
				Connection con = getConnection();
				ResultSet rs = con.createStatement().executeQuery("SELECT * FROM orders");

				while (rs.next()) {
					Object[] row = { rs.getInt("order_id"), rs.getString("user_email"), rs.getDouble("final_amount"),
							rs.getDate("order_date"), rs.getString("status") };
					model.addRow(row);
					allData.add(row);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			search.addKeyListener(new KeyAdapter() {
				public void keyReleased(KeyEvent e) {
					String text = search.getText().toLowerCase();
					model.setRowCount(0);

					for (Object[] row : allData) {
						boolean match = false;

						for (Object cell : row) {
							if (cell.toString().toLowerCase().contains(text)) {
								match = true;
								break;
							}
						}

						if (match)
							model.addRow(row);
					}
				}
			});

			String[] statuses = { "PLACED", "SHIPPED", "OUT FOR DELIVERY", "DELIVERED" };
			JComboBox<String> statusBox = new JComboBox<>(statuses);
			statusBox.setFont(new Font("Segoe UI", Font.BOLD, 14));

			JButton updateBtn = new JButton("Update Status");
			updateBtn.setBackground(new Color(46, 204, 113));
			updateBtn.setForeground(Color.WHITE);
			updateBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

			JButton backBtn = new JButton("← Back");
			backBtn.setBackground(new Color(231, 76, 60));
			backBtn.setForeground(Color.WHITE);
			backBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

			JPanel bottom = new JPanel(new BorderLayout());
			bottom.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

			bottom.add(backBtn, BorderLayout.WEST);

			JPanel rightPanel = new JPanel();
			rightPanel.add(statusBox);
			rightPanel.add(updateBtn);

			bottom.add(rightPanel, BorderLayout.EAST);

			// ===== UPDATE LOGIC (UPDATED POPUPS) =====
			updateBtn.addActionListener(e -> {

				int row = table.getSelectedRow();

				// 🔥 MODERN WARNING DIALOG
				if (row == -1) {
					JDialog dialog = new JDialog(frame, "Warning", true);
					dialog.setSize(320, 180);
					dialog.setLayout(new BorderLayout());
					dialog.setLocationRelativeTo(frame);

					JPanel panel = new JPanel();
					panel.setBackground(Color.WHITE);
					panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
					panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

					JLabel msg = new JLabel("Select an order!");
					msg.setFont(new Font("Segoe UI", Font.BOLD, 15));
					msg.setAlignmentX(Component.CENTER_ALIGNMENT);

					JButton okBtn = new JButton("OK");
					okBtn.setBackground(new Color(241, 196, 15));
					okBtn.setForeground(Color.WHITE);

					panel.add(msg);
					panel.add(Box.createRigidArea(new Dimension(0, 20)));
					panel.add(okBtn);

					dialog.add(panel);

					okBtn.addActionListener(ev -> dialog.dispose());
					dialog.setVisible(true);
					return;
				}

				int orderId = Integer.parseInt(model.getValueAt(row, 0).toString());
				String newStatus = statusBox.getSelectedItem().toString();

				// 🔥 MODERN CONFIRM DIALOG
				JDialog confirm = new JDialog(frame, "Confirm", true);
				confirm.setSize(350, 180);
				confirm.setLayout(new BorderLayout());
				confirm.setLocationRelativeTo(frame);

				final boolean[] proceed = { false };

				JPanel panel = new JPanel();
				panel.setBackground(Color.WHITE);
				panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
				panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

				JLabel msg = new JLabel("Update Order ID " + orderId + "?");
				msg.setFont(new Font("Segoe UI", Font.BOLD, 15));
				msg.setAlignmentX(Component.CENTER_ALIGNMENT);

				JButton yes = new JButton("Yes");
				JButton no = new JButton("No");

				yes.setBackground(new Color(46, 204, 113));
				yes.setForeground(Color.WHITE);

				no.setBackground(new Color(231, 76, 60));
				no.setForeground(Color.WHITE);

				JPanel btns = new JPanel();
				btns.add(yes);
				btns.add(no);

				panel.add(msg);
				panel.add(Box.createRigidArea(new Dimension(0, 15)));
				panel.add(btns);

				confirm.add(panel);

				yes.addActionListener(ev -> {
					proceed[0] = true;
					confirm.dispose();
				});

				no.addActionListener(ev -> confirm.dispose());

				confirm.setVisible(true);

				if (!proceed[0])
					return;

				try {
					Connection con = getConnection();

					PreparedStatement ps = con.prepareStatement("UPDATE orders SET status=? WHERE order_id=?");

					ps.setString(1, newStatus);
					ps.setInt(2, orderId);

					ps.executeUpdate();

					// 🔥 MODERN SUCCESS DIALOG
					JDialog success = new JDialog(frame, "Success", true);
					success.setSize(320, 150);
					success.setLayout(new BorderLayout());
					success.setLocationRelativeTo(frame);

					JLabel okMsg = new JLabel("Updated Successfully!", JLabel.CENTER);
					okMsg.setFont(new Font("Segoe UI", Font.BOLD, 14));

					JButton okBtn = new JButton("OK");
					okBtn.setBackground(new Color(46, 204, 113));
					okBtn.setForeground(Color.WHITE);

					success.add(okMsg, BorderLayout.CENTER);
					success.add(okBtn, BorderLayout.SOUTH);

					okBtn.addActionListener(e2 -> success.dispose());

					success.setVisible(true);

					// REFRESH
					model.setRowCount(0);
					allData.clear();

					ResultSet rs = con.createStatement().executeQuery("SELECT * FROM orders");

					while (rs.next()) {
						Object[] newRow = { rs.getInt("order_id"), rs.getString("user_email"),
								rs.getDouble("final_amount"), rs.getString("status") };
						model.addRow(newRow);
						allData.add(newRow);
					}

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			});

			backBtn.addActionListener(e -> frame.dispose());

			frame.add(search, BorderLayout.NORTH);
			frame.add(new JScrollPane(table), BorderLayout.CENTER);
			frame.add(bottom, BorderLayout.SOUTH);

			frame.setVisible(true);
		}

		void updateStock() {
           
			JFrame frame = new JFrame("Manage Inventory");
			frame.setSize(950, 520);
			frame.setLayout(new BorderLayout());
			frame.setLocationRelativeTo(null);

			// ===== SEARCH =====
			JTextField search = new JTextField();
			search.setFont(new Font("Segoe UI", Font.PLAIN, 14));
			search.setBorder(BorderFactory.createTitledBorder("Search Products"));

			// ===== TABLE =====
			DefaultTableModel model = new DefaultTableModel(new String[] { "ID", "Product", "Price (₹)", "Stock" }, 0) {

				public boolean isCellEditable(int row, int col) {
					return col == 2 || col == 3; // price + stock editable
				}
			};

			JTable table = new JTable(model);
			table.setRowHeight(32);
			table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

			JTableHeader header = table.getTableHeader();
			header.setFont(new Font("Segoe UI", Font.BOLD, 15));
			header.setBackground(new Color(52, 152, 219));
			header.setForeground(Color.WHITE);

			// ===== LOAD DATA =====
			List<Object[]> allData = new ArrayList<>();

			try {
				Connection con = getConnection();
				ResultSet rs = con.createStatement().executeQuery("SELECT * FROM products");

				while (rs.next()) {
					Object[] row = { rs.getInt("id"), rs.getString("name"), rs.getDouble("price"), rs.getInt("stock") };
					model.addRow(row);
					allData.add(row);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			// ===== SEARCH =====
			search.addKeyListener(new KeyAdapter() {
				public void keyReleased(KeyEvent e) {
					String text = search.getText().toLowerCase();
					model.setRowCount(0);

					for (Object[] row : allData) {
						if (row[1].toString().toLowerCase().contains(text)) {
							model.addRow(row);
						}
					}
				}
			});

			// ===== BUTTONS =====
			JButton saveBtn = new JButton("Save Changes");
			saveBtn.setBackground(new Color(46, 204, 113));
			saveBtn.setForeground(Color.WHITE);
			saveBtn.setFocusPainted(false);
			saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

			JButton backBtn = new JButton("← Back");
			backBtn.setBackground(new Color(231, 76, 60));
			backBtn.setForeground(Color.WHITE);
			backBtn.setFocusPainted(false);

			JPanel bottom = new JPanel(new BorderLayout());
			bottom.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
			bottom.add(backBtn, BorderLayout.WEST);
			bottom.add(saveBtn, BorderLayout.EAST);

			// ===== SAVE ACTION =====
			saveBtn.addActionListener(e -> {

				// ===== CONFIRM DIALOG =====
				JDialog confirm = new JDialog(frame, "Confirm Update", true);
				confirm.setSize(350, 180);
				confirm.setLayout(new BorderLayout());
				confirm.setLocationRelativeTo(frame);

				JPanel panel = new JPanel();
				panel.setBackground(Color.WHITE);
				panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
				panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

				JLabel msg = new JLabel("Update stock & price?");
				msg.setFont(new Font("Segoe UI", Font.BOLD, 16));
				msg.setAlignmentX(Component.CENTER_ALIGNMENT);

				JButton yes = new JButton("Yes");
				JButton no = new JButton("Cancel");

				yes.setBackground(new Color(46, 204, 113));
				yes.setForeground(Color.WHITE);

				no.setBackground(new Color(231, 76, 60));
				no.setForeground(Color.WHITE);

				JPanel btns = new JPanel();
				btns.add(yes);
				btns.add(no);

				panel.add(msg);
				panel.add(Box.createRigidArea(new Dimension(0, 15)));
				panel.add(btns);

				confirm.add(panel);

				// ===== YES CLICK =====
				yes.addActionListener(ev -> {
					confirm.dispose();

					// 🔥 IMPORTANT FIX (COMMIT EDITED CELL)
					if (table.isEditing()) {
						table.getCellEditor().stopCellEditing();
					}

					try {
						Connection con = getConnection();
						PreparedStatement ps = con.prepareStatement("UPDATE products SET price=?, stock=? WHERE id=?");

						for (int i = 0; i < model.getRowCount(); i++) {

							int id = Integer.parseInt(model.getValueAt(i, 0).toString());

							double price;
							int stock;

							try {
								price = Double.parseDouble(model.getValueAt(i, 2).toString());
								stock = Integer.parseInt(model.getValueAt(i, 3).toString());

								if (price <= 0 || stock < 0) {

									JDialog error = new JDialog(frame, "Error", true);
									error.setSize(300, 150);
									error.setLayout(new BorderLayout());
									error.setLocationRelativeTo(frame);

									JLabel errMsg = new JLabel("Invalid values at row " + (i + 1), JLabel.CENTER);
									errMsg.setFont(new Font("Segoe UI", Font.BOLD, 14));

									JButton ok = new JButton("OK");
									ok.setBackground(new Color(231, 76, 60));
									ok.setForeground(Color.WHITE);

									error.add(errMsg, BorderLayout.CENTER);
									error.add(ok, BorderLayout.SOUTH);

									ok.addActionListener(e2 -> error.dispose());

									error.setVisible(true);
									return;
								}

							} catch (Exception ex) {

								JDialog error = new JDialog(frame, "Error", true);
								error.setSize(300, 150);
								error.setLayout(new BorderLayout());
								error.setLocationRelativeTo(frame);

								JLabel errMsg = new JLabel("Invalid input at row " + (i + 1), JLabel.CENTER);
								errMsg.setFont(new Font("Segoe UI", Font.BOLD, 14));

								JButton ok = new JButton("OK");
								ok.setBackground(new Color(231, 76, 60));
								ok.setForeground(Color.WHITE);

								error.add(errMsg, BorderLayout.CENTER);
								error.add(ok, BorderLayout.SOUTH);

								ok.addActionListener(e2 -> error.dispose());

								error.setVisible(true);
								return;
							}

							ps.setDouble(1, price);
							ps.setInt(2, stock);
							ps.setInt(3, id);
							ps.addBatch();
						}

						ps.executeBatch();

						// ===== SUCCESS =====
						JDialog success = new JDialog(frame, "Success", true);
						success.setSize(320, 150);
						success.setLayout(new BorderLayout());
						success.setLocationRelativeTo(frame);

						JLabel okMsg = new JLabel("Updated Successfully!", JLabel.CENTER);
						okMsg.setFont(new Font("Segoe UI", Font.BOLD, 14));

						JButton okBtn = new JButton("OK");
						okBtn.setBackground(new Color(46, 204, 113));
						okBtn.setForeground(Color.WHITE);

						success.add(okMsg, BorderLayout.CENTER);
						success.add(okBtn, BorderLayout.SOUTH);

						okBtn.addActionListener(e2 -> success.dispose());

						success.setVisible(true);

					} catch (Exception ex) {
						ex.printStackTrace();
					}
				});

				no.addActionListener(ev -> confirm.dispose());

				confirm.setVisible(true);
			});

			// ===== BACK =====
			backBtn.addActionListener(e -> frame.dispose());

			frame.add(search, BorderLayout.NORTH);
			frame.add(new JScrollPane(table), BorderLayout.CENTER);
			frame.add(bottom, BorderLayout.SOUTH);

			frame.setVisible(true);
		}

		void addProduct() {

			JDialog dialog = new JDialog((Frame) null, "Add Product", true);
			dialog.setSize(420, 350);
			dialog.setLayout(new BorderLayout());
			dialog.setLocationRelativeTo(null);

			// ===== MAIN PANEL =====
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			panel.setBackground(Color.WHITE);
			panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

			JLabel title = new JLabel("Add New Product");
			title.setFont(new Font("Segoe UI", Font.BOLD, 18));
			title.setAlignmentX(Component.CENTER_ALIGNMENT);

			JTextField nameField = new JTextField();
			nameField.setBorder(BorderFactory.createTitledBorder("Product Name"));

			JTextField priceField = new JTextField();
			priceField.setBorder(BorderFactory.createTitledBorder("Price"));

			JTextField stockField = new JTextField();
			stockField.setBorder(BorderFactory.createTitledBorder("Stock"));

			panel.add(title);
			panel.add(Box.createRigidArea(new Dimension(0, 15)));
			panel.add(nameField);
			panel.add(Box.createRigidArea(new Dimension(0, 10)));
			panel.add(priceField);
			panel.add(Box.createRigidArea(new Dimension(0, 10)));
			panel.add(stockField);

			// ===== BUTTONS =====
			RoundedButton backBtn = new RoundedButton("← Back", new Color(231, 76, 60), new Color(192, 57, 43));

			RoundedButton addBtn = new RoundedButton("Add Product", new Color(46, 204, 113), new Color(39, 174, 96));

			JPanel btnPanel = new JPanel(new BorderLayout());
			btnPanel.setBackground(Color.WHITE);
			btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

			btnPanel.add(backBtn, BorderLayout.WEST);
			btnPanel.add(addBtn, BorderLayout.EAST);

			dialog.add(panel, BorderLayout.CENTER);
			dialog.add(btnPanel, BorderLayout.SOUTH);

			// ===== ACTIONS =====
			backBtn.addActionListener(e -> dialog.dispose());

			addBtn.addActionListener(e -> {
				try {
					String name = nameField.getText();
					double price = Double.parseDouble(priceField.getText());
					int stock = Integer.parseInt(stockField.getText());

					Connection con = getConnection();

					PreparedStatement ps = con
							.prepareStatement("INSERT INTO products(name, price, stock) VALUES (?, ?, ?)");

					ps.setString(1, name);
					ps.setDouble(2, price);
					ps.setInt(3, stock);

					ps.executeUpdate();

					JOptionPane.showMessageDialog(dialog, "Product Added Successfully!");
					dialog.dispose();

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(dialog, "Invalid Input!");
				}
			});

			dialog.setVisible(true);
		}

		void deleteProduct() {

			JFrame frame = new JFrame("Delete Product");
			frame.setSize(850, 500);
			frame.setLayout(new BorderLayout());
			frame.setLocationRelativeTo(null);

			// ===== SEARCH BAR =====
			JTextField search = new JTextField();
			search.setFont(new Font("Segoe UI", Font.PLAIN, 14));
			search.setBorder(BorderFactory.createTitledBorder("Search Product"));

			JPanel topPanel = new JPanel(new BorderLayout());
			topPanel.setBackground(Color.WHITE);
			topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			topPanel.add(search, BorderLayout.CENTER);

			// ===== TABLE =====
			DefaultTableModel model = new DefaultTableModel(new String[] { "ID", "Name", "Price", "Stock" }, 0);

			JTable table = new JTable(model);
			table.setRowHeight(30);
			table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

			JTableHeader header = table.getTableHeader();
			header.setFont(new Font("Segoe UI", Font.BOLD, 15));
			header.setBackground(new Color(52, 152, 219));
			header.setForeground(Color.WHITE);

			// ===== STORE ORIGINAL DATA =====
			List<Object[]> allData = new ArrayList<>();

			// ===== LOAD DATA =====
			try {
				Connection con = getConnection();
				ResultSet rs = con.createStatement().executeQuery("SELECT * FROM products");

				while (rs.next()) {

					Object[] row = new Object[] { rs.getInt("id"), rs.getString("name"), rs.getDouble("price"),
							rs.getInt("stock") };

					model.addRow(row);
					allData.add(row); // ⭐ store for filtering
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			// ===== SEARCH FUNCTION =====
			search.addKeyListener(new KeyAdapter() {
				public void keyReleased(KeyEvent e) {

					String text = search.getText().toLowerCase();
					model.setRowCount(0);

					for (Object[] row : allData) {

						boolean match = false;

						for (Object cell : row) {
							if (cell.toString().toLowerCase().contains(text)) {
								match = true;
								break;
							}
						}

						if (match) {
							model.addRow(row);
						}
					}
				}
			});

			// ===== BUTTONS =====
			RoundedButton backBtn = new RoundedButton("← Back", new Color(231, 76, 60), new Color(192, 57, 43));

			RoundedButton deleteBtn = new RoundedButton("Delete", new Color(231, 76, 60), new Color(192, 57, 43));

			JPanel bottom = new JPanel(new BorderLayout());
			bottom.setBackground(Color.WHITE);
			bottom.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

			bottom.add(backBtn, BorderLayout.WEST);
			bottom.add(deleteBtn, BorderLayout.EAST);

			// ===== ACTIONS =====
			backBtn.addActionListener(e -> frame.dispose());

			deleteBtn.addActionListener(e -> {
				int row = table.getSelectedRow();

				if (row == -1) {
					JOptionPane.showMessageDialog(frame, "Select a product!");
					return;
				}

				int id = (int) model.getValueAt(row, 0);

				try {
					Connection con = getConnection();
					PreparedStatement ps = con.prepareStatement("DELETE FROM products WHERE id=?");

					ps.setInt(1, id);
					ps.executeUpdate();

					model.removeRow(row);

					// also remove from allData (important 🔥)
					allData.remove(row);

					JOptionPane.showMessageDialog(frame, "Product Deleted!");

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			});

			// ===== ADD TO FRAME =====
			frame.add(topPanel, BorderLayout.NORTH);
			frame.add(new JScrollPane(table), BorderLayout.CENTER);
			frame.add(bottom, BorderLayout.SOUTH);

			frame.setVisible(true);
		}

		void addHoverEffect(JButton button, Color hover, Color normal) {
			button.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					button.setBackground(hover);
				}

				public void mouseExited(java.awt.event.MouseEvent evt) {
					button.setBackground(normal);
				}
			});
		}

		class RoundedButton extends JButton {

			private Color backgroundColor;
			private Color hoverColor;

			public RoundedButton(String text, Color bg, Color hover) {
				super(text);
				this.backgroundColor = bg;
				this.hoverColor = hover;

				setFocusPainted(false);
				setForeground(Color.WHITE);
				setFont(new Font("Segoe UI", Font.BOLD, 13));
				setContentAreaFilled(false);
				setOpaque(false);

				addMouseListener(new java.awt.event.MouseAdapter() {
					public void mouseEntered(java.awt.event.MouseEvent evt) {
						backgroundColor = hoverColor;
						repaint();
					}

					public void mouseExited(java.awt.event.MouseEvent evt) {
						backgroundColor = bg;
						repaint();
					}
				});
			}

			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				g2.setColor(backgroundColor);
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

				super.paintComponent(g);
			}

			protected void paintBorder(Graphics g) {
				// no border
			}
		}

		/* ================= MAIN ================= */

	public static void main(String[] args) {
      
        new OnlineShoppingGUI();
    }
}