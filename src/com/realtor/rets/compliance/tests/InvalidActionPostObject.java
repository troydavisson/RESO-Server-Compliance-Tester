/* $Header$ 
 */
package com.realtor.rets.compliance.tests;

/**
 *  
 * 
 */
public class InvalidActionPostObject extends NegativeBaseEvaluator {

    private final static String msf_EXPECTED_STATUS_RESPONSE_CODE = "20803";
    
    /**
     * 
     */
    public InvalidActionPostObject() {
        super();
        setCorrectResponseStatus(msf_EXPECTED_STATUS_RESPONSE_CODE);
    }

}
