/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Shivam
 */
package util;

import java.sql.*;
import model.Product;

public class DatabaseManager {
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
