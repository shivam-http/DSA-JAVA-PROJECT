<<<<<<< HEAD
import java.sql.*;
import java.util.Scanner;

class Product {
    int id;
    String name;
    double price;
    int quantity;

    Product(int id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Price: " + price + ", Quantity: " + quantity;
    }
}

class Node {
    Product product;
    Node next;

    Node(Product product) {
        this.product = product;
        this.next = null;
    }
}

class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/ECommerce";
    private static final String USER = "root";
    private static final String PASS = "Shivam@2550";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static void insertProduct(Product p) {
        try (Connection conn = getConnection()) {
            String query = "INSERT INTO products (id, name, price, quantity) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, p.id);
            stmt.setString(2, p.name);
            stmt.setDouble(3, p.price);
            stmt.setInt(4, p.quantity);
            stmt.executeUpdate();
            System.out.println("Product saved to MySQL.");
        } catch (SQLException e) {
            System.out.println("Insert Error: " + e.getMessage());
        }
    }

    public static void viewAllProducts() {
        try (Connection conn = getConnection()) {
            String query = "SELECT * FROM products";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("ID: " + rs.getInt("id") + ", Name: " + rs.getString("name") +
                        ", Price: " + rs.getDouble("price") + ", Quantity: " + rs.getInt("quantity"));
            }
            if (!found) System.out.println("No products found in database.");
        } catch (SQLException e) {
            System.out.println("View Error: " + e.getMessage());
        }
    }

    public static void searchProduct(String keyword) {
        try (Connection conn = getConnection()) {
            String query = "SELECT * FROM products WHERE id = ? OR name = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, keyword);
            stmt.setString(2, keyword);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Product Found: ID: " + rs.getInt("id") + ", Name: " + rs.getString("name") +
                        ", Price: " + rs.getDouble("price") + ", Quantity: " + rs.getInt("quantity"));
            } else {
                System.out.println("Product not found.");
            }
        } catch (SQLException e) {
            System.out.println("Search Error: " + e.getMessage());
        }
    }

    public static void updateProduct(Product p) {
        try (Connection conn = getConnection()) {
            String query = "UPDATE products SET name=?, price=?, quantity=? WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, p.name);
            stmt.setDouble(2, p.price);
            stmt.setInt(3, p.quantity);
            stmt.setInt(4, p.id);
            int updated = stmt.executeUpdate();
            if (updated > 0) {
                System.out.println("Product updated in MySQL.");
            } else {
                System.out.println("Product ID not found.");
            }
        } catch (SQLException e) {
            System.out.println("Update Error: " + e.getMessage());
        }
    }

    public static void deleteProduct(int id) {
        try (Connection conn = getConnection()) {
            String query = "DELETE FROM products WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            int deleted = stmt.executeUpdate();
            if (deleted > 0) {
                System.out.println("Product deleted from MySQL.");
            } else {
                System.out.println("Product ID not found.");
            }
        } catch (SQLException e) {
            System.out.println("Delete Error: " + e.getMessage());
        }
    }
}

class ProductLinkedList {
    Node head;

    void addProduct(Product product) {
        Node newNode = new Node(product);
        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        System.out.println("Product added to LinkedList.");
        DatabaseManager.insertProduct(product);
    }

    void viewProducts() {
        DatabaseManager.viewAllProducts();
    }

    void searchProduct(String keyword) {
        DatabaseManager.searchProduct(keyword);
    }

    void updateProduct(int id, String newName, double newPrice, int newQuantity) {
        Node current = head;
        while (current != null) {
            if (current.product.id == id) {
                current.product.name = newName;
                current.product.price = newPrice;
                current.product.quantity = newQuantity;
                System.out.println("Product updated in LinkedList.");
                DatabaseManager.updateProduct(current.product);
                return;
            }
            current = current.next;
        }
        System.out.println("Product ID not found in LinkedList.");
    }

    void deleteProduct(int id) {
        if (head == null) return;

        if (head.product.id == id) {
            head = head.next;
            DatabaseManager.deleteProduct(id);
            System.out.println("Product deleted from LinkedList.");
            return;
        }

        Node current = head;
        while (current.next != null) {
            if (current.next.product.id == id) {
                current.next = current.next.next;
                DatabaseManager.deleteProduct(id);
                System.out.println("Product deleted from LinkedList.");
                return;
            }
            current = current.next;
        }

        System.out.println("Product ID not found in LinkedList.");
    }

    void loadFromDatabase() {
        try (Connection conn = DatabaseManager.getConnection()) {
            String query = "SELECT * FROM products";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Product p = new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("quantity")
                );
                Node newNode = new Node(p);
                if (head == null) {
                    head = newNode;
                } else {
                    Node current = head;
                    while (current.next != null) {
                        current = current.next;
                    }
                    current.next = newNode;
                }
            }
            System.out.println("Products loaded into LinkedList from DB.");
        } catch (SQLException e) {
            System.out.println("Load Error: " + e.getMessage());
        }
    }

    void printLinkedList() {
        if (head == null) {
            System.out.println("No products in linked list.");
            return;
        }

        Node current = head;
        while (current != null) {
            System.out.println(current.product);
            current = current.next;
        }
    }

    void sortProductsByPrice() {
        if (head == null || head.next == null) return;

        for (Node i = head; i != null; i = i.next) {
            for (Node j = i.next; j != null; j = j.next) {
                if (i.product.price > j.product.price) {
                    Product temp = i.product;
                    i.product = j.product;
                    j.product = temp;
                }
            }
        }
        System.out.println("Products sorted by price in LinkedList:");
        printLinkedList();
    }
}

public class ECommerceAdmin {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ProductLinkedList productList = new ProductLinkedList();

        while (true) {
            System.out.println("\n===== E-Commerce Admin Panel =====");
            System.out.println("1. Add Product");
            System.out.println("2. View All Products");
            System.out.println("3. Search Product by ID/Name");
            System.out.println("4. Update Product");
            System.out.println("5. Delete Product");
            System.out.println("6. Sort Products by Price (LinkedList only)");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter Product ID: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter Product Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Product Price: ");
                    double price = scanner.nextDouble();
                    System.out.print("Enter Product Quantity: ");
                    int qty = scanner.nextInt();
                    productList.addProduct(new Product(id, name, price, qty));
                    break;
                case 2:
                    productList.viewProducts();
                    break;
                case 3:
                    System.out.print("Enter Product ID or Name to search: ");
                    String keyword = scanner.nextLine();
                    productList.searchProduct(keyword);
                    break;
                case 4:
                    System.out.print("Enter Product ID to update: ");
                    int updateId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter new Name: ");
                    String newName = scanner.nextLine();
                    System.out.print("Enter new Price: ");
                    double newPrice = scanner.nextDouble();
                    System.out.print("Enter new Quantity: ");
                    int newQty = scanner.nextInt();
                    productList.updateProduct(updateId, newName, newPrice, newQty);
                    break;
                case 5:
                    System.out.print("Enter Product ID to delete: ");
                    int deleteId = scanner.nextInt();
                    productList.deleteProduct(deleteId);
                    break;
                case 6:
                    productList.head = null;
                    productList.loadFromDatabase();
                    productList.sortProductsByPrice();
                    break;
                case 7:
                    System.out.println("Exiting... Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
=======
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Shivam
 */
// EcommerceAdmin.java

import java.util.Scanner;

class Product { int id; String name; double price; int quantity;

Product(int id, String name, double price, int quantity) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.quantity = quantity;
}

@Override
public String toString() {
    return "ID: " + id + ", Name: " + name + ", Price: " + price + ", Quantity: " + quantity;
}

}

class Node { Product product; Node next;

Node(Product product) {
    this.product = product;
    this.next = null;
}

}

class ProductLinkedList { Node head;

void addProduct(Product product) {
    Node newNode = new Node(product);
    if (head == null) {
        head = newNode;
    } else {
        Node current = head;
        while (current.next != null) {
            current = current.next;
        }
        current.next = newNode;
    }
    System.out.println("Product added successfully.");
}

void viewProducts() {
    if (head == null) {
        System.out.println("No products available.");
        return;
    }
    Node current = head;
    while (current != null) {
        System.out.println(current.product);
        current = current.next;
    }
}

void searchProduct(String keyword) {
    Node current = head;
    boolean found = false;

    while (current != null) {
        if (String.valueOf(current.product.id).equals(keyword) || current.product.name.equalsIgnoreCase(keyword)) {
            System.out.println("Product Found: " + current.product);
            found = true;
            break;
        }
        current = current.next;
    }

    if (!found) {
        System.out.println("Product not found.");
    }
}

void updateProduct(int id, String newName, double newPrice, int newQuantity) {
    Node current = head;
    while (current != null) {
        if (current.product.id == id) {
            current.product.name = newName;
            current.product.price = newPrice;
            current.product.quantity = newQuantity;
            System.out.println("Product updated successfully.");
            return;
        }
        current = current.next;
    }
    System.out.println("Product with ID " + id + " not found.");
}

void deleteProduct(int id) {
    if (head == null) {
        System.out.println("No products to delete.");
        return;
    }

    if (head.product.id == id) {
        head = head.next;
        System.out.println("Product deleted successfully.");
        return;
    }

    Node current = head;
    while (current.next != null) {
        if (current.next.product.id == id) {
            current.next = current.next.next;
            System.out.println("Product deleted successfully.");
            return;
        }
        current = current.next;
    }
    System.out.println("Product with ID " + id + " not found.");
}

void sortProductsByPrice() {
    if (head == null || head.next == null) return;

    for (Node i = head; i != null; i = i.next) {
        for (Node j = i.next; j != null; j = j.next) {
            if (i.product.price > j.product.price) {
                Product temp = i.product;
                i.product = j.product;
                j.product = temp;
            }
        }
    }
    System.out.println("Products sorted by price.");
}

}

public class ECommerceAdmin { public static void main(String[] args) { Scanner scanner = new Scanner(System.in); ProductLinkedList productList = new ProductLinkedList();

while (true) {
        System.out.println("\n===== E-Commerce Admin Panel =====");
        System.out.println("1. Add Product");
        System.out.println("2. View All Products");
        System.out.println("3. Search Product by ID/Name");
        System.out.println("4. Update Product");
        System.out.println("5. Delete Product");
        System.out.println("6. Sort Products by Price");
        System.out.println("7. Exit");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                System.out.print("Enter Product ID: ");
                int id = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Enter Product Name: ");
                String name = scanner.nextLine();
                System.out.print("Enter Product Price: ");
                double price = scanner.nextDouble();
                System.out.print("Enter Product Quantity: ");
                int qty = scanner.nextInt();
                productList.addProduct(new Product(id, name, price, qty));
                break;
            case 2:
                productList.viewProducts();
                break;
            case 3:
                System.out.print("Enter Product ID or Name to search: ");
                String keyword = scanner.nextLine();
                productList.searchProduct(keyword);
                break;
            case 4:
                System.out.print("Enter Product ID to update: ");
                int updateId = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Enter new Name: ");
                String newName = scanner.nextLine();
                System.out.print("Enter new Price: ");
                double newPrice = scanner.nextDouble();
                System.out.print("Enter new Quantity: ");
                int newQty = scanner.nextInt();
                productList.updateProduct(updateId, newName, newPrice, newQty);
                break;
            case 5:
                System.out.print("Enter Product ID to delete: ");
                int deleteId = scanner.nextInt();
                productList.deleteProduct(deleteId);
                break;
            case 6:
                productList.sortProductsByPrice();
                break;
            case 7:
                System.out.println("Exiting... Goodbye!");
                return;
            default:
                System.out.println("Invalid choice. Try again.");
        }
    }
}

>>>>>>> origin/main
}
