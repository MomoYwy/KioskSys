/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shared.models;

/**
 *
 * @author User
 */
public class StringUtils {
    public static String safe(String value) {
        return value == null ? "" : value;
    }

    public static String safe(Object value) {
        return value == null ? "" : value.toString();
    }
}

