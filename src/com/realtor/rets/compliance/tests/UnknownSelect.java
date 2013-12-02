/* $Header$ 
 */
package com.realtor.rets.compliance.tests;

/**
 *  
 * 
 * @author pobrien
 */
public class UnknownSelect extends NegativeBaseEvaluator {
    
    private final static String msf_EXPECTED_STATUS_RESPONSE_CODE = "20202";

    /**
     * 
     */
    public UnknownSelect() {
        super();
        setCorrectResponseStatus(msf_EXPECTED_STATUS_RESPONSE_CODE);
    }

}
