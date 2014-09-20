/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.texsoftware.layouts;

import net.texsoftware.component.Field;
import net.texsoftware.logger.Log;

/**
 *
 * @author Jibz7
 */
public class AbsoluteFieldManager extends Manager {

    public void addFieldElement(Field field) {
        managerFields.addElement(field);        
        setAdjustedSize(field);
    }

    public void addManagerElement(Manager manager) {
        managerFields.addElement(manager);
        setAdjustedSize(manager);
    }

    /**
     * adjusted size sets the MAX height/width of an absolute field manager
     * @param field 
     */
    private void setAdjustedSize(Field field) {
        if (width < field.getxPosLast()) {
            width = field.getxPosLast();
        }

        if (height < field.getPreferredHeight()) {
            height = field.getPreferredHeight();
        }
    }
    
    private void setAdjustedSize(Manager manager) {
        if (width < manager.getxPos()) {
            width = manager.getxPos();
        }

        if (height < manager.getHeight()) {
            height = manager.getHeight();
        }        
    }
}
