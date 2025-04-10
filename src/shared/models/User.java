/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shared.models;

/**
 *
 * @author User
 */
public class User {
    private String userId;
    private String username;
    private String password;
    private String role; // "ADMIN", "SALES", "PURCHASE", "INVENTORY", "FINANCE"
    
    public User(String userId, String username, String password, String role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getters
    public String getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getRole() { return role; }
    
    // Setters
    public void setPassword(String newPassword) { 
        this.password = newPassword; 
    }
}
