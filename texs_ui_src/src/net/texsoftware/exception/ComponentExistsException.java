/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.texsoftware.exception;

/**
 *
 * @author Jibola
 */
public class ComponentExistsException extends Exception {

    public ComponentExistsException() {
        super("Component already exists in a manager.");
    }
    
    public ComponentExistsException(String message) {
        super(message);
    }

    public String getMessage() {
        return "Component already exists in a manager.";
    }
}
