/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.texsoftware.data;

import javax.microedition.rms.RecordComparator;
import javax.microedition.rms.RecordFilter;
import org.json.me.JSONObject;

/**
 *
 * @author Jibz
 */
public interface DataObject {
    
    //unique identifier for this object
    public String getId();    
    public boolean equals(DataObject dataObject);
    
    public byte[] toByteArray();
    public void fromByteArray(byte[] data);
    
    public JSONObject toJSON();
    public void fromJSON(JSONObject json);    
    
    //record store filters and comparators
    public RecordComparator getComparator();
    public RecordFilter getFilter();    
}
