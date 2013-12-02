/* $Header$ 
 */
package com.realtor.rets.compliance.tests;

/**
 *@author pobrien  
 * 
 */
public class InvalidTypePostObject extends NegativeBaseEvaluator {

    private final static String msf_EXPECTED_STATUS_RESPONSE_CODE = "20801";
    
    /**
     * 
     */
    public InvalidTypePostObject() {
        super();
        setCorrectResponseStatus(msf_EXPECTED_STATUS_RESPONSE_CODE);
    }

}
