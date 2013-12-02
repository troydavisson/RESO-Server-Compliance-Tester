/* $Header$ 
 */
package com.realtor.rets.compliance.tests;

/**
 *  
 * 
 */
public class InvalidResourcePostObject extends NegativeBaseEvaluator {

    private final static String msf_EXPECTED_STATUS_RESPONSE_CODE = "20800";
    
    /**
     * 
     */
    public InvalidResourcePostObject() {
        super();
        setCorrectResponseStatus(msf_EXPECTED_STATUS_RESPONSE_CODE);
    }

}
