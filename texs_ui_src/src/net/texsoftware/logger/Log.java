/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.texsoftware.logger;

/**
 *
 * @author Jibola
 */
public class Log {

    public static void out(String text) {
        //## if debug                
        System.out.println(text);  
        System.out.println(text);
        //## end debug
    }

    public static void err(Exception ex) {
        //## if debug   
        System.out.println("Error : " + ex.getMessage() + " \n ");
        ex.printStackTrace();        
        //## end debug
    }

    public static void showFreeMemory() {
        //## if debug   
        long freeMemory = Runtime.getRuntime().freeMemory() / 1024;
        System.out.println("Free memory " + freeMemory + " KB");
        //## end debug
    }
}
