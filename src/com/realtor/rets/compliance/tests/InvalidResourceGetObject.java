/* $Header$ 
 */
package com.realtor.rets.compliance.tests;

/**
 *  
 * 
 * @author pobrien
 */
public class InvalidResourceGetObject extends NegativeBaseEvaluator {

    private final static String msf_EXPECTED_STATUS_RESPONSE_CODE = "20400";
    
    /**
     * 
     */
    public InvalidResourceGetObject() {
        super();
        setCorrectResponseStatus(msf_EXPECTED_STATUS_RESPONSE_CODE);
    }

}
