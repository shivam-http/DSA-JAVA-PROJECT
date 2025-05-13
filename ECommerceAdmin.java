/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Shivam
 */
import java.util.Scanner;
import service.ProductLinkedList;
import model.Product;

public class ECommerceAdmin {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ProductLinkedList productList = new ProductLinkedList();
        productList.loadFromDatabase();

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
}

