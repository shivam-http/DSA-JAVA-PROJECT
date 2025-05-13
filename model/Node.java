/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Shivam
 */
public class Node {
    public Product product;
    public Node next;

    public Node(Product product) {
        this.product = product;
        this.next = null;
    }
}
