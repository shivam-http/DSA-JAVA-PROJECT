/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Shivam
 */
package service;

import model.*;
import util.*;
import java.sql.*;

public class ProductLinkedList {
    public Node head;

    public void addProduct(Product product) {
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

    public void viewProducts() {
        DatabaseManager.viewAllProducts();
    }

    public void searchProduct(String keyword) {
        DatabaseManager.searchProduct(keyword);
    }

    public void updateProduct(int id, String newName, double newPrice, int newQuantity) {
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

    public void deleteProduct(int id) {
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

    public void loadFromDatabase() {
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


    public void printLinkedList() {
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

    public void sortProductsByPrice() {
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
