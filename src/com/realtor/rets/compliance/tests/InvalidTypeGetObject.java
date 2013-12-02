/* $Header$ 
 */
package com.realtor.rets.compliance.tests;

/**
 * 
 * @author pobrien
 */
public class InvalidTypeGetObject extends NegativeBaseEvaluator {

    private final static String msf_EXPECTED_STATUS_RESPONSE_CODE = "20401";
    
    public InvalidTypeGetObject() {
        super();

        setCorrectResponseStatus(msf_EXPECTED_STATUS_RESPONSE_CODE);
    }

}
