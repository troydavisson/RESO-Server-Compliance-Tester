/* $Header$ 
 */
package com.realtor.rets.compliance.metadata;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 *  Gets METADATA xml as an input stream
 * 
 */
public class MetadataUtils {
    
    private static final String ENCODING = "UTF8"; 
    
    public static InputStream InputStreamFromString(String str) {
        byte[] bytes = null;
        ByteArrayInputStream thisInputStream = null;
        
        try {
            bytes = str.getBytes(ENCODING);
            thisInputStream = new ByteArrayInputStream(bytes);
        } catch (Exception e) {
            
        }
        
        return thisInputStream;
    }

}
